package se.kth.mobsec.cryptmsg;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class WebCommuncation {

	private static final String serverUrl = "https://cloudcrypt.me/CaaSdroid/login.php";
	
	public static AESKey pairDevice(String userName, String passWord){
		HttpClient client = new DefaultHttpClient();
		
		
		AESKey personalKey = null;
		HttpPost serverRequest = new HttpPost(serverUrl);
		HttpParams params = new BasicHttpParams();
		params.setParameter("userName", userName);
		params.setParameter("passWd", passWord);
		serverRequest.setParams(params);
		return personalKey;
	}
	
}
