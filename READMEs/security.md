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

Um token nada mais é do que um texto que representa, de maneira segura, informações que serão compartilhadas entre dois
sistemas. No caso de autenticação, o token irá conter dados do usuário autenticado.

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

Um JWT é composto por:

* **header**: cabeçalho do token que contém o _alg_ (algoritmo usado para criar a assinatura do token) e o _typ_ (tipo 
do token - no caso, JWT);
* **payload**: dados da autenticação (como email e senha, por exemplo);
* **signature**: a _assinatura_ do token, formata pela codificação do header e do payload somada a uma chave secreta
(única da aplicação). Essa assinatura é gerada pelo algoritmo especificado no cabeçalho.

Cada uma dessas partes é separada por um ponto (_header.payload.signature_). 

Exemplo:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

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

## Utilizando o JWT

Podemos obter diversas informações sobre o padrão **JWT** no site [jwt.io](https://jwt.io/). Lá, podemos ver diversas
bibliotecas para diversas linguagens diferentes. Iremos usar a [biblioteca da auth0](https://github.com/auth0/java-jwt):

```XML
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.2.1</version>
</dependency>
```

### Geração do token após autenticação por usuario/senha

Para gerarmos os tokens, primeiro precisamos criar uma _secret key_ da aplicação. Ela será usada para encriptar o token 
para garantir que um agente malicioso que intercepte os dados da requisição não descubra os dados do usuário. 

Para isso, adicionamos no _application.properties_ uma propriedade que irá receber como valor uma _variável de 
ambiente_. Isso porque não é uma boa prática deixar dados sensíveis (como _secret key_, senhas, etc.) diretamente no 
código. Ao invés disso, a gente cria variáveis de ambientes e pede para o Spring buscar esses valores em tempo de 
execução. Basta definir o valor da propriedade como ${NOME_DA_VARIAVEL_DE_AMBIENTE:Valor default}:

```
api.security.token.secret=${JWT_SECRET:12345678}
```

Precisamos criar uma classe de serviço para geração dos tokens que irá utilizar a _secret key_ criada:

```Java
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    
    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret); // Algoritmo de encriptação. Recebe como argumento a secret key da aplicação
            String token = JWT.create()
                            .withIssuer("Spring Study") // Define a aplicação que gerou o token
                            .withSubject(user.getLogin()) // Identifica o usuário
                            .withClaim("id", user.getId()) // Podemos adicionar qualquer informação relevante usando o withClaim
                            .withExpiresAt(generateExpirationDate()) // Define a data/hora de expiração do token
                            .sign(algorithm) // Define o algoritmo de encriptação do token;
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar um JWT", exception);
        }
    }
    
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
```

É uma boa prática criar um DTO para devolver o token:

```Java
public record TokenDTO(String token) {}
```

Agora basta usar no _controller_ a classe de serviço criada:

```Java
@PostMapping
public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
    var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.login(), authenticationDTO.password());
    var authentication = authenticationManager.authenticate(authenticationToken);
    var token = tokenService.generateToken((User) authentication.getPrincipal());
    var tokenDTO = new TokenDTO(token);
    return ResponseEntity.ok(tokenDTO);
} 
```

Agora, quando fazemos uma requisição para _/login_ passando um email/senha correto (exemplo):

```JSON
{
    "login": "ana.souza@mail.com",
    "password": "123456"
}
```

Teremos a seguinte resposta:

```JSON
{
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmEuc291emFAbWFpbC5jb20iLCJpc3MiOiJTcHJpbmcgU3R1ZHkiLCJpZCI6MSwiZXhwIjoxNjg0NDI1OTQzfQ.dhyOMuNWglUYVfQasTSOdMpZs74puraLnH-HrfVpkNg"
}
```

### Validando o token e usando-o para autenticação do usuário

Agora que nossa rotina de geração de token está funcionando, precisamos ajustar a aplicação para usá-los como forma 
de autenticação dos usuários.

Primeiro, vamos adicionar um método no _TokenService_ que valide e extraia o _subject_ do token:

```Java
public String extractSubject(String token) {
    try {
        var algorithm = Algorithm.HMAC256(secret);
        var verifier = JWT.require(algorithm)
                        .withIssuer("Spring Study")
                        .build();
        var decodedJWT = verifier.verify(token); // Valida o token

        return decodedJWT.getSubject(); // Pega o subject
    } catch (JWTVerificationException exception) {
        throw new RuntimeException("Token JWT inválido ou expirado!");
    }
}
```

Depois, vamos criar um **Filter** que irá interceptar as requisições antes delas serem processadas pelo Spring para 
verificar se o token foi enviado, validá-lo e informar ao Spring que foi feita a autenticação do usuário. Essa classe 
precisa ser anotada com _@Component_ e extender a classe _OncePerRequestFilter_ para que o Spring saiba que se trata 
de um _filter_.

```Java
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization"); // Pega o token do cabeçalho da requisição
        if (token != null) {
            var subject = tokenService.extractSubject(token.replace("Bearer ", ""));
            var user = userRepository.findByLogin(subject);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); // Autentica o usuário no Spring
        }

        filterChain.doFilter(request, response);  // Continua a execução. Se não chamar, a requisição é interrompida e é enviada uma resposta 200 pro usuário
    }
}
```

Por fim, precisamos informar o Spring que esse filtro deve ser feito antes do seu próprio filtro para que ele não
barre as requisições antes de validarmos o token:

```Java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .csrf().disable() // Não é necessário quando utilizamos token
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desabilita a criação de sessões
            .and().authorizeHttpRequests() // Informa que iremos personalidar as autorizações das requisições
            .requestMatchers(HttpMethod.POST, "/login").permitAll() // Define que qualquer um pode acessar o endpoint /login
            .anyRequest().authenticated() // Exige autorização dos demais endpoints
            .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Coloca a nossa classe filter antes da própria classe do Spring
            .build();
}
```

Agora podemos definir os níveis de **autorização** usando as _roles_ dos usuários autenticados usando o método 
_hasRole()_:

```Java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST, "/login").permitAll()
        .requestMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

Ou ainda, podemos anotar os métodos desses endpoints com a anotação **@Secured("ROLE_X")**:

```Java
@GetMapping("/{id}")
@Secured("ROLE_ADMIN")
public ResponseEntity detalhar(@PathVariable Long id) {
    var medico = repository.getReferenceById(id);
    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
}
```

Essa anotação também pode ser anotada na classe, fazendo com que seja aplicada em todos os métodos da mesma.

**Observação**: para que seja possível usar essas anotações, precisamos adicionar a anotação 
**@EnableMethodSecurity(securedEnabled = true)** na classe _SecurityConfigurations_.