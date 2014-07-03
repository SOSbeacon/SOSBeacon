package icontroller.needhelp;

import icontroller.IController;

public interface INeedHelpController extends IController{
	void callEmergency();
	void sendAlert();
	void cancel();
	void callNow();
	void cancelCall();
}
