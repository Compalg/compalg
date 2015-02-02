classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola1 ( inteiro a )
        retorne a
    fimfuncao

    funcao inteiro hola2 ( inteiro a )
        retorne hola1 ( a )
        retorne a
    fimfuncao

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    pessoa b <- novo pessoa ( ) , a <- novo pessoa ( )
    a.nome <- "Yeny"
    a.idade <- 10
    b.nome <- "David"
    b.idade <- 5
    b.idade <- a.hola1 ( 4 ) + a.idade + b.hola2 ( 10 )
    escreva b.idade
fimalgoritmo

