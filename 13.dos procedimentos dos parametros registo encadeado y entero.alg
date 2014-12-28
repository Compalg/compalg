registo pc
    literal marca
fimregisto

registo pessoa
    literal nome
    inteiro idade
    pc puesto
fimregisto

procedimento fazoa ( pessoa p , pessoa t )
    escreva " 3 hola " , p.nome , " " , t.nome , " " , t.puesto.marca , "\n"
fimprocedimento

procedimento fazo ( pessoa p , pessoa t )
    escreva " 2 hola " , p.nome , " " , " " , t.puesto.marca , "\n"
    p.nome <- "pepe"
    fazoa ( p , t )
fimprocedimento

procedimento fazer ( pessoa p , inteiro a )
    escreva " 1 hola " , p.nome , " " , a , " " , p.puesto.marca , "\n"
    fazo ( p , p )
fimprocedimento

inicio
    escreva "Codigo principal \n"
    pessoa p
    p.nome <- "David"
    p.idade <- 34
    p.puesto.marca <- "Dell"
    fazer ( p , 555 )
fimalgoritmo

