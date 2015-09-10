-- 사용자
CREATE TABLE `tadpole_user` (
	`seq`                    INT         NOT NULL COMMENT '사용자순번', -- 사용자순번
	`input_type`             VARCHAR(20) NULL     DEFAULT 'NORMAL' COMMENT '유형코드', -- 유형코드
	`email`                  VARCHAR(60) NOT NULL COMMENT '이메일', -- 이메일
	`email_key`              VARCHAR(25) NULL     COMMENT '이메일 인증키', -- 이메일 인증키
	`passwd`                 VARCHAR(60) NOT NULL COMMENT '비밀번호', -- 비밀번호
	`role_type`              VARCHAR(20) NULL     DEFAULT 'MANAGER' COMMENT '사용자역할', -- ROLE_TYPE
	`name`                   VARCHAR(60) NOT NULL COMMENT '사용자명', -- 사용자명
	`language`               VARCHAR(50) NULL     COMMENT 'KO.KR, US.ENG ?', -- 사용자언어
	`create_time`            TIMESTAMP   NOT NULL DEFAULT NOW() COMMENT '생성일', -- 생성일
	`delyn`                  CHAR(3)     NOT NULL DEFAULT 'NO' COMMENT '삭제여부', -- 삭제여부
	`approval_yn`            CHAR(3)     NOT NULL DEFAULT 'NO' COMMENT '사용승인여부', -- 사용승인여부
	`use_otp`                CHAR(3)     NOT NULL DEFAULT 'NO' COMMENT 'USER_OPT', -- USER_OPT
	`is_email_certification` CHAR(3)     NULL     DEFAULT 'NO' COMMENT '이메일 인증여부', -- 이메일 인증여부
	`otp_secret`             VARCHAR(50) NOT NULL DEFAULT '' COMMENT 'OPT_SECRET', -- OPT_SECRET
	`allow_ip`             VARCHAR(20) NOT NULL DEFAULT '*' COMMENT 'allow ip' -- OPT_SECRET
)
COMMENT '사용자 정보를 관리한다.';

-- 사용자
ALTER TABLE `tadpole_user`
	ADD CONSTRAINT `pk_tadpole_user` -- 사용자 Primary key
		PRIMARY KEY (
			`seq` -- 사용자순번
		);

ALTER TABLE `tadpole_user`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '사용자순번';

-- 사용자리소스
CREATE TABLE `user_db_resource` (
	`resource_seq`   INT           NOT NULL COMMENT '리소스아이디', -- 리소스아이디
	`resource_types` VARCHAR(10)   NOT NULL COMMENT 'ERD,SQL 구분값', -- 유형
	`user_seq`       INT           NOT NULL COMMENT '사용자순번', -- 사용자순번
	`db_seq`         INT           NOT NULL COMMENT 'DB아이디', -- DB아이디
	`name`           VARCHAR(50)   NULL     COMMENT '리소스 명칭', -- 이름
	`shared_type`    VARCHAR(7)    NOT NULL DEFAULT 'PRIVATE' COMMENT '개인 또는 그룹간 공유 구분', -- 공유종류
	`restapi_yesno`  CHAR(3)       NULL     DEFAULT 'NO' COMMENT '레스트 api지원유무', -- 레스트api 지원유무
	`restapi_uri`    VARCHAR(200)  NULL     COMMENT 'api uri', -- 레스트 api uri
	`restapi_key`    VARCHAR(50)   NULL     COMMENT 'api key', -- 레스트 api key
	`description`    VARCHAR(2000) NULL     COMMENT '리소스 설명', -- 설명
	`create_time`    TIMESTAMP     NOT NULL DEFAULT NOW() COMMENT '생성일시', -- 생성일시
	`delyn`          CHAR(3)       NOT NULL DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여부
)
COMMENT '사용자가 실행하는 쿼리나 일반 텍스트 문서 또는 오브젝트 소스를 등록하여 관리한다.';

-- 사용자리소스
ALTER TABLE `user_db_resource`
	ADD CONSTRAINT `pk_user_db_resource` -- 사용자리소스 기본키
		PRIMARY KEY (
			`resource_seq` -- 리소스아이디
		);

ALTER TABLE `user_db_resource`
	MODIFY COLUMN `resource_seq` INT NOT NULL AUTO_INCREMENT COMMENT '리소스아이디';

-- 데이터베이스
CREATE TABLE `tadpole_db` (
	`seq`                 INT           NOT NULL COMMENT 'DB아이디', -- DB아이디
	`user_seq`            INT           NOT NULL COMMENT '사용자순번', -- 사용자순번
	`operation_type`      VARCHAR(10)   NULL     COMMENT '운영타입', -- 운영타입
	`dbms_type`           VARCHAR(50)   NULL     COMMENT 'oracle, muysql, pgsql', -- 종류
	`url`                 VARCHAR(2000) NULL     COMMENT 'jdbc url', -- 접속주소
	`url_user_parameter`  VARCHAR(2000) NULL     COMMENT '사용자가 추가한 parameter', -- URL USER JDBC PARAMETER
	`db`                  VARCHAR(100)  NULL     COMMENT 'oracle : sid or servicename
	ms_sql : instance & database', -- 데이터베이스
	`group_name`          VARCHAR(50)   NULL     COMMENT '컨넥션 메니져에 그풉핑하기 위한 명칭', -- 그룹명칭
	`display_name`        VARCHAR(50)   NULL     COMMENT '별칭', -- 표시이름
	`host`                VARCHAR(100)  NULL     COMMENT '서버 아이피', -- 서버
	`port`                VARCHAR(100)  NULL     COMMENT '포트', -- 포트
	`locale`              VARCHAR(50)   NULL     COMMENT '언어설정', -- 지역
	`passwd`              VARCHAR(500)  NULL     COMMENT '디비 암호', -- 비밀번호
	`users`               VARCHAR(100)  NULL     COMMENT '디비 사용자', -- 사용자
	`is_profile`          CHAR(3)       NULL     DEFAULT 'NO' COMMENT '사용자 쿼리 저장여부', -- 프로파일여부
	`profile_select_mill` INT           NULL     DEFAULT 0 COMMENT '프로파일제한시간', -- 프로파일 제한시간
	`question_dml`        CHAR(3)       NULL     DEFAULT 'NO' COMMENT '입력,수정,삭제시 물을지 여부', -- DML승인여부
	`is_readonlyconnect`  CHAR(3)       NULL     DEFAULT 'NO' COMMENT 'SELECT만 가능한지 여부. dbtype_role에 정의된 권한이 우선이다.', -- 읽기전용접속
	`is_autocommit`       CHAR(3)       NULL     DEFAULT 'NO' COMMENT '자동커밋', -- 자동커밋
	`is_showtables`       CHAR(3)       NULL     DEFAULT 'NO' COMMENT '테이블 갯수가 대량(1000개정도?)일 경우 표시여부', -- 테이블보이기
	`is_external_browser` CHAR(3)       NULL     DEFAULT 'NO' COMMENT '외부 브라우저 사용여부', -- 외부 브라우저 사용여부
	`is_visible`          CHAR(3)       NULL     DEFAULT 'YES' COMMENT '화면에 보이는 유무', -- 화면에 보이는 유무
	`is_summary_report`   CHAR(3)       NULL     DEFAULT 'YES' COMMENT '디비 요약정보를 보내', -- 디비 요약정보를 보내
	`is_monitoring`       CHAR(3)       NULL     DEFAULT 'NO' COMMENT '모니터링', -- 모니 터링 유무
	`is_lock`             CHAR(3)       NULL     DEFAULT 'NO' COMMENT '디비 잠금', -- 디비 잠금
	`excute_limit`        INT           NULL     COMMENT '쿼리 감사 제한시간', -- 쿼리제한시간
	`create_time`         TIMESTAMP     NULL     DEFAULT NOW() COMMENT '생성일시', -- 생성일시
	`delyn`               CHAR(3)       NULL     DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여부
)
COMMENT '데이터 베이스 연결정보를 등록하고 관리한다.';

