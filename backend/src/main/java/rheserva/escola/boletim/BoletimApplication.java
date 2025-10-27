package rheserva.escola.boletim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.springframework.beans.factory.annotation.Autowired;
import rheserva.escola.boletim.repository.*;
import rheserva.escola.boletim.model.*;

import java.util.List;

@SpringBootApplication
public class BoletimApplication implements CommandLineRunner {

    @Autowired
    AlunoRepository alunoRepository;
    @Autowired
    TurmaRepository turmaRepository;
    @Autowired
    DisciplinaRepository disciplinaRepository;
    @Autowired
    AvaliacaoRepository avaliacaoRepository;
    @Autowired
    AvaliacaoLancamentoRepository avaliacaoLancamentoRepository;

	public static void main(String[] args) {
		SpringApplication.run(BoletimApplication.class, args);
	}

    @Override
    public void run(String... args) {
        System.out.println(">> EXECUTING : CommandLineRunner");

        // Turmas
        Turma t1 = turmaRepository.save(Turma.builder().nome("3º Ano A").build());
        Turma t2 = turmaRepository.save(Turma.builder().nome("2º Ano B").build());

        // Disciplinas
        Disciplina dMath = disciplinaRepository.save(Disciplina.builder().nome("Matemática").build());
        Disciplina dPort = disciplinaRepository.save(Disciplina.builder().nome("Português").build());
        Disciplina dHist = disciplinaRepository.save(Disciplina.builder().nome("História").build());

        // Alunos para a Turma 1
        Aluno a1 = alunoRepository.save(Aluno.builder().nome("Ana Silva").turma(t1).build());
        Aluno a2 = alunoRepository.save(Aluno.builder().nome("Bruno Costa").turma(t1).build());
        Aluno a3 = alunoRepository.save(Aluno.builder().nome("Carla Souza").turma(t1).build());

        // Alunos para a Turma 2
        Aluno b1 = alunoRepository.save(Aluno.builder().nome("Diego Ramos").turma(t2).build());
        Aluno b2 = alunoRepository.save(Aluno.builder().nome("Eva Lima").turma(t2).build());

        // Avaliacoes para a turma 1 + disciplina Matemática
        Avaliacao av1 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Prova")
                .peso(5)
                .disciplina(dMath)
                .turma(t1)
                .build());

        Avaliacao av2 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Trabalho")
                .peso(2)
                .disciplina(dMath)
                .turma(t1)
                .build());

        Avaliacao av3 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Atividade")
                .peso(1)
                .disciplina(dMath)
                .turma(t1)
                .build());

        // Avaliacoes para a turma 1 + disciplina Português
        Avaliacao avP1 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Prova")
                .peso(5)
                .disciplina(dPort)
                .turma(t1)
                .build());

        Avaliacao avP2 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Trabalho")
                .peso(2)
                .disciplina(dPort)
                .turma(t1)
                .build());

        Avaliacao avP3 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Atividade")
                .peso(1)
                .disciplina(dPort)
                .turma(t1)
                .build());

        // Avaliacoes para a turma 2 + disciplina História
        Avaliacao avH1 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Prova")
                .peso(5)
                .disciplina(dHist)
                .turma(t2)
                .build());
        Avaliacao avH2 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Trabalho")
                .peso(2)
                .disciplina(dHist)
                .turma(t2)
                .build());
        Avaliacao avH3 = avaliacaoRepository.save(Avaliacao.builder()
                .titulo("Atividade")
                .peso(1)
                .disciplina(dHist)
                .turma(t2)
                .build());

        // lancamentos (partial)
        avaliacaoLancamentoRepository.saveAll(List.of(
            AvaliacaoLancamento.builder().avaliacao(av1).aluno(a1).nota(8.5).build(),
            AvaliacaoLancamento.builder().avaliacao(av2).aluno(a1).nota(7.0).build(),
            AvaliacaoLancamento.builder().avaliacao(av3).aluno(a1).nota(9.0).build(),
            AvaliacaoLancamento.builder().avaliacao(av1).aluno(a2).nota(6.0).build(),
            AvaliacaoLancamento.builder().avaliacao(av2).aluno(a2).nota(5.5).build(),
            AvaliacaoLancamento.builder().avaliacao(avH1).aluno(b1).nota(7.5).build()
        ));


        //
        //
//         Aluno a = new Aluno();
//         a.setNome("Fulano Cilclano");
//         alunoRepository.save(a);
// 
//         Disciplina d = new Disciplina();
//         d.setNome("Calculo 1");
//         disciplinaRepository.save(d);
// 
//         Turma t = new Turma();
//         t.setNome("3712 - Ciencia da Computação");
//         t.getDisciplinas().add(d);
//         turmaRepository.save(t);
    }

}
