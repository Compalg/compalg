registo cao
    literal nome
    literal raca
fimregisto

classe pessoa
    literal nome
    inteiro idade
       cao mascota
    inteiro valores [ 3 ]
    construtor pessoa ( literal n , inteiro i )
        nome <- n
        idade <- i
               mascota.nome <- "Cujo"
              mascota.raca <- "Boxer"
        valores [ 0 ] <- 3
        valores [ 1 ] <- 6
        valores [ 2 ] <- 9
    fimconstrutor

    funcao inteiro hola ( inteiro a )
        valores [ 1 ] <- valores [ 0 ] + valores [ 2 ]
        retorne a * idade
    fimfuncao

fimclasse

inicio
    escreva "Codigo principal \n"
    pessoa a <- novo pessoa ( "David" , 50 )
    pessoa b <- novo pessoa ( "Yeny" , 101 )
    escreva a.nome , " " , a.hola ( 5 ) , "\n"
    escreva b.nome , " " , b.idade , "\n"
    escreva a.mascota.nome , " " , a.mascota.raca , "\n"
    
    escreva b.hola ( 1 ) , " " , b.valores [ 1 ]
fimalgoritmo