-- 데이터베이스
ALTER TABLE `tadpole_db`
	ADD CONSTRAINT `pk_tadpole_db` -- 데이터베이스 기본키
		PRIMARY KEY (
			`seq` -- DB아이디
		);

ALTER TABLE `tadpole_db`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT 'DB아이디';

-- 사용자정보
CREATE TABLE `user_info_data` (
	`seq`      INT           NOT NULL COMMENT '사용자정보순번', -- 사용자정보순번
	`user_seq` INT           NOT NULL COMMENT '사용자순번', -- 사용자순번
	`db_seq`   INT           NOT NULL COMMENT 'DB순번', -- DB순번
	`name`     VARCHAR(60)   NULL     COMMENT '명칭', -- 명칭
	`value0`   VARCHAR(2000) NULL     COMMENT '값0', -- 값0
	`value1`   VARCHAR(2000) NULL     COMMENT '값1', -- 값1
	`value2`   VARCHAR(2000) NULL     COMMENT '값2', -- 값2
	`value3`   VARCHAR(2000) NULL     COMMENT '값3', -- 값3
	`value4`   VARCHAR(2000) NULL     COMMENT '값4', -- 값4
	`value5`   VARCHAR(2000) NULL     COMMENT '값5' -- 값5
)
COMMENT '사용자 데이터 베이스에 대한 옵션
- 최대 쿼리 갯수등...';

-- 사용자정보
ALTER TABLE `user_info_data`
	ADD CONSTRAINT `pk_user_info_data` -- 사용자정보 기본키
		PRIMARY KEY (
			`seq` -- 사용자정보순번
		);

ALTER TABLE `user_info_data`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '사용자정보순번';

-- 실행SQL, API HISTORY
CREATE TABLE `executed_sql_resource` (
	`seq`              INT           NOT NULL COMMENT '리소스순번', -- 리소스순번
	`user_seq`         INT           NULL     COMMENT '사용자순번', -- 사용자순번
	`db_seq`           INT           NOT NULL COMMENT 'DB순번', -- DB순번
	`types`            VARCHAR(10)   NULL     DEFAULT '00' COMMENT 'insert, update, delete 종류', -- 유형
	`startdateexecute` DATETIME      NOT NULL COMMENT '실행시작일시', -- 실행시작일시
	`enddateexecute`   DATETIME      NOT NULL COMMENT '실행종료일시', -- 실행종료일시
	`duration`         INT           NOT NULL DEFAULT 0 COMMENT '실행시간', -- 실행시간
	`row`              INT           NULL     DEFAULT 0 COMMENT '조회결과갯수', -- 조회결과갯수
	`result`           CHAR(3)       NULL     COMMENT '결과코드', -- 결과코드
	`message`          VARCHAR(2000) NULL     COMMENT '결과메시지', -- 결과메시지
	`create_time`      TIMESTAMP     NOT NULL DEFAULT NOW() COMMENT '생성일시', -- 생성일시
	`delyn`            CHAR(3)       NOT NULL DEFAULT 'NO' COMMENT '삭제여부', -- 삭제여부
	`ipaddress`        VARCHAR(20)   NULL     COMMENT '실행 ip' -- 실행 ip
)
COMMENT '실행SQL, API HISTORY';

-- 실행SQL, API HISTORY
ALTER TABLE `executed_sql_resource`
	ADD CONSTRAINT `pk_executed_sql_resource` -- 실행SQL, API HISTORY 기본키
		PRIMARY KEY (
			`seq` -- 리소스순번
		);

ALTER TABLE `executed_sql_resource`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '리소스순번';

-- 리소스내용
CREATE TABLE `user_db_resource_data` (
	`seq`                  INT           NOT NULL COMMENT '자료순번', -- 자료순번
	`group_seq`            BIGINT        NULL     COMMENT 'group key', -- group key
	`user_db_resource_seq` INT           NOT NULL COMMENT '리소스순번', -- 리소스순번
	`datas`                VARCHAR(2000) NULL     COMMENT '리소스내용', -- 리소스내용
	`delyn`                CHAR(3)       NULL     DEFAULT 'NO' COMMENT '삭제여부', -- 삭제여부
	`create_time`          TIMESTAMP     NULL     DEFAULT NOW() COMMENT '생성일시' -- 생성일시
)
COMMENT '실제 스크립트 또는 오브젝트 소스 내용을 관리한다.';

-- 리소스내용
ALTER TABLE `user_db_resource_data`
	ADD CONSTRAINT `pk_user_db_resource_data` -- 리소스내용 기본키
		PRIMARY KEY (
			`seq` -- 자료순번
		);

ALTER TABLE `user_db_resource_data`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '자료순번';

-- 실행SQL내용
CREATE TABLE `executed_sql_resource_data` (
	`seq`                       INT           NOT NULL COMMENT '내용순번', -- 내용순번
	`executed_sql_resource_seq` INT           NOT NULL COMMENT '리소스순번', -- 리소스순번
	`datas`                     VARCHAR(2000) NULL     COMMENT 'SQL내용' -- SQL내용
)
COMMENT '실행SQL내용';

-- 실행SQL내용
ALTER TABLE `executed_sql_resource_data`
	ADD CONSTRAINT `pk_executed_sql_resource_data` -- 실행SQL내용 기본키
		PRIMARY KEY (
			`seq` -- 내용순번
		);

ALTER TABLE `executed_sql_resource_data`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '내용순번';

-- 시스템정보
CREATE TABLE `tadpole_system` (
	`seq`           INT           NOT NULL COMMENT '일련번호', -- 일련번호
	`name`          VARCHAR(100)  NULL     COMMENT '명칭', -- 명칭
	`major_version` VARCHAR(50)   NULL     COMMENT '버젼번호', -- 버젼번호
	`sub_version`   VARCHAR(50)   NULL     COMMENT '서브버젼', -- 서브버젼
	`information`   VARCHAR(2000) NULL     COMMENT '시스템정보', -- 시스템정보
	`create_time`   TIMESTAMP     NOT NULL DEFAULT NOW() COMMENT '생성일시' -- 생성일시
)
COMMENT '올챙이 시스템의 버젼정보등을 관리한다.';

-- 시스템정보
ALTER TABLE `tadpole_system`
	ADD CONSTRAINT `pk_tadpole_system` -- 시스템정보 기본키
		PRIMARY KEY (
			`seq` -- 일련번호
		);

ALTER TABLE `tadpole_system`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '일련번호';

-- 데이터베이스확장속성
CREATE TABLE `user_db_ext` (
	`seq`   INT          NOT NULL COMMENT 'DB아이디', -- DB아이디
	`ext1`  VARCHAR(100) NULL     COMMENT '오라클...', -- 오라클
	`ext2`  VARCHAR(100) NULL     COMMENT '몽고...', -- 몽고
	`ext3`  VARCHAR(100) NULL     COMMENT '확장속성3', -- 확장속성3
	`ext4`  VARCHAR(100) NULL     COMMENT '확장속성4', -- 확장속성4
	`ext5`  VARCHAR(100) NULL     COMMENT '확장속성5', -- 확장속성5
	`ext6`  VARCHAR(100) NULL     COMMENT '확장속성6', -- 확장속성6
	`ext7`  VARCHAR(100) NULL     COMMENT '확장속성7', -- 확장속성7
	`ext8`  VARCHAR(100) NULL     COMMENT '확장속성8', -- 확장속성8
	`ext9`  VARCHAR(100) NULL     COMMENT '확장속성9', -- 확장속성9
	`ext10` VARCHAR(100) NULL     COMMENT '확장속성10' -- 확장속성10
)
COMMENT 'dbms별로 다른 접속시 필요한 추가 정보들을 정의하고 관리한다.';

