package kwh.awsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AwswebApplication {
	public static void main(String[] args) {
		SpringApplication.run(AwswebApplication.class, args);
	}
}