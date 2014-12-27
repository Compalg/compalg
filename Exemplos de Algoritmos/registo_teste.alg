/*ALGORITMO: Testando as Variaveis Heterogenias
--AUTOR: Augusto Bilabila
--DATA: Sun Jan 29 18:27:30 GMT 2012*/
registo carro
    real peso
    real altura
    inteiro ano
    logico teste
fimregisto
inicio
    carro dados 
    real total[ 2 ]
    escreva ( "-----------------------------\n" )
    escreva ( "Teste de uma variavel heterogenia\n" )
    escreva ( "-----------------------------\n" )
    escreva ( "DIGITE O PESO: " )
    leia ( dados.peso )
    escreva ( "DIGITE A ALTURA: " )
    leia ( dados.altura )
    escreva ( "DIGITE O ANO: " )
    leia ( dados.ano )
    total[ 1 ] <- dados.peso + dados.altura + dados.ano
    se ( total > 2010 ) entao
        dados.teste <- verdadeiro
    senao
        dados.teste <- falso
    fimse
    se ( dados.teste ) entao
        escreva ( "O CARRO ESTA NOVO " )
    senao
        escreva ( "O CARRO ESTA ANTIGO " )
    fimse
    escreva ( "\n-----------------------------" )
fimalgoritmo

