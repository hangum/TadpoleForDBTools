-- 사용자
CREATE TABLE `MY_SCHEMA`.`user` (
`SEQ`         INT         NOT NULL, -- 사용자순번
`EMAIL`       VARCHAR(60) NOT NULL, -- 이메일
`PASSWD`      VARCHAR(60) NOT NULL, -- 비밀번호
`NAME`        VARCHAR(60) NOT NULL, -- 사용자명
`LANGUAGE`    VARCHAR(10) NULL,     -- 사용자언어
`APPROVAL_YN` CHAR(3)     NOT NULL DEFAULT 'NO', -- 승인여부
`CREATE_TIME` TIMESTAMP   NOT NULL DEFAULT NOW(), -- 생성일
`DELYN`       CHAR(3)     NOT NULL DEFAULT 'NO' -- 삭제여부
);

-- 사용자
ALTER TABLE `MY_SCHEMA`.`user`
ADD CONSTRAINT `PK_user` -- 사용자 기본키
PRIMARY KEY (
`SEQ` -- 사용자순번
);

-- 사용자그룹
CREATE TABLE `MY_SCHEMA`.`user_group` (
`SEQ`         INT         NOT NULL, -- 그룹순번
`NAME`        VARCHAR(60) NULL,     -- 그룹명칭
`CREATE_TIME` TIMESTAMP   NOT NULL DEFAULT NOW(), -- 생성일시
`DELYN`       CHAR(3)     NULL     DEFAULT 'NO' -- 삭제여부
);

-- 사용자그룹
ALTER TABLE `MY_SCHEMA`.`user_group`
ADD CONSTRAINT `PK_user_group` -- 사용자그룹 기본키
PRIMARY KEY (
`SEQ` -- 그룹순번
);

ALTER TABLE `MY_SCHEMA`.`user_group`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 사용자리소스
CREATE TABLE `MY_SCHEMA`.`user_db_resource` (
`RESOURCE_ID`    INT           NOT NULL, -- 리소스아이디
`RESOURCE_TYPES` VARCHAR(10)   NOT NULL, -- 유형
`USER_SEQ`       INT           NOT NULL, -- 사용자순번
`DB_SEQ`         INT           NOT NULL, -- DB아이디
`GROUP_SEQ`      INT           NOT NULL, -- 그룹순번
`NAME`           VARCHAR(50)   NULL,     -- 이름
`SHARED_TYPE`    VARCHAR(7)    NOT NULL DEFAULT 'PRIVATE', -- 공유종류
`DESCRIPTION`    VARCHAR(2000) NULL,     -- 설명
`CREATE_TIME`    TIMESTAMP     NOT NULL DEFAULT NOW(), -- 생성일시
`DELYN`          CHAR(3)       NOT NULL DEFAULT 'NO' -- 삭제여부
);

-- 사용자리소스
ALTER TABLE `MY_SCHEMA`.`user_db_resource`
ADD CONSTRAINT `PK_user_db_resource` -- 사용자리소스 기본키
PRIMARY KEY (
`RESOURCE_ID` -- 리소스아이디
);

ALTER TABLE `MY_SCHEMA`.`user_db_resource`
MODIFY COLUMN `RESOURCE_ID` INT NOT NULL AUTO_INCREMENT;

