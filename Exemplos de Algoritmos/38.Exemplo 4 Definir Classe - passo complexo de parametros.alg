classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        escreva "Metodo hola en clase: " , a , "\n"
        retorne a
    fimfuncao

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b <- novo pessoa ( )
    b.nome <- "David"
    b.idade <- 34 + b.hola ( b.hola ( 12 + b.hola ( 12 ) ) )
    escreva b.idade , "\n"
    escreva b.hola ( 12 ) + b.hola ( 5 ) , "\n"
fimalgoritmo

