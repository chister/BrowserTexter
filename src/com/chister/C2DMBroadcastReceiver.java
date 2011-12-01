package com.chister;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
	    
		NotificationManager nm = 
			(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		CharSequence text = "Michael Chi";
		Notification notification = new Notification(R.drawable.icon, text, System.currentTimeMillis());
		
		CharSequence contentTitle = "registration";
		CharSequence contentText = registration_id;
		Intent notificationIntent = new Intent(context, InitBrowserTexter.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		//sendSMS(context, "3107094858", registration_id);
		//nm.notify(1, notification);
	    if (intent.getStringExtra("error") != null) {
	        // Registration failed, should try again later.
	    } else if (intent.getStringExtra("unregistered") != null) {
	        // unregistration done, new messages from the authorized sender will be rejected
	    } else if (registration_id != null) {
	       // Send the registration ID to the 3rd party site that is sending the messages.
	       // This should be done in a separate thread.
	       // When done, remember that all registration is done.
	    	HttpInterface.updateRegistrationId(context, registration_id);
	    }
	}

	private void sendSMS(Context context, String phoneNumber, String message)
    {        
        //PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, InitBrowserTexter.class), 0);                
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
