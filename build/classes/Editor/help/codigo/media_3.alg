//ALGORITMO: Media de 3 notas
//AUTOR: "Augusto Bilabila"
//DATA: Tue Oct 04 22:05:25 BST 2011
inicio
    real nota1 , nota2 , nota3 , media
    escreva "-------------------------\n"
    escreva "Digite a primeira nota\n"
    leia nota1
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
    se ( media > 9 ) entao
        mostre "Aprovado"
    senao
        mostre "Reprovado"
    fimse
fimalgoritmo

