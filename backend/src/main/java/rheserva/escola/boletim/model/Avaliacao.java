package rheserva.escola.boletim.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import rheserva.escola.boletim.model.*;

@Getter 
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "avaliacoes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"turma_id", "disciplina_id", "titulo"}))
public class Avaliacao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Max(value = 10, message = "A nota deve ser no maximo 10")
    @Min(value = 0, message = "A nota deve ser no minimo 0")
	@Column(name = "nota")
	private int nota;

    @Max(value = 5, message = "A peso deve ser no maximo 5")
    @Min(value = 1, message = "O peso deve ser no minimo 1")
	@Column(name = "peso")
	private int peso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

}
