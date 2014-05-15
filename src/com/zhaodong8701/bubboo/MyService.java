package com.zhaodong8701.bubboo;

import com.zhaodong8701.receiver.lockScreenReeiver;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {
	BroadcastReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		KeyguardManager.KeyguardLock k1;
		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		k1 = km.newKeyguardLock("IN");
		k1.disableKeyguard();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new lockScreenReeiver();
		registerReceiver(mReceiver, filter);
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
