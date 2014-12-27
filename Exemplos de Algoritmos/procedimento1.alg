/*ALGORITMO: "Nome do algoritmo"
--AUTOR: hp
--DATA E HORA: 5 de Fevereiro de 2012 | 12:11:35 */
procedimento bila ( )
    inteiro a
    para a de 1 ate 10
        escreva ( "\nOla mundo\n" )
    fimpara
    leia ( a )
    escreva ( "Recebeu = \n" ) , a
fimprocedimento
procedimento pessoa ( inteiro n1 , real n2 )
    real soma <- n2 + n1
    escreva ( "\nOla mundo\n" ) , soma
fimprocedimento
procedimento linha ( )
    escreva ( "\n------------------------\n" )
fimprocedimento
inicio
    //comando
    inteiro s
    real p
    leia s , p
    
    bila ( )
    pessoa ( s , p )
    linha ( )
    
fimalgoritmo

