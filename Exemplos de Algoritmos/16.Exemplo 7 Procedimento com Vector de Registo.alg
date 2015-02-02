registo pessoa
    literal nome
    inteiro idade
fimregisto
procedimento fazer2 ( pessoa p [ 3 ] )
    escreva " 2 hola " , p [ 0 ] .nome + p [ 1 ] .nome , "\n"
fimprocedimento
procedimento fazer ( pessoa p [ 3 ] )
    escreva " 1 hola " , p [ 0 ] .nome + p [ 1 ] .nome , "\n"
    p [ 1 ] .nome <- "Yeny R"
    fazer2 ( p )
fimprocedimento
inicio
    escreva "Codigo principal \n"
    pessoa p [ 3 ]
    p [ 0 ] .nome <- "David D "
    p [ 1 ] .nome <- "David S "
    p [ 2 ] .nome <- "David B "
    fazer ( p )
fimalgoritmo

