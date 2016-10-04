package com.hangum.tadpole.engine.query.dao.agens;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.query.dao.rdb.AbstractDAO;
import com.hangum.tadpole.engine.query.dao.rdb.FieldNameAnnotationClass;

/**
 * agesns vertex dao
 * 
 * @author hangum
 *
 */
public class AgensVertexDAO extends AbstractDAO {
	
	String labname;
	String relid;
	String labowner;
	String labkind;
	String inhrelid;
	String inhparent;
	String inhseqno;
	
	public AgensVertexDAO() {
	}
	
	/**
	 * @return the labname
	 */
	@FieldNameAnnotationClass(fieldKey = "labname")
	public String getLabname() {
		return labname;
	}

	/**
	 * @param labname the labname to set
	 */
	public void setLabname(String labname) {
		this.labname = labname;
	}

	/**
	 * @return the relid
	 */
	@FieldNameAnnotationClass(fieldKey = "relid")
	public String getRelid() {
		return relid;
	}

	/**
	 * @param relid the relid to set
	 */
	public void setRelid(String relid) {
		this.relid = relid;
	}

	/**
	 * @return the labowner
	 */
	@FieldNameAnnotationClass(fieldKey = "labowner")
	public String getLabowner() {
		return labowner;
	}

	/**
	 * @param labowner the labowner to set
	 */
	public void setLabowner(String labowner) {
		this.labowner = labowner;
	}

	/**
	 * @return the labkind
	 */
	@FieldNameAnnotationClass(fieldKey = "labkind")
	public String getLabkind() {
		return labkind;
	}

	/**
	 * @param labkind the labkind to set
	 */
	public void setLabkind(String labkind) {
		this.labkind = labkind;
	}

	/**
	 * @return the inhrelid
	 */
	@FieldNameAnnotationClass(fieldKey = "inhrelid")
	public String getInhrelid() {
		return inhrelid;
	}

	/**
	 * @param inhrelid the inhrelid to set
	 */
	public void setInhrelid(String inhrelid) {
		this.inhrelid = inhrelid;
	}

	/**
	 * @return the inhparent
	 */
	@FieldNameAnnotationClass(fieldKey = "inhparent")
	public String getInhparent() {
		return inhparent;
	}

	/**
	 * @param inhparent the inhparent to set
	 */
	public void setInhparent(String inhparent) {
		this.inhparent = inhparent;
	}

	/**
	 * @return the inhseqno
	 */
	@FieldNameAnnotationClass(fieldKey = "inhseqno")
	public String getInhseqno() {
		return inhseqno;
	}

	/**
	 * @param inhseqno the inhseqno to set
	 */
	public void setInhseqno(String inhseqno) {
		this.inhseqno = inhseqno;
	}

	@Override
	public String getFullName() {
		if(StringUtils.isEmpty(this.getSchema_name())) {
			return this.getSysName();
		}else{
			return String.format("%s.%s", this.getSchema_name(), this.getSysName());
		}
	}

}
