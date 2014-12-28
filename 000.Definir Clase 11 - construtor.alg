classe pessoa
    literal nome
    inteiro idade
    
    construtor pessoa ( literal n , inteiro i )
        nome <- n
        idade <- i
    fimconstrutor

    funcao inteiro hola ( inteiro a )
        retorne hola2 ( a ) * idade
    fimfuncao

    funcao inteiro hola2 ( inteiro a )
        retorne idade * a
    fimfuncao

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa a <- novo pessoa ( "David" , 50 )
    pessoa b <- novo pessoa ( "Yeny" , 101 )
    escreva a.hola ( 5 ) , "\n"
    escreva b.idade , "\n"
fimalgoritmo

