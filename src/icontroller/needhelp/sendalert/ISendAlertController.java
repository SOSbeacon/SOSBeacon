package icontroller.needhelp.sendalert;

import icontroller.IController;

public interface ISendAlertController extends IController {
	void sendAlert(byte[] audio,String[] imagesPath);

	void back();
}
