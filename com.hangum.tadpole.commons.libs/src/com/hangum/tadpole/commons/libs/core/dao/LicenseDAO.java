package com.hangum.tadpole.commons.libs.core.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * License dao
 * 
 * @author hangum
 *
 */
public class LicenseDAO {
	boolean isValidate = false;
	boolean isEnterprise = false;
	String productType = "";
	String term = "";
	String msg = "";
	
	String customer_email = "";
	String customer = "";
	
	String mActivationDate = "";
	String mExpirationDate = "";
	
	int personLimit = 0;
	
	public LicenseDAO() {
	}
	
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * @return the isEnterprise
	 */
	public boolean isEnterprise() {
		return isEnterprise;
	}

	/**
	 * @param isEnterprise the isEnterprise to set
	 */
	public void setEnterprise(boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	/**
	 * @return the isValidate
	 */
	public boolean isValidate() {
		return isValidate;
	}

	/**
	 * @param isValidate the isValidate to set
	 */
	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the customer_email
	 */
	public String getCustomer_email() {
		return customer_email;
	}

	/**
	 * @param customer_email the customer_email to set
	 */
	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}
	
	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}
	
	public void setActivationDate(String aDate) {
		mActivationDate = aDate;
	}
	
	public String getActivationDate() {
		return mActivationDate;
	}
	
	public void setExpirationDate(String aDate) {
		this.mExpirationDate = aDate;
	}
	
	public String getExpirationDate() {
		return mExpirationDate;
	}
	

	/**
	 * @return the personLimit
	 */
	public int getPersonLimit() {
		return personLimit;
	}

	/**
	 * @param personLimit the personLimit to set
	 */
	public void setPersonLimit(int personLimit) {
		this.personLimit = personLimit;
	}

	public long getRemaining() {
		long sRemaining = 0;
		
		if( this.mExpirationDate != "" ) {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				Date sExpirationDate = sDateFormat.parse(this.mExpirationDate);
				Date sToday = new Date (); 
				
				/* Get how many days left */
				sRemaining = (sExpirationDate.getTime() - sToday.getTime()) / (24 * 60 * 60 * 1000); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sRemaining;
	}
	
}
