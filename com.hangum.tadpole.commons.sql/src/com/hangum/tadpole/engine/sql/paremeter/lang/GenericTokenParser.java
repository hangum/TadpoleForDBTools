package com.hangum.tadpole.engine.sql.paremeter.lang;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic token parser
 * 
 * original source : Mybatisc 코드를 수정한것.
 * 
 * @author hangum
 *
 */
public class GenericTokenParser {
	
	private final String openToken;
	private final String closeToken;

	/**
	 * Maps parameter names to arrays of ints which are the parameter indices.
	 */
    private Map<Integer, String> mapIndexToName = new HashMap<Integer, String>();

	public GenericTokenParser(String openToken, String closeToken) {
		this.openToken = openToken;
		this.closeToken = closeToken;
	}

	public String parse(String text) {
		final StringBuilder builder = new StringBuilder();
		final StringBuilder expression = new StringBuilder();
		if (text != null && text.length() > 0) {
			char[] src = text.toCharArray();
			int offset = 0;
			// search open token
			int start = text.indexOf(openToken, offset);
			while (start > -1) {
				if (start > 0 && src[start - 1] == '\\') {
					// this open token is escaped. remove the backslash and
					// continue.
					builder.append(src, offset, start - offset - 1).append(openToken);
					offset = start + openToken.length();
				} else {
					// found open token. let's search close token.
					expression.setLength(0);
					builder.append(src, offset, start - offset);
					offset = start + openToken.length();
					int end = text.indexOf(closeToken, offset);
					while (end > -1) {
						if (end > offset && src[end - 1] == '\\') {
							// this close token is escaped. remove the backslash
							// and continue.
							expression.append(src, offset, end - offset - 1).append(closeToken);
							offset = end + closeToken.length();
							end = text.indexOf(closeToken, offset);
						} else {
							expression.append(src, offset, end - offset);
							offset = end + closeToken.length();
							break;
						}
					}
					if (end == -1) {
						// close token was not found.
						builder.append(src, start, src.length - start);
						offset = src.length;
					} else {
						String strVariable = text.substring(start + openToken.length(), end);
						mapIndexToName.put(mapIndexToName.size()+1, strVariable);
						
						builder.append("?");
						offset = end + closeToken.length();
					}
				}
				start = text.indexOf(openToken, offset);
			}
			if (offset < src.length) {
				builder.append(src, offset, src.length - offset);
			}
		}
		return builder.toString();
	}
	
	 /**
     * Returns the index to param names
     * @param intIndex
     * @return
     */
    public String getIndex(Integer intIndex) {
    	return mapIndexToName.get(intIndex);
    }
    
    public Map<Integer, String> getMapIndexToName() {
		return mapIndexToName;
	}
}
