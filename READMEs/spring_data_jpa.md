# Spring Data JPA

O Spring Data JPA é uma biblioteca ORM que facilita na integração com bancos de dados.

## Configurações de conexão

Quando adicionamos a dependência do Spring Data JPA, o Spring espera que informemos os dados de conexão com uma base de
dados. Caso contrário, ele irá lançar um erro quando a aplicação for inicializada.

Configurações:

```
spring.datasource.driver-class-name=driver (ex: com.mysql.cj.jdbc.Driver)
spring.datasource.url=jdbc:mysql://localhost/nome_da_base_de_dados
spring.datasource.username=usuario
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=valor (create, update, create-drop, validate, none)
```

### YAML

Além do padrão _application.properties_, podemos criar um arquivo usando o formato YAML chamado _application.yml_. Esse
formato é interessante por ser mais legível e menos verboso, visto que ele agrupa configurações por hierarquia.

As configurações acima escritas no formato YAML ficariam da seguinte forma:

```
spring:
    datasource:
        driver-class-name: driver
        url: jdbc:mysql://localhost/nome_da_base_de_dados
        username: usuario
        password: senha
    jpa:
        hibernate:
            ddl-auto: valor
```

## ddl-auto

Quando o Spring verifica os dados de conexão, ele pode tomar algumas atitudes em relação à base de dados informada 
dependendo da propriedade **spring.jpa.hibernate.ddl-auto**:

* **create**: exclui as tabelas gerenciadas pelo Spring e então as cria novamente toda vez que a aplicação é 
inicializada;
* **update**: adiciona colunas adicionadas na aplicação que não existem na base de dados (não altera nada do que existe);
* **create-drop**: além de fazer o que o _create_ já faz, ele também exclui as tabelas quando a aplicação é derrubada;
* **validate**: simplesmente verifica se o mapeamento das entidades da aplicação está de acordo com a base de dados;
* **none**: desliga a geração de _DDL_ do Spring.

## Entidade

Uma entidade é uma classe que representa uma tabela do banco de dados. Para definirmos uma classe como sendo uma 
entidade, usamos a anotação **@Entity**. O Spring irá buscar no banco de dados uma tabela com o mesmo nome da classe. 
Para informarmos que aquela entidade representa uma tabela com outro nome, usamos a anotação 
**@Table(name="nome da tabela")**.

```Java
@Entity
@Table(name="TB_DOCTORS")
public class Doctor { ... }
```

### ID

Toda entidade precisa ter um ID definido. Isso é feito usando a anotação **@Id** em um atributo da classe:

```Java
@Id
private Long id;
```

#### @GeneratedValue

Além disso, quando o id é gerado automaticamente pelo banco de dados, precisamos informar o Spring sobre isso usando a
anotação **@GeneratedValue(strategy=GenerationType.X)**, onde X pode ser:

* **AUTO**: o provedor de persistência escolhe a melhor estratégia de acordo com o banco de dados definido (valor padrão);
* **IDENTITY**: o ID é gerado pela coluna de auto incremento do banco de dados (ex: MySQL);
* **SEQUENCE**: o ID é gerado por uma _sequence_, que pode ser específica para aquela tabela ou geral para todas as 
entidades;
* **TABLE**: os IDs são baseados numa tabela que guarda os IDs gerados (essa opção é pouco recomendada).

