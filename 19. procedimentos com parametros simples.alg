registo datos
    logico varon
    literal pessoa
fimregisto

procedimento fazer2 ( datos x [ 3 ] )
    escreva " ----2 hola" , "\n"
    inteiro t , k
    para t de 0 ate 2
        se x [ t ] .varon = verdadeiro entao
            escreva "----.." , x [ t ] .pessoa + " é hombre"
        senao
            escreva "----.." , x [ t ] .pessoa + " é mulher"
        fimse
        escreva "\n"
    fimpara
    escreva " ----2 fim" , "\n"
fimprocedimento

procedimento fazer ( datos x [ 3 ] )
    escreva " --1 hola " , "\n"
    x [ 1 ] .pessoa <- "Yeny Rendon"
    fazer2 ( x )
    escreva " --1 fim" , "\n"
fimprocedimento

inicio
    escreva "Codigo principal \n"
    datos x [ 3 ]
    x [ 0 ] .varon <- verdadeiro
    x [ 0 ] .pessoa <- "David"
    x [ 1 ] .varon <- falso
    x [ 1 ] .pessoa <- "Yeny"
    x [ 2 ] .varon <- verdadeiro
    x [ 2 ] .pessoa <- "Carlos"
    fazer ( x )
    
fimalgoritmo

