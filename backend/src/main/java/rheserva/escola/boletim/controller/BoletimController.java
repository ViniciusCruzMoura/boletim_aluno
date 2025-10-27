package rheserva.escola.boletim.controller;

import rheserva.escola.boletim.dto.*;
import rheserva.escola.boletim.model.*;
import rheserva.escola.boletim.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/boletim")
public class BoletimController {

    @Autowired
    private final TurmaRepository turmaRepo;
    @Autowired
    private final DisciplinaRepository disciplinaRepo;
    @Autowired
    private final AlunoRepository alunoRepo;
    @Autowired
    private final AvaliacaoRepository avaliacaoRepo;
    @Autowired
    private final AvaliacaoLancamentoRepository lancamentoRepo;

    public BoletimController(TurmaRepository turmaRepo,
                             DisciplinaRepository disciplinaRepo,
                             AlunoRepository alunoRepo,
                             AvaliacaoRepository avaliacaoRepo,
                             AvaliacaoLancamentoRepository lancamentoRepo) {
        this.turmaRepo = turmaRepo;
        this.disciplinaRepo = disciplinaRepo;
        this.alunoRepo = alunoRepo;
        this.avaliacaoRepo = avaliacaoRepo;
        this.lancamentoRepo = lancamentoRepo;
    }

    // 1. Listar turmas (seed)
    @GetMapping("/turmas")
    public List<TurmaDTO> listarTurmas() {
        return turmaRepo.findAll().stream()
                .map(t -> new TurmaDTO(t.getId(), t.getNome()))
                .collect(Collectors.toList());
    }

    // 2. Listar disciplinas (opcional: todas ou filtradas por turma)
    @GetMapping("/disciplinas")
    public List<DisciplinaDTO> listarDisciplinas() {
        return disciplinaRepo.findAll().stream()
                .map(d -> new DisciplinaDTO(d.getId(), d.getNome()))
                .collect(Collectors.toList());
    }

