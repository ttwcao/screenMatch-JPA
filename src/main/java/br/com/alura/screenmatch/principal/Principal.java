package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    //criar uma nova lista para salvar a busca da API
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    //instanciar a interface repositório para uso das operações CRUD
    //chamado injeção de dependência


    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;

        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar Série por Título   
                    5 - Buscar séries por Ator
                    6 - Top 5 séries  
                    7 - Buscar série por categoria            
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTopCincoSeries();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }



    private void buscarSeriePorTitulo() {
        System.out.println("Digite o título da série: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()){
            System.out.println("Dados da série: " +serieBuscada.get());
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();

        //salvar na lista os dados retornados da API
        //dadosSeries.add(dados);

        //informar quais informações serão utilizados para salvar
        Serie serie = new Serie(dados);
        repository.save(serie);

        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        //exibir séries do banco
        listarSeriesBuscadas();

        //busca de episódios no banco de dados
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        //busca a partir do nome informado
        //Optional<Serie> serie = series.stream() repository.findByTituloContainingIgnoreCase(nomeSerie);
        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);
                //.filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                //.findFirst();

        if(serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);

            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        }else{
                System.out.println("Série não encontrada.");
        }




//        //busca dados da serie na API
//        DadosSerie dadosSerie = getDadosSerie();
//        List<DadosTemporada> temporadas = new ArrayList<>();
//
//        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
//            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
//            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//            temporadas.add(dadosTemporada);
//        }
//        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas(){

        //criar uma lista de séries List<> importando a série
        //List<Serie> series = new ArrayList<>();

        //atribuir os dados de dadosSeries
//        series = dadosSeries.stream()
//                //para cada dadosSeries cria uma nova serie ->
//                .map(d -> new Serie(d))
//                //coletar para a lista e atribuir para uma
//                .collect(Collectors.toList());

        //consultar dados no banco
        series = repository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que "+ nomeAtor + " trabalhou.\n");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + "\nAvaliação: " + s.getAvaliacao() + "\n\n"));
                System.out.println(seriesEncontradas);
    }

    private void buscarTopCincoSeries() {
        List<Serie> seriesTop = repository.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + ", avaliação: " +s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Deseja buscar série por qual categoria?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriePorCategoria = repository.findByGenero(categoria);
        System.out.println("Séries da categoria: " + nomeGenero);
        seriePorCategoria.forEach(System.out::println);
    }

}