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
public class AgensGraphPathDAO extends AbstractDAO {
	
	String graphname;
	long nspid;
	
	public AgensGraphPathDAO() {
	}
	
	

	/**
	 * @return the graphname
	 */
	@FieldNameAnnotationClass(fieldKey = "graphname")
	public String getGraphname() {
		return graphname;
	}



	/**
	 * @param graphname the graphname to set
	 */
	public void setGraphname(String graphname) {
		this.graphname = graphname;
	}



	/**
	 * @return the nspid
	 */
	@FieldNameAnnotationClass(fieldKey = "nspid")
	public long getNspid() {
		return nspid;
	}



	/**
	 * @param nspid the nspid to set
	 */
	public void setNspid(long nspid) {
		this.nspid = nspid;
	}



	@Override
	public String getFullName() {
		if(StringUtils.isEmpty(this.getSchema_name())) {
			return this.getSysName();
		}else{
			return String.format("%s", this.getSysName());
		}
	}

}
