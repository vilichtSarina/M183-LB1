package ch.tbz.injection;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InjectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(InjectionApplication.class, args);
	}
	@Bean
	public ModelMapper noteMapper() {
		return new ModelMapper();
	}

}
