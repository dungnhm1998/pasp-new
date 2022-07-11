package asia.leadsgen.pasp.data.access.repository;

import asia.leadsgen.pasp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
