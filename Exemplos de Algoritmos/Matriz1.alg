/*ALGORITMO: "Vectores"
--AUTOR: Bilabila
--DATA E HORA: 21 de Dezembro de 2011 | 22:59:35 */
inicio
    //comando
    literal a [ 4 ] <- {"Inverno" , "Outono" , "Primavera" , "Verão" }
    literal estacao <- ""
    escreva ( "Digite uma estação:" )
    leia ( estacao )
    inteiro i , v
    para i de 0 ate 3
        se ( a [ i ] = estacao ) entao
            escreva ( "Estação Encontrada!!!" )
            v <- v + 1
        fimse
    fimpara
    se ( v = 0 ) entao
        escreva ( "Estação Não Encontrada!!!" )
    fimse
fimalgoritmo

