package icontroller.checkin.checkingroup;

import icontroller.IController;

public interface ICheckinGroupController extends IController{
	void back();

	void changeMessage(int selectedIndex);

	void submit(int groupIndex, int messageIndex, String msg);

	void record();
}
