package asia.leadsgen.pasp.data.access.repository;

import asia.leadsgen.pasp.entity.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, String> {

	Optional<PaymentAccount> findByName(String name);

}
