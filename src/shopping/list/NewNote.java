package shopping.list;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


public class NewNote extends Activity{
	
	int len = 25;
	final CheckBox cbox  [] = new CheckBox[len];
    final EditText etext [] = new EditText[len];
    boolean new_note = true, editted = false;
    String [] text_check;
    String PREFS_NAME = "NoteSync";
    int index = -1;
    EditText list_title;
    String code = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		//getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.newlist);
		
		EditText editText = new EditText(this);
	    getActionBar().setCustomView(editText);
	    getActionBar().setTitle("");
	    
	    Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        String json = "";
        JSONObject obj;
        String title = "";
        ArrayList<String> list = new ArrayList<String>();
        
        if(index != -1){// not new
        	try {
        		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            	json = settings.getString(Integer.toString(index), "null");
				obj = new JSONObject(json);
				//System.out.println("json: "+ obj);  
				JSONArray listdata = new JSONArray();
				
				listdata = obj.getJSONArray("messages");
				
				if (listdata != null) { 
				   for (int i=0;  i < listdata.length(); i++){ 
				    list.add(listdata.get(i).toString());
				   } 
				}
				code = obj.getString("code");
				//System.out.println("code: "+code);
				new_note = false;
			} catch (JSONException e) {
				//e.printStackTrace();
			}
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
	    
	    int al_i = 0;
	    for(int i = 0; i < len; i++){
	    	
	    	LinearLayout temp_ll = new LinearLayout(this);
		    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    temp_ll.setLayoutParams(lp);
		    
	    	cbox[i] = new CheckBox(this);
		    etext[i] = new EditText(this);
		    if(i != 0){
	        	etext[i].setFocusableInTouchMode(false);
	        	cbox[i].setAlpha((float)0.0);  // 50% transparent
	        	cbox[i].setEnabled(false);
	        }else{
	        	etext[i].setFocusableInTouchMode(true);
	        	cbox[i].setAlpha((float)1.0); 
	        	cbox[i].setEnabled(true);
	        }
		    
		    //cbox.setId(i);
		    if(new_note == true){
		    	etext[i].setText("");
		    }else{
		    	try{
		    		if(al_i % 2 == 0){//if even number
		    			etext[i].setText(list.get(al_i));
		    			etext[i].setFocusableInTouchMode(true);
		    		}else{
		    			al_i++;
		    			etext[i].setText(list.get(al_i));
		    			etext[i].setFocusableInTouchMode(true);
		    		}
		    	}catch(Exception e){
		    		//e.printStackTrace();
		    		etext[i].setText("");
		    	}
		    }
		    etext[i].setBackground(null);
		    etext[i].setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		    etext[i].setSelection(etext[i].getText().length());
		    
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

	        final int y = i;
	        etext[i].setOnKeyListener(new View.OnKeyListener() {
	            public boolean onKey(View v, int keyCode, KeyEvent event) {
	                
	            	try{
	            		// If the event is a key-down event on the "enter" button
	            		if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
	            				(keyCode == KeyEvent.KEYCODE_ENTER)) {
	            		// Perform action on key press
	            			etext[y+1].setFocusableInTouchMode(true);
	            			etext[y+1].setSelection(etext[y+1].getText().length());
	            			cbox [y+1].setAlpha((float)1.0);  
	            			cbox [y+1].setEnabled(true);
	            			
	            			return true;
	            		}
	            		/*
	            		if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
	            				(keyCode == KeyEvent.KEYCODE_DEL)) {
	            			System.out.println("<- pressed.");
	            			if(etext[y].getText().toString().equals("")){
	            				etext[y].setFocusableInTouchMode(false);
		            			etext[y-1].setSelection(etext[y-1].getText().length());
		            			cbox [y].setAlpha((float)0.0);  
		            			cbox [y].setEnabled(false);
	            			}
	            			return true;
	            		}*/
	            		
	                }catch(Exception e){
	                	e.printStackTrace();
	                }
	                return false;
	            }
	        });
	        
	        if(new_note == false){
	        	try{
		    		if(al_i % 2 == 0){//if even number
		    			if(list.get(al_i+1).equals("Checked")){
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(true);
		    	        	etext[i].setPaintFlags(etext[i].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		    			}else{
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(false);
		    			}
		    		}else{
		    			if(list.get(al_i).equals("Checked")){
		    				cbox[i].setAlpha((float)1.0);  
		    	        	cbox[i].setEnabled(true);
		    	        	cbox[i].setChecked(true);
		    	        	etext[i].setPaintFlags(etext[x].getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

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

	        etext[i].addTextChangedListener(new TextWatcher()
	        {
	            @Override
	            public void afterTextChanged(Editable mEdit) 
	            {
	            	/*
	            	float textWidth = etext[y].getPaint().measureText(etext[y].getText().toString());
	            	System.out.println("textWidth: "+textWidth);
	            	
	            	if(textWidth >= etext[y].getMeasuredWidth ()){
	            		etext[y+1].setFocusableInTouchMode(true);
	                	etext[y+1].requestFocus();
	                	cbox [y+1].setAlpha((float)1.0);  
	    	        	cbox [y+1].setEnabled(true);
	            	}*/
	            	editted = true;
	            }

	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });
	        
	        cbox[i].setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	            {
	            	editted = true;
	            }
	        });

	        
	        temp_ll.addView(etext[i]);
	        temp_ll.addView(cbox[i]);
	        
	        al_i++;
	        
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
        list_title = ( EditText ) v.findViewById(R.id.list_name_edit);
        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        String json = "";
        JSONObject obj;
        String title = "";
        
        try {
    		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        	json = settings.getString(Integer.toString(index), "null");
			obj = new JSONObject(json);
			//System.out.println("json: "+ obj);  
			
			title = obj.get("list_title").toString();
			//System.out.println("title: "+ title);
			if(!title.equals("")){
				list_title.setText(title);
			}
			 
			new_note = false;
		} catch (JSONException e){
			//e.printStackTrace();
		}
        
        int maxLength = 30;    
        list_title.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        list_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        list_title.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit) 
            {
            	editted = true;
            	
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        
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
	
	/*
    This method randomly generates 5 alphabetic chars and 4 number
	 */
	final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String NUM = "0123456789";
    
    SecureRandom  srnd = new SecureRandom ();
	public String randomString(){
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < 2; i++ ) 
			sb.append( ALPHA.charAt( srnd.nextInt(ALPHA.length()) ) );
		for( int i = 0; i < 3; i++ ) 
			sb.append( NUM.charAt( srnd.nextInt(NUM.length()) ) );
		
		return shuffle(sb.toString());
	}	
	
	public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        System.out.println(output.toString());
        return output.toString();
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
	        	goBack();
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

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	    	goBack();
	        break;
	    }

	    return true;
	}

	public void goBack(){
		boolean empty = true;
    	String text = "_";
		String checkbox = "";
		
		String title = list_title.getText().toString();
		//System.out.println("list_title: "+ title);
		
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
    	}
    	if(editted == false){
    		finish();
    	}
    	try{
    		
    		JSONObject obj = new JSONObject();
    		JSONArray list = new JSONArray();
    		JSONArray log = new JSONArray();
    		
    		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    		String username = settings.getString("username", "null");
    		
			JSONObject old_obj;
			String json;
			JSONArray old_log = null;
			
    		if(new_note == true){
    			obj.put("code", randomString());
    		}else{
    			obj.put("code", code);
    			
    			json = settings.getString(Integer.toString(index), "null");
				old_obj = new JSONObject(json);
				//System.out.println("->old_obj: "+old_obj);
				old_log = old_obj.getJSONArray("log");
    		}
    		
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
	        		
	        		if(new_note == true){
	        			log.put(username);
	        			log.put(new SimpleDateFormat("hh:mm a").format(new Date()));
	        			log.put(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
	        			obj.put("log", log);
	        		}else{
	        			old_log.put(username);
	        			old_log.put(new SimpleDateFormat("hh:mm a").format(new Date()));
	        			old_log.put(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
	        			obj.put("log", old_log);
    					
	        		}
	        		//System.out.println("log: "+log);
	        		
    			}
    		}
    		obj.put("messages", list);
    		
    		obj.put("time", new SimpleDateFormat("hh:mm a").format(new Date()));
    		obj.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    		
    		obj.put("list_title", title);
    		
    		
    		System.out.println("json: " + obj.toString());
    		json = obj.toString();
    		
    		int list_index = settings.getInt("list_index", 0);
    		
    		SharedPreferences.Editor editor = settings.edit();
            
           if(new_note == true){
            	editor.putString(Integer.toString(list_index), json);
            	list_index = list_index + 1;
            	//System.out.println("list_index: " + list_index);
            	editor.putInt("list_index", list_index);
            }else{
            	if(editted == true){
            		//System.out.println("index: " + index);
            		editor.putString(Integer.toString(index), json); //use original index of the note
            	}
            }
            
            editor.commit();
            
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
		finish();
	}	
}