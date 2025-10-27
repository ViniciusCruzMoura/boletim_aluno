package rheserva.escola.boletim.dto;

import java.util.Map;
import lombok.*;

@Builder
public class AlunoNotasDTO {
    private Long alunoId;
    private String nome;
    private Map<Long, Double> notas; // avaliacaoId -> nota
    private String media; // e.g. "8.0" or "-"

    public AlunoNotasDTO() {}

    public AlunoNotasDTO(Long alunoId, String nome, Map<Long, Double> notas, String media) {
        this.alunoId = alunoId;
        this.nome = nome;
        this.notas = notas;
        this.media = media;
    }

    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Map<Long, Double> getNotas() { return notas; }
    public void setNotas(Map<Long, Double> notas) { this.notas = notas; }

    public String getMedia() { return media; }
    public void setMedia(String media) { this.media = media; }
}
