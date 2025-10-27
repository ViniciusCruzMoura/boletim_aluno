package rheserva.escola.boletim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rheserva.escola.boletim.model.*;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByTurmaIdAndDisciplinaId(Long turmaId, Long disciplinaId);
}
