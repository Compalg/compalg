classe pessoa
    literal nome
    inteiro idade
    funcao inteiro hola ( inteiro a )
        retorne a
    fimfuncao

    funcao inteiro hola2 ( inteiro a )
        //hola ( a )
        retorne a
    fimfuncao

fimclasse

inicio
    pessoa b , a
    a.nome <- "Yeny"
    a.idade <- 10
    b.nome <- "David"
    b.idade <- 5
    b.idade <- a.hola ( 4 ) + a.idade + b.hola ( 10 )
fimalgoritmo

