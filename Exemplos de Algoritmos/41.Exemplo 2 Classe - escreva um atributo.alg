classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        escreva "Metodo hola en clase: " , a , "\n"
        escreva "idade hola: " , idade , "\n"
        retorne a
    fimfuncao

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa b
    b.nome <- "David"
    b.idade <- 5
    b.idade <- 34 + b.hola ( 10 )
    escreva "idade inicio:" , b.idade , "\n"
    escreva b.hola ( 12 ) + b.hola ( 5 ) , "\n"
fimalgoritmo

