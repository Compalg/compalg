funcao real pi ( real x )
    escreva " pi , x= " , x , ": "
    retorne 3.00 * x
fimfuncao

procedimento fazer2 ( inteiro a )
    escreva "\nfazer 2: " , a , " ... "
    //    escreva pi ( pi ( pi ( 1 ) ) )
    escreva pi ( pi ( pi ( 2 ) ) + pi ( pi ( 1 + 2 ) ) + 100 )
fimprocedimento

inicio
    escreva pi ( 1 + 2 ) + 10
    escreva "\n"
    //fazer1 ( 20 )
    escreva "\n"
    fazer2 ( 40 )
fimalgoritmo

