package com.healthtraze.etraze.api.security.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name  ="tbl_password_history")
@Setter
@Getter
public class PasswordHistoryModel extends BaseModel {
	private static final long serialVersionUID = 1905122041950251207L;


	@Id 
	private String userId;  
	@ElementCollection
	private List<PasswordDetails> passwordDetail;

	public PasswordHistoryModel(String userId, List<PasswordDetails> passwordDetail) {
		super();
		this.userId = userId;
		this.passwordDetail = passwordDetail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<PasswordDetails> getPasswordDetail() {
		return passwordDetail;
	}

	public void setPasswordDetail(List<PasswordDetails> passwordDetail) {
		this.passwordDetail = passwordDetail;
	}

	

}
