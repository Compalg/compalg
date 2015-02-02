registo pessoa
    literal nome
    inteiro idade
fimregisto
registo datos
    logico varon
    literal nombre
fimregisto
registo datost
    inteiro a
    inteiro b
fimregisto
inicio
    escreva "Codigo principal \n"
    datos x
    x.varon <- verdadeiro
    x.nombre <- "pepe"
    pessoa b
    b.nome <- "David"
    b.idade <- 34
    se x.varon = verdadeiro entao
        escreva b.nome
    senao
        escreva x.nombre
    fimse
    datost t
    t.a <- 2
    t.b <- 3
    escreva t.a + t.b
    
fimalgoritmo

