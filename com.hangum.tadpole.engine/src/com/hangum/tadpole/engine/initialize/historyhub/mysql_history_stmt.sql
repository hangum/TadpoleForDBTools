-- 히스토리허브의 히스토리
CREATE TABLE `historyhub_history` (
	`seq`           BIGINT       NOT NULL COMMENT '순번', -- 일련번호
	`reference_key`  INT          NOT NULL COMMENT 'historyhub_seq 번호', -- 참조키
	`cr_number`     VARCHAR(100) NOT NULL COMMENT '작업 번호', -- 작업 요청번호
	`id`            VARCHAR(60)  NOT NULL COMMENT '작업자 이메일', -- 사용자아이디
	`req_query`     LONGTEXT     NOT NULL COMMENT '사용자 요청쿼리', -- 사용자 요청 쿼리
	`exe_query`     LONGTEXT     NOT NULL COMMENT '사용자 실행쿼리', -- 사용자 실행쿼리
	`primary_keys`  TEXT         NULL     COMMENT '프라이머리 값', -- 프라이머리 키
	`primary_rowid` VARCHAR(200) NULL     COMMENT '오라클일 경우 row_id', -- oracle rowid
	`before_result` LONGTEXT     NOT NULL COMMENT '이전 값', -- 이전 값
	`after_result`  LONGTEXT     NULL     COMMENT '수정된 값', -- 수정 값
	`create_time`   TIMESTAMP    NULL     DEFAULT NOW() COMMENT '생성일' -- 생성일
);

-- 히스토리허브의 히스토리
ALTER TABLE `historyhub_history`
	ADD CONSTRAINT `PK_historyhub_history` -- 히스토리허브의 히스토리 Primary key
		PRIMARY KEY (
			`seq` -- 일련번호
		);

ALTER TABLE `historyhub_history` MODIFY COLUMN `seq` BIGINT NOT NULL AUTO_INCREMENT COMMENT '순번';