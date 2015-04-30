package shopping.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewNote extends Activity{
	
	final CheckBox cbox [] = new CheckBox[20];
    final EditText etext [] = new EditText[20];
    boolean not_new = false;
    String [] text_check;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newlist);
		
		EditText editText = new EditText(this);
	    getActionBar().setCustomView(editText);
	    getActionBar().setTitle("");
	    //getActionBar().setDisplayShowHomeEnabled(false);
	    //getActionBar().setDisplayShowTitleEnabled(false);
	    
	    Intent intent = getIntent();
        String messages = intent.getStringExtra("messages");
        if(messages != null){
        	messages = messages.replace("[", "");
        	messages = messages.replace("]", "");
        	text_check = messages.split(",");
        	System.out.println("text_check: " + text_check[0]);
        	not_new = true;
        }
        
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
	    
	    
	    for(int i = 0; i < 20; i++){
	    	
	    	LinearLayout temp_ll = new LinearLayout(this);
		    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    temp_ll.setLayoutParams(lp);
		    
	    	cbox[i] = new CheckBox(this);
		    etext[i] = new EditText(this);
		    
		    //cbox.setId(i);
		    if(not_new == false){
		    	etext[i].setText("");
		    }else{
		    	try{
		    		String temp = text_check[i].replaceAll("\"", "");
		    		temp = temp.replaceAll("\\", "");
		    		
		    		if(i % 2 == 0){//if even number
		    			etext[i].setText(temp);
		    		}else{
		    			etext[i].setText(temp);
		    		}
		    	}catch(Exception e){
		    		etext[i].setText("");
		    	}
		    }
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
	            	  etext[x].setPaintFlags(etext[x].getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
	              }
	            }
	          });
	       
	        lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        lp.weight = 80;
	        etext[i].setLayoutParams(lp);
	        if(i != 0){
	        	etext[i].setFocusableInTouchMode(false);
	        	cbox[i].setAlpha((float)0.0);  // 50% transparent
	        	cbox[i].setEnabled(false);
	        }else{
	        	etext[i].setFocusableInTouchMode(true);
	        	cbox[i].setAlpha((float)1.0); 
	        	cbox[i].setEnabled(true);
	        }
	        final int y = i;
	        etext[i].setOnKeyListener(new View.OnKeyListener() {
	            public boolean onKey(View v, int keyCode, KeyEvent event) {
	                // If the event is a key-down event on the "enter" button
	                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
	                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
	                  // Perform action on key press
	                	etext[y+1].setFocusableInTouchMode(true);
	                	etext[y+1].requestFocus();
	                	cbox [y+1].setAlpha((float)1.0);  
	    	        	cbox [y+1].setEnabled(true);
	                 
	                  return true;
	                }
	                return false;
	            }
	        });
	        
	        if(not_new == true){
	        	try{
		    		if(i % 2 == 0){//if even number
		    			if(text_check[i+1].equals("Checked")){
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(true);
		    			}else{
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(false);
		    			}
		    		}else{
		    			if(text_check[i].equals("Checked")){
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(true);
		    			}else{
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(false);
		    			}
		    		}
		    	}catch(Exception e){
		    		//do nothing
		    	}
		    }
	        /*
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
	        */
	        /*
	        etext[i].addTextChangedListener(new TextWatcher()
	        {
	            @Override
	            public void afterTextChanged(Editable mEdit) 
	            {
	            	float textWidth = etext[y].getPaint().measureText(etext[y].getText().toString());
	            	System.out.println("textWidth: "+textWidth);
	            	
	            	if(textWidth >= etext[y].getMeasuredWidth ()){
	            		etext[y+1].setFocusableInTouchMode(true);
	                	etext[y+1].requestFocus();
	                	cbox [y+1].setAlpha((float)1.0);  
	    	        	cbox [y+1].setEnabled(true);
	            	}
	            }

	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });*/
	        
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
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    // Read values from the "savedInstanceState"-object and put them in your textview
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    // Save the values you need from your textview into "outState"-object
	    super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_BACK:
	        	boolean empty = true;
	        	int len = cbox.length;
	        	String text = "_";
        		String checkbox = "";
        		EditText list_title =  (EditText) findViewById(R.id.list_name_edit);
        		
	        	for(int i = 0; i < len; i++ ){
        			text = etext[i].getText().toString();
        			if(!text.equals("")){
        				empty = false;
        				break;
        			}
        		}
	        	if(!list_title.getText().toString().equals("")){
	        		empty = false;
	        	}
	        	
	        	if(empty == true){
	        		finish();
		            return true;
	        	}
	        	try{
	        		
	        		JSONObject obj = new JSONObject();
	        		JSONArray list = new JSONArray();
	        		
	        		obj.put("code", Integer.valueOf(100));
	        	
	        		for(int i = 0; i < len; i++ ){
	        			text = etext[i].getText().toString();
	        			
	        			//System.out.println(text);
	        			
	        			if(cbox[i].isChecked()){
	        				checkbox = "Checked"; 
	        			}else{
	        				checkbox = "NotChecked";
	        			}
	        			
	        			Pattern p = Pattern.compile("[a-zA-Z0-9]");
	        			Matcher m = p.matcher(text);
	        			if (m.find()){
	        				list.put(text);
			        		list.put(checkbox);
	        			}
	        		}
	        		obj.put("messages", list);
	        		obj.put("time", new SimpleDateFormat("hh:mm a").format(new Date()));
	        		obj.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
	        		
	        		obj.put("list_title", list_title.getText().toString());
	        		
	        		System.out.println(obj.toString());
	        		String json = obj.toString();
	        		
	        		String PREFS_NAME = "NoteSync";
	        		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	        		int list_index = settings.getInt("list_index", 0);
	        		
	        		SharedPreferences.Editor editor = settings.edit();
	                
	                editor.putString(Integer.toString(list_index), json);
	                
	                list_index = list_index + 1;

	                System.out.println("list_index: " + list_index);

	                editor.putInt("list_index", list_index);
	                
	                editor.commit();
	                
	        	}catch(Exception ex){
	        		ex.printStackTrace();
	        	}

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