-- 데이터베이스
CREATE TABLE `MY_SCHEMA`.`user_db` (
`SEQ`                 INT           NOT NULL, -- DB아이디
`USER_SEQ`            INT           NOT NULL, -- 사용자순번
`EXT_SEQ`             INT           NOT NULL, -- 추가정보아이디
`GROUP_SEQ`           INT           NOT NULL, -- 그룹순번
`OPERATION_TYPE`      VARCHAR(10)   NULL,     -- 운영타입
`DBMS_TYPES`          VARCHAR(50)   NULL,     -- 종류
`URL`                 VARCHAR(2000) NULL,     -- 접속주소
`DB`                  VARCHAR(50)   NULL,     -- 데이터베이스
`GROUP_NAME`          VARCHAR(50)   NULL,     -- 그룹명칭
`DISPLAY_NAME`        VARCHAR(50)   NULL,     -- 표시이름
`HOST`                VARCHAR(50)   NULL,     -- 서버
`PORT`                VARCHAR(10)   NULL,     -- 포트
`LOCALE`              VARCHAR(10)   NULL,     -- 지역
`PASSWD`              VARCHAR(500)  NULL,     -- 비밀번호
`USERS`               VARCHAR(100)  NULL,     -- 사용자
`IS_PROFILE`          CHAR(3)       NULL     DEFAULT 'NO', -- 프로파일여부
`PROFILE_SELECT_MILL` INT           NULL     DEFAULT 0, -- 프로파일링제한시간
`QUESTION_DML`        CHAR(3)       NULL     DEFAULT 'NO', -- DML조회
`IS_READONLYCONNECT`  CHAR(3)       NULL     DEFAULT 'NO', -- 읽기전용접속
`IS_AUTOCOMMIT`       CHAR(3)       NULL     DEFAULT 'NO', -- 자동커밋
`CREATE_TIME`         TIMESTAMP     NULL     DEFAULT NOW(), -- 생성일시
`DELYN`               CHAR(3)       NULL     DEFAULT 'NO' -- 삭제여부
);

-- 데이터베이스
ALTER TABLE `MY_SCHEMA`.`user_db`
ADD CONSTRAINT `PK_user_db` -- 데이터베이스 기본키
PRIMARY KEY (
`SEQ` -- DB아이디
);

ALTER TABLE `MY_SCHEMA`.`user_db`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 사용자정보
CREATE TABLE `MY_SCHEMA`.`user_info_data` (
`SEQ`      INT           NOT NULL, -- 사용자정보순번
`USER_SEQ` INT           NOT NULL, -- 사용자순번
`DB_SEQ`   INT           NOT NULL, -- DB순번
`NAME`     VARCHAR(40)   NULL,     -- 명칭
`VALUE0`   VARCHAR(2000) NULL,     -- 값0
`VALUE1`   VARCHAR(2000) NULL,     -- 값1
`VALUE2`   VARCHAR(2000) NULL,     -- 값2
`VALUE3`   VARCHAR(2000) NULL,     -- 값3
`VALUE4`   VARCHAR(2000) NULL,     -- 값4
`VALUE5`   VARCHAR(2000) NULL      -- 값5
);

-- 사용자정보
ALTER TABLE `MY_SCHEMA`.`user_info_data`
ADD CONSTRAINT `PK_user_info_data` -- 사용자정보 기본키
PRIMARY KEY (
`SEQ` -- 사용자정보순번
);

ALTER TABLE `MY_SCHEMA`.`user_info_data`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 실행SQL
CREATE TABLE `MY_SCHEMA`.`executed_sql_resource` (
`SEQ`              INT           NOT NULL, -- 리소스순번
`USER_SEQ`         INT           NOT NULL, -- 사용자순번
`DB_SEQ`           INT           NOT NULL, -- DB순번
`TYPES`            VARCHAR(10)   NULL     DEFAULT '00', -- 유형
`STARTDATEEXECUTE` DATETIME      NOT NULL, -- 실행시작일시
`ENDDATEEXECUTE`   DATETIME      NOT NULL, -- 실행종료일시
`ROW`              INT           NULL     DEFAULT 0, -- 조회결과갯수
`RESULT`           CHAR(3)       NULL,     -- 결과코드
`MESSAGE`          VARCHAR(2000) NULL,     -- 결과메시지
`CREATE_TIME`      TIMESTAMP     NOT NULL DEFAULT NOW(), -- 생성일시
`DELYN`            CHAR(3)       NOT NULL DEFAULT 'N' -- 삭제여부
);

-- 실행SQL
ALTER TABLE `MY_SCHEMA`.`executed_sql_resource`
ADD CONSTRAINT `PK_executed_sql_resource` -- 실행SQL 기본키
PRIMARY KEY (
`SEQ` -- 리소스순번
);

ALTER TABLE `MY_SCHEMA`.`executed_sql_resource`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 리소스내용
CREATE TABLE `MY_SCHEMA`.`user_db_resource_data` (
`SEQ`                  INT           NOT NULL, -- 자료순번
`USER_DB_RESOURCE_SEQ` INT           NOT NULL, -- 리소스순번
`DATAS`                VARCHAR(2000) NULL      -- 리소스내용
);

