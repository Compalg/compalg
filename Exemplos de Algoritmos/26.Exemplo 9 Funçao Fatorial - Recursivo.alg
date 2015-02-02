funcao real fatorial ( inteiro n )
    se n <= 1 entao
        retorne n
    fimse
    retorne n * fatorial ( n - 1 )
fimfuncao

inicio
    escreva "Fatorial de 5 é " , fatorial ( 5 )
fimalgoritmo

