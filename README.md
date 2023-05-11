# Spring Study

Projeto criado para estudar o ecossistema Spring.

## Spring Framework

O Spring Framework (ou, *Spring MVC*) é um framework de Java que facilita o desenvolvimento de aplicações Web.

Apesar de ser considerado muito melhor do que as outras opções de frameworks para esse intúito, ele ainda requer 
bastante configurações, o que gera bastante trabalho "desnecessário". Inicialmente, essas configurações eram feitas 
através de diversos arquivos de configurações (XML). Para tentar facilitar, uma nova versão foi disponibilizada onde
as configurações podiam ser feitas através de classes Java, mas o trabalho de configuração ainda era grande. A solução 
definitiva do Spring foi criar um novo framework em cima do Spring MVC chamado *Spring Boot*, que substitui os 
diversos arquivos de configuração por convenções. O Spring entende essas convenções através das dependências *starter* 
de cada módulo.

Outra grande diferença é que o Spring Boot traz um container integrado (por padrão o Tomcat) que permite subir a aplicação rapidamente, 
removendo a necessidade de configurarmos um servidor manualmente.

Principais módulos:

* Spring MVC
* Spring Boot
* Spring Data
* Spring Cloud
* Spring Security
* Sping Validation

## Spring Initializer

Uma forma simples de criar um projeto Spring Boot é usando o [Spring Initializer](https://start.spring.io/). Nele, 
podemos definir o gerenciador de dependências (Maven/Gradle), a linguagem de programação, a versão do Spring Boot, 
informações do projeto (nome, descrição, grupo, etc.), as dependências necessárias, etc.

### Dependências comuns

* **Spring Web**: permite o desenvolvimento de aplicações web (Spring MVC)
* **Spring Boot** DevTools: reinicia a aplicação automaticamente sempre que alguma alteração for feita no código;
* **Lombok**: reduz o boilerplate da aplicação (gera automaticamente getters, setters, construtores, toString, etc.)
* **Spring Data JPA**: biblioteca ORM permite fácil comunicação com bancos de dados (baseado no Spring Data e no Hibernate)
* **Driver do banco de dados (ex: MySQL Driver)**: driver de conexão com o banco de dados da aplicação

### Arquivos gerados

O Spring Initializer irá gerar um zip contendo diversos arquivos:

* **.mvn**: arquivos maven necessários
* **NomeDoProjetoApplication.java**: classe contendo o método de inicialização da aplicação, dentro da pasta _src/main/java/.../NomeDoProjetoApplication.java_
* **NomeDoProjetoApplicationTests.java**: classe de testes, dentro da pasta _src/main/test/.../NomeDoProjetoApplicationTests.java_
* **resources**: pasta onde os arquivos de recursos ficarão, como o application.properties e outras subpastas
  * **static**: pasta contendo arquivos estáticos, como arquivos JS, CSS, imagens, etc.
  * **templates**: pasta contendo templates HTML
* **.gitignore**
* **mvn/mvn.cmd**
* **pom.xml**

## pom.xml

Ao analisar o *pom.xml* gerado pelo *Spring Initializr*, vemos que não temos uma dependência do Spring Boot. Isso porque
ele é o *parent* da nossa aplicação, ou seja, nosso projeto herda as configurações do Spring Boot.

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.6</version>
        <relativePath/> <!-- lookup parent from repository -->
	</parent>

## SpringBootApplication

Para inicializarmos uma aplicação usando o Spring Boot, basta executar a classe que contenha a anotação 
_@SpringBootApplication_ e contenha um método _main_ que chame 
_SpringApplication.run(NomeDaClasseApplication.class, args)_:
    
    @SpringBootApplication
    public class SpringStudyApplication {    
        public static void main(String[] args) {
            SpringApplication.run(SpringStudyApplication.class, args);
        }    
    }

## Controllers

Para recebermos requisições, precisamos definir quais URLs nossa aplicação pode receber e como responder cada uma. Para 
isso, usamos classes anotadas com **@Controller** ou **@RestController**. Além disso, definimos que um método é 
responsável por uma URL/método HTTP usando a respectiva anotação + URL.

Exemplo:

    @Controller
    public class HelloWorldController {
    
        @GetMapping("/hello-world")
        @ResponseBody
        public String helloWorld() {
            return "Hello, world!";
        }
    
    }

Para mais informações, veja o README específico sobre controllers [aqui](src/main/java/br/com/ferraz/springstudy/controller/README.md).

## CORS

O CORS (acrônimo de _Cross-Origin Resource Sharing_) é a política da aplicação sobre compartilhamento de recursos entre 
diferentes origens. Em outras palavras, é a permissão que a aplicação dá para acesso de endpoints para requisições de
origens diferentes da aplicação em si. A **origem** é definda pela combinação do protocolo utilizado (http/https), porta 
e host. 

Exemplo: considerando "https://site.com.br/caminho1" como URL da aplicação, veja o resultado de requisições das 
seguintes origens.

| URL                               | Resultado    | Motivo                   |
|-----------------------------------|--------------|--------------------------|
| https://site.com.br/caminho2      | Mesma origem | Só o caminho é diferente | 
| http://site.com.br/caminho2       | Erro         | Protocolo diferente      | 
| https://outrosite.com.br/caminho1 | Erro         | Host diferente           | 

Um cenário muito comum é termos uma aplicação back-end rodando em uma porta e uma aplicação front-end, que consome a
outra, rodando no mesmo endereço mas em outra porta. Nesse caso, teremos erro de CORS. Para que essa interação funcione,
é necessário que, quando a aplicação Front-end enviar uma requisição para a API, a API retorne no header da resposta um
campo **Access-Control-Allow-Origin** contendo o host + porta da aplicação front-end (ex: Access-Control-Allow-Origin: 
http://localhost:3000).

É possível usar o asterisco (*) para definir que a API pode ser acessada por qualquer origem, mas isso não é recomendado 
para qualquer aplicação, somente para APIs públicas.

Para informar o Spring sobre as políticas de CORS da aplicação, precisamos criar uma classe de configuração que 
implemente **WebMvcConfigurer** e define todas as origens e métodos aceitos:

```Java
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
```

Note que podemos especificar endpoints específicos que podem ou não ser acessados por outras origens. Quando queremos 
liberar para todos os endpoints da aplicação, podemos simplesmente usar **"/\*\*"**;
