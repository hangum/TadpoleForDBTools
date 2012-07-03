package com.hangum.tadpole.mongodb.core.dto;

import java.util.ArrayList;
import java.util.List;

import com.hangum.db.dao.mysql.TableColumnDAO;

/**
 * mongodb collection infos
 * 
 * @author hangum
 *
 */
public class MongoDBCollectionInfoDTO {
	private String name;
	private int count;
	private int size;
	private int storage;
	private int index;
	private double avgObj;
	private int padding;
	
	private List<TableColumnDAO> child = new ArrayList<TableColumnDAO>(); 
	
	public MongoDBCollectionInfoDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getAvgObj() {
		return avgObj;
	}

	public void setAvgObj(double avgObj) {
		this.avgObj = avgObj;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public List<TableColumnDAO> getChild() {
		return child;
	}	
	
	public void setChild(List<TableColumnDAO> child) {
		this.child = child;
	}
}
