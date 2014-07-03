package imodel;

public interface IRegisterModel2 {
	void submit(String imei, String phoneNumber,String phoneType,String action);
	void addRegsiterCompletedListener(IRegisterCompleted2 listener);
	void removeRegisterCompletedListener(IRegisterCompleted2 listener);
	String getImei();
}
