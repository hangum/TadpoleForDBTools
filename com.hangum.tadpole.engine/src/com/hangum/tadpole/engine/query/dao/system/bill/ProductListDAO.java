package com.hangum.tadpole.engine.query.dao.system.bill;

/**
 * product list
 * 
 * @author hangum
 *
 */
public class ProductListDAO {
	int seq;
	String type = "";
	String name = "";
	String description = "";
	int price = 0;
	String del_yn = "NO";

	public ProductListDAO() {
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the del_yn
	 */
	public String getDel_yn() {
		return del_yn;
	}

	/**
	 * @param del_yn the del_yn to set
	 */
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}

}
