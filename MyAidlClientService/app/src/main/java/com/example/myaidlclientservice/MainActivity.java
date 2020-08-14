package com.example.myaidlclientservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	IAidlService mService = null;

	private Button bindBtn, unbindBtn;
	private TextView dataTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		initViews();


	}

	private void initViews() {
		bindBtn = findViewById(R.id.bind_btn);
		unbindBtn = findViewById(R.id.unbind_btn);
		dataTV = findViewById(R.id.data_tv);

		bindBtn.setOnClickListener(mClickListener);
		unbindBtn.setOnClickListener(mClickListener);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IAidlService.Stub.asInterface(service);
			changeViewsState(true);

			try {
				mService.registerCallback(mCallback);
			} catch (RemoteException e) { /*NOP*/ }

			Toast.makeText(MainActivity.this, R.string.connected, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			changeViewsState(false);

			Toast.makeText(MainActivity.this, R.string.disconnected, Toast.LENGTH_SHORT).show();
		}
	};

	private IAidlServiceCallback mCallback = new IAidlServiceCallback.Stub() {
		public void valueChanged(Bundle bundle) {
			bundle.setClassLoader(getClass().getClassLoader());
			Weather weather = bundle.getParcelable("Weather");
			mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, weather));
		}
	};

	private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.bind_btn:
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("com.example.myaidlclientservice",
							"com.example.myaidlclientservice.AidlService"));
					intent.setAction(IAidlService.class.getName());
					bindService(intent, mConnection, BIND_AUTO_CREATE);
					break;

				case R.id.unbind_btn:
					if(mService != null) {
						try {
							mService.unregisterCallback(mCallback);
						} catch (RemoteException e) {
							//NOP
						}
					}

					unbindService(mConnection);
					changeViewsState(false);
					break;
			}
		}
	};

	private void changeViewsState(boolean isBound) {
		bindBtn.setEnabled(!isBound);
		unbindBtn.setEnabled(isBound);
		if(!isBound) {
			mService = null;
			dataTV.setText(R.string.null_data);
		}
	}

	private static final int BUMP_MSG = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case BUMP_MSG:
					Weather weather = ((Weather)msg.obj);
					dataTV.setText("Weather: " + weather.getType() + ", temperature: " + weather.getTemperature());
					break;

				default:
					super.handleMessage(msg);
			}
		}
	};
}
