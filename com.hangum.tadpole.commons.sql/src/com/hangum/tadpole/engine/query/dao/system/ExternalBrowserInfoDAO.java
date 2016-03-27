package com.hangum.tadpole.engine.query.dao.system;

/**
 * external_browser_info dao
 * 
 * 데이터베이스에서 연결하고 싶은 외부 브라우저를 지정합니다.
 * 
 * @author hangum
 *
 */
public class ExternalBrowserInfoDAO {
	int seq;
	int db_seq;
	String is_used = "NO";
	String name;
	String url;
	String comment = "";
	
	public ExternalBrowserInfoDAO() {
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
	 * @return the db_seq
	 */
	public int getDb_seq() {
		return db_seq;
	}

	/**
	 * @param db_seq the db_seq to set
	 */
	public void setDb_seq(int db_seq) {
		this.db_seq = db_seq;
	}

	/**
	 * @return the is_used
	 */
	public String getIs_used() {
		return is_used;
	}

	/**
	 * @param is_used the is_used to set
	 */
	public void setIs_used(String is_used) {
		this.is_used = is_used;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExternalBrowserInfoDAO [seq=" + seq + ", db_seq=" + db_seq
				+ ", is_used=" + is_used + ", name=" + name + ", url=" + url
				+ ", comment=" + comment + "]";
	}
	
	

}
