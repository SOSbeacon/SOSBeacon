package icontroller.group.contacts.addnew;

import icontroller.IController;

public interface IAddContactController extends IController{

	void back(String name, String email, String voice, String text);

	void save(String name, String email, String voice, String text);

	void importPhone();
	
	void goBack();

}
