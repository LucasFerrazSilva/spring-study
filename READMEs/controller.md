# Controllers

Para recebermos requisições, precisamos definir quais URLs nossa aplicação pode receber e como responder cada uma. Para
isso, usamos classes anotadas com **@Controller** ou **@RestController**. Além disso, definimos que um método é
responsável por uma URL/método HTTP usando a respectiva anotação + URL.

Exemplo:

```Java
@Controller
public class HelloWorldController {
    @GetMapping("/hello-world")
    @ResponseBody
    public String helloWorld() {
        return "Hello, world!";
    }
}
```

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

### Mapeamento de atributos

Quando anotamos um DTO com _@RequestBody_, o Spring faz o _binding_ dos campos recebidos com os atributos do DTO
automaticamente pelo nome. Porém, quando recebemos um campo do JSON com um padrão de nomenclatura diferente do 
recomendado (cammel case), podemos usar a anotação **@JsonAlias** para fazer o _binding_ manualmente.

**Exemplo**

Considere que estamos recebendo o seguinte JSON:

```JSON
{
    “produto_id” : 12,
    “data_da_compra” : “01/01/2022”
}
```

O DTO ficaria da seguinte forma:

```Java
public record DadosCompra(
    @JsonAlias({"produto_id", "id_produto"}) Long idProduto,
    @JsonAlias("data_da_compra") LocalDate dataCompra
){}
```

Note que a anotação pode receber mais de um valor de mapeamento (ou seja, tanto faz o campo estar como "produto_id" ou 
"id_produto", o Spring irá mapear para o campo _idProduto_).

### @JsonFormat

O Spring espera um padrão de datas específico para fazer o mapeamento para um _LocalDateTime_ (
"ano-mes-diaThora:minutos:segundos", ex: _2023-03-04T10:40:52_). Para usarmos um formato diferente, podemos usar a 
anotação _@JsonFormat(pattern="x")_.

Exemplo:
```Java
@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
LocalDateTime data;
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

Existem diversos códigos HTTP que podem ser utilizados. Esses códigos são agrupados em categorias, que são 
identificadas pelo primeiro número do código:

* **1XX**: informação. A requisição foi aceita ou está em andamento;
* **2XX**: confirmação. A ação foi concluída ou entendida;
* **3XX**: redirecionamento. Indica que algo mais foi feito para conclusão da solicitação;
* **4XX**: erro do cliente/requisição. Existe algum problema com a solicitação;
* **5XX**: erro no servidor. O servidor não conseguiu processar a solicitação.

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

#### created()

Quando um recurso é criado, é esperado que o retorno seja um 201 (Created). O detalhe é que esse retorno espera duas 
coisas:

* Uma URI do recurso (ou seja, a URI de um endpoint que permita acessar esse recurso)
* A representação do novo recurso criado

Para criar a URI, podemos usar a classe do Spring **UriComponentsBuilder** (que pode ser injetada pelo Spring)
e então usar os métodos _path()_, _buildAndExpand()_ e _toURI()_ para gerar a URI:

```Java
URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(doctor.getId()).toUri();
```

A representação do recurso deve ser passada para o _ResponseEntity_ usando o método _body()_.

Exemplo de um endpoint que adiciona um novo médico:

```Java
@PostMapping
@Transactional
public ResponseEntity add(@RequestBody @Valid DoctorCreateDTO doctorCreateDTO, UriComponentsBuilder uriBuilder) {
    Doctor doctor = new Doctor(doctorCreateDTO);
    repository.save(doctor);

    URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(doctor.getId()).toUri();
    DoctorInfosDTO dto = new DoctorInfosDTO(doctor);

    return ResponseEntity.created(uri).body(dto);
}
```

## Lidando com erros na API

É uma boa prática tentar retornar mensagens de erros claras e com somente as informações necessárias.

A primeira boa prática é dizer para o Spring não enviar o _stack trace_ nas mensagens de erro. Para isso, basta 
alterar a propriedade:

    server.error.include-stacktrace=never

Outra boa prática é criar uma classe que será responsável por tratar erros comuns, como recursos não encontrados, etc.

Essa classe deve conter a anotação **@RestControllerAdvice**. Dentro da classe, devemos criar um método específico 
para cada exceção. Para definir a exceção que um método irá tratar, usamos a anotação **@ExceptionHandler()**, passando
a exceção em questão como argumento.

### EntityNotFoundException

A classe **EntityNotFoundException** representa o famoso erro 404 (Not Found):

```Java
@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleError404() {
      return ResponseEntity.notFound().build();
    }
}
```

### MethodArgumentNotValidException

Para tratar erros de validação, ou seja, quando os valores enviados não estão no padrão esperado (_404 - Bad Request_), 
usamos a classe _MethodArgumentNotValidException_:

```Java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity handleError400(MethodArgumentNotValidException exception) {
    List<FieldError> errors = exception.getFieldErrors();

    List<ValidationExceptionDataDTO> errorsDTO = errors.stream().map(ValidationExceptionDataDTO::new).toList();

    return ResponseEntity.badRequest().body(errorsDTO);
}

private record ValidationExceptionDataDTO(String field, String message){
    public ValidationExceptionDataDTO(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}
```

Note que recebemos a exceção lançada como argumento no método. Isso é importante para quando precisamos de detalhes 
sobre o que levou a exceção a ser lançada.