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

classe emprego
    literal nome
    pessoa dono
    
    construtor emprego ( literal n , pessoa d )
        nome <- n
        dono <- d
    fimconstrutor

    funcao literal apresentar ( )
        escreva dono.hola ( 1 ) , "\n"
        retorne dono.nome + " é bom " + nome
    fimfuncao

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa dsb <- novo pessoa ( "David" , 50 )
    emprego profe
    escreva profe.apresentar ( ) , "\n"
fimalgoritmo

