package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    SUSPENSE("Suspense"),
    ANIMACAO("Animation"),
    TERROR("Horror");

    private String categoriaOmdb;

    //construtor para receber os dados para associar os valores
    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()){
            if(categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a série!");
    }
}
