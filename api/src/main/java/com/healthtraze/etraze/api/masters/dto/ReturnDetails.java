package com.healthtraze.etraze.api.masters.dto;

import java.util.List;

import com.healthtraze.etraze.api.masters.model.Return;
import com.healthtraze.etraze.api.masters.model.ReturnAttachment;
import com.healthtraze.etraze.api.masters.model.ReturnNote;
import com.healthtraze.etraze.api.masters.model.ReturnStatusHistory;

public class ReturnDetails {

	private Return returnData;
	private List<ReturnAttachment> returnAttachments;
	private List<ReturnNote> returnNotes;
	private List<ReturnStatusHistory> history;

	public Return getReturnData() {
		return returnData;
	}

	public void setReturnData(Return returnData) {
		this.returnData = returnData;
	}

	public List<ReturnAttachment> getReturnAttachments() {
		return returnAttachments;
	}

	public void setReturnAttachments(List<ReturnAttachment> returnAttachments) {
		this.returnAttachments = returnAttachments;
	}

	public List<ReturnNote> getReturnNotes() {
		return returnNotes;
	}

	public void setReturnNotes(List<ReturnNote> returnNotes) {
		this.returnNotes = returnNotes;
	}

	public List<ReturnStatusHistory> getHistory() {
		return history;
	}

	public void setHistory(List<ReturnStatusHistory> history) {
		this.history = history;
	}

}
