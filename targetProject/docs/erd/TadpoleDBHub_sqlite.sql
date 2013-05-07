--
--  mysql로 작업된 스키마를 sqlite로 수정하여 작
--  2013/05/04 마지막 회의 
--

--
-- tadpole system information table
--
CREATE TABLE tadpole_system (
	seq INTEGER PRIMARY KEY AUTOINCREMENT,
	name		VARCHAR(100) NOT NULL,
	major_version VARCHAR(50) NOT NULL,
	sub_version VARCHAR(50) NOT NULL,	
	information VARCHAR(2000) NOT NULL,
	create_time DATE DEFAULT (datetime('now','localtime'))
);

--
-- user_group
--
CREATE TABLE user_group (
	SEQ         INTEGER PRIMARY KEY AUTOINCREMENT, -- 그룹순번
	NAME        VARCHAR(60) NULL,     -- 그룹명칭
	CREATE_TIME DATE DEFAULT (datetime('now','localtime')), -- 생성일시
	DELYN       CHAR(3)     NULL     DEFAULT 'NO' -- 삭제여부
);

--
-- user
--
CREATE TABLE user (
	SEQ         INTEGER PRIMARY KEY AUTOINCREMENT, -- 사용자순번
	EMAIL       VARCHAR(60) NOT NULL, -- 이메일
	PASSWD      VARCHAR(60) NOT NULL, -- 비밀번호
	NAME        VARCHAR(60) NOT NULL, -- 사용자명
	LANGUAGE    VARCHAR(10) NULL,     -- 사용자언어
	APPROVAL_YN CHAR(3)     NOT NULL DEFAULT 'NO', -- 승인여부
	CREATE_TIME DATE DEFAULT (datetime('now','localtime')), -- 생성일
	DELYN       CHAR(3)     NOT NULL DEFAULT 'NO' -- 삭제여부
);

