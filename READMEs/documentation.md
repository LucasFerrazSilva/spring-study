# Documentação

Quando construímos uma API, é fundamental disponibilizar uma documentação que possibilite aos usuários saber quais
são os endpoints disponíveis sem precisar olhar o código fonte da aplicação.

Existem diversas ferramentas que analisam o código da aplicação e geram essa documentação automaticamente. Além de
poupar o trabalho num primeiro momento, essas ferramentas são fundamentais no longo prazo, pois ela irá atualizar 
a documentação automaticamente conforme formos modificando os endpoints.

Uma das principais bibliotecas para Spring é o [SpringDoc](https://springdoc.org/).

## Dependência

```XML
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.1.0</version>
</dependency>
```

## Liberando no Spring Security

Precisamos adicionar a seguinte linha no método _securityFilterChain()_, da classe _SecurityConfiguration_:

```Java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                ...
                .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // SpringDoc  
                ...
                .build();
}
```