-- 리소스내용
ALTER TABLE `MY_SCHEMA`.`user_db_resource_data`
ADD CONSTRAINT `PK_user_db_resource_data` -- 리소스내용 기본키
PRIMARY KEY (
`SEQ` -- 자료순번
);

ALTER TABLE `MY_SCHEMA`.`user_db_resource_data`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 실행SQL내용
CREATE TABLE `MY_SCHEMA`.`executed_sql_resource_data` (
`SEQ`                       INT           NOT NULL, -- 내용순번
`EXECUTED_SQL_RESOURCE_SEQ` INT           NOT NULL, -- 리소스순번
`DATAS`                     VARCHAR(2000) NULL      -- SQL내용
);

-- 실행SQL내용
ALTER TABLE `MY_SCHEMA`.`executed_sql_resource_data`
ADD CONSTRAINT `PK_executed_sql_resource_data` -- 실행SQL내용 기본키
PRIMARY KEY (
`SEQ` -- 내용순번
);

-- 계정추가정보
CREATE TABLE `MY_SCHEMA`.`account_ext` (
`SEQ`         INT           NOT NULL, -- 추가정보아이디
`USER_SEQ`    INT           NOT NULL, -- 사용자순번
`TYPES`       VARCHAR(100)  NULL,     -- 유형
`NAME`        VARCHAR(1000) NULL,     -- 속성명
`VALUE0`      VARCHAR(1000) NULL,     -- 속성값0
`VALUE1`      VARCHAR(1000) NULL,     -- 속성값1
`VALUE2`      VARCHAR(1000) NULL,     -- 속성값2
`VALUE3`      VARCHAR(1000) NULL,     -- 속성값3
`VALUE4`      VARCHAR(1000) NULL,     -- 속성값4
`VALUE5`      VARCHAR(1000) NULL,     -- 속성값5
`VALUE6`      VARCHAR(1000) NULL,     -- 속성값6
`VALUE7`      VARCHAR(1000) NULL,     -- 속성값7
`VALUE8`      VARCHAR(1000) NULL,     -- 속성값8
`VALUE9`      VARCHAR(1000) NULL,     -- 속성값9
`SUCCESS`     VARCHAR(10)   NULL,     -- 성공
`MESSAGE`     VARCHAR(1000) NULL,     -- 메시지
`CREATE_TIME` TIMESTAMP     NOT NULL DEFAULT NOW(), -- 생성일시
`DELYN`       CHAR(3)       NOT NULL DEFAULT 'NO' -- 삭제여부
);

-- 계정추가정보
ALTER TABLE `MY_SCHEMA`.`account_ext`
ADD CONSTRAINT `PK_account_ext` -- 계정추가정보 기본키
PRIMARY KEY (
`SEQ` -- 추가정보아이디
);

ALTER TABLE `MY_SCHEMA`.`account_ext`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 시스템정보
CREATE TABLE `MY_SCHEMA`.`tadpole_system` (
`SEQ`           INT           NOT NULL, -- 일련번호
`NAME`          VARCHAR(100)  NULL,     -- 명칭
`MAJOR_VERSION` VARCHAR(50)   NULL,     -- 버젼번호
`SUB_VERSION`   VARCHAR(50)   NULL,     -- 서브버젼
`INFORMATION`   VARCHAR(2000) NULL,     -- 시스템정보
`CREATE_TIME`   TIMESTAMP     NOT NULL DEFAULT NOW() -- 생성일시
);

-- 시스템정보
ALTER TABLE `MY_SCHEMA`.`tadpole_system`
ADD CONSTRAINT `PK_tadpole_system` -- 시스템정보 기본키
PRIMARY KEY (
`SEQ` -- 일련번호
);

ALTER TABLE `MY_SCHEMA`.`tadpole_system`
MODIFY COLUMN `SEQ` INT NOT NULL AUTO_INCREMENT;