```Java
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

### @Column

O Spring irá utilizar algumas convensões para mapear automaticamente os atributos daquela entidade com as colunas no 
banco de dados, por exemplo:

* Irá buscar com o mesmo nome ignorando a capitalização (ex: street -> STREET)
* Irá buscar o nome substituindo o _cammel case_ por _snake case_ (ex: zipCode -> ZIP_CODE)
* Irá converter o tipo do Java por um similar do banco de dados (ex: String -> Varchar)

Caso seja o caso de alguma dessas convensões não se aplicar, ou for necessário deixar explícito como uma coluna deve 
estar, podemos usar a anotação **@Column**. As principais propriedades dessa anotação são:

* **name**: define o nome da coluna
* **updatable**: define se a coluna pode ser atualizada (true/false)
* **insertable**: define se a coluna pode ser inicializada (true/false)
* **length**: quantidade máxima de caracteres
* **nullable**: define se a coluna pode estar vazia (true/false)
* **columnDefinition**: a definição exata da coluna usando SQL (ex: VARCHAR(255) NOT NULL)
* **unique**: se a coluna é uma chave única


### @Embedded e @Embeddable

Quando temos um conjunto de campos que é comum para mais de uma entidade, é uma boa prática extrair esses campos em uma
classe _incorporável_ por outras.

Essa classe que será utilizada pelas outras deve receber a anotação **@Embeddable**:

```Java
@Embeddable
public record Address(String street, String neighborhood, String zipCode) {}
```

Para incorporar essa classe em outra, usamos a anotação @Embedded:

```Java
@Entity
@Table(name="TB_DOCTORS")
public class Doctor {
    ...
    @Embedded
    private Address address;
}
```

Se for necessário, podemos sobreescrever metadados sobre atributos da classe incorporada:

```Java
@Entity
@Table(name="TB_DOCTORS")
public class Doctor {
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="zipCode", column=@Column(name="ZIP_CODE"))
    })
    private Address address;
}
```

## Repository

Ao invés de utilizar o padrão de projeto _DAO (Data Transfer Object)_, o Spring usa o _Repository_. Segundo o livro 
_Domain-Driven Design_, a definição do padrão _Repository_ é:

> O repositório é um mecanismo para encapsular armazenamento, recuperação e comportamento de pesquisa, que emula uma 
> coleção de objetos.

Para realizar as ações no banco de dados (como inserção, atualização, etc.) precisamos criar uma interface para cada 
entidade que extende _JpaRepository_, especificando a entidade a que ela se refere e o tipo da coluna ID:

```Java
public interface DoctorRepository extends JpaRepository<Doctor, Long> {}
```

Essa interface pode então ser injetada pelo Spring:

```Java
import br.com.ferraz.springstudy.domain.doctor.DoctorRepository;

public class DoctorController {
    private final DoctorRepository repository;

    public DoctorController(DoctorRepository repository) {
        this.repository = repository;
    }
}
```

Ela irá conter todas as interações mais comuns com o banco de dados (listagem, busca por ID, inserção, 
deleção, etc.):

```Java
repository.save(entity); // Grava entidade no banco de dados
repository.findAll(); // Busca todos os elementos da tabela
repository.findById(id); // Busca elemento específico pelo ID
repository.count(); // Conta quantidade de registros na tabela
repository.delete(entity); // Remove entidade
```

### Transações

Métodos que fazem operações de escrita no banco de dados precisam usar uma transação. Ela é importante pois, caso ocorra 
algum erro durante o método, seja possível fazer o rollback dos passos anteriores, evitando que o banco fique num estado
indesejado.

Para que um método use uma transação, basta usar a anotação **@Transactional**:

```Java