    // 3. Listar alunos por turma
    @GetMapping("/turmas/{turmaId}/alunos")
    public ResponseEntity<List<AlunoDTO>> listarAlunosPorTurma(@PathVariable Long turmaId) {
        if (!turmaRepo.existsById(turmaId)) return ResponseEntity.notFound().build();
        List<AlunoDTO> alunos = alunoRepo.findByTurmaIdOrderByNome(turmaId).stream()
                .map(a -> new AlunoDTO(a.getId(), a.getNome()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(alunos);
    }

    // 4. Listar avaliações (colunas) para turma+disciplina
    @GetMapping("/turmas/{turmaId}/disciplinas/{disciplinaId}/avaliacoes")
    public ResponseEntity<List<AvaliacaoDTO>> listarAvaliacoes(@PathVariable Long turmaId,
                                                               @PathVariable Long disciplinaId) {
        if (!turmaRepo.existsById(turmaId) || !disciplinaRepo.existsById(disciplinaId))
            return ResponseEntity.notFound().build();

        List<AvaliacaoDTO> avals = avaliacaoRepo.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId)
                .stream()
                .map(av -> new AvaliacaoDTO(av.getId(), av.getTitulo(), av.getPeso()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(avals);
    }

    // 5. Obter lançamentos (notas) para turma+disciplina — retorna estrutura para preencher tabela
    @GetMapping("/turmas/{turmaId}/disciplinas/{disciplinaId}/lancamentos")
    public ResponseEntity<BoletimGridDTO> obterLancamentos(@PathVariable Long turmaId,
                                                           @PathVariable Long disciplinaId) {
        if (!turmaRepo.existsById(turmaId) || !disciplinaRepo.existsById(disciplinaId))
            return ResponseEntity.notFound().build();

        List<Aluno> alunos = alunoRepo.findByTurmaIdOrderByNome(turmaId);
        List<Avaliacao> avaliacoes = avaliacaoRepo.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId);
        List<AvaliacaoLancamento> lancamentos = lancamentoRepo
                .findByAvaliacao_TurmaIdAndAvaliacao_DisciplinaId(turmaId, disciplinaId);

        // Map key (alunoId -> (avaliacaoId -> nota))
        Map<Long, Map<Long, Double>> mapa = new HashMap<>();
        for (Aluno a : alunos) mapa.put(a.getId(), new HashMap<>());

        for (AvaliacaoLancamento l : lancamentos) {
            mapa.computeIfAbsent(l.getAluno().getId(), k -> new HashMap<>())
                    .put(l.getAvaliacao().getId(), Math.round(l.getNota() * 10.0) / 10.0);
        }

//         List<AlunoNotasDTO> linhas = alunos.stream().map(a -> {
//             Map<Long, Double> notas = mapa.getOrDefault(a.getId(), Collections.emptyMap());
//             return AlunoNotasDTO.builder()
//                     .alunoId(a.getId())
//                     .nome(a.getNome())
//                     .notas(notas)
//                     .build();
//         }).collect(Collectors.toList());

        // Build map avaliacaoId -> peso
        Map<Long, Integer> pesos = avaliacoes.stream()
            .collect(Collectors.toMap(Avaliacao::getId, Avaliacao::getPeso));

        // create linhas with media
        List<AlunoNotasDTO> linhas = alunos.stream().map(a -> {
            Map<Long, Double> notas = mapa.getOrDefault(a.getId(), Collections.emptyMap());
            // calcular média
            Double mediaVal = null;
            try {
                mediaVal = rheserva.escola.boletim.util.MediaUtil.calcularMediaPonderada(notas, pesos);
            } catch (IllegalArgumentException ex) {
                // em caso de dado inválido, tratar como null (frontend valida também)
                mediaVal = null;
            }
            String mediaStr = mediaVal == null ? "-" : String.format("%.1f", mediaVal);
            return new AlunoNotasDTO(a.getId(), a.getNome(), notas, mediaStr);
        }).collect(Collectors.toList());

        List<AvaliacaoDTO> avalDTOs = avaliacoes.stream()
                .map(av -> new AvaliacaoDTO(av.getId(), av.getTitulo(), av.getPeso()))
                .collect(Collectors.toList());

        BoletimGridDTO grid = new BoletimGridDTO(avalDTOs, linhas);
        return ResponseEntity.ok(grid);
    }

    // 6. Salvar lançamentos em lote
    @PostMapping("/turmas/{turmaId}/disciplinas/{disciplinaId}/lancamentos")
    @Transactional
    public ResponseEntity<Void> salvarLancamentos(@PathVariable Long turmaId,
                                                  @PathVariable Long disciplinaId,
                                                  @Valid @RequestBody LancamentoBatchRequest request) {
        if (!turmaRepo.existsById(turmaId) || !disciplinaRepo.existsById(disciplinaId))
            return ResponseEntity.notFound().build();

        // Validar estrutura mínima: lista de notas
        List<NotaDTO> notas = Optional.ofNullable(request.getNotas()).orElse(Collections.emptyList());

        // Carga: buscar avaliações relevantes e alunos para evitar N+1
        List<Avaliacao> avaliacoes = avaliacaoRepo.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId);
        Set<Long> avaliacaoIds = avaliacoes.stream().map(Avaliacao::getId).collect(Collectors.toSet());
        List<Aluno> alunos = alunoRepo.findByTurmaId(turmaId);
        Set<Long> alunoIds = alunos.stream().map(Aluno::getId).collect(Collectors.toSet());
        Map<String, AvaliacaoLancamento> existentes = lancamentoRepo.findByAvaliacao_TurmaIdAndAvaliacao_DisciplinaId(turmaId, disciplinaId)
                .stream()
                .collect(Collectors.toMap(l -> l.getAluno().getId() + "_" + l.getAvaliacao().getId(), l -> l));

        List<AvaliacaoLancamento> toSave = new ArrayList<>();
        for (NotaDTO n : notas) {
            if (n.getNota() != null && (n.getNota() < 0 || n.getNota() > 10)) continue; // ignorar inválidas
            if (!avaliacaoIds.contains(n.getAvaliacaoId()) || !alunoIds.contains(n.getAlunoId())) continue;

            String key = n.getAlunoId() + "_" + n.getAvaliacaoId();
            AvaliacaoLancamento existing = existentes.get(key);
            if (existing != null) {
                existing.setNota(n.getNota());
                toSave.add(existing);
            } else {
                Avaliacao av = avaliacoes.stream().filter(a -> a.getId().equals(n.getAvaliacaoId())).findFirst().orElse(null);
                Aluno al = alunos.stream().filter(a -> a.getId().equals(n.getAlunoId())).findFirst().orElse(null);
                if (av != null && al != null) {
                    toSave.add(AvaliacaoLancamento.builder()
                            .avaliacao(av)
                            .aluno(al)
                            .nota(n.getNota())
                            .build());
                }
            }
        }

        lancamentoRepo.saveAll(toSave);
        return ResponseEntity.ok().build();
    }
}

