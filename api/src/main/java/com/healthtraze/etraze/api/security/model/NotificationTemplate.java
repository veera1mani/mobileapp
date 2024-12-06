/**
 * 
 */
package com.healthtraze.etraze.api.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

/**
 * @author Mavensi Lap025
 *
 */
@Entity
@Table(name  ="tbl_notification_Template")
public class NotificationTemplate extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -926840138710809126L;

	@Id
	@Column(name = Constants.NOTIFICATIONTEMPLATEID)
	private String notificationTemplateId;

	@Column(name = Constants.SUBJECT)
	private String subject;

	
	@Column(name = Constants.NOTIFICATIONTEMPLATE)
	private String notificationTemplate;


	public String getNotificationTemplateId() {
		return notificationTemplateId;
	}


	public void setNotificationTemplateId(String notificationTemplateId) {
		this.notificationTemplateId = notificationTemplateId;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getNotificationTemplate() {
		return notificationTemplate;
	}


	public void setNotificationTemplate(String notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
  