@PostMapping
@Transactional
public void add(@RequestBody Doctor doctor) {
    ...
}
```

### Derived Query Methods

Além dos diversos métodos que o Spring disponibiliza por padrão para as interfaces _Repository_, podemos criar queries
customizadas facilmente usando as _Derived Query_.

Por exemplo, caso seja necessário criar uma busca pelo nome da entidade:

```Java
List<Entidade> findByName(String name);
```

Para isso, só precisamos criar um método na interface que siga as seguintes regras:

#### Introducer

A primeira parte do nome do método (antes do _By_) é o _introducer_, ou seja, define como será realizada a pesquisa. O método pode 
começar com _find, query, read, count e get_. Podemos ainda usar _Distinct, First e Top_ para remover registros 
duplicados ou limitar a quantidade de resultados.

```Java
List<Entidade> findByName(String name);
List<Entidade> findTop3ByName(String name);
List<Entidade> queryDistinctByName(String name);
List<Entidade> readByName(String name);
int countByName(String name);
List<Entidade> getFirstByName(String name);
Boolean existsByName(String name);
```

#### Criteria

A segunda parte (após o _By_) é a _criteria_, que define as condições da busca (os campos que serão considerados). 
Podemos simplesmente informar o nome do campo, ou então adicionar _Is_ ou _Equals_ após eles para melhorar a 
redibilidade do código:

```Java
List<Doctor> findByName(String name);
List<Doctor> findByNameIs(String name);
List<Doctor> findByNameEquals(String name);
```

Para buscar para casos onde seja **diferente**:

```Java
List<Doctor> findByNameIsNot(String name);
```

Para buscar por campos nulos/não nulos ou booleanos, temos formas ainda mais simples:

```Java
List<Doctor> findByNameIsNull();
List<Doctor> findByNameIsNotNull();
List<Doctor> findByActiveIsTrue();
List<Doctor> findByNameIsFalse();
```

Podemos ainda concatenar mais de um atributo para especificar mais a busca:

```Java
List<Doctor> findByNameOrEmail(String name, String email);
List<Doctor> findByNameAndEmailIsNotNull(String name);
```

Podemos buscar também por similaridade ou verificando um trecho do campo:

```Java
List<Entidade> findByNameStartingWith(String namePrefix);
List<Entidade> findByNameEndingWith(String nameSuffix);
List<Entidade> findByNameContaining(String nameInfix);
```

Caso seja necessário uma forma mais específica de busca por similaridade, podemos usar o _like_:

```Java
List<Entidade> findByNameLike(String likePattern);
...
repository.findByNameLike("a%b%c");
```

Para filtrar usando _comparação_, temos:

```Java
List<Entidade> findByAgeLessThan(Integer age);
List<Entidade> findByAgeLessThanEqual(Integer age);
List<Entidade> findByAgeGreaterThan(Integer age);
List<Entidade> findByAgeGreaterThanEqual(Integer age);
List<Entidade> findByAgeBetween(Integer startAge, endAge);
List<Entidade> findByAgeIn(Collection<Integer> ages);
```

Para compara por data, podemos usar o _Before_ e o _After_:

```Java
List<Entidade> findByBirthDateBefore(ZonedDateTime birthDate);
List<Entidade> findByBirthDateAfter(ZonedDateTime birthDate);
```

Por fim, podemos ainda definir a **ordenação** dos elementos usando o _OrderBy_:

```Java
List<Entidade> findByNameOrderByName(String name);
List<Entidade> findByNameOrderByNameAsc(String name);
List<Entidade> findByNameOrderByNameDesc(String name);
```

Caso seja necessário retornar uma _Page_ ao invés de uma _List_, basta receber um _Pageable_ como último parâmetro:

```Java
List<Entidade> findByNameOrderByName(String name, Pageable pageable);
```

## Paginação

A paginação é uma parte fundamental da busca por elementos em aplicações reais, visto que a maior parte das bases de 
dados contém uma grande quantidade de dados e retornar todos os dados sempre com certeza não seria uma boa ideia.

O Spring disponibiliza as interfaces **Page** e **Pageable** que permitem a paginação de forma simples e prática.

Por exemplo, o método _findAll_ possui uma sobrecarga que recebe um _pageable_ e retorna uma _Page<Entidade>_, que é
similar à uma lista, mas com dados da paginação.

### Pageable

Precisamos receber um _Pageable_ nos métodos de listagem para que a paginação funcione. Os principais atributos desse
pageable serão:

* **size**: quantidade de registros por página;
* **page**: página atual;
* **sort**: coluna(s) que serão usadas para ordenação;
* **direction**: se a ordenação será ascendente ou descendente.

Exemplo de requisição utilizando os parâmetros de ordenação:

```
http://localhost:8080/medicos?size=1&page=1&sort=name,desc
```

Podemos trocar o nome desses parâmetros no _application.properties_:

```
spring.data.web.pageable.page-parameter=pagina
spring.data.web.pageable.size-parameter=tamanho
spring.data.web.sort.sort-parameter=ordem
```

A requisição então ficaria:

```
http://localhost:8080/medicos?tamanho=1&pagina=1&ordem=name,desc
```

### @PageableDefault

É uma boa prática anotarmos os parâmetros _pageable_ com **@PageableDefault** para definirmos valores padrões, tornando
opcional para a requisição informar os dados de paginação.

```Java
@GetMapping
public Page<ObjectDTO> list(@PageableDefault(sort="name", direction = Sort.Direction.ASC, size=10) Pageable pageable) {
    ...
} 
```

### Page

O Spring retorna das buscas paginadas uma **Page<Entidade>**, que é similar à uma List, mas com os dados da paginação
inclusos.

```JSON
{
    "content": [
        {
            "name": "Aline Cardoso",
            "email": "aline.cardoso@mail.com",
            "crm": "654321",
            "expertise": "CARDIOLOGIA"
        },
        {
            "name": "Rodrigo Ferreira",
            "email": "rodrigo.ferreira@mail.com",
            "crm": "123456",
            "expertise": "ORTOPEDIA"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
```

#### Convertendo uma Page<Entidade> para uma Page<DTO>

Uma forma bem simples de converter uma página de uma entidade para uma página de DTO é usando o método _map()_:

```Java
@GetMapping
public Page<DoctorReadDTO> list(@PageableDefault(sort="name", direction = Sort.Direction.ASC, size=10) Pageable pageable) {
    Page<Doctor> doctors = repository.findAll(pageable);
    Page<DoctorReadDTO> list = doctors.map(DoctorReadDTO::new);
    return list;
}
```