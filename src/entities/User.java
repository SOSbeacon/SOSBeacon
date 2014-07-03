package entities;

import net.rim.device.api.util.Persistable;

public class User implements Persistable{
	public final static long KEY=0x4bb4fe9f6a4dfdcL;
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	private boolean isActived;
	private String number;
	private String address;
	private String imei;
	private String phoneName;
	private String phoneType;
	public User(){}
	public User(String userName, String email, String password,
			String phoneNumber, boolean isActived, String number,
			String address, String imei, String phoneName, String phoneType) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.isActived = isActived;
		this.number = number;
		this.address = address;
		this.imei = imei;
		this.phoneName = phoneName;
		this.phoneType = phoneType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isActived() {
		return isActived;
	}
	public void setActived(boolean isActived) {
		this.isActived = isActived;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getPhoneName() {
		return phoneName;
	}
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	
}