-- 사용자역활
CREATE TABLE user_role (
	SEQ       INTEGER PRIMARY KEY AUTOINCREMENT, -- 역활순번
	GROUP_SEQ INT         NOT NULL, -- 그룹순번
	USER_SEQ  INT         NOT NULL, -- 사용자순번
	ROLE_TYPE VARCHAR(20) NOT NULL DEFAULT '00', -- 사용자유형
	NAME      VARCHAR(50) NULL,     -- 역활명
	DELYN     CHAR(3)     NOT NULL DEFAULT 'NO', -- 삭제여부
	FOREIGN KEY (GROUP_SEQ) REFERENCES user_group (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY (USER_SEQ) REFERENCES user (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);


-- 데이터베이스
CREATE TABLE user_db (
	SEQ                 INTEGER PRIMARY KEY AUTOINCREMENT, -- 사용자순번
	USER_SEQ            INT           NOT NULL, -- 사용자순번
	EXT_SEQ             INT           NOT NULL, -- 추가정보아이디
	GROUP_SEQ           INT           NOT NULL, -- 그룹순번
	OPERATION_TYPE      VARCHAR(10)   NULL,     -- 운영타입
	DBMS_TYPES          VARCHAR(50)   NULL,     -- 종류
	URL                 VARCHAR(2000) NULL,     -- 접속주소
	DB                  VARCHAR(50)   NULL,     -- 데이터베이스
	GROUP_NAME          VARCHAR(50)   NULL,     -- 그룹명칭
	DISPLAY_NAME        VARCHAR(50)   NULL,     -- 표시이름
	HOST                VARCHAR(50)   NULL,     -- 서버
	PORT                VARCHAR(10)   NULL,     -- 포트
	LOCALE              VARCHAR(10)   NULL,     -- 지역
	PASSWD              VARCHAR(500)  NULL,     -- 비밀번호
	USERS               VARCHAR(100)  NULL,     -- 사용자
	IS_PROFILE          CHAR(3)       NULL     DEFAULT 'NO', -- 프로파일여부
	PROFILE_SELECT_MILL INT           NULL     DEFAULT 0, -- 프로파일링제한시간
	QUESTION_DML        CHAR(3)       NULL     DEFAULT 'NO', -- DML조회
	IS_READONLYCONNECT  CHAR(3)       NULL     DEFAULT 'NO', -- 읽기전용접속
	IS_AUTOCOMMIT       CHAR(3)       NULL     DEFAULT 'NO', -- 자동커밋
	CREATE_TIME 	DATE DEFAULT (datetime('now','localtime')), -- 생성일
	DELYN               CHAR(3)       NULL     DEFAULT 'NO', -- 삭제여부
	FOREIGN KEY (USER_SEQ) REFERENCES user (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY (GROUP_SEQ) REFERENCES user_group (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);
	
-- 사용자정보
CREATE TABLE user_info_data (
	SEQ      INTEGER PRIMARY KEY AUTOINCREMENT, -- 사용자순번
	USER_SEQ INT           NOT NULL, -- 사용자순번
	DB_SEQ   INT           NOT NULL, -- DB순번
	NAME     VARCHAR(40)   NULL,     -- 명칭
	VALUE0   VARCHAR(2000) NULL,     -- 값0
	VALUE1   VARCHAR(2000) NULL,     -- 값1
	VALUE2   VARCHAR(2000) NULL,     -- 값2
	VALUE3   VARCHAR(2000) NULL,     -- 값3
	VALUE4   VARCHAR(2000) NULL,     -- 값4
	VALUE5   VARCHAR(2000) NULL,      -- 값5
	FOREIGN KEY (USER_SEQ) REFERENCES user (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY (DB_SEQ) REFERENCES user_db (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- 실행SQL
CREATE TABLE executed_sql_resource (
	SEQ              INTEGER PRIMARY KEY AUTOINCREMENT, -- 사용자순번
	USER_SEQ         INT           NOT NULL, -- 사용자순번
	DB_SEQ           INT           NOT NULL, -- DB순번
	TYPES            VARCHAR(10)   NULL     DEFAULT '00', -- 유형
	STARTDATEEXECUTE DATETIME      NOT NULL, -- 실행시작일시
	ENDDATEEXECUTE   DATETIME      NOT NULL, -- 실행종료일시
	ROW              INT           NULL     DEFAULT 0, -- 조회결과갯수
	RESULT           CHAR(3)       NULL,     -- 결과코드
	MESSAGE          VARCHAR(2000) NULL,     -- 결과메시지
	CREATE_TIME 	DATE DEFAULT (datetime('now','localtime')), -- 생성일
	DELYN            CHAR(3)       NOT NULL DEFAULT 'N', -- 삭제여부
	FOREIGN KEY (USER_SEQ) REFERENCES user (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY (DB_SEQ) REFERENCES user_db (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- 실행SQL내용
CREATE TABLE executed_sql_resource_data (
	SEQ                       INTEGER PRIMARY KEY AUTOINCREMENT, -- 내용순번
	EXECUTED_SQL_RESOURCE_SEQ INT           NOT NULL, -- 리소스순번
	DATAS                     VARCHAR(2000) NULL,      -- SQL내용
	FOREIGN KEY (EXECUTED_SQL_RESOURCE_SEQ) REFERENCES executed_sql_resource  (seq) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- 계정추가정보
CREATE TABLE account_ext (
	SEQ         INTEGER PRIMARY KEY AUTOINCREMENT, -- 추가정보아이디
	USER_SEQ    INT           NOT NULL, -- 사용자순번
	TYPES       VARCHAR(100)  NULL,     -- 유형
	NAME        VARCHAR(1000) NULL,     -- 속성명
	VALUE0      VARCHAR(1000) NULL,     -- 속성값0
	VALUE1      VARCHAR(1000) NULL,     -- 속성값1
	VALUE2      VARCHAR(1000) NULL,     -- 속성값2
	VALUE3      VARCHAR(1000) NULL,     -- 속성값3
	VALUE4      VARCHAR(1000) NULL,     -- 속성값4
	VALUE5      VARCHAR(1000) NULL,     -- 속성값5
	VALUE6      VARCHAR(1000) NULL,     -- 속성값6
	VALUE7      VARCHAR(1000) NULL,     -- 속성값7
	VALUE8      VARCHAR(1000) NULL,     -- 속성값8
	VALUE9      VARCHAR(1000) NULL,     -- 속성값9
	SUCCESS     VARCHAR(10)   NULL,     -- 성공
	MESSAGE     VARCHAR(1000) NULL,     -- 메시지
	DELYN       CHAR(3)       NOT NULL DEFAULT 'NO', -- 삭제여부
	CREATE_TIME DATE DEFAULT (datetime('now','localtime')), -- 생성일시
	FOREIGN KEY (USER_SEQ) REFERENCES user (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- 테이블필터
CREATE TABLE user_db_filter (
	SEQ                  INTEGER PRIMARY KEY AUTOINCREMENT, -- DB아이디
	IS_TABLE_FILTER      CHAR(3)       NOT NULL DEFAULT 'NO', -- 테이블필터여부
	TABLE_FILTER_INCLUDE VARCHAR(2000) NULL,     -- 포함테이블
	TABLE_FILTER_EXCLUDE VARCHAR(2000) NULL      -- 제외테이블
);

-- 데이터베이스확장속성
CREATE TABLE user_db_ext (
	SEQ    INTEGER PRIMARY KEY AUTOINCREMENT, -- DB아이디
	ORACLE VARCHAR(10)        NULL,     -- 오라클
	MONGO  VARCHAR(10)        NULL,     -- 몽고
	EXT3   VARCHAR(10)        NULL,     -- 확장속성3
	EXT4   VARCHAR(10) NULL,     -- 확장속성4
	EXT5   VARCHAR(10) NULL,     -- 확장속성5
	EXT6   VARCHAR(10) NULL,     -- 확장속성6
	EXT7   VARCHAR(10) NULL,     -- 확장속성7
	EXT8   VARCHAR(10) NULL,     -- 확장속성8
	EXT9   VARCHAR(10) NULL,     -- 확장속성9
	EXT10  VARCHAR(10) NULL      -- 확장속성10
);


-- 클래스 목록
CREATE TABLE security_class (
	SEQ         INTEGER PRIMARY KEY AUTOINCREMENT, -- 순번
	GROUP_SEQ  INT           NOT NULL, -- 그룹순번
	CLASS_NAME VARCHAR(255)  NOT NULL, -- 클래스명
	CLASS_DESC VARCHAR(2000) NULL ,     -- 클래스설명
	FOREIGN KEY (GROUP_SEQ) REFERENCES user_group (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);


-- 데이터보안정보
CREATE TABLE data_security (
	SEQ         INTEGER PRIMARY KEY AUTOINCREMENT, -- 순번
	DB_SEQ      INT         NOT NULL, -- DB아이디
	CLASS_SEQ   INT         NOT NULL, -- 클래스순번
	USER_SEQ    INT         NOT NULL, -- 사용자순번
	TABLE_ID    VARCHAR(30) NOT NULL, -- 테이블명
	COLUMN_ID   VARCHAR(30) NOT NULL, -- 컬럼명
	CREATE_TIME DATE DEFAULT (datetime('now','localtime')), -- 생성일시
	DELYN       CHAR(3)     NOT NULL DEFAULT 'NO', -- 삭제여부
	FOREIGN KEY (DB_SEQ) REFERENCES security_class  (seq) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY (DB_SEQ) REFERENCES user_db (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);


-- 사용자리소스
CREATE TABLE user_db_resource (
	RESOURCE_ID    INTEGER PRIMARY KEY AUTOINCREMENT, -- 사용자순번
	RESOURCE_TYPES VARCHAR(10)   NOT NULL, -- 유형
	USER_SEQ       INT           NOT NULL, -- 사용자순번
	DB_SEQ         INT           NOT NULL, -- DB아이디
	GROUP_SEQ      INT           NOT NULL, -- 그룹순번
	NAME           VARCHAR(50)   NULL,     -- 이름
	SHARED_TYPE    VARCHAR(7)    NOT NULL DEFAULT 'PRIVATE', -- 공유종류
	DESCRIPTION    VARCHAR(2000) NULL,     -- 설명
	CREATE_TIME 	DATE DEFAULT (datetime('now','localtime')), -- 생성일
	DELYN          CHAR(3)       NOT NULL DEFAULT 'NO', -- 삭제여부
	FOREIGN KEY (USER_SEQ) REFERENCES user (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	FOREIGN KEY (DB_SEQ) REFERENCES user_db (SEQ) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- 리소스내용
CREATE TABLE user_db_resource_data (
	SEQ                  INTEGER PRIMARY KEY AUTOINCREMENT, -- 자료순번
	USER_DB_RESOURCE_SEQ INT           NOT NULL, -- 리소스순번
	DATAS                VARCHAR(2000) NULL,      -- 리소스내용
	FOREIGN KEY (USER_DB_RESOURCE_SEQ) REFERENCES user_db_resource (RESOURCE_ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);