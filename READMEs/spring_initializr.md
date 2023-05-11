# Spring Initializer

Uma forma simples de criar um projeto Spring Boot é usando o [Spring Initializer](https://start.spring.io/). Nele,
podemos definir o gerenciador de dependências (Maven/Gradle), a linguagem de programação, a versão do Spring Boot,
informações do projeto (nome, descrição, grupo, etc.), as dependências necessárias, etc.

## Dependências comuns

* **Spring Web**: permite o desenvolvimento de aplicações web (Spring MVC)
* **Spring Boot** DevTools: reinicia a aplicação automaticamente sempre que alguma alteração for feita no código;
* **Lombok**: reduz o boilerplate da aplicação (gera automaticamente getters, setters, construtores, toString, etc.)
* **Spring Data JPA**: biblioteca ORM permite fácil comunicação com bancos de dados (baseado no Spring Data e no Hibernate)
* **Driver do banco de dados (ex: MySQL Driver)**: driver de conexão com o banco de dados da aplicação

## Arquivos gerados

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