-- 데이터베이스확장속성
ALTER TABLE `user_db_ext`
	ADD CONSTRAINT `pk_user_db_ext` -- 데이터베이스확장속성 기본키
		PRIMARY KEY (
			`seq` -- DB아이디
		);

-- 사용자역활
CREATE TABLE `user_role` (
	`seq`         INT         NOT NULL COMMENT '역활순번', -- 역활순번
	`user_seq`    INT         NOT NULL COMMENT '사용자순번', -- 사용자순번
	`role_type`   VARCHAR(20) NOT NULL DEFAULT '00' COMMENT '관리자,DBA,개발자', -- 사용자유형
	`approval_yn` CHAR(3)     NOT NULL DEFAULT 'NO' COMMENT '승인여부', -- 승인여부
	`name`        VARCHAR(50) NULL     COMMENT '역활명', -- 역활명
	`delyn`       CHAR(3)     NOT NULL DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여부
)
COMMENT '그룹별로 사용자 역할을 정의한다.';

-- 사용자역활
ALTER TABLE `user_role`
	ADD CONSTRAINT `pk_user_role` -- 사용자역활 기본키
		PRIMARY KEY (
			`seq` -- 역활순번
		);

ALTER TABLE `user_role`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '역활순번';

-- 객체변경이력
CREATE TABLE `schema_history` (
	`seq`         INT         NOT NULL COMMENT '일련번호', -- 일련번호
	`db_seq`      INT         NOT NULL COMMENT 'DB아이디', -- DB아이디
	`user_seq`    INT         NOT NULL COMMENT '사용자순번', -- 사용자순번
	`object_id`   VARCHAR(50) NOT NULL COMMENT '테이블 이름(? 모르면 공백)', -- 객체식별자
	`work_type`   VARCHAR(15) NOT NULL COMMENT '작업일 일어난 곳(ex, editor, object explore)', -- 작업유형
	`object_type` VARCHAR(30) NOT NULL COMMENT 'insert, update, delte, ddl 로 구분', -- 객첵유형
	`create_date` DATETIME    NOT NULL COMMENT '객체를 생성한 날짜를 정의한다.', -- 생성일시
	`update_date` DATETIME    NOT NULL DEFAULT '9999-12-31 12:59:59' COMMENT '객체를 변경한 날짜를 정의한다.', -- 폐기일시
	`del_yn`      CHAR(3)     NOT NULL DEFAULT 'NO' COMMENT '삭제여부', -- 삭제여부
	`ipaddress`   VARCHAR(20) NULL     COMMENT '실행 IP' -- 실행 ip
)
COMMENT '테이블, 프로시져 등의 DDL스크립트가 변경되면 변경된 DDL문장을 보관한다.';

-- 객체변경이력
ALTER TABLE `schema_history`
	ADD CONSTRAINT `pk_schema_history` -- 객체변경이력 Primary key
		PRIMARY KEY (
			`seq` -- 일련번호
		);

ALTER TABLE `schema_history`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '일련번호';

ALTER TABLE `schema_history`
	AUTO_INCREMENT = 1;

-- 객체변경이력 상세
CREATE TABLE `schema_history_detail` (
	`seq`        INT           NOT NULL COMMENT '라인번호', -- 상세순번
	`schema_seq` INT           NOT NULL COMMENT '변경이력 순번', -- 일련번호
	`source`     VARCHAR(2000) NULL     COMMENT '라인단위의 DDL문장' -- 스크립트소스
)
COMMENT '실제 변경된 DDL문장을 라인 단위로 저장한다.';

-- 객체변경이력 상세
ALTER TABLE `schema_history_detail`
	ADD CONSTRAINT `pk_schema_history_detail` -- 객체변경이력 상세 Primary key
		PRIMARY KEY (
			`seq` -- 상세순번
		);

ALTER TABLE `schema_history_detail`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '라인번호';

-- 데이터베이스 추가 정보(브라우저로 확장할수있는)
CREATE TABLE `external_browser_info` (
	`seq`     INT          NOT NULL COMMENT '자동증가', -- 순번
	`db_seq`  INT          NOT NULL COMMENT 'DB아이디', -- DB아이디
	`is_used` CHAR(3)      NOT NULL DEFAULT 'NO' COMMENT '사용유무', -- 사용유무
	`name`    VARCHAR(100) NOT NULL COMMENT '타이틀', -- 이름
	`url`     VARCHAR(500) NOT NULL COMMENT 'url', -- url
	`comment` VARCHAR(500) NULL     COMMENT '해당 url에 대한 정보' -- 코멘트
)
COMMENT '데이터베이스 추가 정보(브라우저로 확장할수있는)';

-- 데이터베이스 추가 정보(브라우저로 확장할수있는)
ALTER TABLE `external_browser_info`
	ADD CONSTRAINT `pk_external_browser_info` -- 데이터베이스 추가 정보(브라우저로 확장할수있는) 기본키
		PRIMARY KEY (
			`seq`,    -- 순번
			`db_seq`  -- DB아이디
		);

ALTER TABLE `external_browser_info`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '자동증가';

-- DB 역활
CREATE TABLE `tadpole_role` (
	`role_id`   VARCHAR(20) NOT NULL COMMENT '역활식별자', -- 역활식별자
	`role_name` VARCHAR(40) NULL     COMMENT '역활명칭' -- 역활명칭
)
COMMENT 'SuperAdmin, Admin, Manager, DBA, Developer, Tester, User';

-- DB 역활
ALTER TABLE `tadpole_role`
	ADD CONSTRAINT `pk_tadpole_role` -- DB 역활 Primary key
		PRIMARY KEY (
			`role_id` -- 역활식별자
		);

-- 사용자 DB 역활정의
CREATE TABLE `tadpole_user_db_role` (
	`seq`                    INT         NOT NULL COMMENT '사용자DB역활순번', -- 사용자DB역활순번
	`user_seq`               INT         NOT NULL COMMENT '사용자순번', -- 사용자순번
	`db_seq`                 INT         NOT NULL COMMENT 'DB아이디', -- DB아이디
	`role_id`                VARCHAR(20) NULL     COMMENT '역활식별자', -- 역활식별자
	`access_ip`              VARCHAR(20) NULL     DEFAULT '*' COMMENT '사용자 접근가능IP', -- 사용자 접근가능IP
	`terms_of_use_starttime` DATETIME    NULL     COMMENT '사용기간-시작', -- 사용기간-시작
	`terms_of_use_endtime`   DATETIME    NULL     COMMENT '사용기간_종료', -- 사용기간_종료
	`delyn`                  CHAR(3)     NULL     DEFAULT 'NO' COMMENT '삭제여부', -- 삭제여부
	`create_time`            TIMESTAMP   NULL     DEFAULT NOW() COMMENT '생성일' -- 생성일
)
COMMENT '사용자별로 접근가능한 데이터 베이스와 데이터베이스를 사용하는 역활을 정의한다.';

-- 사용자 DB 역활정의
ALTER TABLE `tadpole_user_db_role`
	ADD CONSTRAINT `pk_tadpole_user_db_role` -- 사용자 DB 역활정의 Primary key
		PRIMARY KEY (
			`seq` -- 사용자DB역활순번
		);

ALTER TABLE `tadpole_user_db_role`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '사용자DB역활순번';

