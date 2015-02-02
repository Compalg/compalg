registo cao
    literal nome
    literal raca
fimregisto

classe pessoa
    cao mascota
    construtor pessoa (  )
        mascota.nome <- "Cujo"
        mascota.raca <- "Boxer"
    fimconstrutor

fimclasse

inicio
    pessoa a <- novo pessoa (  )
    escreva a.mascota.nome , " " , a.mascota.raca , "\n"
fimalgoritmo