-- 테이블필터
CREATE TABLE `MY_SCHEMA`.`user_db_filter` (
`SEQ`                  INT           NOT NULL, -- DB아이디
`IS_TABLE_FILTER`      CHAR(3)       NOT NULL DEFAULT 'NO', -- 테이블필터여부
`TABLE_FILTER_INCLUDE` VARCHAR(2000) NULL,     -- 포함테이블
`TABLE_FILTER_EXCLUDE` VARCHAR(2000) NULL      -- 제외테이블
);

-- 테이블필터
ALTER TABLE `MY_SCHEMA`.`user_db_filter`
ADD CONSTRAINT `PK_user_db_filter` -- 테이블필터 기본키
PRIMARY KEY (
`SEQ` -- DB아이디
);

-- SSH연결정보
CREATE TABLE `MY_SCHEMA`.`ssh_info` (
);

-- 데이터베이스확장속성
CREATE TABLE `MY_SCHEMA`.`user_db_ext` (
`SEQ`     INT                NOT NULL, -- DB아이디
`ORACLE_` VARCHAR(10)        NULL,     -- 오라클
`MONGO_`  <데이터 타입 없음> NULL,     -- 몽고
`EXT3`    <데이터 타입 없음> NULL,     -- 확장속성3
`EXT4`    <데이터 타입 없음> NULL,     -- 확장속성4
`EXT5`    <데이터 타입 없음> NULL,     -- 확장속성5
`EXT6`    <데이터 타입 없음> NULL,     -- 확장속성6
`EXT7`    <데이터 타입 없음> NULL,     -- 확장속성7
`EXT8`    <데이터 타입 없음> NULL,     -- 확장속성8
`EXT9`    <데이터 타입 없음> NULL,     -- 확장속성9
`EXT10`   <데이터 타입 없음> NULL      -- 확장속성10
);

-- 데이터베이스확장속성
ALTER TABLE `MY_SCHEMA`.`user_db_ext`
ADD CONSTRAINT `PK_user_db_ext` -- 데이터베이스확장속성 기본키
PRIMARY KEY (
`SEQ` -- DB아이디
);

-- 사용자역활
CREATE TABLE `MY_SCHEMA`.`user_role` (
`SEQ`       INT         NOT NULL, -- 역활순번
`GROUP_SEQ` INT         NOT NULL, -- 그룹순번
`USER_SEQ`  INT         NOT NULL, -- 사용자순번
`ROLE_TYPE` VARCHAR(20) NOT NULL DEFAULT '00', -- 사용자유형
`NAME`      VARCHAR(50) NULL,     -- 역활명
`DELYN`     CHAR(3)     NOT NULL DEFAULT 'NO' -- 삭제여부
);

-- 사용자역활
ALTER TABLE `MY_SCHEMA`.`user_role`
ADD CONSTRAINT `PK_user_role` -- 사용자역활 기본키
PRIMARY KEY (
`SEQ` -- 역활순번
);

-- 데이터보안정보
CREATE TABLE `MY_SCHEMA`.`data_security` (
`SEQ`         INT         NOT NULL, -- 순번
`DB_SEQ`      INT         NOT NULL, -- DB아이디
`CLASS_SEQ`   INT         NOT NULL, -- 클래스순번
`USER_SEQ`    INT         NOT NULL, -- 사용자순번
`TABLE_ID`    VARCHAR(30) NOT NULL, -- 테이블명
`COLUMN_ID`   VARCHAR(30) NOT NULL, -- 컬럼명
`CREATE_TIME` TIMESTAMP   NOT NULL DEFAULT NOW(), -- 생성일시
`DELYN`       CHAR(3)     NOT NULL DEFAULT 'NO' -- 삭제여부
);

-- 데이터보안정보
ALTER TABLE `MY_SCHEMA`.`data_security`
ADD CONSTRAINT `PK_data_security` -- 데이터보안정보 기본키
PRIMARY KEY (
`SEQ` -- 순번
);

-- 클래스 목록
CREATE TABLE `MY_SCHEMA`.`security_class` (
`SEQ`        INT           NOT NULL, -- seq
`GROUP_SEQ`  INT           NOT NULL, -- 그룹순번
`CLASS_NAME` VARCHAR(255)  NOT NULL, -- 클래스명
`CLASS_DESC` VARCHAR(2000) NULL      -- 클래스설명
);