-- 기능목록
CREATE TABLE `tadpole_action` (
	`seq`          INT          NOT NULL COMMENT '기능순번', -- 기능순번
	`action_group` VARCHAR(50)  NOT NULL COMMENT 'Class에 별도로 정의한 상수값 ID', -- 기능그룹
	`action_item`  VARCHAR(50)  NOT NULL COMMENT '버튼 또는 프로시져의 id, visible, enable처리할 대상', -- 세부기능
	`action_name`  VARCHAR(100) NULL     COMMENT '기능명칭', -- 기능명칭
	`action_desc`  VARCHAR(500) NULL     COMMENT '기능설명', -- 기능설명
	`delyn`        VARCHAR(3)   NOT NULL DEFAULT 'NO' COMMENT '삭데된 기능은 모든 롤의 사용자가 사용가능하다', -- 삭제여부
	`create_time`  TIMESTAMP    NULL     DEFAULT NOW() COMMENT '생성일시' -- 생성일시
)
COMMENT '올챙이에 구현된 화면단위 기능 목록
- 자식테이블로 정의하고 각 화면의 Button이나 Procedure 단위로 등록하여 사용가능 여부를 정의할 것인가?';

-- 기능목록
ALTER TABLE `tadpole_action`
	ADD CONSTRAINT `pk_tadpole_action` -- 기능목록 Primary key
		PRIMARY KEY (
			`seq` -- 기능순번
		);

ALTER TABLE `tadpole_action`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '기능순번';

-- 역활별 기능제한 목록
CREATE TABLE `tadpole_role_action` (
	`seq`        INT         NOT NULL COMMENT '역활기능순번', -- 역활기능순번
	`action_seq` INT         NULL     COMMENT '기능순번', -- 기능순번
	`type_code`  VARCHAR(10) NULL     COMMENT '운영타입', -- 운영타입
	`role_id`    VARCHAR(20) NULL     COMMENT '어떤 롤을 갖는 사용자가 사용할수 있는지 정의한다.', -- 역활식별자
	`grantyn`    VARCHAR(3)  NOT NULL DEFAULT 'NO' COMMENT '기본값은 No , Yes로 일시적인 허용 가능하도록 한다.' -- 허용여부
)
COMMENT '사용자 그룹별로 정의된 기능(화면)단위로 사용할 수 없게할 기능을 정의한다.';

-- 역활별 기능제한 목록
ALTER TABLE `tadpole_role_action`
	ADD CONSTRAINT `pk_tadpole_role_action` -- 역활별 기능제한 목록 Primary key
		PRIMARY KEY (
			`seq` -- 역활기능순번
		);

ALTER TABLE `tadpole_role_action`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '역활기능순번';

-- 운영타입별역활
CREATE TABLE `dbtype_role` (
	`seql`               INT         NOT NULL COMMENT '운영타입 역활순번', -- 운영타입 역활순번
	`role_id`            VARCHAR(20) NULL     COMMENT '역활식별자', -- 역활식별자
	`type_code`          VARCHAR(10) NULL     COMMENT '운영타입', -- 운영타입
	`is_userdisplay`     CHAR(3)     NULL     DEFAULT 'NO'
	 COMMENT '접속 계정에 대한 정보를 DB목록에 표시할지 여부', -- 연결정보 표시여부
	`is_autocommit`      CHAR(3)     NULL     DEFAULT 'NO' COMMENT '자동커밋', -- 자동커밋
	`is_summary_report`  CHAR(3)     NULL     DEFAULT 'YES' COMMENT '슈퍼어드민에게 요양정보 발송 여부', -- 요약정보 전송여부
	`execute_limit`      INT         NOT NULL DEFAULT 3 COMMENT '3초이상 쿼리, 2초이상 쿼리등 정의', -- 쿼리제한시간
	`is_readonlyconnect` CHAR(3)     NULL     DEFAULT 'NO' COMMENT 'SELECT만 가능한지 여부. 디비에 정의된 권한보다 우선한다.' -- 읽기전용접속
)
COMMENT '디비에이가 운영서버에 접속할 경우는 스키마/유저정보를 표시한다. / 안한다 같이 역활별로 디비 타입에 따라 정의되는 기능을 정의한다.';

-- 운영타입별역활
ALTER TABLE `dbtype_role`
	ADD CONSTRAINT `pk_dbtype_role` -- 운영타입별역활 Primary key
		PRIMARY KEY (
			`seql` -- 운영타입 역활순번
		);

ALTER TABLE `dbtype_role`
	MODIFY COLUMN `seql` INT NOT NULL AUTO_INCREMENT COMMENT '운영타입 역활순번';

-- 운영타입
CREATE TABLE `dbtype` (
	`type_code`  VARCHAR(10) NOT NULL COMMENT '개발,테스트,운영 구분값', -- 운영타입
	`type_name`  VARCHAR(50) NULL     COMMENT '운영타입명칭', -- 운영타입명칭
	`small_name` VARCHAR(20) NULL     COMMENT '운영타입약어' -- 운영타입약어
)
COMMENT 'Product, Develop, Test, Backup';

-- 운영타입
ALTER TABLE `dbtype`
	ADD CONSTRAINT `pk_dbtype` -- 운영타입 Primary key
		PRIMARY KEY (
			`type_code` -- 운영타입
		);

-- 로그인 이력
CREATE TABLE `login_history` (
	`seq`             INT         NOT NULL COMMENT '로그순번', -- 로그순번
	`user_seq`        INT         NULL     COMMENT '사용자순번', -- 사용자순번
	`login_ip`        VARCHAR(20) NULL     COMMENT 'IP', -- IP
	`connet_time`     TIMESTAMP   NOT NULL COMMENT '접속일시', -- 접속일시
	`disconnect_time` TIMESTAMP   NULL     COMMENT '접속종료' -- 접속종료
)
COMMENT '로그인 이력';

-- 로그인 이력
ALTER TABLE `login_history`
	ADD CONSTRAINT `pk_login_history` -- 로그인 이력 Primary key
		PRIMARY KEY (
			`seq` -- 로그순번
		);

ALTER TABLE `login_history`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '로그순번';

-- 스케줄 내용 
CREATE TABLE `schedule_detail` (
	`seq`          INT           NOT NULL COMMENT '자료순번', -- 자료순번
	`schedule_seq` INT           NOT NULL COMMENT '순번', -- 순번
	`datas`        VARCHAR(2000) NOT NULL COMMENT '리소스내용' -- 리소스내용
)
COMMENT '실제 스크립트 또는 오브젝트 소스 내용을 관리한다.';

-- 스케줄 내용 
ALTER TABLE `schedule_detail`
	ADD CONSTRAINT `pk_schedule_detail` -- 스케줄 내용  기본키
		PRIMARY KEY (
			`seq` -- 자료순번
		);

ALTER TABLE `schedule_detail`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '자료순번';

-- 모니터링 메인
CREATE TABLE `monitoring_main` (
	`seq`                INT           NOT NULL COMMENT '순번', -- 순번
	`user_seq`           INT           NULL     COMMENT '사용자순번', -- 사용자순번
	`db_seq`             INT           NULL     COMMENT 'DB SEQ', -- DB SEQ
	`read_method`        VARCHAR(20)   NULL     COMMENT '유형코드(SQL, PL/SQL, REST API)', -- 유형코드(SQL, PL/SQL, REST API)
	`title`              VARCHAR(50)   NULL     COMMENT '리소스 명칭', -- 이름
	`description`        VARCHAR(2000) NULL     COMMENT '리소스 설명', -- 설명
	`advice`             VARCHAR(2000) NULL     COMMENT '조언', -- 조언
	`cron_exp`           VARCHAR(30)   NULL     COMMENT 'CRON', -- CRON
	`query`              TEXT          NULL     COMMENT '쿼리', -- 쿼리
	`param_1_column`     VARCHAR(100)  NULL     COMMENT '파라미터 1의 컬럼 명', -- 파라미터 1의 컬럼 명
	`param_1_init_value` VARCHAR(100)  NULL     COMMENT '파라미터 1의 값', -- 파라미터 1의 값
	`param_2_column`     VARCHAR(100)  NULL     COMMENT '파라미터 2 의 컬럼 명', -- 파라미터 2 의 컬럼 명
	`param_2_init_value` VARCHAR(100)  NULL     COMMENT '파라미터 2 값', -- 파라미터 2 값
	`is_result_save`     CHAR(3)       NULL     DEFAULT 'NO' COMMENT '결과 저장', -- 결과 저장
	`is_snapshot_save`   CHAR(3)       NULL     DEFAULT 'NO' COMMENT '스냅샷 결과 저장', -- 스냅샷 결과 저장
	`receiver`           VARCHAR(2000) NULL     COMMENT '이메일 받을사람', -- 이메일 받을사람
	`create_time`        TIMESTAMP     NULL     DEFAULT NOW() COMMENT '생성일시', -- 생성일시
	`summary_term`       VARCHAR(10)   NULL     COMMENT '요약주기(하지않음, 5분, 10분, 60분)', -- 요약주기(하지않음, 5분, 10분, 60분)
	`mod_time`           TIMESTAMP     NULL     COMMENT '수정일시', -- 수정일시
	`delyn`              CHAR(3)       NULL     DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여부
)
COMMENT '사용자가 실행하는 쿼리나 일반 텍스트 문서 또는 오브젝트 소스를 등록하여 관리한다.';

