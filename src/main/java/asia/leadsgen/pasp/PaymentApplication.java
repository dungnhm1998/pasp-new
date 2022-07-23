package asia.leadsgen.pasp;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableJpaRepositories
@Log4j2
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

	@PostConstruct
	public void doThings() {
		log.info("*********************************************************\r\n");
		log.info("*********************************************************\r\n");
	}

}
