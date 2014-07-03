package icontroller.group.contacts;

import icontroller.IController;
import entities.Contact;
import entities.ContactGroup;

public interface IContactController extends IController{

	void back();

	void add();

	void enterContact();

	void importGoogle();

	void importYahoo();

	void edit(ContactGroup g, Contact c);

	void delete(int index);

	void save();

	void goBack();

}