-- 모니터링 메인
ALTER TABLE `monitoring_main`
	ADD CONSTRAINT `pk_monitoring_main` -- 모니터링 메인 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `monitoring_main`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 스케줄
CREATE TABLE `schedule` (
	`seq`               INT           NOT NULL COMMENT '순번', -- 순번
	`schedule_main_seq` INT           NULL     COMMENT '순번2', -- 순번2
	`send_seq`          INT           NULL     COMMENT '실행순서', -- 실행순서
	`name`              VARCHAR(50)   NULL     COMMENT '이름', -- 이름
	`description`       VARCHAR(2000) NULL     COMMENT '설명', -- 설명
	`delyn`             CHAR(3)       NULL     DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여부
)
COMMENT '스케줄';

-- 스케줄
ALTER TABLE `schedule`
	ADD CONSTRAINT `pk_schedule` -- 스케줄 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

-- 모니터링 결과 
CREATE TABLE `monitoring_result` (
	`seq`                  INT           NOT NULL COMMENT '순번', -- 순번
	`unique_id`            VARCHAR(36)   NULL     COMMENT '유일한 ID', -- 유일한 ID
	`relation_id`          INT           NULL     COMMENT '함께 모니터링 했던 ID', -- 함께 모니터링 했던 ID
	`monitoring_seq`       INT           NULL     COMMENT '모니터링SEQ', -- 모니터링SEQ
	`monitoring_index_seq` INT           NULL     COMMENT '모니터링 인덱스 SEQ', -- 모니터링 인덱스 SEQ
	`monitroing_type`      VARCHAR(30)   NULL     COMMENT '모니터링타입', -- 모니터링타입
	`kpi_type`             VARCHAR(30)   NULL     COMMENT '새 컬럼', -- 새 컬럼
	`user_seq`             INT           NULL     COMMENT '사용자순번', -- 사용자순번
	`db_seq`               INT           NULL     COMMENT 'DB SEQ', -- DB SEQ
	`result`               VARCHAR(20)   NULL     COMMENT '성공(Clean, Warring, Critical)', -- 성공(Clean, Warring, Critical)
	`index_value`          VARCHAR(100)  NULL     COMMENT '지표 값', -- 지표 값
	`system_description`   VARCHAR(2000) NULL     COMMENT '시스템 사유', -- 시스템 사유
	`is_user_confirm`      CHAR(3)       NULL     DEFAULT 'NO' COMMENT '사용자 확인', -- 사용자 확인
	`user_description`     VARCHAR(2000) NULL     COMMENT '사용자 사유', -- 사용자 사유
	`after_description`    VARCHAR(2000) NULL     COMMENT '실패시 처리 사유', -- 실패시 처리 사유
	`create_time`          TIMESTAMP     NULL     DEFAULT now() COMMENT '생성일시', -- 생성일시
	`mod_time`             TIMESTAMP     NULL     COMMENT '수정일시', -- 수정일시
	`query_result`         TEXT          NULL     COMMENT '결과', -- 결과
	`snapshot`             TEXT          NULL     COMMENT '에러 일때 백 데이터' -- 에러 일때 백 데이터
)
COMMENT '모니터링 결과 ';

-- 모니터링 결과 
ALTER TABLE `monitoring_result`
	ADD CONSTRAINT `pk_monitoring_result` -- 모니터링 결과  기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `monitoring_result`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 모니터링 통계 데이터
CREATE TABLE `monitoring_statistics` (
	`seq`            INT          NOT NULL COMMENT '순번', -- 순번
	`monitoring_seq` INT          NULL     COMMENT '모니터링 메인 순번', -- 모니터링 메인 순번
	`type`           CHAR(4)      NULL     COMMENT '5,60,240', -- 5,60,240
	`title`          VARCHAR(100) NULL     COMMENT '보이는 컬럼', -- 보이는 컬럼
	`avr`            DOUBLE       NULL     COMMENT '평균값', -- 평균값
	`suc_count`      INT          NULL     COMMENT '성공값', -- 성공값
	`fail_count`     INT          NULL     COMMENT '실패값', -- 실패값
	`create_time`    TIMESTAMP    NULL     DEFAULT NOW() COMMENT '생성일시' -- 생성일시
)
COMMENT '모니터링 통계 데이터';

-- 모니터링 통계 데이터
ALTER TABLE `monitoring_statistics`
	ADD CONSTRAINT `pk_monitoring_statistics` -- 모니터링 통계 데이터 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `monitoring_statistics`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 모니터링 데이터 읽기 유형
CREATE TABLE `monitoring_read_type` (
	`type`        VARCHAR(20)   NOT NULL COMMENT '유형코드(SQL, PL/SQL, REST API)', -- 유형코드(SQL, PL/SQL, REST API)
	`name`        VARCHAR(50)   NULL     COMMENT '이름', -- 이름
	`description` VARCHAR(2000) NULL     COMMENT '설명' -- 설명
)
COMMENT '모니터링 데이터 읽기 유형';

-- 모니터링 데이터 읽기 유형
ALTER TABLE `monitoring_read_type`
	ADD CONSTRAINT `pk_monitoring_read_type` -- 모니터링 데이터 읽기 유형 기본키
		PRIMARY KEY (
			`type` -- 유형코드(SQL, PL/SQL, REST API)
		);

-- 모니터링 인덱스
CREATE TABLE `monitoring_index` (
	`seq`                       INT         NOT NULL COMMENT '순번', -- 순번
	`monitoring_seq`            INT         NULL     COMMENT '모니터링순번', -- 모니터링순번
	`condition_type`            VARCHAR(30) NULL     COMMENT '비교유형(EQUALS, LEAST, GREATEST, NOT_CHECK, RISE_EXCEPTION)', -- 비교유형(EQUALS, LEAST, GREATEST, NOT_CHECK, RISE_EXCEPTION)
	`monitoring_type`           VARCHAR(50) NULL     COMMENT 'CPU, NETWORK_IO,  SESSION_LIST', -- CPU, NETWORK_IO,  SESSION_LIST
	`kpi_type`                  VARCHAR(30) NULL     COMMENT '성능지표 타입', -- 성능지표 타입
	`after_type`                VARCHAR(20) NULL     COMMENT 'EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH', -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
	`index_nm`                  VARCHAR(30) NULL     COMMENT '기준 컬럼', -- 기준 컬럼
	`condition_value`           VARCHAR(20) NULL     COMMENT '기준 비교 값', -- 기준 비교 값
	`exception_index_nm`        VARCHAR(30) NULL     COMMENT '제외 할 경우 컬럼', -- 제외 할 경우 컬럼
	`exception_condition_type`  VARCHAR(30) NULL     COMMENT '제외 할 비교유형', -- 제외 할 비교유형
	`exception_condition_value` VARCHAR(20) NULL     COMMENT '제외 할 비교 유형 값', -- 제외 할 비교 유형 값
	`create_time`               TIMESTAMP   NULL     DEFAULT NOW() COMMENT '생성일시', -- 생성일시
	`mod_time`                  TIMESTAMP   NULL     COMMENT '수정일시', -- 수정일시
	`delyn`                     CHAR(3)     NULL     DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여부
)
COMMENT '모니터링 인덱스';

