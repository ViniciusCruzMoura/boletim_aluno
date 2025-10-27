package rheserva.escola.boletim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rheserva.escola.boletim.model.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
}
