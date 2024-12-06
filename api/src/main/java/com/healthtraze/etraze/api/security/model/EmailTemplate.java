package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

@Entity
@Table(name  ="tbl_mail_template")
public class EmailTemplate extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.MAILTEMPLATEID)
	private String mailTemplateId;

	@Column(name = Constants.SUBJECT)
	private String subject;

	 
  	@Column(name = Constants.MAILTEMPLATE  , columnDefinition = "text") 
	private String mailTemplate;

	public String getMailTemplateId() {
		return mailTemplateId;
	}

	public void setMailTemplateId(String mailTemplateId) {
		this.mailTemplateId = mailTemplateId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(String mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

}
