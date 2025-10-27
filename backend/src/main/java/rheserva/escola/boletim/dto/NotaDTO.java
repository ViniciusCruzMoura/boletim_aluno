package rheserva.escola.boletim.dto;

import jakarta.validation.constraints.*;

public class NotaDTO {
    @NotNull
    private Long alunoId;
    @NotNull
    private Long avaliacaoId;
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double nota;

    public NotaDTO() {}

    public NotaDTO(Long alunoId, Long avaliacaoId, Double nota) {
        this.alunoId = alunoId;
        this.avaliacaoId = avaliacaoId;
        this.nota = nota;
    }

    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public Long getAvaliacaoId() { return avaliacaoId; }
    public void setAvaliacaoId(Long avaliacaoId) { this.avaliacaoId = avaliacaoId; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }
}

