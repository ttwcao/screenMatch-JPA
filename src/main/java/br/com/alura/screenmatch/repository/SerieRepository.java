package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

//esta interface herda do JPARepository, faz o extends
// é preciso informara qual a entidade que esta sendo persistida, qual repository
// esta sendo manipulado, no caso Serie, em seguida dizer qual o tipo do ID
// no caso Long

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
