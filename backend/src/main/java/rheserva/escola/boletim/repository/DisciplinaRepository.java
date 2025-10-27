package rheserva.escola.boletim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rheserva.escola.boletim.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
}
