package com.hangum.tadpole.engine.query.dao.system.ledger;

import java.sql.Timestamp;

/**
 * 원장관리 오라클 파트용 히스토리 데이터
 * 
 * @author hangum
 *
 */
public class DelegerHistoryDAO {
	int SEQ;
	int REFERENCE_KEY;
	String CR_NUMBER;
	String ID;
	String REQ_QUERY;
	String EXE_QUERY;
	String PR_KEY;
	String BEFORE_RESULT;
	String AFTER_RESULT;
	Timestamp CREATE_TIME;
	String PR_ROWID;
	
	public DelegerHistoryDAO() {
	}

	/**
	 * @return the sEQ
	 */
	public int getSEQ() {
		return SEQ;
	}

	/**
	 * @param sEQ the sEQ to set
	 */
	public void setSEQ(int sEQ) {
		SEQ = sEQ;
	}

	/**
	 * @return the rEFERENCE_KEY
	 */
	public int getREFERENCE_KEY() {
		return REFERENCE_KEY;
	}

	/**
	 * @param rEFERENCE_KEY the rEFERENCE_KEY to set
	 */
	public void setREFERENCE_KEY(int rEFERENCE_KEY) {
		REFERENCE_KEY = rEFERENCE_KEY;
	}

	/**
	 * @return the cR_NUMBER
	 */
	public String getCR_NUMBER() {
		return CR_NUMBER;
	}

	/**
	 * @param cR_NUMBER the cR_NUMBER to set
	 */
	public void setCR_NUMBER(String cR_NUMBER) {
		CR_NUMBER = cR_NUMBER;
	}

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * @return the rEQ_QUERY
	 */
	public String getREQ_QUERY() {
		return REQ_QUERY;
	}

	/**
	 * @param rEQ_QUERY the rEQ_QUERY to set
	 */
	public void setREQ_QUERY(String rEQ_QUERY) {
		REQ_QUERY = rEQ_QUERY;
	}

	/**
	 * @return the eXE_QUERY
	 */
	public String getEXE_QUERY() {
		return EXE_QUERY;
	}

	/**
	 * @param eXE_QUERY the eXE_QUERY to set
	 */
	public void setEXE_QUERY(String eXE_QUERY) {
		EXE_QUERY = eXE_QUERY;
	}

	/**
	 * @return the pR_KEY
	 */
	public String getPR_KEY() {
		return PR_KEY;
	}

	/**
	 * @param pR_KEY the pR_KEY to set
	 */
	public void setPR_KEY(String pR_KEY) {
		PR_KEY = pR_KEY;
	}

	/**
	 * @return the bEFORE_RESULT
	 */
	public String getBEFORE_RESULT() {
		return BEFORE_RESULT;
	}

	/**
	 * @param bEFORE_RESULT the bEFORE_RESULT to set
	 */
	public void setBEFORE_RESULT(String bEFORE_RESULT) {
		BEFORE_RESULT = bEFORE_RESULT;
	}

	/**
	 * @return the aFTER_RESULT
	 */
	public String getAFTER_RESULT() {
		return AFTER_RESULT;
	}

	/**
	 * @param aFTER_RESULT the aFTER_RESULT to set
	 */
	public void setAFTER_RESULT(String aFTER_RESULT) {
		AFTER_RESULT = aFTER_RESULT;
	}

	/**
	 * @return the cREATE_TIME
	 */
	public Timestamp getCREATE_TIME() {
		return CREATE_TIME;
	}

	/**
	 * @param cREATE_TIME the cREATE_TIME to set
	 */
	public void setCREATE_TIME(Timestamp cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}

	/**
	 * @return the pR_ROWID
	 */
	public String getPR_ROWID() {
		return PR_ROWID;
	}

	/**
	 * @param pR_ROWID the pR_ROWID to set
	 */
	public void setPR_ROWID(String pR_ROWID) {
		PR_ROWID = pR_ROWID;
	}
	
}
