# Controllers

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

## Principais anotações

* **@Controller**: define que aquela classe contém métodos que recebem e respondem requisições;
* **@RestController**: similar à @Controller, mas com a adição que todos os métodos são anotados com _@ResponseBody_;
* **@ResponseBody**: o retorno do método é retornado direto como resposta da requisição ao invés do Spring buscar um template
  HTML com a String retornada;
* **@GetMapping("url")**: mapeia aquele método para receber requisições do tipo GET para a URL "url";
* **@PostMapping("url")**: similar ao @GetMapping, mas para requisições POST (também existe PUT, DELETE, etc.);
* **@RequestMapping("url")**: mapeamento somente da URL, sem especificar o método HTTP. Pode ser usada na classe para
  definir uma URL base para todos os métodos da mesma;
* **@RequestBody DTO obj**: indica ao Spring que o _objeto_ deve ser preenchido com os dados presentes no corpo da 
requisição.  

## Data Transfer Object (DTO)

O DTO (ou _Data Transfer Object_) é um padrão de projeto que recomenda a utilização de classes auxiliares para 
recebimento e envio de dados nos endpoints ao invés de receber os dados diretamente na classe da entidade em questão.

```Java
@PostMapping
public String add(@RequestBody ObjectDTO obj) { ... }
```

Para facilitar a criação desse tipo de classe, o Java disponibilizou na sua versão 16 os _records_, que geram 
automaticamente classes imutáveis com todo o boilerplate necessário para classes do tipo DTO, como getters, construtor, 
toString, etc.:

```Java
public record ObjectDTO (String field1, Integer field2, AnotherObject anotherObject) {}
```