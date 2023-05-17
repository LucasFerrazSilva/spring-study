# Spring Secutiry

O **Spring Security** é o módulo do Spring Framework responsável por tratar da segurança das aplicações.

As principais funcionalidades desse módulo são:

* **Autenticação**: diversas formas de login dos usuários;
* **Autorização**: controle de acesso à recursos da aplicação;
* **Proteção contra ataques**: possui mecanismos parar lidar com os principais tipos de ataques (CSRF, clickjacking,
  etc.).

## Stateful x Stateless

Um ponto importante a se considerar é a forma como funciona a autenticação em aplicações Web padrões e em APIs Rest.
Enquanto numa aplicação Web é desejado que os dados do usuário perdurem enquanto o usuário estiver utilizando a
aplicação (com sessão ativa - Stateful), numa API Rest cada requisição é independente (sem sessão - Stateless). Essa é a
diferença entre Stateful e Stateless.

## Principais formas de autenticação

A autenticação é fundamental para garantir a segurança do usuário. Quando o usuário entende que a sua aplicação possui
uma forma segura de autenticação, ele se sente mais confortável para inserir dados sensíveis, como dados de pagamento,
por exemplo.

Sendo assim, vamos ver quais são as principais formas de autenticação.

### Usuário e senha

Formulário simples onde o usuário insere o seu _username_ (ou e-mail) e a senha que ele mesmo configurou quando se 
cadastrou. Apesar de ser uma forma bastante simples e amplamente utilizada, não é muito segura pois basta um agente
malicioso descobrir a senha que ele terá acesso à conta do usuário. 

Além disso, é comum que usuários menos informados usem senhas óbvias (1234, senha, o próprio nome, aniversário de 
casamento, etc.), facilitando muito para usuários maliciosos.

Outro aspecto importante é que muitos usuários tendem a usar a mesma senha para diversos sites e aplicações diferentes,
o que se torna um problema quando ocorre vazamento da base de usuários de um desses sites. Uma pessoa mal intensionada
pode testar a combinação de e-mail e senha vazados em diversos outros sites.

Um detalhe importante é que as senhas dos usuários **não devem ser armazenadas em texto aberto**, e sim através de um
hash gerado usando um algoritmo de _hashing_. Um _algoritmo de hashing_ nada mais é do que uma função matemática que 
transforma um texto em outro texto totalmente diferente e de uma forma que é muito difícil fazer o caminho inverso (ou 
seja, descobrir o texto original a partir do texto gerado).

Para fazer a autenticação posteriormete, a aplicação irá pegar a senha informada pelo usuário, transformá-la em um
hash usando o mesmo algoritmo de _hashing_ e comparar com o hash contido no banco de dados.

Os principais algoritmos de hashing hoje em dia são:

* Bcrypt
* Scrypt
* Argon2
* PBKDF2

### Biometria

A autenticação por biometria consiste na utilização de uma característica física única do usuário para autenticação, 
como digital, reconhecimento facial, íriz ou até mesmo utilizando a voz. Essa é considerada uma das formas mais seguras
de autenticação.

### Autenticação em dois fatores

Essa é a forma mais comum de utilização hoje em dia e se utiliza de duas formas de autenticação para garantir que é o 
usuário que está se autenticando. Uma forma bastante comum é a solicitação de usuário/senha e depois a solicitação de um
código enviado para o e-mail ou smartphone do usuário.

### Autenticação por sessão

Forma _Stateful_ de autenticação, onde o usuário faz o processo de autenticação uma vez e é gerada uma sessão para ele 
na aplicação. É retornado para o usuário um _cookie_ que irá identificar a sessão do usuário na aplicação. Esse cookie
deve ser enviado a cada requisição para o servidor, que irá resgatar os dados da sessão do usuário.

Apesar de ser bastante prática pra obter os dados do usuário pela sessão, conforme a aplicação escalar, o gerenciamento
de sessões pode se tornar um desafio.

### Autenticação por token

