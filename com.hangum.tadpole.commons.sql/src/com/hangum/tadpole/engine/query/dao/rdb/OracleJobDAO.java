/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - 데이터베이스 링크 정보
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.rdb;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 데이터베이스에 정의된 링크 정보를 조회한다.
 * 
 * @author nilriri
 * 
 */
public class OracleJobDAO extends AbstractDAO {

	int job;
	String what;
	String log_user;
	String priv_user;
	String last_date;
	String this_date;
	String this_sec;
	String next_date;
	String next_sec;
	long total_time;
	String broken;
	String interval;
	long failures;
	String nls_env;
	long instance;

	public OracleJobDAO() {
	}

	public OracleJobDAO(UserDBDAO userDB) {
		this.schema_name = userDB.getSchema();
	}

	@Override
	public String getFullName() {
		return (job + "");
	}

	@FieldNameAnnotationClass(fieldKey = "job")
	public int getJob() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

	@FieldNameAnnotationClass(fieldKey = "what")
	public String getWhat() {
		return what == null ? "" : what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	@FieldNameAnnotationClass(fieldKey = "log_user")
	public String getLog_user() {
		return log_user;
	}

	public void setLog_user(String log_user) {
		this.log_user = log_user;
	}

	@FieldNameAnnotationClass(fieldKey = "priv_user")
	public String getPriv_user() {
		return priv_user;
	}

	public void setPriv_user(String priv_user) {
		this.priv_user = priv_user;
	}

	@FieldNameAnnotationClass(fieldKey = "last_date")
	public String getLast_date() {
		return last_date;
	}

	public void setLast_date(String last_date) {
		this.last_date = last_date;
	}

	@FieldNameAnnotationClass(fieldKey = "this_date")
	public String getThis_date() {
		return this_date;
	}

	public void setThis_date(String this_date) {
		this.this_date = this_date;
	}

	@FieldNameAnnotationClass(fieldKey = "this_sec")
	public String getThis_sec() {
		return this_sec;
	}

	public void setThis_sec(String this_sec) {
		this.this_sec = this_sec;
	}

	@FieldNameAnnotationClass(fieldKey = "next_date")
	public String getNext_date() {
		return next_date;
	}

	public void setNext_date(String next_date) {
		this.next_date = next_date;
	}

	@FieldNameAnnotationClass(fieldKey = "next_sec")
	public String getNext_sec() {
		return next_sec;
	}

	public void setNext_sec(String next_sec) {
		this.next_sec = next_sec;
	}

	@FieldNameAnnotationClass(fieldKey = "total_time")
	public long getTotal_time() {
		return total_time;
	}

	public void setTotal_time(String total_time) {
		this.total_time = Long.parseLong(total_time==null?"0":total_time);;
	}

	@FieldNameAnnotationClass(fieldKey = "broken")
	public String getBroken() {
		return broken;
	}

	public void setBroken(String broken) {
		this.broken = broken;
	}

	@FieldNameAnnotationClass(fieldKey = "interval")
	public String getInterval() {
		return interval == null ? "" : interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	@FieldNameAnnotationClass(fieldKey = "failures")
	public long getFailures() {
		return failures;
	}

	public void setFailures(String failures) {
		this.failures = Long.parseLong(failures==null?"0":failures);
	}

	@FieldNameAnnotationClass(fieldKey = "nls_env")
	public String getNls_env() {
		return nls_env;
	}

	public void setNls_env(String nls_env) {
		this.nls_env = nls_env;
	}

	@FieldNameAnnotationClass(fieldKey = "instance")
	public long getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = Long.parseLong(instance==null?"0":instance);;
	}

	public int getNextYear() {
		return Integer.parseInt(this.next_date.substring(0, 4));
	}

	public int getNextMonth() {
		return Integer.parseInt(this.next_date.substring(5, 7)) - 1;
	}

	public int getNextDay() {
		return Integer.parseInt(this.next_date.substring(8, 10));
	}
}
