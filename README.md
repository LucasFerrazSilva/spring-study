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

Para mais informações, veja [esse README](READMEs/spring_initializr.md).

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

Para mais informações, veja o README específico sobre controllers [aqui](READMEs/controller.md).

## CORS

O CORS (acrônimo de _Cross-Origin Resource Sharing_) é a política da aplicação sobre compartilhamento de recursos entre 
diferentes origens. Em outras palavras, é a permissão que a aplicação dá para acesso de endpoints para requisições de
origens diferentes da aplicação em si. 

Para mais informações, veja o [README sobre CORS](READMEs/CORS.md)

## Spring Data JPA

O **Spring Data JPA** é uma biblioteca ORM que facilita na integração com bancos de dados. 

Para mais informações, veja [esse README](READMEs/spring_data_jpa.md).