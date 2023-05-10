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

Dependências comuns:

* Spring Web: permite o desenvolvimento de aplicações web (Spring MVC)
* Spring Boot DevTools: reinicia a aplicação automaticamente sempre que alguma alteração for feita no código;
* Lombok: reduz o boilerplate da aplicação (gera automaticamente getters, setters, construtores, toString, etc.)
* Spring Data JPA: biblioteca ORM permite fácil comunicação com bancos de dados (baseado no Spring Data e no Hibernate)
* Driver do banco de dados (ex: MySQL Driver): driver de conexão com o banco de dados da aplicação

## pom.xml

Ao analisar o *pom.xml* gerado pelo *Spring Initializr*, vemos que não temos uma dependência do Spring Boot. Isso porque
ele é o *parent* da nossa aplicação, ou seja, nosso projeto herda as configurações do Spring Boot.

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.6</version>
        <relativePath/> <!-- lookup parent from repository -->
	</parent>
