package com.chister;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class HttpInterface {
	
	private static final String TAG = "HttpInterface";

	public static void updateRegistrationId(Context context, String registrationId) {
		XmlPullParserFactory factory;
		StringBuilder sb = new StringBuilder();
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);

			XmlPullParser xpp = factory.newPullParser();

			HttpClient client = new DefaultHttpClient();
			StringBuilder apiCall = new StringBuilder();
			apiCall.append("http://50.19.255.176/chister/go/updateRegistrationId.doit?");
			apiCall.append("emailAddress=");
			apiCall.append(getEmail(context));
			apiCall.append("&registrationId=");
			apiCall.append(registrationId);

			HttpPost post = new HttpPost(apiCall.toString());

			HttpResponse response = client.execute(post);
		} catch (Exception e) {
		}
	}
	
	public static boolean checkIfRegistered(Context context) {
		XmlPullParserFactory factory;
		StringBuilder sb = new StringBuilder();
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);

			XmlPullParser xpp = factory.newPullParser();

			HttpClient client = new DefaultHttpClient();
			StringBuilder apiCall = new StringBuilder();
			String deviceEmail = getEmail(context);
			Log.v(TAG, deviceEmail);
			apiCall.append("http://50.19.255.176/chister/go/checkIfRegistered.doit?emailAddress=");
			apiCall.append(deviceEmail);

			HttpGet get = new HttpGet(apiCall.toString());

			HttpResponse response = client.execute(get);
			xpp.setInput(response.getEntity().getContent(), "UTF-8");
			
			int eventType = xpp.getEventType();
	        do {
	            if(eventType == xpp.TEXT) {
	            	processText(xpp, sb);
	            }
	            eventType = xpp.next();
	        } while (eventType != xpp.END_DOCUMENT);
	        
	        String status = sb.toString().trim();
	        Log.v(TAG, "Response from CHISTER web service: " + status);
	        return status.equalsIgnoreCase("REGISTERED") ? true : false;
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
		}
		return false;
	}

	public static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}
	
	static int holderForStartAndLength[] = new int[2];
	
    public static void processText (XmlPullParser xpp, StringBuilder sb) throws XmlPullParserException
    {
        char ch[] = xpp.getTextCharacters(holderForStartAndLength);
        int start = holderForStartAndLength[0];
        int length = holderForStartAndLength[1];
        
        for (int i = start; i < start + length; i++) {
        	sb.append(ch[i]);
        }
    }
}
