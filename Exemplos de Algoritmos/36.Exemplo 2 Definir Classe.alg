classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( )
        escreva "Hola en clase \n"
        retorne 10
    fimfuncao

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b <- novo pessoa ( )
    b.nome <- "David"
    b.idade <- 34 + b.hola ( )
    escreva b.idade
    inteiro a
    a <- b.hola ( )
fimalgoritmo

