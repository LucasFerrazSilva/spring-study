# Documentação

Quando construímos uma API, é fundamental disponibilizar uma documentação que possibilite aos usuários saber quais
são os endpoints disponíveis sem precisar olhar o código fonte da aplicação. Até 2010, não havia um consenso sobre
como deveria ser essa documentação, até que surgiu a especificação [Swagger](https://swagger.io/), que foi
amplamente adotado como o padrão de documentação de APIs. Posteriormente, essa especificação foi renomeada para 
[OpenAPI](https://www.openapis.org/).

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

## JWT na documentação

Para informarmos na documentação que certos _endpoints_ exigem um token JWT, precisamos criar uma classe de configuração
_SpringDocConfigurations_ e adicionar um _Bean_ que adiciona a configuração de _bearer-key_:

```Java
@Configuration
public class SpringDocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
```

Após isso, precisamos anotar os endpoints que requerem um token JWT. Podemos anotar os métodos individualmente ou a 
classe para que aplique para todos os métodos da mesma:

```Java
@SecurityRequirement(name="bearer-key")
public class AppointmentController { ... }

// Ou

@GetMapping("/{id}")
@SecurityRequirement(name="bearer-key")
public ResponseEntity<DoctorInfosDTO> get(@PathVariable Long id) { ... }
```

## Outras configurações importantes

Além de configurar o token, também é importante adicionar nas configurações os dados da API e a licença:

```Java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .components(new Components()
                    .addSecuritySchemes("bearer-key",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")))
                    .info(new Info()
                            .title("Spring Study API")
                            .description("API Rest da aplicação Spring Study, contendo as funcionalidades de CRUD de médicos e de pacientes, além de agendamento e cancelamento de consultas")
                            .contact(new Contact()
                                    .name("Time Backend")
                                    .email("backend@springstudy.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://springstudy/api/licenca")));
}
```

Para mais informações sobre essas e outras configurações, veja a 
[documentação oficial](https://spec.openapis.org/oas/latest.html#schema).