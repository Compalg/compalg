registo dados
    literal nome
    inteiro numero
fimregisto

funcao dados troca2 ( dados x )
    x.nome <- "troca DOS"
    x.numero <- 2
    retorne x
fimfuncao

funcao dados troca ( dados x )
    dados y
    y <- troca2 ( x )
    //erro, não é permitido llamar função y acceder ao campo do registro na mesma instruçao
    x.nome <- "troca" + ": " + troca2 ( x ) .nome
    x.numero <- 100 * y.numero
    retorne x
fimfuncao

inicio
    dados p
    p.nome <- "David"
    p.numero <- 30
    escreva p.nome , "; " , p.numero , "\n"
    p <- troca ( p )
    escreva p.nome , "; " , p.numero
fimalgoritmo

