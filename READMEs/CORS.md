# CORS

O CORS (acrônimo de _Cross-Origin Resource Sharing_) é a política da aplicação sobre compartilhamento de recursos entre
diferentes origens. Em outras palavras, é a permissão que a aplicação dá para acesso de endpoints para requisições de
origens diferentes da aplicação em si. A **origem** é definda pela combinação do protocolo utilizado (http/https), porta
e host.

Exemplo: considerando "https://site.com.br/caminho1" como URL da aplicação, veja o resultado de requisições das
seguintes origens.

| URL                               | Resultado    | Motivo                   |
|-----------------------------------|--------------|--------------------------|
| https://site.com.br/caminho2      | Mesma origem | Só o caminho é diferente | 
| http://site.com.br/caminho2       | Erro         | Protocolo diferente      | 
| https://outrosite.com.br/caminho1 | Erro         | Host diferente           | 

Um cenário muito comum é termos uma aplicação back-end rodando em uma porta e uma aplicação front-end, que consome a
outra, rodando no mesmo endereço mas em outra porta. Nesse caso, teremos erro de CORS. Para que essa interação funcione,
é necessário que, quando a aplicação Front-end enviar uma requisição para a API, a API retorne no header da resposta um
campo **Access-Control-Allow-Origin** contendo o host + porta da aplicação front-end (ex: Access-Control-Allow-Origin:
http://localhost:3000).

É possível usar o asterisco (*) para definir que a API pode ser acessada por qualquer origem, mas isso não é recomendado
para qualquer aplicação, somente para APIs públicas.

Para informar o Spring sobre as políticas de CORS da aplicação, precisamos criar uma classe de configuração que
implemente **WebMvcConfigurer** e define todas as origens e métodos aceitos:

```Java
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
```

Note que podemos especificar endpoints específicos que podem ou não ser acessados por outras origens. Quando queremos
liberar para todos os endpoints da aplicação, podemos simplesmente usar **"/\*\*"**;