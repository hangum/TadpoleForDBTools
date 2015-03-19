/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Date;

/**
 * <pre>
 * 	외부 사용자 계정 정보입니다.
 * 
 * 		1. Amazon RDS의 경우.
 * 			- value0에 access key
 * 			- value1에 secret key
 * 			- value2에 endpoint 를 넣습니다.
 * 
 * 			기타 디비의존 정보는 ext컬럼에 넣습니다.
 * </pre>
 * 
 * @author hangum
 *
 */
public class ExternalAccountDAO {
	int seq;
	int user_seq;
	String types;
	String name;
	String value0;
	String value1;
	String value2;
	String value3;
	String value4;
	String value5;
	String value6;
	String value7;
	String value8;
	String value9;
	String success;
	String message;
	Date create_time;
	String delYn;
	
	public ExternalAccountDAO() {
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the user_seq
	 */
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the types
	 */
	public String getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
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
	 * @return the value0
	 */
	public String getValue0() {
		return value0;
	}

	/**
	 * @param value0 the value0 to set
	 */
	public void setValue0(String value0) {
		this.value0 = value0;
	}

	/**
	 * @return the value1
	 */
	public String getValue1() {
		return value1;
	}

	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}

	/**
	 * @return the value2
	 */
	public String getValue2() {
		return value2;
	}

	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}

	/**
	 * @return the value3
	 */
	public String getValue3() {
		return value3;
	}

	/**
	 * @param value3 the value3 to set
	 */
	public void setValue3(String value3) {
		this.value3 = value3;
	}

	/**
	 * @return the value4
	 */
	public String getValue4() {
		return value4;
	}

	/**
	 * @param value4 the value4 to set
	 */
	public void setValue4(String value4) {
		this.value4 = value4;
	}

	/**
	 * @return the value5
	 */
	public String getValue5() {
		return value5;
	}

	/**
	 * @param value5 the value5 to set
	 */
	public void setValue5(String value5) {
		this.value5 = value5;
	}

	/**
	 * @return the value6
	 */
	public String getValue6() {
		return value6;
	}

	/**
	 * @param value6 the value6 to set
	 */
	public void setValue6(String value6) {
		this.value6 = value6;
	}

	/**
	 * @return the value7
	 */
	public String getValue7() {
		return value7;
	}

	/**
	 * @param value7 the value7 to set
	 */
	public void setValue7(String value7) {
		this.value7 = value7;
	}

	/**
	 * @return the value8
	 */
	public String getValue8() {
		return value8;
	}

	/**
	 * @param value8 the value8 to set
	 */
	public void setValue8(String value8) {
		this.value8 = value8;
	}

	/**
	 * @return the value9
	 */
	public String getValue9() {
		return value9;
	}

	/**
	 * @param value9 the value9 to set
	 */
	public void setValue9(String value9) {
		this.value9 = value9;
	}

	/**
	 * @return the success
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the delYn
	 */
	public String getDelYn() {
		return delYn;
	}

	/**
	 * @param delYn the delYn to set
	 */
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	
}
