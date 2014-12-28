registo pessoa
    literal nome
    inteiro idade
fimregisto
procedimento fazer ( pessoa p )
    escreva "hola " , p.nome
fimprocedimento
inicio
    escreva "Codigo principal \n"
    pessoa p
    p.nome <- "David"
    p.idade <- 34
    fazer ( p )
fimalgoritmo

