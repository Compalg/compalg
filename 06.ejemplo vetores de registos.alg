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
    datos x [ 3 ]
    x [ 0 ] .varon <- verdadeiro
    x [ 0 ] .pessoa.nome <- "David"
    x [ 0 ] .pessoa.idade <- 34
    x [ 1 ] .varon <- falso
    x [ 1 ] .pessoa.nome <- "Silva"
    x [ 1 ] .pessoa.idade <- 38
    x [ 2 ] .varon <- verdadeiro
    x [ 2 ] .pessoa.nome <- "Barrera"
    x [ 2 ] .pessoa.idade <- 43
    inteiro t
    para t de 0 ate 2
        se x [ t ] .varon = verdadeiro entao
            escreva x [ t ] .pessoa.nome
        senao
            escreva x [ t ] .pessoa.idade
        fimse
    fimpara
fimalgoritmo

