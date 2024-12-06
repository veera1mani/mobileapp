package com.healthtraze.etraze.api.masters.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TenantDashBoardDto {
	
	
	
	private BigInteger dispatched;
	private BigInteger cartonsLocal;
	private BigInteger cartonsOutstation;
	
	public BigInteger getDispatched() {
		return dispatched;
	}
	public void setDispatched(BigInteger dispatched) {
		this.dispatched = dispatched;
	}

	public BigInteger getCartonsLocal() {
		return cartonsLocal;
	}
	public void setCartonsLocal(BigInteger cartonsLocal) {
		this.cartonsLocal = cartonsLocal;
	}
	public BigInteger getCartonsOutstation() {
		return cartonsOutstation;
	}
	public void setCartonsOutstation(BigInteger cartonsOutstation) {
		this.cartonsOutstation = cartonsOutstation;
	}

	private BigInteger disaptchedLocal;
	private BigInteger dispatchedOutstaion;
	private BigInteger deliveredLocal;
	private BigInteger delivereddOutstaion;
	private BigInteger deadlineInvoice;
	private BigInteger deadlineCartonCount;
	
	
		


	public BigInteger getDeadlineInvoice() {
		return deadlineInvoice;
	}
	public void setDeadlineInvoice(BigInteger deadlineInvoice) {
		this.deadlineInvoice = deadlineInvoice;
	}
	public BigInteger getDeadlineCartonCount() {
		return deadlineCartonCount;
	}
	public void setDeadlineCartonCount(BigInteger deadlineCartonCount) {
		this.deadlineCartonCount = deadlineCartonCount;
	}
	public BigInteger getDisaptchedLocal() {
		return disaptchedLocal;
	}
	public void setDisaptchedLocal(BigInteger disaptchedLocal) {
		this.disaptchedLocal = disaptchedLocal;
	}
	public BigInteger getDispatchedOutstaion() {
		return dispatchedOutstaion;
	}
	public void setDispatchedOutstaion(BigInteger dispatchedOutstaion) {
		this.dispatchedOutstaion = dispatchedOutstaion;
	}
	public BigInteger getDeliveredLocal() {
		return deliveredLocal;
	}
	public void setDeliveredLocal(BigInteger deliveredLocal) {
		this.deliveredLocal = deliveredLocal;
	}
	public BigInteger getDelivereddOutstaion() {
		return delivereddOutstaion;
	}
	public void setDelivereddOutstaion(BigInteger delivereddOutstaion) {
		this.delivereddOutstaion = delivereddOutstaion;
	}
	private int orderTotal;
	private int completedOrder;
	private int blocked;
	private BigDecimal invoiceAmountLocal;
	private BigInteger localStockist;
	private BigInteger outStaionStockist;
	private BigInteger claimBoth;
	private BigInteger claimSale;
	private BigInteger claimNonSale;
	private BigInteger internalActionItems;
	private BigInteger stockistRetunsCompalint;
	private BigInteger stockistShortageComplaint;
	private BigInteger hoNotification;
	private BigInteger stockistQuey;
	private BigInteger stockistOtherComplaint;
	private BigInteger hoQuery;
	private BigInteger enqueiry;
	private BigInteger pod;
	private BigInteger hoComplaints;
	private BigInteger others;
	private BigInteger totalType;
	private BigInteger codSecurity;
	private BigInteger  codeAdvance;
	private BigInteger codReceive;
	private BigInteger codDelivery;
	private BigInteger deplocal;
	private BigInteger depOutStaion;
	private BigDecimal janI;
	private BigDecimal febI;
	private BigDecimal marI;
	private BigDecimal aprI;
	private BigDecimal mayI;
	private BigDecimal junI;
	private BigDecimal julyI;
	private BigDecimal augI;
	private BigDecimal sepI;
	private BigDecimal octI;
	private BigDecimal novI;
	private BigDecimal decI;
	private BigDecimal ljanI;
	private BigDecimal lfebI;
	private BigDecimal lmarI;
	private BigDecimal laprI;
	private BigDecimal lmayI;
	private BigDecimal ljunI;
	private BigDecimal ljulyI;
	private BigDecimal laugI;
	private BigDecimal lsepI;
	private BigDecimal loctI;
	private BigDecimal lnovI;
	private BigDecimal ldecI;
	private BigInteger cjan;
	private BigInteger cFeb;
	private BigInteger cMar;
	private BigInteger cApr;
	private BigInteger cMay;
	private BigInteger cjun;
	private BigInteger cJuly;
	private BigInteger cAug;
	private BigInteger cSep;
	private BigInteger cOct;
	
	private BigDecimal dJan;
	private BigDecimal dFeb;
	private BigDecimal dMar;
	private BigDecimal dApr;
	private BigDecimal dMay;
	private BigDecimal dJun;
	private BigDecimal dJul;
	private BigDecimal dAug;
	private BigDecimal dSep;
	private BigDecimal dOct;
	private BigDecimal dNov;
	private BigDecimal dDec;
	private BigDecimal dJanl;
	private BigDecimal dFebl;
	private BigDecimal dMarl;
	private BigDecimal dAprl;
	private BigDecimal dMayl;
	private BigDecimal dJunl;
	private BigDecimal dJull;
	private BigDecimal dAugl;
	private BigDecimal dSepl;
	private BigDecimal dOctl;
	private BigDecimal dNovl;
	private BigDecimal dDecl;
	
	private  BigInteger  belowThresHold;
	
   private BigInteger paymentNotLocal;
   
   private BigInteger paymentNotOutstaion;
   private BigInteger nosequirity;
   
   private BigInteger blockedOrder;
    
   private BigInteger podNotReceived;
   private  BigInteger lrNotReceived;
   
   
   private BigInteger ticketMiseed;
   private int orderMissed;
   private int retuenMissed;
   private BigInteger deliveredTlt;
   private BigInteger ccdNotDeliverd;
   
   private BigInteger outward;
   
   
	
	
	public BigInteger getOutward() {
	return outward;
}
public void setOutward(BigInteger outward) {
	this.outward = outward;
}
	public BigInteger getBelowThresHold() {
	return belowThresHold;
}
public void setBelowThresHold(BigInteger belowThresHold) {
	this.belowThresHold = belowThresHold;
}
public BigInteger getPaymentNotLocal() {
	return paymentNotLocal;
}
public void setPaymentNotLocal(BigInteger paymentNotLocal) {
	this.paymentNotLocal = paymentNotLocal;
}
public BigInteger getPaymentNotOutstaion() {
	return paymentNotOutstaion;
}
public void setPaymentNotOutstaion(BigInteger paymentNotOutstaion) {
	this.paymentNotOutstaion = paymentNotOutstaion;
}
public BigInteger getNosequirity() {
	return nosequirity;
}
public void setNosequirity(BigInteger nosequirity) {
	this.nosequirity = nosequirity;
}
public BigInteger getBlockedOrder() {
	return blockedOrder;
}
public void setBlockedOrder(BigInteger blockedOrder) {
	this.blockedOrder = blockedOrder;
}
public BigInteger getPodNotReceived() {
	return podNotReceived;
}
public void setPodNotReceived(BigInteger podNotReceived) {
	this.podNotReceived = podNotReceived;
}
public BigInteger getLrNotReceived() {
	return lrNotReceived;
}
public void setLrNotReceived(BigInteger lrNotReceived) {
	this.lrNotReceived = lrNotReceived;
}
public BigInteger getTicketMiseed() {
	return ticketMiseed;
}
public void setTicketMiseed(BigInteger ticketMiseed) {
	this.ticketMiseed = ticketMiseed;
}
public int getOrderMissed() {
	return orderMissed;
}
public void setOrderMissed(int orderMissed) {
	this.orderMissed = orderMissed;
}
public int getRetuenMissed() {
	return retuenMissed;
}
public void setRetuenMissed(int retuenMissed) {
	this.retuenMissed = retuenMissed;
}
public BigInteger getDeliveredTlt() {
	return deliveredTlt;
}
public void setDeliveredTlt(BigInteger deliveredTlt) {
	this.deliveredTlt = deliveredTlt;
}
public BigInteger getCcdNotDeliverd() {
	return ccdNotDeliverd;
}
public void setCcdNotDeliverd(BigInteger ccdNotDeliverd) {
	this.ccdNotDeliverd = ccdNotDeliverd;
}
	
	public BigDecimal getdJan() {
	return dJan;
}
public void setdJan(BigDecimal dJan) {
	this.dJan = dJan;
}
public BigDecimal getdFeb() {
	return dFeb;
}
public void setdFeb(BigDecimal dFeb) {
	this.dFeb = dFeb;
}
public BigDecimal getdMar() {
	return dMar;
}
public void setdMar(BigDecimal dMar) {
	this.dMar = dMar;
}
public BigDecimal getdApr() {
	return dApr;
}
public void setdApr(BigDecimal dApr) {
	this.dApr = dApr;
}
public BigDecimal getdMay() {
	return dMay;
}
public void setdMay(BigDecimal dMay) {
	this.dMay = dMay;
}
public BigDecimal getdJun() {
	return dJun;
}
public void setdJun(BigDecimal dJun) {
	this.dJun = dJun;
}
public BigDecimal getdJul() {
	return dJul;
}
public void setdJul(BigDecimal dJul) {
	this.dJul = dJul;
}
public BigDecimal getdAug() {
	return dAug;
}
public void setdAug(BigDecimal dAug) {
	this.dAug = dAug;
}
public BigDecimal getdSep() {
	return dSep;
}
public void setdSep(BigDecimal dSep) {
	this.dSep = dSep;
}
public BigDecimal getdOct() {
	return dOct;
}
public void setdOct(BigDecimal dOct) {
	this.dOct = dOct;
}
public BigDecimal getdNov() {
	return dNov;
}
public void setdNov(BigDecimal dNov) {
	this.dNov = dNov;
}
public BigDecimal getdDec() {
	return dDec;
}
public void setdDec(BigDecimal dDec) {
	this.dDec = dDec;
}
public BigDecimal getdJanl() {
	return dJanl;
}
public void setdJanl(BigDecimal dJanl) {
	this.dJanl = dJanl;
}
public BigDecimal getdFebl() {
	return dFebl;
}
public void setdFebl(BigDecimal dFebl) {
	this.dFebl = dFebl;
}
public BigDecimal getdMarl() {
	return dMarl;
}
public void setdMarl(BigDecimal dMarl) {
	this.dMarl = dMarl;
}
public BigDecimal getdAprl() {
	return dAprl;
}
public void setdAprl(BigDecimal dAprl) {
	this.dAprl = dAprl;
}
public BigDecimal getdMayl() {
	return dMayl;
}
public void setdMayl(BigDecimal dMayl) {
	this.dMayl = dMayl;
}
public BigDecimal getdJunl() {
	return dJunl;
}
public void setdJunl(BigDecimal dJunl) {
	this.dJunl = dJunl;
}
public BigDecimal getdJull() {
	return dJull;
}
public void setdJull(BigDecimal dJull) {
	this.dJull = dJull;
}
public BigDecimal getdAugl() {
	return dAugl;
}
public void setdAugl(BigDecimal dAugl) {
	this.dAugl = dAugl;
}
public BigDecimal getdSepl() {
	return dSepl;
}
public void setdSepl(BigDecimal dSepl) {
	this.dSepl = dSepl;
}
public BigDecimal getdOctl() {
	return dOctl;
}
public void setdOctl(BigDecimal dOctl) {
	this.dOctl = dOctl;
}
public BigDecimal getdNovl() {
	return dNovl;
}
public void setdNovl(BigDecimal dNovl) {
	this.dNovl = dNovl;
}
public BigDecimal getdDecl() {
	return dDecl;
}
public void setdDecl(BigDecimal dDecl) {
	this.dDecl = dDecl;
}
	public BigInteger getCjan() {
		return cjan;
	}
	public void setCjan(BigInteger cjan) {
		this.cjan = cjan;
	}
	public BigInteger getcFeb() {
		return cFeb;
	}
	public void setcFeb(BigInteger cFeb) {
		this.cFeb = cFeb;
	}
	public BigInteger getcMar() {
		return cMar;
	}
	public void setcMar(BigInteger cMar) {
		this.cMar = cMar;
	}
	public BigInteger getcApr() {
		return cApr;
	}
	public void setcApr(BigInteger cApr) {
		this.cApr = cApr;
	}
	public BigInteger getcMay() {
		return cMay;
	}
	public void setcMay(BigInteger cMay) {
		this.cMay = cMay;
	}
	public BigInteger getCjun() {
		return cjun;
	}
	public void setCjun(BigInteger cjun) {
		this.cjun = cjun;
	}
	public BigInteger getcJuly() {
		return cJuly;
	}
	public void setcJuly(BigInteger cJuly) {
		this.cJuly = cJuly;
	}
	public BigInteger getcAug() {
		return cAug;
	}
	public void setcAug(BigInteger cAug) {
		this.cAug = cAug;
	}
	public BigInteger getcSep() {
		return cSep;
	}
	public void setcSep(BigInteger cSep) {
		this.cSep = cSep;
	}
	public BigInteger getcOct() {
		return cOct;
	}
	public void setcOct(BigInteger cOct) {
		this.cOct = cOct;
	}
	public BigInteger getcNov() {
		return cNov;
	}
	public void setcNov(BigInteger cNov) {
		this.cNov = cNov;
	}
	public BigInteger getcDec() {
		return cDec;
	}
	public void setcDec(BigInteger cDec) {
		this.cDec = cDec;
	}
	public BigInteger getCjanl() {
		return cjanl;
	}
	public void setCjanl(BigInteger cjanl) {
		this.cjanl = cjanl;
	}
	public BigInteger getcFebl() {
		return cFebl;
	}
	public void setcFebl(BigInteger cFebl) {
		this.cFebl = cFebl;
	}
	public BigInteger getcMarl() {
		return cMarl;
	}
	public void setcMarl(BigInteger cMarl) {
		this.cMarl = cMarl;
	}
	public BigInteger getcAprl() {
		return cAprl;
	}
	public void setcAprl(BigInteger cAprl) {
		this.cAprl = cAprl;
	}
	public BigInteger getcMayl() {
		return cMayl;
	}
	public void setcMayl(BigInteger cMayl) {
		this.cMayl = cMayl;
	}
	public BigInteger getCjunl() {
		return cjunl;
	}
	public void setCjunl(BigInteger cjunl) {
		this.cjunl = cjunl;
	}
	public BigInteger getcJulyl() {
		return cJulyl;
	}
	public void setcJulyl(BigInteger cJulyl) {
		this.cJulyl = cJulyl;
	}
	public BigInteger getcAugl() {
		return cAugl;
	}
	public void setcAugl(BigInteger cAugl) {
		this.cAugl = cAugl;
	}
	public BigInteger getcSepl() {
		return cSepl;
	}
	public void setcSepl(BigInteger cSepl) {
		this.cSepl = cSepl;
	}
	public BigInteger getcOctl() {
		return cOctl;
	}
	public void setcOctl(BigInteger cOctl) {
		this.cOctl = cOctl;
	}
	public BigInteger getcNovl() {
		return cNovl;
	}
	public void setcNovl(BigInteger cNovl) {
		this.cNovl = cNovl;
	}
	public BigInteger getcDecl() {
		return cDecl;
	}
	public void setcDecl(BigInteger cDecl) {
		this.cDecl = cDecl;
	}
	private BigInteger cNov;
	private BigInteger cDec;
	private BigInteger cjanl;
	private BigInteger cFebl;
	private BigInteger cMarl;
	private BigInteger cAprl;
	private BigInteger cMayl;
	private BigInteger cjunl;
	private BigInteger cJulyl;
	private BigInteger cAugl;
	private BigInteger cSepl;
	private BigInteger cOctl;
	private BigInteger cNovl;
	private BigInteger cDecl;
	private BigDecimal chJan;
	private BigDecimal chfeb;
	private BigDecimal chmar;
	private BigDecimal chapr;
	private BigDecimal chmay;
	private BigDecimal chJun;
	private BigDecimal chJul;
	private BigDecimal chaug;
	private BigDecimal chsep;
	private BigDecimal chOct;
	private BigDecimal chnov;
	private BigDecimal chdec;
	private BigDecimal chJanl;
	private BigDecimal chfebl;
	private BigDecimal chmarl;
	private BigDecimal chaprl;
	private BigDecimal chmayl;
	private BigDecimal chJunl;
	private BigDecimal chJull;
	private BigDecimal chaugl;
	private BigDecimal chsepl;
	private BigDecimal chOctl;
	
	private   BigInteger   claimNonSaleMatch;
	private   BigInteger claimSaleMatch;
	private   BigInteger  claimBothMatch;
	public BigInteger getClaimNonSaleMatch() {
		return claimNonSaleMatch;
	}
	public void setClaimNonSaleMatch(BigInteger claimNonSaleMatch) {
		this.claimNonSaleMatch = claimNonSaleMatch;
	}
	public BigInteger getClaimSaleMatch() {
		return claimSaleMatch;
	}
	public void setClaimSaleMatch(BigInteger claimSaleMatch) {
		this.claimSaleMatch = claimSaleMatch;
	}
	public BigInteger getClaimBothMatch() {
		return claimBothMatch;
	}
	public void setClaimBothMatch(BigInteger claimBothMatch) {
		this.claimBothMatch = claimBothMatch;
	}
	public BigDecimal getChOct() {
		return chOct;
	}
	public void setChOct(BigDecimal chOct) {
		this.chOct = chOct;
	}
	public BigDecimal getChOctl() {
		return chOctl;
	}
	public void setChOctl(BigDecimal chOctl) {
		this.chOctl = chOctl;
	}
	private BigDecimal chnovl;
	private BigDecimal chdecl;
	
	
	public BigDecimal getChJan() {
		return chJan;
	}
	public void setChJan(BigDecimal chJan) {
		this.chJan = chJan;
	}
	public BigDecimal getChfeb() {
		return chfeb;
	}
	public void setChfeb(BigDecimal chfeb) {
		this.chfeb = chfeb;
	}
	public BigDecimal getChmar() {
		return chmar;
	}
	public void setChmar(BigDecimal chmar) {
		this.chmar = chmar;
	}
	public BigDecimal getChapr() {
		return chapr;
	}
	public void setChapr(BigDecimal chapr) {
		this.chapr = chapr;
	}
	public BigDecimal getChmay() {
		return chmay;
	}
	public void setChmay(BigDecimal chmay) {
		this.chmay = chmay;
	}
	public BigDecimal getChJun() {
		return chJun;
	}
	public void setChJun(BigDecimal chJun) {
		this.chJun = chJun;
	}
	public BigDecimal getChJul() {
		return chJul;
	}
	public void setChJul(BigDecimal chJul) {
		this.chJul = chJul;
	}
	public BigDecimal getChaug() {
		return chaug;
	}
	public void setChaug(BigDecimal chaug) {
		this.chaug = chaug;
	}
	public BigDecimal getChsep() {
		return chsep;
	}
	public void setChsep(BigDecimal chsep) {
		this.chsep = chsep;
	}
	public BigDecimal getChnov() {
		return chnov;
	}
	public void setChnov(BigDecimal chnov) {
		this.chnov = chnov;
	}
	public BigDecimal getChdec() {
		return chdec;
	}
	public void setChdec(BigDecimal chdec) {
		this.chdec = chdec;
	}
	public BigDecimal getChJanl() {
		return chJanl;
	}
	public void setChJanl(BigDecimal chJanl) {
		this.chJanl = chJanl;
	}
	public BigDecimal getChfebl() {
		return chfebl;
	}
	public void setChfebl(BigDecimal chfebl) {
		this.chfebl = chfebl;
	}
	public BigDecimal getChmarl() {
		return chmarl;
	}
	public void setChmarl(BigDecimal chmarl) {
		this.chmarl = chmarl;
	}
	public BigDecimal getChaprl() {
		return chaprl;
	}
	public void setChaprl(BigDecimal chaprl) {
		this.chaprl = chaprl;
	}
	public BigDecimal getChmayl() {
		return chmayl;
	}
	public void setChmayl(BigDecimal chmayl) {
		this.chmayl = chmayl;
	}
	public BigDecimal getChJunl() {
		return chJunl;
	}
	public void setChJunl(BigDecimal chJunl) {
		this.chJunl = chJunl;
	}
	public BigDecimal getChJull() {
		return chJull;
	}
	public void setChJull(BigDecimal chJull) {
		this.chJull = chJull;
	}
	public BigDecimal getChaugl() {
		return chaugl;
	}
	public void setChaugl(BigDecimal chaugl) {
		this.chaugl = chaugl;
	}
	public BigDecimal getChsepl() {
		return chsepl;
	}
	public void setChsepl(BigDecimal chsepl) {
		this.chsepl = chsepl;
	}
	public BigDecimal getChnovl() {
		return chnovl;
	}
	public void setChnovl(BigDecimal chnovl) {
		this.chnovl = chnovl;
	}
	public BigDecimal getChdecl() {
		return chdecl;
	}
	public void setChdecl(BigDecimal chdecl) {
		this.chdecl = chdecl;
	}
	private BigInteger claimAmtBoth;
	private BigInteger claimAmtSal;
	private BigInteger claimAmtNonSal;


	

	
	
	
	
	
	public BigInteger getClaimAmtBoth() {
		return claimAmtBoth;
	}
	public void setClaimAmtBoth(BigInteger claimAmtBoth) {
		this.claimAmtBoth = claimAmtBoth;
	}
	public BigInteger getClaimAmtSal() {
		return claimAmtSal;
	}
	public void setClaimAmtSal(BigInteger claimAmtSal) {
		this.claimAmtSal = claimAmtSal;
	}
	public BigInteger getClaimAmtNonSal() {
		return claimAmtNonSal;
	}
	public void setClaimAmtNonSal(BigInteger claimAmtNonSal) {
		this.claimAmtNonSal = claimAmtNonSal;
	}
	
	public BigDecimal getJanI() {
		return janI;
	}
	public void setJanI(BigDecimal janI) {
		this.janI = janI;
	}
	public BigDecimal getFebI() {
		return febI;
	}
	public void setFebI(BigDecimal febI) {
		this.febI = febI;
	}
	public BigDecimal getMarI() {
		return marI;
	}
	public void setMarI(BigDecimal marI) {
		this.marI = marI;
	}
	public BigDecimal getAprI() {
		return aprI;
	}
	public void setAprI(BigDecimal aprI) {
		this.aprI = aprI;
	}
	public BigDecimal getMayI() {
		return mayI;
	}
	public void setMayI(BigDecimal mayI) {
		this.mayI = mayI;
	}
	public BigDecimal getJunI() {
		return junI;
	}
	public void setJunI(BigDecimal junI) {
		this.junI = junI;
	}
	public BigDecimal getJulyI() {
		return julyI;
	}
	public void setJulyI(BigDecimal julyI) {
		this.julyI = julyI;
	}
	public BigDecimal getAugI() {
		return augI;
	}
	public void setAugI(BigDecimal augI) {
		this.augI = augI;
	}
	public BigDecimal getSepI() {
		return sepI;
	}
	public void setSepI(BigDecimal sepI) {
		this.sepI = sepI;
	}
	public BigDecimal getOctI() {
		return octI;
	}
	public void setOctI(BigDecimal octI) {
		this.octI = octI;
	}
	public BigDecimal getNovI() {
		return novI;
	}
	public void setNovI(BigDecimal novI) {
		this.novI = novI;
	}
	public BigDecimal getDecI() {
		return decI;
	}
	public void setDecI(BigDecimal decI) {
		this.decI = decI;
	}
	public BigDecimal getLjanI() {
		return ljanI;
	}
	public void setLjanI(BigDecimal ljanI) {
		this.ljanI = ljanI;
	}
	public BigDecimal getLfebI() {
		return lfebI;
	}
	public void setLfebI(BigDecimal lfebI) {
		this.lfebI = lfebI;
	}
	public BigDecimal getLmarI() {
		return lmarI;
	}
	public void setLmarI(BigDecimal lmarI) {
		this.lmarI = lmarI;
	}
	public BigDecimal getLaprI() {
		return laprI;
	}
	public void setLaprI(BigDecimal laprI) {
		this.laprI = laprI;
	}
	public BigDecimal getLmayI() {
		return lmayI;
	}
	public void setLmayI(BigDecimal lmayI) {
		this.lmayI = lmayI;
	}
	public BigDecimal getLjunI() {
		return ljunI;
	}
	public void setLjunI(BigDecimal ljunI) {
		this.ljunI = ljunI;
	}
	public BigDecimal getLjulyI() {
		return ljulyI;
	}
	public void setLjulyI(BigDecimal ljulyI) {
		this.ljulyI = ljulyI;
	}
	public BigDecimal getLaugI() {
		return laugI;
	}
	public void setLaugI(BigDecimal laugI) {
		this.laugI = laugI;
	}
	public BigDecimal getLsepI() {
		return lsepI;
	}
	public void setLsepI(BigDecimal lsepI) {
		this.lsepI = lsepI;
	}
	public BigDecimal getLoctI() {
		return loctI;
	}
	public void setLoctI(BigDecimal loctI) {
		this.loctI = loctI;
	}
	public BigDecimal getLnovI() {
		return lnovI;
	}
	public void setLnovI(BigDecimal lnovI) {
		this.lnovI = lnovI;
	}
	public BigDecimal getLdecI() {
		return ldecI;
	}
	public void setLdecI(BigDecimal ldecI) {
		this.ldecI = ldecI;
	}
	public BigInteger getDeplocal() {
		return deplocal;
	}
	public void setDeplocal(BigInteger deplocal) {
		this.deplocal = deplocal;
	}
	public BigInteger getDepOutStaion() {
		return depOutStaion;
	}
	public void setDepOutStaion(BigInteger depOutStaion) {
		this.depOutStaion = depOutStaion;
	}
	public BigInteger getCodReceive() {
		return codReceive;
	}
	public void setCodReceive(BigInteger codReceive) {
		this.codReceive = codReceive;
	}
	public BigInteger getCodDelivery() {
		return codDelivery;
	}
	public void setCodDelivery(BigInteger codDelivery) {
		this.codDelivery = codDelivery;
	}
	public BigInteger getCodSecurity() {
		return codSecurity;
	}
	public void setCodSecurity(BigInteger codSecurity) {
		this.codSecurity = codSecurity;
	}
	public BigInteger getCodeAdvance() {
		return codeAdvance;
	}
	public void setCodeAdvance(BigInteger codeAdvance) {
		this.codeAdvance = codeAdvance;
	}
	public BigInteger getTotalType() {
		return totalType;
	}
	public void setTotalType(BigInteger totalType) {
		this.totalType = totalType;
	}
	public BigInteger getInternalActionItems() {
		return internalActionItems;
	}
	public void setInternalActionItems(BigInteger internalActionItems) {
		this.internalActionItems = internalActionItems;
	}
	public BigInteger getStockistRetunsCompalint() {
		return stockistRetunsCompalint;
	}
	public void setStockistRetunsCompalint(BigInteger stockistRetunsCompalint) {
		this.stockistRetunsCompalint = stockistRetunsCompalint;
	}
	public BigInteger getStockistShortageComplaint() {
		return stockistShortageComplaint;
	}
	public void setStockistShortageComplaint(BigInteger stockistShortageComplaint) {
		this.stockistShortageComplaint = stockistShortageComplaint;
	}
	public BigInteger getHoNotification() {
		return hoNotification;
	}
	public void setHoNotification(BigInteger hoNotification) {
		this.hoNotification = hoNotification;
	}
	public BigInteger getStockistQuey() {
		return stockistQuey;
	}
	public void setStockistQuey(BigInteger stockistQuey) {
		this.stockistQuey = stockistQuey;
	}
	public BigInteger getStockistOtherComplaint() {
		return stockistOtherComplaint;
	}
	public void setStockistOtherComplaint(BigInteger stockistOtherComplaint) {
		this.stockistOtherComplaint = stockistOtherComplaint;
	}
	public BigInteger getHoQuery() {
		return hoQuery;
	}
	public void setHoQuery(BigInteger hoQuery) {
		this.hoQuery = hoQuery;
	}
	public BigInteger getEnqueiry() {
		return enqueiry;
	}
	public void setEnqueiry(BigInteger enqueiry) {
		this.enqueiry = enqueiry;
	}
	public BigInteger getPod() {
		return pod;
	}
	public void setPod(BigInteger pod) {
		this.pod = pod;
	}
	public BigInteger getHoComplaints() {
		return hoComplaints;
	}
	public void setHoComplaints(BigInteger hoComplaints) {
		this.hoComplaints = hoComplaints;
	}
	public BigInteger getOthers() {
		return others;
	}
	public void setOthers(BigInteger others) {
		this.others = others;
	}
	public BigInteger getClaimNonSale() {
		return claimNonSale;
	}
	public void setClaimNonSale(BigInteger claimNonSale) {
		this.claimNonSale = claimNonSale;
	}
	public BigInteger getClaimBoth() {
		return claimBoth;
	}
	public void setClaimBoth(BigInteger claimBoth) {
		this.claimBoth = claimBoth;
	}
	public BigInteger getClaimSale() {
		return claimSale;
	}
	public void setClaimSale(BigInteger claimSale) {
		this.claimSale = claimSale;
	}

	
	public BigInteger getLocalStockist() {
		return localStockist;
	}
	public void setLocalStockist(BigInteger localStockist) {
		this.localStockist = localStockist;
	}
	public BigInteger getOutStaionStockist() {
		return outStaionStockist;
	}
	public void setOutStaionStockist(BigInteger outStaionStockist) {
		this.outStaionStockist = outStaionStockist;
	}
	public BigDecimal getInvoiceAmountLocal() {
		return invoiceAmountLocal;
	}
	public void setInvoiceAmountLocal(BigDecimal invoiceAmountLocal) {
		this.invoiceAmountLocal = invoiceAmountLocal;
	}
	public BigDecimal getInvoiceAmountOutstation() {
		return invoiceAmountOutstation;
	}
	public void setInvoiceAmountOutstation(BigDecimal invoiceAmountOutstation) {
		this.invoiceAmountOutstation = invoiceAmountOutstation;
	}
	private BigDecimal invoiceAmountOutstation;
	
	
	public int getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(int orderTotal) {
		this.orderTotal = orderTotal;
	}
	public int getCompletedOrder() {
		return completedOrder;
	}
	public void setCompletedOrder(int completedOrder) {
		this.completedOrder = completedOrder;
	}
	public int getBlocked() {
		return blocked;
	}
	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}
	

}
