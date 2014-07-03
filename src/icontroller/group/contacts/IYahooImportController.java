package icontroller.group.contacts;

import icontroller.IController;

public interface IYahooImportController extends IController{

	void receiveVerifier(String textContent);

	void back();

}
