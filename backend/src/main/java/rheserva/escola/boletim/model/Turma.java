package rheserva.escola.boletim.model;

import jakarta.persistence.*;
import lombok.*;
// import org.springframework.data.annotation.*;
import rheserva.escola.boletim.model.*;
import java.util.Set;
import java.util.HashSet;

@Getter 
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "turmas")
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "nome", nullable = false, unique = true)
	private String nome;

//     @ManyToMany
//     @JoinTable(
//         name = "disciplina_turma",
//         joinColumns = @JoinColumn(name = "turma_id"),
//         inverseJoinColumns = @JoinColumn(name = "disciplina_id")
//     )
//     Set<Disciplina> disciplinas = new HashSet<>();

}
