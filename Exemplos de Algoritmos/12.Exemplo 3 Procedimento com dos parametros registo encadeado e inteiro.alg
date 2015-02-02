registo pc
    literal marca
fimregisto
registo pessoa
    literal nome
    inteiro idade
    pc puesto
fimregisto
procedimento fazer ( pessoa p , inteiro a )
    escreva "hola " , p.nome , " " , a, " ", p.puesto.marca
fimprocedimento
inicio
    escreva "Codigo principal \n"
    pessoa p
    p.nome <- "David"
    p.idade <- 34
    p.puesto.marca <- "Dell"
    fazer ( p , 2 )
fimalgoritmo

