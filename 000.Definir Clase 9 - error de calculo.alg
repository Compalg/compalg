classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        retorne 3 * 5
    fimfuncao

    funcao inteiro hola2 ( inteiro a )
        retorne hola ( a ) * 2
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
    escreva a.hola2 ( 1 )  "\n"
fimalgoritmo

