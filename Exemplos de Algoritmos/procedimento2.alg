/*ALGORITMO: Uso de Procedimentos
--AUTOR: Bilabila
--DATA E HORA: 7 de Fevereiro de 2012 | 13:26:40 */
procedimento idade ( inteiro idade )
    se ( idade > 18 ) entao
        mostre ( "Adulto " ) , idade
    senao
        mostre ( "Criança " ) , idade
    fimse
fimprocedimento
inicio
    escreva ( "Uso de procedimento " )
    mostre ( "\n--------------------------------\n" )
    escreva "Digite a sua idade:"
    inteiro a
    leia a
    idade ( a )
fimalgoritmo

