package iview.checkin.checkingroup.record;

public interface IRecordView {
	void showMessage(String message);

	void hideProgress();

	void showProgress(String message);

	void updateProgress(String message);
}
