package com.hangum.tadpole.db.csv;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class CVSFileWrite {
	public static void main(String[] args) {
		try {
			String csv = "./output2.csv";
			CSVWriter writer = new CSVWriter(new FileWriter(csv));
			 
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] {"India", "New Delhi"});
			data.add(new String[] {"SELECT deposits_id, book_id, book_no, book_title, publisher, writer, transfer, pubdate, pages, isbn, plate, price, book_cnt, new, recommand, best, main, mainsort, series, series_no, bookintro, authorintro, tablecontents, pubreview, imgurl, imgurl2, imgfilename, award, tag, read_no, useyn, disposal_reson, circum_id, useobj_id, regdate, regid, moddate, modid  FROM books", "Washington D.C"});
			data.add(new String[] {"Germ\"a,ny", "Berlin"});
			 
			writer.writeAll(data);
			 
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
