funcao real pi ( real x )
escreva " pi , x= " , x , ": "
retorne 3.00 * x
fimfuncao
procedimento fazer1 ( inteiro a )
    escreva "\nfazer 1: "
    escreva pi ( 1.1 ) , " "
    escreva pi ( 1.2 ) , " "
    escreva pi ( 1.3 ) , " "
fimprocedimento
procedimento fazer2 ( inteiro a )
    escreva "\nfazer 2: "
    escreva pi ( pi ( pi ( 1 ) ) ) , pi ( pi ( pi ( 2 ) ) + pi ( pi ( 1 + 2 ) ) + 100 )
fimprocedimento
inicio
    pi ( 356 )
    escreva "\n"
    fazer1 ( 20 )
    escreva "\n"
    fazer2 ( 40 )
fimalgoritmo