-- 모니터링 인덱스
ALTER TABLE `monitoring_index`
	ADD CONSTRAINT `pk_monitoring_index` -- 모니터링 인덱스 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `monitoring_index`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 알림타입
CREATE TABLE `monitoring_after_type` (
	`type`        VARCHAR(20)   NOT NULL COMMENT 'EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH', -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
	`name`        VARCHAR(50)   NULL     COMMENT '이름', -- 이름
	`description` VARCHAR(2000) NULL     COMMENT '설명' -- 설명
)
COMMENT '알림타입';

-- 알림타입
ALTER TABLE `monitoring_after_type`
	ADD CONSTRAINT `pk_monitoring_after_type` -- 알림타입 기본키
		PRIMARY KEY (
			`type` -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
		);

-- 모니터링 타입
CREATE TABLE `monitoring_type` (
	`type`        VARCHAR(50)   NOT NULL COMMENT 'CPU, NETWORK_IO,  SESSION_LIST', -- CPU, NETWORK_IO,  SESSION_LIST
	`name`        VARCHAR(50)   NULL     COMMENT '이름', -- 이름
	`description` VARCHAR(2000) NULL     COMMENT '설명' -- 설명
)
COMMENT '모니터링 타입';

-- 모니터링 타입
ALTER TABLE `monitoring_type`
	ADD CONSTRAINT `pk_monitoring_type` -- 모니터링 타입 기본키
		PRIMARY KEY (
			`type` -- CPU, NETWORK_IO,  SESSION_LIST
		);

-- 모니터링 코드 템플릿
CREATE TABLE `tadpole_monitoring_template` (
	`seq`                       INT           NOT NULL COMMENT '순번', -- 순번
	`user_seq`                  INT           NULL     COMMENT '사용자순번', -- 사용자순번
	`db_type`                   VARCHAR(30)   NOT NULL COMMENT 'DB TYPE', -- 디비 타입
	`db_ver`                    VARCHAR(30)   NULL     COMMENT '디비 버전', -- 디비 버전
	`template_type`             VARCHAR(20)   NULL     DEFAULT 'BASIC' COMMENT '템플릿 유형 코드(BASIC, INTERMEDIATE, ADVANCE)', -- 템플릿 유형 코드(BASIC, INTERMEDIATE, ADVANCE)
	`title`                     VARCHAR(30)   NULL     COMMENT 'MONITORING,SUMMARY_REPORT', -- 이름
	`description`               VARCHAR(2000) NULL     COMMENT '설명', -- 설명
	`advice`                    VARCHAR(2000) NULL     COMMENT '조언', -- 조언
	`is_result_save`            CHAR(3)       NULL     DEFAULT 'NO' COMMENT '결과저장', -- 결과저장
	`is_snapshot_save`          CHAR(3)       NULL     DEFAULT 'NO' COMMENT '스냅샷 저장 유무', -- 스냅샷 저장 유무
	`query`                     TEXT          NULL     COMMENT '내용', -- 쿼리
	`index_nm`                  VARCHAR(30)   NULL     COMMENT '보여줄컬럼', -- 보여줄컬럼
	`condition_type`            VARCHAR(30)   NULL     COMMENT '비교유형(EQUALS, UNEQUAL, LEAST, GREATEST, NOT_CHECK, RISE_EXCEPTION)', -- 비교유형(EQUALS, UNEQUAL, LEAST, GREATEST, NOT_CHECK, RISE_EXCEPTION)
	`condition_value`           VARCHAR(20)   NULL     COMMENT '비교 유형 값', -- 비교 유형 값
	`exception_index_nm`        VARCHAR(30)   NULL     COMMENT '제외 할 경우 컬럼', -- 제외 할 경우 컬럼
	`exception_condition_type`  VARCHAR(30)   NULL     COMMENT '제외 할 비교유형', -- 제외 할 비교유형
	`exception_condition_value` VARCHAR(20)   NULL     COMMENT '제외 할 비교 유형 값', -- 제외 할 비교 유형 값
	`param_1_column`            VARCHAR(100)  NULL     COMMENT '파라미터 1의 컬럼 명', -- 파라미터 1의 컬럼 명
	`param_1_init_value`        VARCHAR(100)  NULL     COMMENT '파라미터 1의 값', -- 파라미터 1의 값
	`param_2_column`            VARCHAR(100)  NULL     COMMENT '파라미터 2 의 컬럼 명', -- 파라미터 2 의 컬럼 명
	`param_2_init_value`        VARCHAR(100)  NULL     COMMENT '파라미터 2 값', -- 파라미터 2 값
	`after_type`                VARCHAR(20)   NULL     COMMENT 'EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH', -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
	`monitoring_type`           VARCHAR(50)   NULL     COMMENT 'CPU, NETWORK_IO,  SESSION_LIST', -- CPU, NETWORK_IO,  SESSION_LIST
	`kpi_type`                  VARCHAR(30)   NULL     COMMENT '성능지표 타입(SECURITY, PERFORMANCE, OTHERS)', -- 성능지표 타입(SECURITY, PERFORMANCE, OTHERS)
	`summary_term`              VARCHAR(10)   NULL     COMMENT '요약주기(하지않음, 5분, 10분, 60분)', -- 요약주기(하지않음, 5분, 10분, 60분)
	`create_time`               TIMESTAMP     NULL     DEFAULT NOW() COMMENT '생성일', -- 생성일
	`delyn`                     CHAR(3)       NULL     DEFAULT 'NO' COMMENT '삭제여부' -- 삭제여뷰
)
COMMENT '모니터링 코드 템플릿';

-- 모니터링 코드 템플릿
ALTER TABLE `tadpole_monitoring_template`
	ADD CONSTRAINT `pk_tadpole_monitoring_template` -- 모니터링 코드 템플릿 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `tadpole_monitoring_template`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 일련번호 테이블
CREATE TABLE `tadpole_sequence` (
	`name` VARCHAR(10)     NULL COMMENT '순번이름', -- 이름
	`no`   BIGINT UNSIGNED NULL COMMENT '순번' -- 순번
)
COMMENT '일련번호 테이블';

-- 접근제어
CREATE TABLE `db_access_control` (
	`seq`         INT       NOT NULL COMMENT '순번', -- 순번
	`db_role_seq` INT       NULL     COMMENT '사용자DB역활순번', -- 사용자DB역활순번
	`select_lock` CHAR(3)   NULL     DEFAULT 'NO' COMMENT 'SELECT 잠금', -- SELECT 잠금
	`insert_lock` CHAR(3)   NULL     DEFAULT 'NO' COMMENT 'INSERT 잠금', -- INSERT 잠금
	`update_lock` CHAR(3)   NULL     DEFAULT 'NO' COMMENT 'UPDATE 잠금', -- UPDATE 잠금
	`delete_locl` CHAR(3)   NULL     DEFAULT 'NO' COMMENT 'DELETE 잠금', -- DELETE 잠금
	`ddl_lock`    CHAR(3)   NULL     DEFAULT 'NO' COMMENT 'DDL 잠금', -- DDL 잠금
	`create_time` TIMESTAMP NOT NULL DEFAULT NOW() COMMENT '생성일자', -- 생성일자
	`delyn`       CHAR(3)   NOT NULL DEFAULT 'NO' COMMENT '삭제유무' -- 삭제유무
)
COMMENT '접근제어';

