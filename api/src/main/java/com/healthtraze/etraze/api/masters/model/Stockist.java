package com.healthtraze.etraze.api.masters.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;

@Entity
@Table(name = "tbl_stockist")
public class Stockist extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2628295858513469578L;

	@Id
	@Column(name = "stockistId")
	private String stockistId;
	
	@Transient
	private String stateName;
	

	@Column(name="stockistName")
	private String stockistName;


	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "mobile")
	private String mobile;

	@Column(name = "email")
	private String email;
	
	@Column(name="stateId")
	private String stateId;

	@Column(name = "cityId")
	private String cityId;
	
	@Column(name="country")
	private String country;

	@Column(name = "gstNumber")
	private String gstNumber;

	@Column(name = "cinNumber")
	private String cinNumber;

	@Column(name = "tanNumber")
	private String tanNumber;

	@Column(name = "tinNumber")
	private String tinNumber;

	@Column(name = "panNumber")
	private String panNumber;
	
	@Column(name = "noOfManufacture")
	private String noOfManufacture;

	@Column(name = "fssaiNumber")
	private String fssaiNumber;

	@Temporal(TemporalType.DATE)
	@Column(name = "fssaiExpiryDate")
	private Date fssaiExpiryDate;

	@Column(name = "drugLicenseNumber20B")
	private String drugLicenseNumber20B;

	@Column(name = "drugLicenseNumber21B")
	private String drugLicenseNumber21B;

	@Temporal(TemporalType.DATE)
	@Column(name = "drugLicenseExpiryDate")
	private Date drugLicenseExpiryDate;

	@Column(name = "status")
	private String status;

	@Column(name = "companyRegisteredAddress")
	private String companyRegisteredAddress;

	@Column(name = "companyOwnerEmailId")
	private String companyOwnerEmailId;

	@Column(name = "companyOwnerContact")
	private String companyOwnerContact;

	@Column(name = "companyManagerEmailId")
	private String companyManagerEmailId;

	@Column(name = "companyManagerContact")
	private String companyManagerContact;
	
	@Column(name = "pinCode")
	private String pinCode; 	
		
	
	@Transient
	private String remarks;
	
	@Transient
	private String cityName;
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockistManufacture> stockistManufacturesList;
	
	@Transient    
	private List<Manufacturer> manufacturesList;
	
	@Transient
	private StockistManufacture stockistManufacture;

	
	public List<Manufacturer> getManufacturesList() {
		return manufacturesList;
	}

	public void setManufacturesList(List<Manufacturer> manufacturesList) {
		this.manufacturesList = manufacturesList;
	}

	public String getStockistId() {
		return stockistId;
	}

	public void setStockistId(String stockistId) {
		this.stockistId = stockistId;
	}
	

	public String getStockistName() {
		return stockistName;
	}

	public void setStockistName(String stockistName) {
		this.stockistName = stockistName;
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


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getCinNumber() {
		return cinNumber;
	}

	public void setCinNumber(String cinNumber) {
		this.cinNumber = cinNumber;
	}

	public String getTanNumber() {
		return tanNumber;
	}

	public void setTanNumber(String tanNumber) {
		this.tanNumber = tanNumber;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getFssaiNumber() {
		return fssaiNumber;
	}

	public void setFssaiNumber(String fssaiNumber) {
		this.fssaiNumber = fssaiNumber;
	}

	public Date getFssaiExpiryDate() {
		return fssaiExpiryDate;
	}

	public void setFssaiExpiryDate(Date fssaiExpiryDate) {
		this.fssaiExpiryDate = fssaiExpiryDate;
	}

	public String getDrugLicenseNumber20B() {
		return drugLicenseNumber20B;
	}

	public void setDrugLicenseNumber20B(String drugLicenseNumber20B) {
		this.drugLicenseNumber20B = drugLicenseNumber20B;
	}

	public String getDrugLicenseNumber21B() {
		return drugLicenseNumber21B;
	}

	public void setDrugLicenseNumber21B(String drugLicenseNumber21B) {
		this.drugLicenseNumber21B = drugLicenseNumber21B;
	}

	public Date getDrugLicenseExpiryDate() {
		return drugLicenseExpiryDate;
	}

	public void setDrugLicenseExpiryDate(Date drugLicenseExpiryDate) {
		this.drugLicenseExpiryDate = drugLicenseExpiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompanyRegisteredAddress() {
		return companyRegisteredAddress;
	}

	public void setCompanyRegisteredAddress(String companyRegisteredAddress) {
		this.companyRegisteredAddress = companyRegisteredAddress;
	}

	public String getCompanyOwnerEmailId() {
		return companyOwnerEmailId;
	}

	public void setCompanyOwnerEmailId(String companyOwnerEmailId) {
		this.companyOwnerEmailId = companyOwnerEmailId;
	}

	public String getCompanyOwnerContact() {
		return companyOwnerContact;
	}

	public void setCompanyOwnerContact(String companyOwnerContact) {
		this.companyOwnerContact = companyOwnerContact;
	}

	public String getCompanyManagerEmailId() {
		return companyManagerEmailId;
	}

	public void setCompanyManagerEmailId(String companyManagerEmailId) {
		this.companyManagerEmailId = companyManagerEmailId;
	}

	public String getCompanyManagerContact() {
		return companyManagerContact;
	}

	public void setCompanyManagerContact(String companyManagerContact) {
		this.companyManagerContact = companyManagerContact;
	}

	public List<StockistManufacture> getStockistManufacturesList() {
		return stockistManufacturesList;
	}

	public void setStockistManufacturesList(List<StockistManufacture> stockistManufacturesList) {
		this.stockistManufacturesList = stockistManufacturesList;
	}

	public String getNoOfManufacture() {
		return noOfManufacture;
	}

	public void setNoOfManufacture(String noOfManufacture) {
		this.noOfManufacture = noOfManufacture;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	

	public StockistManufacture getStockistManufacture() {
		return stockistManufacture;
	}

	public void setStockistManufacture(StockistManufacture stockistManufacture) {
		this.stockistManufacture = stockistManufacture;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public String toString() {
		return "Stockist [stockistId=" + stockistId + ", stateName=" + stateName + ", stockistName=" + stockistName
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", mobile=" + mobile + ", email=" + email
				+ ", stateId=" + stateId + ", cityId=" + cityId + ", country=" + country + ", gstNumber=" + gstNumber
				+ ", cinNumber=" + cinNumber + ", tanNumber=" + tanNumber + ", tinNumber=" + tinNumber + ", panNumber="
				+ panNumber + ", noOfManufacture=" + noOfManufacture + ", fssaiNumber=" + fssaiNumber
				+ ", fssaiExpiryDate=" + fssaiExpiryDate + ", drugLicenseNumber20B=" + drugLicenseNumber20B
				+ ", drugLicenseNumber21B=" + drugLicenseNumber21B + ", drugLicenseExpiryDate=" + drugLicenseExpiryDate
				+ ", status=" + status + ", companyRegisteredAddress=" + companyRegisteredAddress
				+ ", companyOwnerEmailId=" + companyOwnerEmailId + ", companyOwnerContact=" + companyOwnerContact
				+ ", companyManagerEmailId=" + companyManagerEmailId + ", companyManagerContact="
				+ companyManagerContact + ", pinCode=" + pinCode + ", stockistManufacturesList="
				+ stockistManufacturesList + ", manufacturesList=" + manufacturesList + ", stockistManufacture="
				+ stockistManufacture + "]";
	}
	
	

	
		
	
}
