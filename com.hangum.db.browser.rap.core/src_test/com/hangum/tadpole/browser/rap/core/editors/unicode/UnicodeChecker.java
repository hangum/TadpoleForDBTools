/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.browser.rap.core.editors.unicode;

import java.lang.Character.UnicodeBlock;

public class UnicodeChecker {

	/**
	 * 유니코드 인지 아닌지 검사
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String str = "INSERT INTO sampl3e_table (id, name) VALUES (‌49, '한글'); ";

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			
			UnicodeBlock ub = UnicodeBlock.of(c);
			if (ub.equals(UnicodeBlock.BASIC_LATIN)) {
				System.out.println("[unicode]" + c);
			} else {
				Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);
				if( UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock) ||  
			        	UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock) || 
			            UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock) ||
			            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(unicodeBlock) ||
			            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(unicodeBlock) ||
			            UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B.equals(unicodeBlock) ||
			            UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(unicodeBlock) ||
			            UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT.equals(unicodeBlock) ||
			            UnicodeBlock.HIRAGANA.equals(unicodeBlock) ||
			            UnicodeBlock.KATAKANA.equals(unicodeBlock) ||
			            UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS.equals(unicodeBlock)
			           ){

			        	System.out.println(c + "\t ===> 통과");
			        } else {
			        	System.out.println(c + "\t ===>  실패");
			        
			        }
			}
		}

	}

}
