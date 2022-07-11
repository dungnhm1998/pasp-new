package asia.leadsgen.pasp.entity;

import asia.leadsgen.pasp.util.DBParams;
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
@Entity(name = DBParams.TB_BASE_GROUP)
public class BaseGroup {
	@Id
	@Column(name = DBParams.S_ID)
	private String id;
	@Column(name = DBParams.S_PARENT_ID)
	private String parentId;
	@Column(name = DBParams.S_NAME)
	private String name;
	@Column(name = DBParams.S_DESC)
	private String desc;
	@Column(name = DBParams.N_POSITION)
	private Integer position;
	@Column(name = DBParams.S_STATE)
	private String state;
	@Column(name = DBParams.D_CREATE)
	private Date createDate;
	@Column(name = DBParams.D_UPDATE)
	private Date updateDate;

	public BaseGroup(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
