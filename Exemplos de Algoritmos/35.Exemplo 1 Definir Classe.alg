classe pessoa
    literal nome
    inteiro idade
    procedimento hola ( )
        escreva "Hola en clase \n"
    fimprocedimento

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b <- novo pessoa ( )
    b.nome <- "David"
    b.idade <- 34
    escreva b.idade
    b.hola ( )
fimalgoritmo

