package rheserva.escola.boletim.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import rheserva.escola.boletim.model.*;

@Getter 
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "alunos")
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @NotBlank(message = "Nome é obrigatorio")
	@Column(name = "nome", nullable = false)
	private String nome;

//     @OneToMany(mappedBy = "aluno")
//     private Set<Avaliacao> avaliacoes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

//     public int mediaPonderada() {
//         int nota_p = avaliacaoProvaNota * avaliacaoProvaPeso;
//         int nota_t = avaliacaoTrabalhoNota * avaliacaoTrabalhoPeso;
//         int nota_a = avaliacaoAtividadeNota * avaliacaoAtividadePeso;
//         int soma_das_notas = nota_p + nota_t + nota_a;
//         int soma_dos_pesos = avaliacaoProvaPeso + avaliacaoTrabalhoPeso + avaliacaoAtividadePeso;
//         return soma_das_notas / soma_dos_pesos;
//     }

}