-- 클래스 목록
ALTER TABLE `MY_SCHEMA`.`security_class`
ADD CONSTRAINT `PK_security_class` -- 클래스 목록 기본키
PRIMARY KEY (
`SEQ` -- seq
);

-- 데이터베이스
ALTER TABLE `MY_SCHEMA`.`user_db`
ADD CONSTRAINT `FK_user_TO_user_db` -- 사용자 -> 데이터베이스
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
),
ADD INDEX `FK_user_TO_user_db` (
`USER_SEQ` ASC -- 사용자순번
);

-- 사용자리소스
ALTER TABLE `MY_SCHEMA`.`user_db_resource`
ADD CONSTRAINT `FK_user_db_TO_user_db_resource` -- 데이터베이스 -> 사용자리소스
FOREIGN KEY (
`DB_SEQ` -- DB아이디
)
REFERENCES `MY_SCHEMA`.`user_db` ( -- 데이터베이스
`SEQ` -- DB아이디
);

-- 사용자리소스
ALTER TABLE `MY_SCHEMA`.`user_db_resource`
ADD CONSTRAINT `FK_user_TO_user_db_resource` -- 사용자 -> 사용자리소스
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
);

-- 사용자리소스
ALTER TABLE `MY_SCHEMA`.`user_db_resource`
ADD CONSTRAINT `FK_user_group_TO_user_db_resource` -- 사용자그룹 -> 사용자리소스
FOREIGN KEY (
`GROUP_SEQ` -- 그룹순번
)
REFERENCES `MY_SCHEMA`.`user_group` ( -- 사용자그룹
`SEQ` -- 그룹순번
);

-- 데이터베이스
ALTER TABLE `MY_SCHEMA`.`user_db`
ADD CONSTRAINT `FK_account_ext_TO_user_db` -- 계정추가정보 -> 데이터베이스
FOREIGN KEY (
`EXT_SEQ` -- 추가정보아이디
)
REFERENCES `MY_SCHEMA`.`account_ext` ( -- 계정추가정보
`SEQ` -- 추가정보아이디
);

-- 데이터베이스
ALTER TABLE `MY_SCHEMA`.`user_db`
ADD CONSTRAINT `FK_user_group_TO_user_db` -- 사용자그룹 -> 데이터베이스
FOREIGN KEY (
`GROUP_SEQ` -- 그룹순번
)
REFERENCES `MY_SCHEMA`.`user_group` ( -- 사용자그룹
`SEQ` -- 그룹순번
);

-- 사용자정보
ALTER TABLE `MY_SCHEMA`.`user_info_data`
ADD CONSTRAINT `FK_user_TO_user_info_data` -- 사용자 -> 사용자정보
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
);

-- 사용자정보
ALTER TABLE `MY_SCHEMA`.`user_info_data`
ADD CONSTRAINT `FK_user_db_TO_user_info_data` -- 데이터베이스 -> 사용자정보
FOREIGN KEY (
`DB_SEQ` -- DB순번
)
REFERENCES `MY_SCHEMA`.`user_db` ( -- 데이터베이스
`SEQ` -- DB아이디
);

-- 실행SQL
ALTER TABLE `MY_SCHEMA`.`executed_sql_resource`
ADD CONSTRAINT `FK_user_TO_executed_sql_resource` -- 사용자 -> 실행SQL
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
);

-- 실행SQL
ALTER TABLE `MY_SCHEMA`.`executed_sql_resource`
ADD CONSTRAINT `FK_user_db_TO_executed_sql_resource` -- 데이터베이스 -> 실행SQL
FOREIGN KEY (
`DB_SEQ` -- DB순번
)
REFERENCES `MY_SCHEMA`.`user_db` ( -- 데이터베이스
`SEQ` -- DB아이디
);

