registo pc
    literal marca
    real custo
fimregisto
registo cadastro
    literal nome
    inteiro idade
    pc computadoras [ 5 ]
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
    x [ 0 ] .pessoa.computadoras [ 0 ] .marca <- "Lenovo"
    x [ 0 ] .pessoa.computadoras [ 0 ] .custo <- 1020.0
    x [ 0 ] .pessoa.computadoras [ 1 ] .marca <- "Dell"
    x [ 0 ] .pessoa.computadoras [ 1 ] .custo <- 2020.0
    x [ 1 ] .varon <- falso
    x [ 1 ] .pessoa.nome <- "Yeny"
    x [ 1 ] .pessoa.idade <- 30
    x [ 1 ] .pessoa.computadoras [ 0 ] .marca <- "Sansung"
    x [ 1 ] .pessoa.computadoras [ 0 ] .custo <- 3020.0
    x [ 1 ] .pessoa.computadoras [ 1 ] .marca <- "HP"
    x [ 1 ] .pessoa.computadoras [ 1 ] .custo <- 4020.0
    x [ 2 ] .varon <- verdadeiro
    x [ 2 ] .pessoa.nome <- "Carlos"
    x [ 2 ] .pessoa.idade <- 43
    x [ 2 ] .pessoa.computadoras [ 0 ] .marca <- "Toshiba"
    x [ 2 ] .pessoa.computadoras [ 0 ] .custo <- 5020.0
    x [ 2 ] .pessoa.computadoras [ 1 ] .marca <- "IBM"
    x [ 2 ] .pessoa.computadoras [ 1 ] .custo <- 6020.0
    inteiro t , k
    para t de 0 ate 2
        se x [ t ] .varon = verdadeiro entao
            escreva x [ t ] .pessoa.nome + " é home"
        senao
            escreva x [ t ] .pessoa.nome + " é mulher"
        fimse
        escreva " e ten " , x [ t ] .pessoa.idade , " anos"
        escreva "\n Suas computadoras são:"
        para k de 0 ate 1
            escreva "\n --" , x [ t ] .pessoa.computadoras [ k ] .marca , " com custo " , x [ t ] .pessoa.computadoras [ k ] .custo
        fimpara
        escreva "\n"
    fimpara
fimalgoritmo

