package asia.leadsgen.pasp.model.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BurgerContext {
	@ApiModelProperty(hidden = true)
	private String userId;
	@ApiModelProperty(hidden = true)
	private String aspRefId;
	@ApiModelProperty(hidden = true)
	private Boolean isOwner;
	@ApiModelProperty(hidden = true)
	private Set<String> domainNames;
	@ApiModelProperty(hidden = true)
	private String timeZone;
	@ApiModelProperty(hidden = true)
	private Set<String> modulePermissions;
	@ApiModelProperty(hidden = true)
	private Set<String> globalPermissions;
	@ApiModelProperty(hidden = true)
	private List<String> dropshipStoreIds;

}
