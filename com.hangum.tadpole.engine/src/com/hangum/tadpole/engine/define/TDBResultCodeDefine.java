/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.define;

/**
 * Tadpole 에서 정의하는 SQL code를 정의 합니다.
 * 
 * 참조) https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C
 * 
 * @author hangum
 *
 */
public class TDBResultCodeDefine {

	/* 성공 (2xx) */
		/** 쿼리 성공 */
		public static int NORMAL_SUCC = 200;
	
	/* 요청 오류 (4xx) */
		/** 잘못된 요청(쿼리 실패)  */
		public static int BAD_REQUEST = 400;
		
		/** 권한 없음(인증관련)  */
		public static int UNAUTHENTICATED = 401;
		
		/** 사용자가 필요 권한을 가지고 있지 않다.(접근제어 관련 권한이 없다) */
		public static int FORBIDDEN = 403;

		/** 데이터베이스 연결 실패(서버가 없을 경우) */
		public static int NOT_FOUND = 404;
		
		/** 사전 조건 오류 (파라미티 오류 등) */
		public static int PRECONDITION_FAILED = 412;
		
	/* 서버 오류 (5xx) */
		/** 내부 서버 오류 */
		public static int INTERNAL_SERVER_ERROR = 500;

		/** 대역폭 초과 */
		public static int NOT_EXTENDED = 509;
}
