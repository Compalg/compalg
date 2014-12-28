classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        retorne hola2 ( a ) * 2
    fimfuncao

    funcao inteiro hola2 ( inteiro a )
        retorne a * 2
    fimfuncao

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b , a
    a.nome <- "Yeny"
    a.idade <- 10
    b.nome <- "David"
    b.idade <- 5
    b.idade <- a.hola ( 1 )
    escreva b.idade , "\n"
    escreva a.hola2 ( 5 ) , "\n"
fimalgoritmo