-- 접근제어
ALTER TABLE `db_access_control`
	ADD CONSTRAINT `pk_db_access_control` -- 접근제어 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `db_access_control`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 접근제어_오브젝트이름
CREATE TABLE `access_ctl_object` (
	`seq`            INT           NOT NULL COMMENT '순번', -- 순번
	`access_seq`     INT           NULL     COMMENT '억세스 컨트롤순번', -- 억세스 컨트롤순번
	`type`           CHAR(20)      NOT NULL COMMENT 'SELECT,INSERT,UPDATE,DELETE,DDL', -- SELECT,INSERT,UPDATE,DELETE,DDL
	`obj_name`       VARCHAR(50)   NULL     COMMENT 'TABLE NAME(OBJECT NAME)', -- TABLE NAME(OBJECT NAME)
	`dontuse_object` CHAR(3)       NULL     DEFAULT 'NO' COMMENT 'DO NOT USER THIS OBJECT', -- DO NOT USER THIS OBJECT
	`detail_obj`     VARCHAR(2000) NULL     COMMENT '컬럼명', -- 컬럼명
	`description`    VARCHAR(2000) NULL     COMMENT 'DESCRIPTION', -- DESCRIPTION
	`delyn`          CHAR(3)       NULL     DEFAULT 'NO' COMMENT '삭제유무', -- 삭제유무
	`create_time`    TIMESTAMP     NULL     DEFAULT NOW() COMMENT '생성일자' -- 생성일자
)
COMMENT '접근제어_오브젝트이름';

-- 접근제어_오브젝트이름
ALTER TABLE `access_ctl_object`
	ADD CONSTRAINT `pk_access_ctl_object` -- 접근제어_오브젝트이름 기본키
		PRIMARY KEY (
			`seq` -- 순번
		);

ALTER TABLE `access_ctl_object`
	MODIFY COLUMN `seq` INT NOT NULL AUTO_INCREMENT COMMENT '순번';

-- 데이터베이스
ALTER TABLE `tadpole_db`
	ADD CONSTRAINT `fk_tadpole_user_to_tadpole_db` -- 사용자가 데이터 베이스를 등록한다.
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		),
	ADD INDEX `fk_tadpole_user_to_tadpole_db` (
		`user_seq` ASC -- 사용자순번
	);

-- 사용자리소스
ALTER TABLE `user_db_resource`
	ADD CONSTRAINT `fk_tadpole_db_to_user_db_resource` -- 데이터베이스 -> 사용자리소스
		FOREIGN KEY (
			`db_seq` -- DB아이디
		)
		REFERENCES `tadpole_db` ( -- 데이터베이스
			`seq` -- DB아이디
		);

-- 사용자리소스
ALTER TABLE `user_db_resource`
	ADD CONSTRAINT `fk_tadpole_user_to_user_db_resource` -- 사용자 -> 사용자리소스
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		);

-- 데이터베이스
ALTER TABLE `tadpole_db`
	ADD CONSTRAINT `fk_dbtype_to_tadpole_db` -- 운영타입 -> 데이터베이스
		FOREIGN KEY (
			`operation_type` -- 운영타입
		)
		REFERENCES `dbtype` ( -- 운영타입
			`type_code` -- 운영타입
		);

-- 사용자정보
ALTER TABLE `user_info_data`
	ADD CONSTRAINT `fk_tadpole_user_to_user_info_data` -- 사용자 -> 사용자정보
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		);

-- 실행SQL, API HISTORY
ALTER TABLE `executed_sql_resource`
	ADD CONSTRAINT `fk_tadpole_db_to_executed_sql_resource` -- 데이터베이스 -> 실행SQL, API HISTORY
		FOREIGN KEY (
			`db_seq` -- DB순번
		)
		REFERENCES `tadpole_db` ( -- 데이터베이스
			`seq` -- DB아이디
		);

-- 리소스내용
ALTER TABLE `user_db_resource_data`
	ADD CONSTRAINT `fk_user_db_resource_to_user_db_resource_data` -- 사용자리소스 -> 리소스내용
		FOREIGN KEY (
			`user_db_resource_seq` -- 리소스순번
		)
		REFERENCES `user_db_resource` ( -- 사용자리소스
			`resource_seq` -- 리소스아이디
		);

-- 실행SQL내용
ALTER TABLE `executed_sql_resource_data`
	ADD CONSTRAINT `fk_executed_sql_resource_to_executed_sql_resource_data` -- 실행SQL, API HISTORY -> 실행SQL내용
		FOREIGN KEY (
			`executed_sql_resource_seq` -- 리소스순번
		)
		REFERENCES `executed_sql_resource` ( -- 실행SQL, API HISTORY
			`seq` -- 리소스순번
		);

-- 데이터베이스확장속성
ALTER TABLE `user_db_ext`
	ADD CONSTRAINT `fk_tadpole_db_to_user_db_ext` -- 데이터베이스 -> 데이터베이스확장속성
		FOREIGN KEY (
			`seq` -- DB아이디
		)
		REFERENCES `tadpole_db` ( -- 데이터베이스
			`seq` -- DB아이디
		);

-- 사용자역활
ALTER TABLE `user_role`
	ADD CONSTRAINT `fk_tadpole_user_to_user_role` -- 사용자 -> 사용자역활
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		);

-- 객체변경이력
ALTER TABLE `schema_history`
	ADD CONSTRAINT `fk_tadpole_db_to_schema_history` -- 데이터베이스 -> 객체변경이력
		FOREIGN KEY (
			`db_seq` -- DB아이디
		)
		REFERENCES `tadpole_db` ( -- 데이터베이스
			`seq` -- DB아이디
		);

-- 객체변경이력
ALTER TABLE `schema_history`
	ADD CONSTRAINT `fk_tadpole_user_to_schema_history` -- 사용자 -> 객체변경이력
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		);

-- 객체변경이력 상세
ALTER TABLE `schema_history_detail`
	ADD CONSTRAINT `fk_schema_history_to_schema_history_detail` -- 객체변경이력 -> 객체변경이력 상세
		FOREIGN KEY (
			`schema_seq` -- 일련번호
		)
		REFERENCES `schema_history` ( -- 객체변경이력
			`seq` -- 일련번호
		);

-- 데이터베이스 추가 정보(브라우저로 확장할수있는)
ALTER TABLE `external_browser_info`
	ADD CONSTRAINT `fk_tadpole_db_to_external_browser_info` -- 데이터베이스 -> 데이터베이스 추가 정보(브라우저로 확장할수있는)
		FOREIGN KEY (
			`db_seq` -- DB아이디
		)
		REFERENCES `tadpole_db` ( -- 데이터베이스
			`seq` -- DB아이디
		);

-- 사용자 DB 역활정의
ALTER TABLE `tadpole_user_db_role`
	ADD CONSTRAINT `fk_tadpole_user_to_tadpole_user_db_role` -- 사용자 -> 사용자 DB 역활정의
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		);

-- 사용자 DB 역활정의
ALTER TABLE `tadpole_user_db_role`
	ADD CONSTRAINT `fk_tadpole_db_to_tadpole_user_db_role` -- 데이터베이스 -> 사용자 DB 역활정의
		FOREIGN KEY (
			`db_seq` -- DB아이디
		)
		REFERENCES `tadpole_db` ( -- 데이터베이스
			`seq` -- DB아이디
		);

-- 사용자 DB 역활정의
ALTER TABLE `tadpole_user_db_role`
	ADD CONSTRAINT `fk_tadpole_role_to_tadpole_user_db_role` -- DB 역활 -> 사용자 DB 역활정의
		FOREIGN KEY (
			`role_id` -- 역활식별자
		)
		REFERENCES `tadpole_role` ( -- DB 역활
			`role_id` -- 역활식별자
		);

