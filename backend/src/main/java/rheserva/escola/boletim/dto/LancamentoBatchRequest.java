package rheserva.escola.boletim.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import jakarta.validation.Valid;

public class LancamentoBatchRequest {
    @NotNull
    @Size(min = 0)
    private List<@Valid NotaDTO> notas;

    public LancamentoBatchRequest() {}

    public LancamentoBatchRequest(List<NotaDTO> notas) { this.notas = notas; }

    public List<NotaDTO> getNotas() { return notas; }
    public void setNotas(List<NotaDTO> notas) { this.notas = notas; }
}

