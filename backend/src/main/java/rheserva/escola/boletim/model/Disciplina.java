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
@Table(name = "disciplinas")
public class Disciplina {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "nome", nullable = false, unique = true)
	private String nome;

//     @ManyToMany(mappedBy = "disciplinas")
//     Set<Turma> turmas = new HashSet<>();

}
