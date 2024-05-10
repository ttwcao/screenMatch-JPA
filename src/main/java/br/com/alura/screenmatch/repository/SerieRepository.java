package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//esta interface herda do JPARepository, faz o extends
// é preciso informara qual a entidade que esta sendo persistida, qual repository
// esta sendo manipulado, no caso Serie, em seguida dizer qual o tipo do ID
// no caso Long

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    //aqui categoria é um enum
    List<Serie> findByGenero(Categoria categoria);
}
