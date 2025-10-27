package rheserva.escola.boletim;

import org.springframework.boot.SpringApplication;

public class TestBoletimApplication {

	public static void main(String[] args) {
		SpringApplication.from(BoletimApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
