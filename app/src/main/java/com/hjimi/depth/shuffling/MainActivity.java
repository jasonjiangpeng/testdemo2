package com.hjimi.depth.shuffling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.hjimi.depth.R;
import com.hjimi.depth.actvity.LogAct;
import com.hjimi.depth.actvity.MedeoAcitvity;
import com.hjimi.depth.devices.DevicesImi;
import com.hjimi.depth.sound.Soundplay;
import com.hjimi.depth.utils.CrashHandler;
import com.hjimi.depth.utils.Mlog;


public class MainActivity extends Activity {
	private Image3DSwitchView imageSwitchView;
    private DevicesImi myImiInit;
 private Soundplay soundplay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main3);
		imageSwitchView = (Image3DSwitchView) findViewById(R.id.image_switch_view);
		soundplay=new Soundplay();
			myImiInit=new DevicesImi(this,handler);
			new Thread(){
				@Override
				public void run() {
					while ( isrun){
						if (isrun2){
							handler.sendEmptyMessage(100);
							SystemClock.sleep(3000);
						}

					}
				}
			}.start();



	}
	@Override
	protected void onResume() {
		super.onResume();
	    myImiInit.resume();
		isrun2=true;

	}


	@Override
	protected void onStop() {
		super.onStop();
		myImiInit.stop();
		isrun2=false;

	}
	private  boolean isrun=true;
	private  boolean isrun2=true;
    private Handler handler =new Handler(){
	@Override
	public void handleMessage(Message msg) {

		switch (msg.what){
			case 100:
				imageSwitchView.scrollToNext();
				break;
			case 1000:
				Mlog.log("sucessDevices");
				myImiInit.start();
				break;
			case 999:
				Mlog.log("FailDevices");
				showMessageDialog();
				break;
			case 889:

				showMessageDialog();
				break;
			case 0:
				break;
			default:
				Intent intent =new Intent(MainActivity.this, MedeoAcitvity.class);
				int a=msg.what-1;
				soundplay.play(a);
				intent.putExtra("Value",a);
				startActivity(intent);
				break;
		}

	}
};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isrun=false;
		isrun2=false;
		soundplay.stop();
		imageSwitchView.clear();
		myImiInit.destory();
	}
	private void showMessageDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("The device is not connected!!!");
		builder.setPositiveButton("quit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				finish();
			}
		});
		builder.show();
	}
}
