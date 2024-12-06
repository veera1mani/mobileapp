package com.healthtraze.etraze.api.masters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "tbl_deadline_list")
public class DeadlineList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "deadlineId")
	private Long deadlineId;
	
	@Column(name = "deadlineName")
	private String deadlineName;
	
	@Column(name = "deadlineType")
	private String deadlineType;
	
}
