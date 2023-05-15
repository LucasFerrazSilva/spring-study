# Spring Validation

O Spring Validation é a biblioteca que adiciona diversas recursos úteis que auxiliam na validação dos dados recebidos.

## Anotações

A forma mais simples de usar essa biblioteca é usando as anotações.

Principais anotações:

* **@NotNull**: campo não pode ser nulo;
* **@NotBlank**: campo não pode estar vazio (String);
* **@Email**: verifica se o campo está no formato de um e-mail;
* **@Pattern(regexp=expressaoRegular)**: verifica se o campo está de acordo com a _expressão regular_ informada (ex: 
_@Pattern(regexp="\\d{4,6}")_ -> Verifica se o campo possui de 4 a 6 dígitos)
* **@Valid**: solicita ao Spring Validation que verifique se os campos do objeto são validos

Podemos passar para essas anotações a propriedade **message**, que define uma mensagem de erro customizada caso a 
validação não seja atendida.

Vale ressaltar que, para o Spring Validation validar um objeto, precisamos anotá-lo com **@Valid**.

Para verificar todas as anotações disponíveis, veja a 
[documentação oficial](https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html#builtinconstraints).

## Erro

Quando a validação não é atendida, o Spring irá retornar um erro 400 (Bad Request) junto com informações da razão do 
erro (qual a validação não foi atendida, a mensagem de erro, etc.).

Exemplo:

```JSON
{
    "timestamp": "2023-05-15T21:16:02.717+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": "org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument ...",
    "message": "Validation failed for object='doctorCreateDTO'. Error count: 1",
    "errors": [
        {
            "codes": [
                "NotBlank.doctorCreateDTO.name",
                "NotBlank.name",
                "NotBlank.java.lang.String",
                "NotBlank"
            ],
            "arguments": [
                {
                    "codes": [
                        "doctorCreateDTO.name",
                        "name"
                    ],
                    "arguments": null,
                    "defaultMessage": "name",
                    "code": "name"
                }
            ],
            "defaultMessage": "O nome não pode estar vazio.",
            "objectName": "doctorCreateDTO",
            "field": "name",
            "rejectedValue": "",
            "bindingFailure": false,
            "code": "NotBlank"
        }
    ],
    "path": "/medicos"
}
```