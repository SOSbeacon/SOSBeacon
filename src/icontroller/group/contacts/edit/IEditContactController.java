package icontroller.group.contacts.edit;

import icontroller.IController;

public interface IEditContactController extends IController{

	void back(String name, String email, String voice, String text);

	void save(String name, String email, String voice, String text);

	void goBack();

}
