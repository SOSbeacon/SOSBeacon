package icontroller.setting.tellafriend;

import icontroller.IController;

public interface ITellAFriendController extends IController{

	void back();

	void send(String to, String cc, String subject, String content);

}
