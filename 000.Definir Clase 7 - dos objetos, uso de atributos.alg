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
    pessoa b , a
    a.nome <- "Yeny"
    a.idade <- 10
    b.nome <- "David"
    b.idade <- 5
    b.idade <- a.hola ( 4 ) + a.idade + b.hola ( 10 )
    escreva "idade inicio:" , b.idade , "\n"
    escreva a.hola ( 12 ) + b.hola ( 5 ) , "\n"
fimalgoritmo

