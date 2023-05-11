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

## @Controller

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

No exemplo acima, o método _helloWorld()_ é responsável por receber requisições do tipo GET para a URL "/hello-world" 
(_@GetMapping("/hello-world")_) e respondê-la com um texto simples (return "Hello, world!"). Vale ressaltar que se for 
feita uma requisição do tipo POST para a URL "/hello-world", o Spring não irá retornar nada, visto que não existe um
método para a combinação _URL + método HTTP_.

Note que o método também possui a anotação _@ResponseBody_. Ela é responsável por informar ao Spring que a resposta 
deve ser o retorno direto do método. Sem ela, o Spring iria buscar um template com mesmo nome que a String retornada.

**Obs**: Veremos mais a frente como responder de forma mais sofisticada, seja retornando um arquivo HTML ou uma resposta 
formatada (como um JSON ou XML).

### Principais anotações

* **@Controller**: define que aquela classe contém métodos que recebem e respondem requisições;
* **@RestController**: similar à @Controller, mas com a adição que todos os métodos são anotados com _@ResponseBody_;
* **@ResponseBody**: o retorno do método é retornado direto como resposta da requisição ao invés do Spring buscar um template
HTML com a String retornada;
* **@GetMapping("url")**: mapeia aquele método para receber requisições do tipo GET para a URL "url";
* **@PostMapping("url")**: similar ao @GetMapping, mas para requisições POST (também existe PUT, DELETE, etc.);
* **@RequestMapping("url")**: mapeamento somente da URL, sem especificar o método HTTP. Pode ser usada na classe para 
definir uma URL base para todos os métodos da mesma.