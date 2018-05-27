package com.hangum.tadpole.engine.query.dao.system.commons;

/**
 * tadpole_sequence
 * 
 * @author hangum
 *
 */
public class TadpoleSequenceDAO {
	String name;
	int no;

	public TadpoleSequenceDAO() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the no
	 */
	public int getNo() {
		return no;
	}

	/**
	 * @param no the no to set
	 */
	public void setNo(int no) {
		this.no = no;
	}
	
}
