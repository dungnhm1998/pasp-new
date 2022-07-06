package asia.leadsgen.pasp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_BASE_GROUP")
public class BaseGroup {
	@Id
	@Column(name = "S_ID")
	private String id;
	@Column(name = "S_PARENT_ID")
	private String parentId;
	@Column(name = "S_NAME")
	private String name;
	@Column(name = "S_DESC")
	private String desc;
	@Column(name = "N_POSITION")
	private Integer position;
	@Column(name = "S_STATE")
	private String state;
	@Column(name = "D_CREATE")
	private Date createDate;
	@Column(name = "D_UPDATE")
	private Date updateDate;

	public BaseGroup(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
