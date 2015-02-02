classe pessoa
    literal nome
    inteiro idade [ 3 ]
    funcao inteiro hola ( inteiro a )
        escreva "Metodo hola en clase: " , a , "\n"
        escreva "idade hola: " , idade [ a ] , "\n"
        retorne idade [ a ]
    fimfuncao

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b<-novo pessoa()
    b.nome <- "David"
    b.idade [ 0 ] <- 5
    b.idade [ 1 ] <- 6
    b.idade [ 2 ] <- 7
    b.idade [ 1 ] <- 34 + b.hola ( 1 )
    escreva "idade inicio:" , b.idade [ 1 ] , "\n"
    escreva b.hola ( 1 ) + b.hola ( 2 ) , "\n"
fimalgoritmo

