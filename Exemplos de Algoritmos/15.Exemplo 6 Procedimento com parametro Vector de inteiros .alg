registo pessoa
    literal nome
    inteiro idade
fimregisto
procedimento fazer2 ( inteiro p [ 3 ] )
    escreva " 2 hola " , p [ 0 ] + p [ 1 ]
fimprocedimento
procedimento fazer ( inteiro p [ 3 ] )
    escreva " 1 hola " , p [ 0 ] + p [ 1 ]
    p [ 1 ] <- 4
    fazer2 ( p )
fimprocedimento
inicio
    escreva "Codigo principal \n"
    inteiro p [ 3 ]
    p [ 0 ] <- 3
    p [ 1 ] <- 6
    p [ 2 ] <- 9
    fazer ( p )
fimalgoritmo

