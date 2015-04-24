 package shopping.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import java.io.Serializable;
import java.util.HashSet;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	    
		Thread t = new Thread(){
			public void run(){
				try {
					sleep(3000);
			
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					Intent startNewActivityOpen = new Intent(Splash.this, Home.class);
					startActivityForResult(startNewActivityOpen, 0);
				}
			}
		};
		t.start();
	}
}