package com.healthtraze.etraze.api.masters.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMailDTO {
	
	private String userName;
	private String EmailId;

}
