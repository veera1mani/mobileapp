	package com.healthtraze.etraze.api.security.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.masters.model.ManagerManufacturerMapping;

import lombok.ToString;

@Entity
@Embeddable
@ToString
@Table(name  ="tbl_user")
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Id
 	@Column(name = Constants.USERID)
	protected String userId;

	@Column(name = Constants.FIRSTNAME)
	protected String firstName;

	@Column(name = Constants.LASTNAME)
	protected String lastName;

	@Column(name = Constants.EMAIL)
	protected String email;

	@Column(name = Constants.PHONENO)
	protected String phoneNo;

	@Column(name = Constants.ROLEID)
	protected String roleId;

	@Column(name = Constants.ROLENAME)
	protected String roleName;

	@Column(name = Constants.ONLINE)
	private boolean online;

	@Column(name = Constants.STATUS)
	protected String status;

	@Column(name = Constants.IMAGEURL)
	private String imageUrl;

	@Column(name = Constants.REFERRERCODE)
	private String referrerCode;

	@Column(name = Constants.NEWUSERVALIDATEWEB)
	protected Boolean newUserValidateWeb;

	@Column(name = Constants.NEWUSERVALIDATEMOBILE)
	protected Boolean newUserValidateMobile;

	@Column(name = Constants.MOBILECHANNEL)
	protected Boolean mobileChannel;
	
	@Column(name = "hierarchy_id")
	protected String hierarachyId;
	
	@Column(name="isenable")
	private boolean isEnable;
	
	@Column(name="countryCode")
	private String countryCode;

	
	@Transient
	private List<ManagerManufacturerMapping> managerManufactureMapping;
	
	@Transient
	private List<UserRoleService> userRoleServices;

	@Column(name = Constants.WEBCHANNEL)
	protected Boolean webChannel;

	@Column(name = Constants.ISUSERONBOARDED)
	protected Boolean isUserOnboarded;

	@Column(name = Constants.OTPVERIFIED)
	protected Boolean otpVerified;
	
	@Column(name = "manufacturerId")
	private String manufacturerId;
	
	
	@Column(name = Constants.USERAPPTYPE)
	private String userAppType;
	
	
	@Column(name="picked")
	private boolean picked;
	
	@Column(name="checked")
	private boolean checked;
	
	@Column(name="packed")
	private boolean packed;
	
	@Column(name="dispatched")
	private boolean dispatched;
	
	@Column(name="saleable")
	private boolean saleable;
	
	@Column(name="nonSaleable")
	private boolean nonSaleable;	
	
	@Transient
	private String generatePin;
	
	@Column(name="warehouseLocation")
	private String warehouseLocation;

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getReferrerCode() {
		return referrerCode;
	}

	public void setReferrerCode(String referrerCode) {
		this.referrerCode = referrerCode;
	}

	public Boolean getNewUserValidateWeb() {
		return newUserValidateWeb;
	}

	public void setNewUserValidateWeb(Boolean newUserValidateWeb) {
		this.newUserValidateWeb = newUserValidateWeb;
	}

	public Boolean getNewUserValidateMobile() {
		return newUserValidateMobile;
	}

	public void setNewUserValidateMobile(Boolean newUserValidateMobile) {
		this.newUserValidateMobile = newUserValidateMobile;
	}

	public Boolean getMobileChannel() {
		return mobileChannel;
	}

	public void setMobileChannel(Boolean mobileChannel) {
		this.mobileChannel = mobileChannel;
	}

	public String getHierarachyId() {
		return hierarachyId;
	}

	public void setHierarachyId(String hierarachyId) {
		this.hierarachyId = hierarachyId;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public List<ManagerManufacturerMapping> getManagerManufactureMapping() {
		return managerManufactureMapping;
	}

	public void setManagerManufactureMapping(List<ManagerManufacturerMapping> managerManufactureMapping) {
		this.managerManufactureMapping = managerManufactureMapping;
	}

	public List<UserRoleService> getUserRoleServices() {
		return userRoleServices;
	}

	public void setUserRoleServices(List<UserRoleService> userRoleServices) {
		this.userRoleServices = userRoleServices;
	}

	public Boolean getWebChannel() {
		return webChannel;
	}

	public void setWebChannel(Boolean webChannel) {
		this.webChannel = webChannel;
	}

	public Boolean getIsUserOnboarded() {
		return isUserOnboarded;
	}

	public void setIsUserOnboarded(Boolean isUserOnboarded) {
		this.isUserOnboarded = isUserOnboarded;
	}

	public Boolean getOtpVerified() {
		return otpVerified;
	}

	public void setOtpVerified(Boolean otpVerified) {
		this.otpVerified = otpVerified;
	}

	public String getUserAppType() {
		return userAppType;
	}

	public void setUserAppType(String userAppType) {
		this.userAppType = userAppType;
	}

	public boolean isPicked() {
		return picked;
	}

	public void setPicked(boolean picked) {
		this.picked = picked;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isPacked() {
		return packed;
	}

	public void setPacked(boolean packed) {
		this.packed = packed;
	}

	public boolean isDispatched() {
		return dispatched;
	}

	public void setDispatched(boolean dispatched) {
		this.dispatched = dispatched;
	}

	public boolean isSaleable() {
		return saleable;
	}

	public void setSaleable(boolean saleable) {
		this.saleable = saleable;
	}

	public boolean isNonSaleable() {
		return nonSaleable;
	}

	public void setNonSaleable(boolean nonSaleable) {
		this.nonSaleable = nonSaleable;
	}

	public String getGeneratePin() {
		return generatePin;
	}

	public void setGeneratePin(String generatePin) {
		this.generatePin = generatePin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getWarehouseLocation() {
		return warehouseLocation;
	}

	public void setWarehouseLocation(String warehouseLocation) {
		this.warehouseLocation = warehouseLocation;
	}
	
	
	
	
}
