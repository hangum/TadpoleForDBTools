package com.hangum.tadpole.dao;

/**
 * 올챙이에서 사용하는 디비 정보를 정의합니다.
 * 
 * @author hangum
 *
 */
public class DBInfoDAO {
	String productversion;
	String productlevel;
	String edition;
	
	public DBInfoDAO() {
	}

	public String getProductversion() {
		return productversion;
	}
	
	public void setProductversion(String productversion) {
		this.productversion = productversion;
	}

	public String getProductlevel() {
		return productlevel;
	}

	public void setProductlevel(String productlevel) {
		this.productlevel = productlevel;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	@Override
	public String toString() {
		return "DBInfoDAO [productversion=" + productversion
				+ ", productlevel=" + productlevel + ", edition=" + edition
				+ "]";
	}
	
	
}
