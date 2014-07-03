package imodel.group.contacts;

public interface IYahooImportModel {

	void addGetRequestTokenCompletedListener(IYahooGetRequestTokenListener li);
	void removeGetRequestTokenCompletedListener(IYahooGetRequestTokenListener li);
	void getRequestToken(String yahooConsumerKey, String nonce,
			String signatureMethod, String consumeSecret, String timestamp, String version,
			String langPref, String callback);
	void getAccessToken(String consumer, String nonce, String method, String consumerSecret,String tokenSecret, String timespan, String version, String oauthToken, String verifierCode);
	void addGetAccessTokenCompletedListener(IYahooGetAccessTokenListener li);
	void removeGetAccessTokenCompletedListener(IYahooGetAccessTokenListener li);
	
	void addGetContactsCompletedListener(IGetContactsCompletedListener li);
	void removeGetContactsCompletedListener(IGetContactsCompletedListener li);
	void getContacts(String guid, String view, String version, String nonce, String timestamp, String consumer, String accessToken, String tokenSecret);
}
