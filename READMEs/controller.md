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

## @PathVariable

Quando trabalhamos com um elemento específico, é comum que seja passado na URL o ID do elemento. Esse ID então se torna
uma _variável de caminho_ (_Path Variable_). Para obtermos essa variável, basta receber um parâmetro no método e 
anotá-lo com **@PathVariable**:

```Java
@DeleteMapping("/{id}")
@Transactional
public String delete(@PathVariable(name="id") Long id) {
    ...
}
```

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

### Mass Assignment Attack

O padrão DTO é fundamental para evitar o **Mass Assignment Attack**, que consiste no usuário malicioso enviar uma 
requisição alterando um campo que não deveria ser alterado.

Vamos considerar uma classe Usuário, que contém o atributo booleano _admin_:

```Java
public class Usuario {
    private String nome;
    private String email;
    private boolean admin;
    
    ...
  
    public void setAsAdmin() {
        this.admin = true;
    }
}
```

Esse atributo teoricamente só deveria ser alterado com uma chamada específica, mas, no endpoint de atualização de dados 
básicos (como nome, email, etc.), o usuário passa também o campo admin:

```JSON
{
  "nome": "José Silva",
  "admin": true
}
```

Como estamos trabalhando diretamente com o objeto, essa requisição irá alterar atributos que não deveria. Por isso, 
utilizar um DTO sem o campo admin seria a melhor opção.

## PUT x PATCH

Uma dúvida comum é quando utilizar requisições PUT ou PATCH, visto que ambas lidam com a atualização de um elemento 
existente. A diferença entre as duas é realmente pequena: o PUT tem como finalidade a atualização total de um elemento
(ou seja, a modificação de todos os atributos do mesmo), enquanto o PATCH deve ser utilizado quando queremos fazer uma 
alteração partical do elemento (somente alguns atributos). Porém, nem sempre é possível saber com certeza quantos campos
serão de fato alterados, por isso, é mais comum que o método PUT seja sempre utilizado quando estamos atualizando um 
elemento.

## Retorno Correto da API

Quando nossa API responde uma requisição, a resposta deve estar de acordo com a situação.

Alguns exemplos de códigos HTTP e quando devem ser utilizados:

* **500 (Internal Server Error)**: ocorreu algum erro interno da aplicação durante a execução da requisição;
* **404 (Not Found)**: o recurso solicitado não foi encontrado;
* **200 (Ok)**: a requisição foi executada corretamente;
* **201 (Created)**: a requisição foi executada corretamente e um novo recurso foi criado;
* **204 (No Content)**: a requisição foi executada corretamente, mas nenhum conteúdo precisa ser retornado como 
resposta.

Para uma lista completa de todos os códigos de status HTTP, veja 
[esse artigo](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status).

### ResponseEntity

No Spring, ao invés do método retornar uma simples String, ou uma listagem, o recomendo é retornar um 
**ResponseEntity**, que irá conter o conteúdo do retorno e também o código HTTP correto. O Spring disponibiliza 
diversos métodos estáticos com os principais retornos (_ok(), notFound(), badRequest()_, etc.)

```Java
@DeleteMapping("/{id}")
@Transactional
public ResponseEntity delete(@PathVariable(name="id") Long id) {
    Doctor doctor = repository.getReferenceById(id);
    doctor.inactivate();
    return ResponseEntity.noContent().build();
}
```