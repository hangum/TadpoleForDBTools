package com.hangum.tadpole.ext.appm;

import java.util.Map;

import com.hangum.tadpole.ext.appm.core.IAPPM;

/**
 * <pre>
 * 	디비의 패스워드를 받아옵니다.
 *  서버의 정보는 {@code KABangDefine#CONFIG_FILE}의 설정을 따릅니다.
 * 	
 * </pre>
 * @author hangum
 *
 */
public class APPMHandler implements IAPPM {
	private static APPMHandler instance;

	private APPMHandler() {
	}

	public static APPMHandler getInstance() {
		if (instance == null) {
			instance = new APPMHandler();
		}

		return instance;
	}

	/**
	 * get password
	 * 
	 * @param mapAppm
	 * @return
	 * @throws Exception
	 */
	public String getPassword(Map<String, String> mapAppm) throws Exception {
		return "";
	}

}