-- 역활별 기능제한 목록
ALTER TABLE `tadpole_role_action`
	ADD CONSTRAINT `fk_tadpole_action_to_tadpole_role_action` -- 기능목록 -> 역활별 기능제한 목록
		FOREIGN KEY (
			`action_seq` -- 기능순번
		)
		REFERENCES `tadpole_action` ( -- 기능목록
			`seq` -- 기능순번
		);

-- 역활별 기능제한 목록
ALTER TABLE `tadpole_role_action`
	ADD CONSTRAINT `fk_tadpole_role_to_tadpole_role_action` -- 역활에 따라서 사용 가능한 권한을 정의한다.
		FOREIGN KEY (
			`role_id` -- 역활식별자
		)
		REFERENCES `tadpole_role` ( -- DB 역활
			`role_id` -- 역활식별자
		);

-- 역활별 기능제한 목록
ALTER TABLE `tadpole_role_action`
	ADD CONSTRAINT `fk_dbtype_to_tadpole_role_action` -- 운영타입 -> 역활별 기능제한 목록
		FOREIGN KEY (
			`type_code` -- 운영타입
		)
		REFERENCES `dbtype` ( -- 운영타입
			`type_code` -- 운영타입
		);

-- 운영타입별역활
ALTER TABLE `dbtype_role`
	ADD CONSTRAINT `fk_tadpole_role_to_dbtype_role` -- DB 역활 -> 운영타입별역활
		FOREIGN KEY (
			`role_id` -- 역활식별자
		)
		REFERENCES `tadpole_role` ( -- DB 역활
			`role_id` -- 역활식별자
		);

-- 운영타입별역활
ALTER TABLE `dbtype_role`
	ADD CONSTRAINT `fk_dbtype_to_dbtype_role` -- 운영타입 -> 운영타입별역활
		FOREIGN KEY (
			`type_code` -- 운영타입
		)
		REFERENCES `dbtype` ( -- 운영타입
			`type_code` -- 운영타입
		);

-- 로그인 이력
ALTER TABLE `login_history`
	ADD CONSTRAINT `fk_tadpole_user_to_login_history` -- 사용자 -> 로그인 이력
		FOREIGN KEY (
			`user_seq` -- 사용자순번
		)
		REFERENCES `tadpole_user` ( -- 사용자
			`seq` -- 사용자순번
		);

-- 스케줄 내용 
ALTER TABLE `schedule_detail`
	ADD CONSTRAINT `fk_schedule_to_schedule_detail` -- 스케줄 -> 스케줄 내용 
		FOREIGN KEY (
			`schedule_seq` -- 순번
		)
		REFERENCES `schedule` ( -- 스케줄
			`seq` -- 순번
		);

-- 모니터링 메인
ALTER TABLE `monitoring_main`
	ADD CONSTRAINT `fk_monitoring_read_type_to_monitoring_main` -- 모니터링 데이터 읽기 유형 -> 모니터링 메인
		FOREIGN KEY (
			`read_method` -- 유형코드(SQL, PL/SQL, REST API)
		)
		REFERENCES `monitoring_read_type` ( -- 모니터링 데이터 읽기 유형
			`type` -- 유형코드(SQL, PL/SQL, REST API)
		);

-- 스케줄
ALTER TABLE `schedule`
	ADD CONSTRAINT `fk_monitoring_main_to_schedule` -- 모니터링 메인 -> 스케줄
		FOREIGN KEY (
			`schedule_main_seq` -- 순번2
		)
		REFERENCES `monitoring_main` ( -- 모니터링 메인
			`seq` -- 순번
		);

-- 모니터링 결과 
ALTER TABLE `monitoring_result`
	ADD CONSTRAINT `fk_monitoring_index_to_monitoring_result` -- 모니터링 인덱스 -> 모니터링 결과 
		FOREIGN KEY (
			`monitoring_index_seq` -- 모니터링 인덱스 SEQ
		)
		REFERENCES `monitoring_index` ( -- 모니터링 인덱스
			`seq` -- 순번
		);

-- 모니터링 결과 
ALTER TABLE `monitoring_result`
	ADD CONSTRAINT `fk_monitoring_main_to_monitoring_result` -- 모니터링 메인 -> 모니터링 결과 
		FOREIGN KEY (
			`monitoring_seq` -- 모니터링SEQ
		)
		REFERENCES `monitoring_main` ( -- 모니터링 메인
			`seq` -- 순번
		);

-- 모니터링 통계 데이터
ALTER TABLE `monitoring_statistics`
	ADD CONSTRAINT `fk_monitoring_main_to_monitoring_statistics` -- 모니터링 메인 -> 모니터링 통계 데이터
		FOREIGN KEY (
			`monitoring_seq` -- 모니터링 메인 순번
		)
		REFERENCES `monitoring_main` ( -- 모니터링 메인
			`seq` -- 순번
		);

-- 모니터링 인덱스
ALTER TABLE `monitoring_index`
	ADD CONSTRAINT `fk_monitoring_main_to_monitoring_index` -- 모니터링 메인 -> 모니터링 인덱스
		FOREIGN KEY (
			`monitoring_seq` -- 모니터링순번
		)
		REFERENCES `monitoring_main` ( -- 모니터링 메인
			`seq` -- 순번
		);

-- 모니터링 인덱스
ALTER TABLE `monitoring_index`
	ADD CONSTRAINT `fk_monitoring_type_to_monitoring_index` -- 모니터링 타입 -> 모니터링 인덱스
		FOREIGN KEY (
			`monitoring_type` -- CPU, NETWORK_IO,  SESSION_LIST
		)
		REFERENCES `monitoring_type` ( -- 모니터링 타입
			`type` -- CPU, NETWORK_IO,  SESSION_LIST
		);

-- 모니터링 인덱스
ALTER TABLE `monitoring_index`
	ADD CONSTRAINT `fk_monitoring_after_type_to_monitoring_index` -- 알림타입 -> 모니터링 인덱스
		FOREIGN KEY (
			`after_type` -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
		)
		REFERENCES `monitoring_after_type` ( -- 알림타입
			`type` -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
		);

-- 모니터링 코드 템플릿
ALTER TABLE `tadpole_monitoring_template`
	ADD CONSTRAINT `fk_monitoring_after_type_to_tadpole_monitoring_template` -- 알림타입 -> 모니터링 코드 템플릿
		FOREIGN KEY (
			`after_type` -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
		)
		REFERENCES `monitoring_after_type` ( -- 알림타입
			`type` -- EMAIL, PUSH, KILL AFTER_EMAIL, KILL AFTER_PUSH
		);

-- 모니터링 코드 템플릿
ALTER TABLE `tadpole_monitoring_template`
	ADD CONSTRAINT `fk_monitoring_type_to_tadpole_monitoring_template` -- 모니터링 타입 -> 모니터링 코드 템플릿
		FOREIGN KEY (
			`monitoring_type` -- CPU, NETWORK_IO,  SESSION_LIST
		)
		REFERENCES `monitoring_type` ( -- 모니터링 타입
			`type` -- CPU, NETWORK_IO,  SESSION_LIST
		);

-- 접근제어
ALTER TABLE `db_access_control`
	ADD CONSTRAINT `fk_tadpole_user_db_role_to_db_access_control` -- 사용자 DB 역활정의 -> 접근제어
		FOREIGN KEY (
			`db_role_seq` -- 사용자DB역활순번
		)
		REFERENCES `tadpole_user_db_role` ( -- 사용자 DB 역활정의
			`seq` -- 사용자DB역활순번
		);

-- 접근제어_오브젝트이름
ALTER TABLE `access_ctl_object`
	ADD CONSTRAINT `fk_db_access_control_to_access_ctl_object` -- 접근제어 -> 접근제어_오브젝트이름
		FOREIGN KEY (
			`access_seq` -- 억세스 컨트롤순번
		)
		REFERENCES `db_access_control` ( -- 접근제어
			`seq` -- 순번
		);