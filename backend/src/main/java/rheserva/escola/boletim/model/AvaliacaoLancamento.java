package rheserva.escola.boletim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import rheserva.escola.boletim.model.*;

@Entity
@Table(name = "avaliacao_lancamentos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"avaliacao_id", "aluno_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoLancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Column(precision = 4)//, scale = 2)
    private Double nota; // null = não lançado ainda
}