-- 리소스내용
ALTER TABLE `MY_SCHEMA`.`user_db_resource_data`
ADD CONSTRAINT `FK_user_db_resource_TO_user_db_resource_data` -- 사용자리소스 -> 리소스내용
FOREIGN KEY (
`USER_DB_RESOURCE_SEQ` -- 리소스순번
)
REFERENCES `MY_SCHEMA`.`user_db_resource` ( -- 사용자리소스
`RESOURCE_ID` -- 리소스아이디
);

-- 실행SQL내용
ALTER TABLE `MY_SCHEMA`.`executed_sql_resource_data`
ADD CONSTRAINT `FK_executed_sql_resource_TO_executed_sql_resource_data` -- 실행SQL -> 실행SQL내용
FOREIGN KEY (
`EXECUTED_SQL_RESOURCE_SEQ` -- 리소스순번
)
REFERENCES `MY_SCHEMA`.`executed_sql_resource` ( -- 실행SQL
`SEQ` -- 리소스순번
);

-- 계정추가정보
ALTER TABLE `MY_SCHEMA`.`account_ext`
ADD CONSTRAINT `FK_user_TO_account_ext` -- 사용자 -> 계정추가정보
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
);

-- 테이블필터
ALTER TABLE `MY_SCHEMA`.`user_db_filter`
ADD CONSTRAINT `FK_user_db_TO_user_db_filter` -- 데이터베이스 -> 테이블필터
FOREIGN KEY (
`SEQ` -- DB아이디
)
REFERENCES `MY_SCHEMA`.`user_db` ( -- 데이터베이스
`SEQ` -- DB아이디
);

-- 데이터베이스확장속성
ALTER TABLE `MY_SCHEMA`.`user_db_ext`
ADD CONSTRAINT `FK_user_db_TO_user_db_ext` -- 데이터베이스 -> 데이터베이스확장속성
FOREIGN KEY (
`SEQ` -- DB아이디
)
REFERENCES `MY_SCHEMA`.`user_db` ( -- 데이터베이스
`SEQ` -- DB아이디
);

-- 사용자역활
ALTER TABLE `MY_SCHEMA`.`user_role`
ADD CONSTRAINT `FK_user_group_TO_user_role` -- 사용자그룹 -> 사용자역활
FOREIGN KEY (
`GROUP_SEQ` -- 그룹순번
)
REFERENCES `MY_SCHEMA`.`user_group` ( -- 사용자그룹
`SEQ` -- 그룹순번
);

-- 사용자역활
ALTER TABLE `MY_SCHEMA`.`user_role`
ADD CONSTRAINT `FK_user_TO_user_role` -- 사용자 -> 사용자역활
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
);

-- 데이터보안정보
ALTER TABLE `MY_SCHEMA`.`data_security`
ADD CONSTRAINT `FK_user_db_TO_data_security` -- 데이터베이스 -> 데이터보안정보
FOREIGN KEY (
`DB_SEQ` -- DB아이디
)
REFERENCES `MY_SCHEMA`.`user_db` ( -- 데이터베이스
`SEQ` -- DB아이디
);

-- 데이터보안정보
ALTER TABLE `MY_SCHEMA`.`data_security`
ADD CONSTRAINT `FK_security_class_TO_data_security` -- 클래스 목록 -> 데이터보안정보
FOREIGN KEY (
`CLASS_SEQ` -- 클래스순번
)
REFERENCES `MY_SCHEMA`.`security_class` ( -- 클래스 목록
`SEQ` -- seq
);

-- 데이터보안정보
ALTER TABLE `MY_SCHEMA`.`data_security`
ADD CONSTRAINT `FK_user_TO_data_security` -- 사용자 -> 데이터보안정보
FOREIGN KEY (
`USER_SEQ` -- 사용자순번
)
REFERENCES `MY_SCHEMA`.`user` ( -- 사용자
`SEQ` -- 사용자순번
);

-- 클래스 목록
ALTER TABLE `MY_SCHEMA`.`security_class`
ADD CONSTRAINT `FK_user_group_TO_security_class` -- 사용자그룹 -> 클래스 목록
FOREIGN KEY (
`GROUP_SEQ` -- 그룹순번
)
REFERENCES `MY_SCHEMA`.`user_group` ( -- 사용자그룹
`SEQ` -- 그룹순번
);