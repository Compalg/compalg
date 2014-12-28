registo cadastro
    literal nome
    inteiro idade
fimregisto
registo datos
    logico varon
    cadastro pessoa
fimregisto
inicio
    escreva "Codigo principal \n"
    datos x
    x.varon <- verdadeiro
    x.pessoa.nome <- "David"
    x.pessoa.idade <- 34
    se x.varon = verdadeiro entao
        escreva x.pessoa.nome
    senao
        escreva x.pessoa.idade
    fimse
fimalgoritmo

