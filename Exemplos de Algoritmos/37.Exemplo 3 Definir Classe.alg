classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        escreva "Hola en clase \n" , a
        retorne a
    fimfuncao

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b <- novo pessoa ( )
    b.nome <- "David"
    b.idade <- 34 + b.hola ( 10 )
    escreva b.idade
    inteiro a
    a <- b.hola ( 5 )
fimalgoritmo

