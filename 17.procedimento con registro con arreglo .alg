registo pessoa
    literal nome [ 3 ]
fimregisto
procedimento fazer2 ( pessoa p )
    escreva " 2 hola " , p .nome [ 0 ] + p .nome [ 1 ] , "\n"
fimprocedimento
procedimento fazer ( pessoa p )
    escreva " 1 hola " , p .nome [ 0 ] , p .nome [ 1 ] , "\n"
    p .nome [ 1 ] <- "Yeny R"
    fazer2 ( p )
fimprocedimento
inicio
    escreva "Codigo principal \n"
    pessoa p
    p .nome [ 0 ] <- "David D "
    p .nome [ 1 ] <- "David S "
    p .nome [ 2 ] <- "David B "
    fazer ( p )
fimalgoritmo

