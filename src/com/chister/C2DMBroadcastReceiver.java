package com.chister;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

public class C2DMBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
	        handleRegistration(context, intent);
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	        handleMessage(context, intent);
	     }
	 }

	private void handleRegistration(Context context, Intent intent) {
	    String registration_id = intent.getStringExtra("registration_id");
	    
	    HttpInterface.updateRegistrationId(context, registration_id);
	}

	private void sendSMS(Context context, String phoneNumber, String message)
    {        
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);        
    }  
	
	private void handleMessage(Context context, Intent intent) {
		Bundle data = intent.getExtras();
		String phoneNumber = (String) data.get("phoneNumber");
		String smsMessage = (String) data.get("smsMessage");

		sendSMS(context, phoneNumber, smsMessage);
	}

}
