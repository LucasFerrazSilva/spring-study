# Spring Data JPA

O Spring Data JPA é uma biblioteca ORM que facilita na integração com bancos de dados.

## Configurações de conexão

Quando adicionamos a dependência do Spring Data JPA, o Spring espera que informemos os dados de conexão com uma base de
dados. Caso contrário, ele irá lançar um erro quando a aplicação for inicializada.

Configurações:

```
spring.datasource.url=jdbc:mysql://localhost/nome_da_base_de_dados
spring.datasource.username=usuario
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=validate
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