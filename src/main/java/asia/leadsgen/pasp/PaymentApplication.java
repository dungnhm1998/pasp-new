package asia.leadsgen.pasp;

import asia.leadsgen.pasp.data.access.repository.BaseGroupRepository;
import asia.leadsgen.pasp.service.AddBaseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableJpaRepositories
@Log4j2
public class PaymentApplication {

	@Autowired
	AddBaseService addBaseService;
	@Autowired
	BaseGroupRepository baseGroupRepository;

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

	@PostConstruct
	public void doThings() {
		log.info("*********************************************************\r\n");
		log.info("*********************************************************\r\n");
	}




}
