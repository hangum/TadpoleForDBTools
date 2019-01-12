package com.tadpole.common.define.core.define;

import java.util.HashMap;
import java.util.Map;

/*
 * 테드폴허브 이벤트 사유를 기록합니다
 * 
어드민 일 경우
	사용자 패스워드 초기화 : 101
	사용자 생성 : 102
	사용자 장기 로그인 해제 : 103
	사용자 블럭 : 104

User 일 경우
	패스워드 여러번 실패 계정 잠김 : 201
	장기 미로그인 계정 잠김 : 202

DB 일 경우 
	SQL 권한없음 301
 */
public enum TadpoleEventReason {
	ADMIN_PASSWORD_INITIALIZE(101), ADMIN_CREATE_USER(102), ADMIN_LONG_TERM_USER_RELEASE(103), ADMIN_BLOCK_USER(104),
	USER_SOME_TIME_PASSWORD_WRONG(201), USER_LONG_TERM_LOGIN(202),
	DB_SEQ_NON_AUTHORITH(301);
	
	private final int intReason;
	private static Map mapReasonIntTReasonEnum = new HashMap();
	static {
        for (TadpoleEventReason eventReason : TadpoleEventReason.values()) {
            mapReasonIntTReasonEnum.put(eventReason.getIntReason(), eventReason);
        }
    }
	
	private TadpoleEventReason(int intReason) {
		this.intReason = intReason;
	}
	
	/**
	 * get reason type to int
	 * 
	 * @return
	 */
	public int getIntReason() {
		return intReason;
	}
	
	/**
	 * get int to enum name  
	 * 
	 * @param intReason
	 * @return
	 */
	public static TadpoleEventReason valueOf(int intReason) {
		return (TadpoleEventReason)mapReasonIntTReasonEnum.get(intReason);
	}

}
