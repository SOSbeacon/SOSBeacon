package icontroller.checkin.checkingroup.record;

import icontroller.IController;

public interface IRecordController extends IController{
	void back();

	void save(byte[] voiceNote, String[] fileNames);
}
