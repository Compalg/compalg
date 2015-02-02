registo pessoa
    literal nome
    inteiro idade
fimregisto
procedimento fazer ( pessoa p , inteiro a )
    escreva "hola " , p.nome , " " , a
fimprocedimento
inicio
    escreva "Codigo principal \n"
    pessoa p
    p.nome <- "David"
    p.idade <- 34
    fazer ( p , 2 )
fimalgoritmo

