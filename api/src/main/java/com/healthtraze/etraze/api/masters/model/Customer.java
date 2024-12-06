package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_customer")
public class Customer extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5480673516085174876L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "location")
	private String location;

//	@JsonIgnore
//	@OneToMany()
//	@JoinColumn(name="customer_id", referencedColumnName = "id")
//	private List<Cheque> cheques;
	


	
	

}
