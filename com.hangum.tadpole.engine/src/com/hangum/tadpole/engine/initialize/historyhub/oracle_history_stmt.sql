-- oracle 파트.
CREATE SEQUENCE historyhub_history_seq START WITH 1 INCREMENT BY 1 MAXVALUE 9999999999999 CYCLE NOCACHE;

/* 전산원장관리 히스토리 */
CREATE TABLE historyhub_history (
	seq NUMBER NOT NULL, /* 순번 */
	reference_key NUMBER NOT NULL, /* 참조키 */
	cr_number varchar2(100) NOT NULL, /* cr_number */
	id varchar2(100) NOT NULL, /* id */
	req_query NCLOB NOT NULL, /* req_query */
	exe_query NCLOB NULL, /* exe_query */
	primary_keys varchar2(4000), /* 프라이머리키 */
	primary_rowid varchar(200), /* rowid */
	before_result NCLOB, /* 이전결과 */
	after_result NCLOB, /* 이후 결과 */
	create_time Timestamp DEFAULT current_timestamp /* 작업시간 */
);

CREATE UNIQUE INDEX PK_historyhub_history ON historyhub_history (seq ASC);

ALTER TABLE historyhub_history ADD CONSTRAINT PK_historyhub_history PRIMARY KEY (seq);
