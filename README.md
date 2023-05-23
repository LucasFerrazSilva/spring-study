# Spring Study

Projeto criado para estudar o ecossistema Spring.

## Spring Framework

O Spring Framework é um ecossistema composto de diversos frameworks que auxiliam na resolução de diversos problemas
comuns do mundo Java. Seu principal framework é o Spring MVC, que facilita o desenvolvimento de aplicações Web.

Principais frameworks do ecossistema Spring:

* Spring MVC
* Spring Boot
* Spring Data
* Spring Cloud
* Spring Security
* Sping Validation

### Spring MVC

Apesar de ser considerado muito melhor do que as outras opções de frameworks para esse intúito, ele ainda requer
bastante configurações, o que gera bastante trabalho "desnecessário". Inicialmente, essas configurações eram feitas
através de diversos arquivos de configurações (XML). Para facilitar, uma nova versão foi disponibilizada onde
as configurações podiam ser feitas através de classes Java, mas o trabalho de configuração ainda era grande.

A solução definitiva do Spring foi criar um novo framework em cima do Spring MVC chamado *Spring Boot*, que substitui
os diversos arquivos de configuração por convenções. O Spring entende essas convenções através das dependências
*starter* de cada módulo.

Outra grande diferença é que o Spring Boot traz um container integrado (por padrão o Tomcat) que permite subir a
aplicação rapidamente, removendo a necessidade de configurarmos um servidor manualmente.

## Spring Initializer

Uma forma simples de criar um projeto Spring Boot é usando o [Spring Initializer](https://start.spring.io/). Nele,
podemos definir o gerenciador de dependências (Maven/Gradle), a linguagem de programação, a versão do Spring Boot,
informações do projeto (nome, descrição, grupo, etc.), as dependências necessárias, etc.

Para mais informações, veja [esse README](READMEs/spring_initializr.md).

## pom.xml

Ao analisar o *pom.xml* gerado pelo *Spring Initializr*, vemos que não temos uma dependência do Spring Boot. Isso porque
ele é o *parent* da nossa aplicação, ou seja, nosso projeto herda as configurações do Spring Boot.

```XML
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.6</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

## SpringBootApplication

Para inicializarmos uma aplicação usando o Spring Boot, basta executar a classe que contenha a anotação
_@SpringBootApplication_ e contenha um método _main_ que chame
_SpringApplication.run(NomeDaClasseApplication.class, args)_:

```Java
@SpringBootApplication
public class SpringStudyApplication {    
    public static void main(String[] args) {
        SpringApplication.run(SpringStudyApplication.class, args);
    }    
}
```

## Controllers

Para recebermos requisições, precisamos definir quais URLs nossa aplicação pode receber e como responder cada uma. Para
isso, usamos classes anotadas com **@Controller** ou **@RestController**. Além disso, definimos que um método é
responsável por uma URL/método HTTP usando a respectiva anotação + URL.

Exemplo:

```Java
@Controller
public class HelloWorldController {

    @GetMapping("/hello-world")
    @ResponseBody
    public String helloWorld() {
        return "Hello, world!";
    }
}
```

Para mais informações, veja o README específico sobre controllers [aqui](READMEs/controller.md).

## CORS

O CORS (acrônimo de _Cross-Origin Resource Sharing_) é a política da aplicação sobre compartilhamento de recursos entre
diferentes origens. Em outras palavras, é a permissão que a aplicação dá para acesso de endpoints para requisições de
origens diferentes da aplicação em si.

Para mais informações, veja o [README sobre CORS](READMEs/CORS.md)

## Spring Data JPA

O **Spring Data JPA** é uma biblioteca ORM que facilita na integração com bancos de dados.

Para mais informações, veja [esse README](READMEs/spring_data_jpa.md).

## Flyway

O Flyway é uma ferramenta de controle de versão do banco de dados, também chamada de ferramenta de migração
(_Migrations_).

Para mais informações, veja [esse README](READMEs/flyway.md).

## Validation

O Spring Validation é a biblioteca que adiciona diversas anotações úteis que auxiliam na validação dos dados recebidos.

Para mais informações, veja [esse README](READMEs/validation.md).

## Spring Secutiry

O **Spring Security** é o módulo do Spring Framework responsável por tratar da segurança das aplicações.

Para mais informações, veja [esse README](READMEs/security.md).

## OpenAPI

A **OpenAPI** é uma especificação sobre como documentar APIs. Ela é muito útil para que os clientes da sua API possam
saber mais sobre os endpoints disponíveis sem precisarem acessar o código fonte da aplicação.

Para mais informações, veja [esse README](READMEs/documentation.md).

## Testes automatizados

Os testes automatizados são fundamentais nas aplicação por garantirem que todas as funcionalidades da aplicação estão
funcionando corretamente, garantindo maior confiabilidade do código e maior segurança caso seja necessário fazer alguma
alteração nele futuramente.

Para mais informações, veja [esse README](READMEs/tests.md);