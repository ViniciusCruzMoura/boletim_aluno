package rheserva.escola.boletim.dto;

import java.util.List;

public class BoletimGridDTO {
    private List<AvaliacaoDTO> avaliacoes;
    private List<AlunoNotasDTO> linhas;

    public BoletimGridDTO() {}

    public BoletimGridDTO(List<AvaliacaoDTO> avaliacoes, List<AlunoNotasDTO> linhas) {
        this.avaliacoes = avaliacoes;
        this.linhas = linhas;
    }

    public List<AvaliacaoDTO> getAvaliacoes() { return avaliacoes; }
    public void setAvaliacoes(List<AvaliacaoDTO> avaliacoes) { this.avaliacoes = avaliacoes; }

    public List<AlunoNotasDTO> getLinhas() { return linhas; }
    public void setLinhas(List<AlunoNotasDTO> linhas) { this.linhas = linhas; }
}

