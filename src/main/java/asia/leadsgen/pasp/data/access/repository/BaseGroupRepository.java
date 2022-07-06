package asia.leadsgen.pasp.data.access.repository;

import asia.leadsgen.pasp.entity.BaseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BaseGroupRepository extends JpaRepository<BaseGroup, String> {

	List<BaseGroup> findAllByStateAndIdNotIn(String approved, String[] strings);

	Optional<BaseGroup> findByName(String baseGroupId);
}
