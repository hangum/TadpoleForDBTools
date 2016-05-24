package com.hangum.tadpole.engine.query.dao.system.bill;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.system.UserDAO;

/**
 * User bill dao
 * @author hangum
 *
 */
public class UserBillDAO {
	private int seq;
	private int user_seq;
	private int product_seq = 1;
	/** 학생(C01), 프리랜서(C02), 5인이하기업(C03), 기업(C04)*/
	private String userType = "";
	
	private String iamport_code = "";
	
	private String pg_company = "jtnet";
	private String pay_method = "card";
	private String merchant_uid = "";
	
	/** product name */
	private String name = "";
	
	private int original_amount = 0;
	private int ea = 0;
	private int term_month = 0;
	private int total_amount = 0;
	
	private String buyer_email = "";
	private String buyer_name = "";
	private String buyer_tel = "";
	private String buyer_addr = "";
	private String buyer_postcode = "";
	
	private String result_status = "NONE";
	/** 실패시 메시지 */
	private String fail_message = "";
	
	/** 성공시 메시지 */
	private String succ_imp_uid = "";
	private String succ_pg_tid = "";

	private String succ_apply_num = "";
	private String succ_paid_at = "";
	private Timestamp create_time;
	
	private String description = "";
	private String start_yn = "";
	private Timestamp service_end;
	
	/** 서비스를 사용하고 있는유저 정보 */
	private List<AssignedServiceDAO> listUser = new ArrayList<AssignedServiceDAO>();
	
	public UserBillDAO() {
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the user_seq
	 */
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the product_seq
	 */
	public int getProduct_seq() {
		return product_seq;
	}

	/**
	 * @param product_seq the product_seq to set
	 */
	public void setProduct_seq(int product_seq) {
		this.product_seq = product_seq;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the iamport_code
	 */
	public String getIamport_code() {
		return iamport_code;
	}

	/**
	 * @param iamport_code the iamport_code to set
	 */
	public void setIamport_code(String iamport_code) {
		this.iamport_code = iamport_code;
	}

	/**
	 * @return the pg_company
	 */
	public String getPg_company() {
		return pg_company;
	}

	/**
	 * @param pg_company the pg_company to set
	 */
	public void setPg_company(String pg_company) {
		this.pg_company = pg_company;
	}

	/**
	 * @return the pay_method
	 */
	public String getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method the pay_method to set
	 */
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the merchant_uid
	 */
	public String getMerchant_uid() {
		return merchant_uid;
	}

	/**
	 * @param merchant_uid the merchant_uid to set
	 */
	public void setMerchant_uid(String merchant_uid) {
		this.merchant_uid = merchant_uid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the original_amount
	 */
	public int getOriginal_amount() {
		return original_amount;
	}

	/**
	 * @param original_amount the original_amount to set
	 */
	public void setOriginal_amount(int original_amount) {
		this.original_amount = original_amount;
	}

	/**
	 * @return the ea
	 */
	public int getEa() {
		return ea;
	}

	/**
	 * @param ea the ea to set
	 */
	public void setEa(int ea) {
		this.ea = ea;
	}

	/**
	 * @return the term_month
	 */
	public int getTerm_month() {
		return term_month;
	}

	/**
	 * @param term_month the term_month to set
	 */
	public void setTerm_month(int term_month) {
		this.term_month = term_month;
	}

	/**
	 * @return the total_amount
	 */
	public int getTotal_amount() {
		return total_amount;
	}

	/**
	 * @param total_amount the total_amount to set
	 */
	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}

	/**
	 * @return the buyer_email
	 */
	public String getBuyer_email() {
		return buyer_email;
	}

	/**
	 * @param buyer_email the buyer_email to set
	 */
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	/**
	 * @return the buyer_name
	 */
	public String getBuyer_name() {
		return buyer_name;
	}

	/**
	 * @param buyer_name the buyer_name to set
	 */
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	/**
	 * @return the buyer_tel
	 */
	public String getBuyer_tel() {
		return buyer_tel;
	}

	/**
	 * @param buyer_tel the buyer_tel to set
	 */
	public void setBuyer_tel(String buyer_tel) {
		this.buyer_tel = buyer_tel;
	}

	/**
	 * @return the buyer_addr
	 */
	public String getBuyer_addr() {
		return buyer_addr;
	}

	/**
	 * @param buyer_addr the buyer_addr to set
	 */
	public void setBuyer_addr(String buyer_addr) {
		this.buyer_addr = buyer_addr;
	}

	/**
	 * @return the buyer_postcode
	 */
	public String getBuyer_postcode() {
		return buyer_postcode;
	}

	/**
	 * @param buyer_postcode the buyer_postcode to set
	 */
	public void setBuyer_postcode(String buyer_postcode) {
		this.buyer_postcode = buyer_postcode;
	}

	/**
	 * @return the result_status
	 */
	public String getResult_status() {
		return result_status;
	}

	/**
	 * @param result_status the result_status to set
	 */
	public void setResult_status(String result_status) {
		this.result_status = result_status;
	}

	/**
	 * @return the fail_message
	 */
	public String getFail_message() {
		return fail_message;
	}

	/**
	 * @param fail_message the fail_message to set
	 */
	public void setFail_message(String fail_message) {
		this.fail_message = fail_message;
	}

	/**
	 * @return the succ_imp_uid
	 */
	public String getSucc_imp_uid() {
		return succ_imp_uid;
	}

	/**
	 * @param succ_imp_uid the succ_imp_uid to set
	 */
	public void setSucc_imp_uid(String succ_imp_uid) {
		this.succ_imp_uid = succ_imp_uid;
	}

	/**
	 * @return the succ_pg_tid
	 */
	public String getSucc_pg_tid() {
		return succ_pg_tid;
	}

	/**
	 * @param succ_pg_tid the succ_pg_tid to set
	 */
	public void setSucc_pg_tid(String succ_pg_tid) {
		this.succ_pg_tid = succ_pg_tid;
	}

	/**
	 * @return the succ_apply_num
	 */
	public String getSucc_apply_num() {
		return succ_apply_num;
	}

	/**
	 * @param succ_apply_num the succ_apply_num to set
	 */
	public void setSucc_apply_num(String succ_apply_num) {
		this.succ_apply_num = succ_apply_num;
	}

	/**
	 * @return the succ_paid_at
	 */
	public String getSucc_paid_at() {
		return succ_paid_at;
	}

	/**
	 * @param succ_paid_at the succ_paid_at to set
	 */
	public void setSucc_paid_at(String succ_paid_at) {
		this.succ_paid_at = succ_paid_at;
	}

	/**
	 * @return the create_time
	 */
	public Timestamp getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the start_yn
	 */
	public String getStart_yn() {
		return start_yn;
	}

	/**
	 * @param start_yn the start_yn to set
	 */
	public void setStart_yn(String start_yn) {
		this.start_yn = start_yn;
	}

	/**
	 * @return the service_end
	 */
	public Timestamp getService_end() {
		return service_end;
	}

	/**
	 * @param service_end the service_end to set
	 */
	public void setService_end(Timestamp service_end) {
		this.service_end = service_end;
	}

	/**
	 * @return the listUser
	 */
	public List<AssignedServiceDAO> getListUser() {
		return listUser;
	}

	/**
	 * @param listUser the listUser to set
	 */
	public void setListUser(List<AssignedServiceDAO> listUser) {
		this.listUser = listUser;
	}


}
