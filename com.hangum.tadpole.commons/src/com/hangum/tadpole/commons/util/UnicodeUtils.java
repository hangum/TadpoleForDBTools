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
package com.hangum.tadpole.commons.util;

import java.lang.Character.UnicodeBlock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 쿼리에 ascii 코드가 들어가면 오류가 발생하여서 쿼리문자중에 유니코드만 들어가도록 검증합니다.
 * 
 * @author hangum
 *
 */
public class UnicodeUtils {
	private static final Logger logger = Logger.getLogger(UnicodeUtils.class);
	
	/**
	 * 텍스트 중간에 ascii 코드가 있어 문제가 되는것을 해결하기 위해서
	 * 
	 * @param content
	 * @return
	 */
	public static String getUnicode(String content) {
		StringBuffer sbData = new StringBuffer();
		
		for (int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			UnicodeBlock ub = UnicodeBlock.of(c);
//			logger.debug("[check unicode]" + c + "[ascii code]" + (int)c);
		
			if (ub.equals(UnicodeBlock.BASIC_LATIN)) sbData.append(c);
			else {
//				Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);
				
				if(logger.isDebugEnabled()) logger.debug("[unicode] [" + c + "]");
				
				if( UnicodeBlock.HANGUL_SYLLABLES.equals(ub) ||  
			        	UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(ub) || 
			            UnicodeBlock.HANGUL_JAMO.equals(ub) ||
			            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(ub) ||
			            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(ub) ||
			            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B.equals(ub) ||
			            UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(ub) ||
			            UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT.equals(ub) ||
			            UnicodeBlock.HIRAGANA.equals(ub) ||
			            UnicodeBlock.KATAKANA.equals(ub) ||
			            UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(ub)
		           ){

					sbData.append(c);
				//
				// html 에 있는 코드 복사하여 붙여 넣을시 공백이 ascii code 160으로 표현됨.
				//
				} else if((int)c == 160){
					sbData.append(" ");
				}
//				} else
//					logger.debug("[i won't unicode block] [" + (int)c + "]");
//				}
			}
		}
		
		return StringUtils.trimToEmpty(sbData.toString());
		
	}
}
