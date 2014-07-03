package entities;

public class Contact {
	String id;
	String name;
	String email;
	String voicePhone;
	String textPhone;
	String type;
	public Contact(String id,String name,String email) {
		super();
		this.id=id;
		this.name = name;
		this.email = email;
	}
	
	public Contact(String id,String name,String email, String voicePhone,
			String textPhone,String type) {
		this(id,name,email);
		if(voicePhone==null||voicePhone.equalsIgnoreCase("null"))
			this.voicePhone="";
		else this.voicePhone = voicePhone;
		if(textPhone==null||textPhone.equalsIgnoreCase("null"))
			this.textPhone="";
		else this.textPhone = textPhone;
		this.type = type;
		commit();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getVoicePhone() {
		return voicePhone;
	}

	public void setVoicePhone(String voicePhone) {
		this.voicePhone = voicePhone;
	}

	public String getTextPhone() {
		return textPhone;
	}

	public void setTextPhone(String textPhone) {
		this.textPhone = textPhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	String _name,_email,_voice,_text;
	public void commit(){
		_name=name;
		_email=email;
		_voice=voicePhone;
		_text=textPhone;
	}
	public void uncommit(){
		name=_name;
		email=_email;
		voicePhone=_voice;
		textPhone=_text;
	}
	public boolean isChanged(){
		return !(_name.equalsIgnoreCase(name)&&_email.equalsIgnoreCase(email)&&_voice.equalsIgnoreCase(voicePhone)&&_text.equalsIgnoreCase(textPhone));
	}
	public static Contact[] concat(Contact[] c, Contact n){
		for(int i=0;i<c.length;i++){
			if(c[i].getId().equalsIgnoreCase(n.getId())){
				c[i]=n;
				return c;
			}
		}
		Contact[] result = new Contact[c.length+1];
		for(int i=0;i<c.length;i++){
			result[i]=c[i];
		}
		result[result.length-1]=n;
		return result;
	}
}
