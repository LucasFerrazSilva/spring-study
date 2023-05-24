# Build

Quando queremos disponibilizar a aplicação para que outras pessoas possam acessá-la, precisamos colocar a aplicação em
algum servidor, seja um servidor próprio (on-premise) ou em algum provedor (nuvem). Só que, para isso, precisamos
configurar a _build_ da aplicação.

## Configurações do banco de dados

Na maioria das vezes, as propriedades de conexão com o banco de dados em produção serão diferentes das propriedades no
ambiente de desenvolvimento. Por isso, é comum que exista um _application.properties_ específico para as configurações
de produção, chamado **application-prod.properties**:

```properties
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${USERNAME}
spring.datasource.password=${PASSWORD}

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```

Lembrando que só precisamos alterar as propriedades que serão diferentes em relação ao ambiente de desenvolvimento, as
que são iguais serão carregadas do _application.properties_ (primeiro o Spring carrega o _application.properties_ e
depois sobreescreve as propriedades usando o arquivo específico do contexto, como testes ou produção).

## Empacotamento

Para gerar o arquivo que será disponibilizado no servidor, iremos usar o comando **package** do maven, que primeiro irá 
rodar os testes e então gerar o arquivo **.jar** dentro da pasta _target_.

## Executando o arquivo .jar

Para que o Spring saiba qual contexto (ou _profile_) ele deve considerar, precisamos especificar na linha de comando
quando formos executar o jar

```shell
java "-Dspring.profiles.active=prod" -jar target\spring-study-0.0.1.jar
```

Além disso, é necessário configurar as variáveis de ambiente necessárias (URL do banco, username, etc.). Podemos passar
essas variáveis como parâmetro usando o -D:

```shell
java "-Dspring.profiles.active=prod" "-DDATASOURCE_URL=jdbc:mysql://localhost/spring_study" "-DUSERNAME=spring_study_user" "-DPASSWORD=spring_study_user" -jar target\spring-study-0.0.1.jar
```