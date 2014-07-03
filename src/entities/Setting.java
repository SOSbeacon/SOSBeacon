package entities;

import net.rim.device.api.util.Persistable;

public class Setting implements Persistable{
	public static final long KEY=0xc139c3b5cac9d328L;
	private String settingId;
	private String locationId;
	private String phoneId;
	private String voiceDuration;
	private String locationDuration;
	private String panicNumber;
	private String alertSendToGroup;
	private String goodSamaritanStatus;
	private String goodSamaritanRange;
	private String incomingGovernmentAlert;
	private String panicAlertGoodSamaritanStatus;
	private String panicAlertGoodSamaritanRange;
	
	public Setting(String settingId, String locationId, String phoneId,
			String voiceDuration, String locationDuration, String panicNumber,
			String alertSendToGroup, String goodSamaritanStatus,
			String goodSamaritanRange, String incomingGovernmentAlert,
			String panicAlertGoodSamaritanStatus,
			String panicAlertGoodSamaritanRange) {
		super();
		this.settingId = settingId;
		this.locationId = locationId;
		this.phoneId = phoneId;
		this.voiceDuration = voiceDuration;
		this.locationDuration = locationDuration;
		this.panicNumber = panicNumber;
		this.alertSendToGroup = alertSendToGroup;
		this.goodSamaritanStatus = goodSamaritanStatus;
		this.goodSamaritanRange = goodSamaritanRange;
		this.incomingGovernmentAlert = incomingGovernmentAlert;
		this.panicAlertGoodSamaritanStatus = panicAlertGoodSamaritanStatus;
		this.panicAlertGoodSamaritanRange = panicAlertGoodSamaritanRange;
	}
	public Setting(){}
	public String getSettingId() {
		return settingId;
	}
	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public String getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(String voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public String getLocationDuration() {
		return locationDuration;
	}
	public void setLocationDuration(String locationDuration) {
		this.locationDuration = locationDuration;
	}
	public String getPanicNumber() {
		return panicNumber;
	}
	public void setPanicNumber(String panicNumber) {
		this.panicNumber = panicNumber;
	}
	public String getAlertSendToGroup() {
		return alertSendToGroup;
	}
	public void setAlertSendToGroup(String alertSendToGroup) {
		this.alertSendToGroup = alertSendToGroup;
	}
	public String getGoodSamaritanStatus() {
		return goodSamaritanStatus;
	}
	public void setGoodSamaritanStatus(String goodSamaritanStatus) {
		this.goodSamaritanStatus = goodSamaritanStatus;
	}
	public String getGoodSamaritanRange() {
		return goodSamaritanRange;
	}
	public void setGoodSamaritanRange(String goodSamaritanRange) {
		this.goodSamaritanRange = goodSamaritanRange;
	}
	public String getIncomingGovernmentAlert() {
		return incomingGovernmentAlert;
	}
	public void setIncomingGovernmentAlert(String incomingGovernmentAlert) {
		this.incomingGovernmentAlert = incomingGovernmentAlert;
	}
	public String getPanicAlertGoodSamaritanStatus() {
		return panicAlertGoodSamaritanStatus;
	}
	public void setPanicAlertGoodSamaritanStatus(
			String panicAlertGoodSamaritanStatus) {
		this.panicAlertGoodSamaritanStatus = panicAlertGoodSamaritanStatus;
	}
	public String getPanicAlertGoodSamaritanRange() {
		return panicAlertGoodSamaritanRange;
	}
	public void setPanicAlertGoodSamaritanRange(String panicAlertGoodSamaritanRange) {
		this.panicAlertGoodSamaritanRange = panicAlertGoodSamaritanRange;
	}
	
}
