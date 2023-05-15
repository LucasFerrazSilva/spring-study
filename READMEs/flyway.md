# Flyway

O Flyway é uma ferramenta de controle de versão do banco de dados, também chamada de ferramenta de migração 
(_Migrations_).

## Dependências

Para usarmos o flyway, precisamos adicionar a dependência do **flyway-core** e a dependência específica do banco de 
dados utilizado:

```XML
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

## Arquivos

O versionamento é feito através da criação de arquivos _SQL_, que devem ficar dentro de _src/main/resources/db/migration_.

**Atenção!** Antes de criar _migrations_, é importante parar a aplicação.

O nome dos arquivos precisa seguir o seguinte padrão:

```
VX__descricao-da-migracao.sql
```

Onde X é a versão (começando do 1 e incrementando a cada nova migração).

Após criar o arquivo e inserir os comandos SQL, basta subir a aplicação que o Flyway irá executar os scripts da nova
versão automaticamente.

```
Current version of schema `spring_study`: << Empty Schema >>
Migrating schema `spring_study` to version "1 - create-table-doctors"
```

O histórico das versão gerenciadas pelo Flyway pode ser verificado na tabela _flyway_schema_history_.

## Erro ao executar migração

Caso ocorra um erro ao executar uma migração (como por causa de um comando inválido, por exemplo), precisamos executar o
comando _delete from flyway_schema_history where success = 0;_ no banco de dados.

Pode acontecer de a migração ter criado tabelas/campos antes de ocorrer o erro. Nesse caso, o comando acima não irá 
resolver o problema. Uma forma prática (mas nem sempre possível) de resolver o problema é excluíndo a base de dados e 
então executar novamente as migrações desde o início.