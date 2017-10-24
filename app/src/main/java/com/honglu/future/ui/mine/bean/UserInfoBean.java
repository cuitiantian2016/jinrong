package com.honglu.future.ui.mine.bean;

//用户信息
public class UserInfoBean {
    /**
     * uid : 422
     * username : 13120678525
     * token : 3d17a21c0811f5b47d2fc019c2205ddb29743944111c0fbeb5c10034e1ac0ba5
     * realname : 谢井文
     * sessionid : 89D76DB739E5400F640E164A6D2BAD20
     */

    private int    uid;
    private String username;
    private String token;
    private String realname;
    private String sessionid;
    private String idNumber;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

	/*private int id;
    private int uid;

	private String user_sign;
	
	private String username;
	private String realname;
	private String token;
	private String id_card;
	
	private int real_pay_pwd_status;
	private int real_verify_status;
	private int real_work_status;
	private int real_contact_status;
	private int real_zmxy_status;
	private int real_bind_bank_card_status;
	private int real_work_fzd_status;
	private int real_jxl_status;
	private int status;
	
	private String is_novice;
	private String phone_change_available;
	private String show_process;
	private String bank_card_count;
	private String show_process_bank;
	private String sessionid;
	private List<BankItem> card_list;

    	   
    private String lqd_money;
   	private String lqd_text;
   	private String fzd_money;
   	private String fzd_text;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public String getRealname() {
		return realname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getIs_novice() {
		return is_novice;
	}
	public void setIs_novice(String is_novice) {
		this.is_novice = is_novice;
	}
	public String getPhone_change_available() {
		return phone_change_available;
	}
	public void setPhone_change_available(String phone_change_available) {
		this.phone_change_available = phone_change_available;
	}
	public String getShow_process() {
		return show_process;
	}
	public void setShow_process(String show_process) {
		this.show_process = show_process;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBank_card_count() {
		return bank_card_count;
	}
	public void setBank_card_count(String bank_card_count) {
		this.bank_card_count = bank_card_count;
	}
	public String getShow_process_bank() {
		return show_process_bank;
	}
	public void setShow_process_bank(String show_process_bank) {
		this.show_process_bank = show_process_bank;
	}
	public String getUser_sign()
	{
		return user_sign;
	}
	public void setUser_sign(String user_sign)
	{
		this.user_sign = user_sign;
	}
	public int getReal_pay_pwd_status() {
		return real_pay_pwd_status;
	}
	public void setReal_pay_pwd_status(int real_pay_pwd_status) {
		this.real_pay_pwd_status = real_pay_pwd_status;
	}
	public int getReal_verify_status() {
		return real_verify_status;
	}
	public void setReal_verify_status(int real_verify_status) {
		this.real_verify_status = real_verify_status;
	}
	public int getReal_work_status() {
		return real_work_status;
	}
	public void setReal_work_status(int real_work_status) {
		this.real_work_status = real_work_status;
	}
	public int getReal_contact_status() {
		return real_contact_status;
	}
	public void setReal_contact_status(int real_contact_status) {
		this.real_contact_status = real_contact_status;
	}
	public int getReal_zmxy_status() {
		return real_zmxy_status;
	}
	public void setReal_zmxy_status(int real_zmxy_status) {
		this.real_zmxy_status = real_zmxy_status;
	}
	public int getReal_bind_bank_card_status() {
		return real_bind_bank_card_status;
	}
	public void setReal_bind_bank_card_status(int real_bind_bank_card_status) {
		this.real_bind_bank_card_status = real_bind_bank_card_status;
	}
	public int getReal_work_fzd_status() {
		return real_work_fzd_status;
	}
	public void setReal_work_fzd_status(int real_work_fzd_status) {
		this.real_work_fzd_status = real_work_fzd_status;
	}
	public List<BankItem> getCard_list() {
		return card_list;
	}
	public void setCard_list(List<BankItem> card_list) {
		this.card_list = card_list;
	}
	public String getLqd_money() {
		return lqd_money;
	}
	public void setLqd_money(String lqd_money) {
		this.lqd_money = lqd_money;
	}
	public String getLqd_text() {
		return lqd_text;
	}
	public void setLqd_text(String lqd_text) {
		this.lqd_text = lqd_text;
	}
	public String getFzd_money() {
		return fzd_money;
	}
	public void setFzd_money(String fzd_money) {
		this.fzd_money = fzd_money;
	}
	public String getFzd_text() {
		return fzd_text;
	}
	public void setFzd_text(String fzd_text) {
		this.fzd_text = fzd_text;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public int getReal_jxl_status()
	{
		return real_jxl_status;
	}

	public void setReal_jxl_status(int real_jxl_status)
	{
		this.real_jxl_status = real_jxl_status;
	}*/
}
