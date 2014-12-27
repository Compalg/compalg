/*ALGORITMO: "Resultado de Um Aluno"
--AUTOR: Bilabila
--DATA E HORA: 04 de Outubro de 2011 | 22:05:25 */
inicio
    //comando
    real nota1 , nota2 , nota3 , media
    escreva "-------------------------\n"
    escreva "Digite a primeira nota\n"
    leia nota1 , nota2 , nota3
    escreva "-------------------------\n"
    escreva "Digite a segunda nota\n"
    leia nota2
    escreva "-------------------------\n"
    escreva "Digite a segunda nota\n"
    leia nota3
    escreva "-------- Media ----------\n"
    media <- ( nota1 + nota2 + nota3 ) / 3
    mostre media , "\n"
    escreva "------- Resultado -------\n"
    se media > 9 entao
        mostre "Aprovado"
    senao
        mostre "Reprovado"
    fimse
fimalgoritmo

