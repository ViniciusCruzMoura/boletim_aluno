package rheserva.escola.boletim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rheserva.escola.boletim.model.*;

public interface AvaliacaoLancamentoRepository extends JpaRepository<AvaliacaoLancamento, Long> {
    List<AvaliacaoLancamento> findByAvaliacao_TurmaIdAndAvaliacao_DisciplinaId(Long turmaId, Long disciplinaId);
}
