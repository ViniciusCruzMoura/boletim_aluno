package rheserva.escola.boletim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rheserva.escola.boletim.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByNomeContaining(String nome);
    List<Aluno> findByTurmaIdOrderByNome(Long turmaId);
    List<Aluno> findByTurmaId(Long turmaId);
}
