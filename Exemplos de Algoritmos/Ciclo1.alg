/*ALGORITMO: "Ciclos (Estruturas de repetição)"
--AUTOR: Bilabila
--DATA E HORA: 19 de Dezembro de 2011 | 15:34:08 */
inicio
    //comando
    inteiro b
    //------ Ciclo Faça...Enquanto ----
    faca
        escreva " Faça = " , b
        b <- b + 1
    enquanto ( b < 10 )
    escreva ( "\n" )
    //-------- Ciclo Para -------------
    para b de 1 ate 10 passo 1
        escreva " Para = " , b
    fimpara
    escreva "\n"
    //-------- Ciclo Enquanto ---------
    enquanto ( b > 1 ) faca
        escreva " Enquanto = " , b
        b <- b - 1
    fimenquanto
    escreva "\n"
    //-------- Ciclo Repita -----------
    repita
        escreva " Repita = " , b
        b <- b + 1
    ate ( b = 10 )
fimalgoritmo

