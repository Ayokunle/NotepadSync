package shopping.list;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class NewList extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newlist);
		
		EditText editText = new EditText(this);
	    getActionBar().setCustomView(editText);
	    getActionBar().setTitle("");
	    //getActionBar().setDisplayShowHomeEnabled(false);
	    //getActionBar().setDisplayShowTitleEnabled(false);
	     			
	    
	    LinearLayout.LayoutParams  lp;
	    
	    LinearLayout ll = (LinearLayout) findViewById(R.id.newList_layout); //newList_layout
	    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    ll.setLayoutParams(lp);
	    
	    ScrollView sc = new ScrollView(this);
	    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	    sc.setLayoutParams(lp);
	    
	    LinearLayout ll2 = new LinearLayout(this);
	    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
	    ll2.setOrientation(LinearLayout.VERTICAL);
	    ll2.setLayoutParams(lp);
	    
	    final CheckBox cbox [] = new CheckBox[20];
	    final EditText etext [] = new EditText[20];
	    
	    for(int i = 0; i < 20; i++){
	    	
	    	LinearLayout temp_ll = new LinearLayout(this);
		    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    temp_ll.setLayoutParams(lp);
		    
	    	cbox[i] = new CheckBox(this);
		    etext[i] = new EditText(this);
		    
		    //cbox.setId(i);
		    
		    etext[i].setText("");
		    etext[i].setBackground(null);
		    final int x = i;
	        
	    	lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        cbox[i].setLayoutParams(lp);
	        cbox[i].setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	              if(((CheckBox) v).isChecked()) {
	            	  etext[x].setPaintFlags(etext[x].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	              }else{ 
	            	  etext[x].setPaintFlags( etext[x].getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
	              }
	            }
	          });
	        
	        if(i != 0){
	        	//System.out.println("Opacity: "+cbox.getAlpha());
	        	cbox[i].setAlpha((float)0.1);  // 50% transparent
	        	cbox[i].setEnabled(false);
	        	
	        }
	        lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        lp.weight = 80;
	        etext[i].setLayoutParams(lp);
	        etext[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
	            @Override
	            public void onFocusChange(View v, boolean hasFocus) {
	            	if (hasFocus) {
	            		cbox[x].setAlpha((float)0.5);
	                }else{
	                	etext[x].addTextChangedListener(new TextWatcher()
	        	        {
	        	            @Override
	        	            public void afterTextChanged(Editable mEdit) 
	        	            {
	        	                if(mEdit.toString().length() != 0){
	        	                	cbox[x].setAlpha((float)1.0);
	        	                	cbox[x].setEnabled(true);
	        	                }else{	                	
	        	                	cbox[x].setAlpha((float)0.1);
	        	                	cbox[x].setEnabled(false);
	        	                }
	        	            }

	        	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	        	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        	        });
	                }
	            }
	        });
	        
	        etext[i].addTextChangedListener(new TextWatcher()
	        {
	            @Override
	            public void afterTextChanged(Editable mEdit) 
	            {
	                if(mEdit.toString().length() != 0){
	                	cbox[x].setAlpha((float)1.0);
	                	cbox[x].setEnabled(true);
	                }else{	                	
	                	cbox[x].setAlpha((float)0.1);
	                	cbox[x].setEnabled(false);
	                }
	            }

	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });
	        
	        temp_ll.addView(etext[i]);
	        temp_ll.addView(cbox[i]);
	        
	        ll2.addView(temp_ll);
	        
	        View ruler = new View(this); 
	        ruler.setBackgroundColor(Color.GRAY);
	        ll2.addView(ruler,
	         new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2));
	    }
	    sc.addView(ll2);
	    ll.addView(sc);
	    //ll.setBackgroundColor(0x00ffff);
        this.setContentView(ll);
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_actions, menu);
	    /** Get the action view of the menu item whose id is search */
        View v = (View) menu.findItem(R.id.list_name).getActionView();
 
        /** Get the edit text from the action view */
        EditText txtSearch = ( EditText ) v.findViewById(R.id.list_name_edit);
 
        /** Setting an action listener */
//        txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
// 
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Toast.makeText(getBaseContext(), "Search : " + v.getText(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_BACK:
	            //doSomething();
	        	//Splash s = new Splash();
	        	//showMsg("Menu");
	        	//settings = true;
//	        	Intent startMain = new Intent(this, Menu.class);
//	    		startActivity(startMain);
	    		finish();
	            return true;
	    }
	    return super.onKeyDown(keycode, e);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.share_list:
	            //openSettings();
	            Log.d("Action", "share_list");
	            return true;
	       case R.id.view_log:
	            //openSettings();
	            Log.d("Action", "view_log");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
