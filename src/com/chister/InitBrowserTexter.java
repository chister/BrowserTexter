package com.chister;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class InitBrowserTexter extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (HttpInterface.checkIfRegistered(getBaseContext())) {
        	registerC2DM();	
       
        } else {
        	// open a view to register
        	Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://50.19.255.176/chister/go/goToRegistration.doit"));
        	startActivity(viewIntent);  
        }
        finish();
    }
    
	
	private void registerC2DM() {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", "chister@gmail.com");
		startService(registrationIntent);
	}
}