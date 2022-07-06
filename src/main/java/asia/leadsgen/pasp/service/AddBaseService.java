package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.repository.BaseGroupRepository;
import asia.leadsgen.pasp.entity.BaseGroup;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AddBaseService {
	@Autowired
	private BaseGroupRepository baseGroupRepository;

	public void test() {
		String baseGroupId = "NVngNagqBFbd3011";
		BaseGroup baseGroup = baseGroupRepository.findById(baseGroupId).orElse(baseGroupRepository.findByName(baseGroupId).orElse(new BaseGroup()));
		log.info("found:" + baseGroup);
	}
}