O usuário fará o processo de autenticação uma vez e receberá um _token_ que irá identificar a autenticação daquele 
usuário. Cada nova requisição deverá enviar o token no cabeçalho para garantir que aquele usuário está autenticado. 
Esse token tem um tempo de vida útil geralmente curto (por volta de 10 minutos), após isso, ele expira e é necessário 
gerar um novo (pode-se repetir o processo de autenticação ou usar o _refresh token_).

Esse envio do token deve ser feito no cabeçalho das requisições, no campo _Authorization_:

    Authorization: Bearer TOKEN_JWT

Onde _TOKEN_JWT_ é o token gerado.

O processo de autorização ocorrerá a seguir: a cada processamento de requisição, a aplicação deverá verificar se aquele
usuário (identificado pelo token) tem autorização para acessar aquele endpoint/recurso.

Uma das principais formas de se trabalhar usando tokens é utilizando o padrão **JWT (JSON Web Token)**.

## Dependências

```XML
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Autenticação padrão do Spring Security

Assim que adicionamos a dependência do Spring Security, ele automaticamente bloqueia todos os endpoints e gera uma senha
de acesso aleatória. Para acessarmos os endpoints, precisamos então enviar "user" e a senha gerada para autenticação.

## Autenticação usando base de usuários

É claro que numa aplicação real, teremos diversos usuários com suas respectivas senhas. 

Primeiro, criamos uma _migration_ que irá criar a tabela de usuários:

```SQL
CREATE TABLE TB_USERS (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    LOGIN VARCHAR(100) NOT NULL UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL
);
```

A seguir, criamos a entidade que irá representar essa tabela. Essa classe precisa implementar a interface 
**UserDetails**:

```Java
@Entity
@Table(name="TB_USERS")
@NoArgsConstructor @AllArgsConstructor @Getter @EqualsAndHashCode(of="id")
public class User implements UserDetails {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String login;
  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getUsername() { return login; }

  @Override
  public boolean isAccountNonExpired() { return true; }

  @Override
  public boolean isAccountNonLocked() { return true; }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() { return true; }
  
}

```

Assim como um _repository_ para essa entidade, que deve conter um método que busca usuários pelo _username/login_:

```Java
public interface UserRepository extends JpaRepository<User, Long> {
  User findByLogin(String login);
}
```

A seguir, precisamos informar o Spring que ele deve usar essas classes como base de usuários. Para isso, precisamos 
criar uma classe de serviço que implemente _UserDetailsService_ e o método _loadUserByUsername()_, que deve usar
o _repository_ que acabamos de criar:

```Java
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}
```

Agora precisamos alterar as configurações do Spring Security para considerar essa nova forma de autenticação. Para isso,
criamos uma classe de configuração (_@Configuration_) que sobreescreve as configurações do Spring Security (
_@EnableWebSecurity_). Dentro dessa classe, criaremos três métodos que gerarão os _beans_ necessários:

* **SecurityFilterChain**: usaremos para definir que o Spring Security não deve mais esperar um CSRF e nem criar sessões
para usuários autenticados (_stateless_);
* **AuthenticationManager**: iremos gerar o AuthenticationManager através da classe injetada 
_AuthenticationConfiguration_ e de seu método _getAuthenticationManager()_;
* **PasswordEncoder**: por fim, iremos definir qual será o algoritmo de _hashing_ que o Spring deverá utilizar para
validar as senhas no banco de dados (no caso, utilizaremos o BCrypt).

```Java
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable() // Não é necessário quando utilizamos token
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desabilita a criação de sessões
                .and().build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
```

Iremos precisar de um DTO que receba os campos de login (usuário/senha):

```Java
public record AuthenticationDTO(
        @NotBlank String login, 
        @NotBlank String password
) {}
```

Agora que as configurações estão feitas, precisamos criar um endpoint para que o usuário possa fazer login usando
usuário/senha. Vamos criar um _controller_ com esse endpoint que recebe o DTO com os dados de autenticação, cria um 
token e então realiza a autenticação chamando o _authenticationManager.authenticate()_ passando o token gerado:

```Java
@RestController
@RequestMapping("/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var token = new UsernamePasswordAuthenticationToken(authenticationDTO.login(), authenticationDTO.password());
        var authentication = authenticationManager.authenticate(token);
        return ResponseEntity.ok().build();
    }
}
```