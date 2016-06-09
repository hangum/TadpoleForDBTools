/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.utils.zip.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * zip file
 * 
 * @author hangum
 *
 */
public class ZipUtils {
	
	public static String pack(String fullPath) throws Exception {
		int intlastSep = fullPath.lastIndexOf(File.separator);
		
		String base_dir = fullPath.substring(0, intlastSep);
		String zipFile = fullPath.substring(intlastSep+1);
		
		return pack(base_dir, zipFile);
	}
	
	/**
	 * 
	 * @param base_dir
	 * @param zipFile
	 * @return
	 * @throws Exception
	 */
	public static String pack(String base_dir, String zipFile) throws Exception {
		String zipFullPath = PublicTadpoleDefine.TEMP_DIR + zipFile + ".zip";
		ZipUtil.pack(new File(base_dir), new File(zipFullPath),
				new NameMapper() {
					public String map(String name) {
						try {
							if (!StringUtils.equals(name,StringEscapeUtils.escapeJava(name))) {
								name = "download_files" + StringUtils.substring(name,StringUtils.lastIndexOf(name, '.'));
							}
							name = new String(name.getBytes(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return name;
					}
				});

		return zipFullPath;
	}
}
