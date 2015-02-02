classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        escreva "--Metodo hola en clase: " , a , "\n"
        escreva "--idade hola: " , idade , "\n"
        retorne a
    fimfuncao

    funcao inteiro hola2 ( inteiro a )
        hola ( a )
        escreva "Metodo hola 2 en clase: " , a , "\n"
        escreva "idade hola2 : " , idade , "\n"
        retorne a
    fimfuncao

    construtor pessoa ( )
    fimconstrutor

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa a <- novo pessoa ( )
    a.nome <- "Yeny"
    a.idade <- 10
    escreva a.hola2 ( 12 ) 
fimalgoritmo

