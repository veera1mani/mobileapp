/**
 * 
 */
package com.healthtraze.etraze.api.file.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;

import javax.persistence.Id;

/**
 * @author mavensi
 *
 */
@Entity
@Table(name = "tbl_files")
public class File extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = Constants.FAILED)
	private String fileId;

	@Column(name = Constants.FILENAME)
	private String fileName;

	@Column(name = Constants.FILETYPE)
	private String fileType;

	@Column(name = Constants.KEY)
	private String key;

	@Column(name = Constants.ICON)
	private String icon;

	@Column(name = Constants.URL)
	private String url;

	@Column(name = Constants.PATH)
	private String path;

	@Column(name = Constants.SIZE)
	private Long size;

	@Column(name = Constants.DESCRIPTION)
	private String description;

	@Column(name = "mappingId")
	private String mappingId;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

}
