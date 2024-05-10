package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    SUSPENSE("Suspense", "Suspense"),
    ANIMACAO("Animation", "Animação"),
    TERROR("Horror", "Terror");

    private String categoriaOmdb;

    private String categoriaPortugues;

    //construtor para receber os dados para associar os valores
    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaPortugues = categoriaPortugues;
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

    public static Categoria fromPortugues(String text){
        for (Categoria categoria : Categoria.values()){
            if(categoria.categoriaPortugues.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a série!");
    }
}
