# Testes automatizados

Os testes automatizados são fundamentais nas aplicação por garantirem que todas as funcionalidades da aplicação estão
funcionando corretamente, garantindo maior confiabilidade do código e maior segurança caso seja necessário fazer alguma
alteração nele futuramente.

## Dependências

A dependência _spring-boot-starter-test_ já adiciona diversas bibliotecas úteis de testes, como o JUnit, o Mockito, 
o Hamcrest, AssertJ, etc.

```XML
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Pacotes

As classes de teste devem ter o mesmo caminho das classes que serão testadas, mas partindo da pasta **test**, ao invés
de **main**. Outro padrão é que as classes de teste devem ter o nome da classe que será testada com **Test** no final (
ex: _DoctorRepository_ vira _DoctorRepositoryTest_).

## Principais anotações:

* **@Test**: indica que aquele método é um teste unitário;
* **@DataJpaTest**: indica que aquela classe testa métodos que acessam o banco de dados (geralmente uma interface 
_Repository_);

## Banco de Dados de testes

### Em memória

Por padrão, o Spring irá tentar usar um banco de dados em memória para realizar os testes de persistência. Essa opção
é vantajosa quando queremos que os testes sejam realizados rapidamente. Para isso, precisamos adicionar a dependência
de um banco de dados em memória, como o **H2**:

```XML
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

E pronto, não será necessário fazer nenhuma configuração adicional, o próprio Spring irá se encarregar de criar o banco
de dados, as tabelas, as permissões, etc. automaticamente.

### Utilizando o mesmo SGBD da aplicação

Porém, por ser um SGBD diferente da aplicação, pode ser que ocorram situações onde os testes funcionem e a 
aplicação não. Podemos dizer para o spring usar o mesmo que a aplicação usando a anotação 
**@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)**. Porém, fazendo somente isso, teremos
o problema de existirem dados presentes no banco de dados da aplicação. O ideal é termos um banco de dados limpo para 
termos segurança nos testes.

Portanto, o ideal é usar o mesmo SGBD (MySQL, SQL Server, etc.), mas um banco de dados exclusivo para testes. Para isso,
precisamos criar um _application-test.properties_, que irá conter os dados do banco de teste. Nesse arquivo, deixamos
apenas a propriedade da URL do banco de testes. As demais propriedades (usuário, senha, etc.) ele continuará buscando 
do _application.properties_ original. Para que o Spring use esse arquivo de configurações, precisamos adicionar a 
anotação **@ActiveProfiles("test")** nas classes de teste.

## Criando testes de _Repositories_

Vale ressaltar que os métodos que usam a sintaxe padrão do Spring Data (findByX, countByY, etc.) já são validados pelo 
Spring. Porém, quando criamos queries personalidas, temos que testá-la devidamente para garantir que sempre está 
funcionando conforme o esperado.

O primeiro passo é criar a classe de testes referente ao _Repository_ que iremos testar. Essa classe precisa ter a
anotação **@DataJpaTest**. Caso você esteja utilizando um banco de dados em disco (como o MySQL), também são necessárias
as anotações **@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)** e
**@ActiveProfiles("test")**. Também é necessário injetar o repositório e o _TestEntityManager_:

```Java
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository repository;

    @Autowired
    private TestEntityManager em;

    // Testes
}
```

Como iremos validar dados do banco de dados, precisamos criar os métodos que irão criar esses dados quando necessário, 
usando o _TestEntityManager_:

```Java
private Patient createPatient() {
    var patient = new Patient(
            new NewPatientDTO("Paciente 1", "paciente1@mail.com", "1234-5678",
                    "00000000000", createAddressDTO())
    );
    em.persist(patient);
    return patient;
}
```

Para criar os testes em si, precisamos criar um método que irá testar cada situação. Esses métodos precisam da anotação
**@Test** e **@DisplayName("Mensagem descrevendo o teste")**:

```Java
@Test
@DisplayName("Deve retornar null quando unico medico cadastrado nao esta disponível na data")
void testFindRandomFreeDoctorAtTheAppointmentTimeWithExpertiseCase1() {
    // Given
    LocalDateTime mondayAt10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
    var doctor = createDoctor();
    var patient = createPatient();
    createAppointment(doctor, patient, mondayAt10);

    // When
    Doctor freeDoctor = repository.findRandomFreeDoctorAtTheAppointmentTimeWithExpertise(mondayAt10, Expertise.CARDIOLOGIA).orElse(null);

    // Then
    assertThat(freeDoctor).isNull();
}
```

A cada teste, o Spring irá limpar a base de dados para que um teste não influencie nos outros.

## Criando testes de _Controllers_

Quando vamos testar os _controllers_, podemos fazer de duas formas: subindo a aplicação e fazendo requisições ou 
_simulando_ que a aplicação está rodando utilizando _mocks_. O primeiro caso é chamado de **teste de integração**. Ele
é bem mais pesado, pois cada teste irá realmente executar todo o fluxo da aplicação, inclusive o acesso ao banco de 
dados. Apesar de mais pesado, ele acaba passando mais segurança. Já o segundo caso é bem mais leve, pois os recursos 
que são utilizados pelo controller são apenas _mocks_. No exemplo a seguir, iremos usar o segundo caso.

Primeiro, criamos a classe para testar o controller em questão e então precisamos anotá-la com **@SpringBootTest** e
**@AutoConfigureMockMvc**:

```Java
@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest { ... }
```

Após isso, precisamos solicitar a injeção da classe **MockMvc**, que irá simular as requisições ao controller:

```Java
@Autowired
private MockMvc mockMvc; // simula requisições no padrão mvc
```

Quando vamos testar endpoints que requerem autenticação, podemos pedir ao Spring que ignore essa parte de segurança
usando a anotação **@WithMockUser**.

Para simular uma requisição usando o **MockMvc**, usamos o método **perform()**, passando um método estático com o tipo
de requisição desejada:

```Java
@Test
@DisplayName("Deve devolver codigo htttp 400 quando informações estão inválidas")
@WithMockUser
void textScheduleCase1() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(post("/consultas")).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
}
```

Podemos entar pegar o objeto **MockHttpServletResponse** para validar dados da resposta enviada pelo controller.

