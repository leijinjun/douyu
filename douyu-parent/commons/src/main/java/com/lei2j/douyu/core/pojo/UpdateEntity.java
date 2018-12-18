package com.lei2j.douyu.core.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * @author lei2j
 * Created by lei2j on 2018/6/24.
 */
public class UpdateEntity extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 5257081749943414120L;

	protected Timestamp createAt;

	protected Timestamp updateAt;

	protected Integer createBy;

	protected Integer updateBy;

	public Timestamp getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
}
