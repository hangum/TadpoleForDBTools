package com.hangum.tadpole.commons.util;

/**
 * OS마다 다른 단축키를 검사합니다.
 * 
 * @author hangum
 * 
 */
public class ShortcutPrefixUtils {

	/**
	 * window기준으로 ctrl키를 시스템 별로 정의합니다.
	 * 
	 * @return
	 */
	public static String getCtrlShortcut() {
		ServletUserAgent.OS_SIMPLE_TYPE osSimpleType = RequestInfoUtils.findOSSimpleType();
		String prefixOSShortCut = "Ctrl ";
		if (osSimpleType == ServletUserAgent.OS_SIMPLE_TYPE.MACOSX)
			prefixOSShortCut = "Command ";

		return prefixOSShortCut;
	}
}
