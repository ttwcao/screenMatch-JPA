package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

//aqui é indicado para o JPA que esta classe será uma tabela
@Entity
//aqui é definido como será o nome da tabela, diferente da classe, é possível
//também personalizar o nome da coluna @Column(name"nomeDaColuna")

@Table(name = "series")
public class Serie {
    //definição da chave primária na tabela
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    @Transient //informa para o JPA que esta lista não vai para o banco
    private List<Episodio> episodios = new ArrayList<>();

    //Construtor padrão para o JPA fazer consultas no banco
    public Serie(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        //conversão de string para double
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        //filtro aplicado para obter somente a primeira palavra no campos, nos dados retornados da API
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        //this.sinopse = ConsultaChatGPT.obterTraducao(dadosSerie.sinopse()).trim();
        this.sinopse = dadosSerie.sinopse();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return "Gênero = " + genero +
                ", Título = " + titulo + '\'' +
                ", Total de Temporadas = " + totalTemporadas +
                ", Avaliação = " + avaliacao +
                ", Atores = " + atores + '\'' +
                ", Poster ='" + poster + '\'' +
                ", Sinopse ='" + sinopse + '\'' +
                '}';
    }
}
