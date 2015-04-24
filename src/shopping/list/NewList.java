package shopping.list;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
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
	    getActionBar().setTitle("New List");
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
	    
	    for(int i =0; i < 50; i++){
	    	
	    	LinearLayout temp_ll = new LinearLayout(this);
		    lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    temp_ll.setLayoutParams(lp);
		    
	    	final CheckBox cbox = new CheckBox(this);
		    EditText etext = new EditText(this);
		    
		    //cbox.setId(i);
		    
		    etext.setText("");
		    etext.setBackground(null);
		        
	        
	    	lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        
	        cbox.setLayoutParams(lp);
	        if(i != 0){
	        	//System.out.println("Opacity: "+cbox.getAlpha());
	        	cbox.setAlpha((float)0.1);  // 50% transparent
	        	cbox.setEnabled(false);
	        	
	        }
	        lp = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        lp.weight = 80;
	        etext.setLayoutParams(lp);
	        final int x = i;
	        etext.addTextChangedListener(new TextWatcher()
	        {
	            @Override
	            public void afterTextChanged(Editable mEdit) 
	            {
	                if(mEdit.toString().length() != 0){
	                	cbox.setAlpha((float)1.0);
	                	cbox.setEnabled(true);
	                }else{
	                	if(x != 0){
	                		cbox.setAlpha((float)0.1);
		                	cbox.setEnabled(false);
	                	}
	                }
	            }

	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });
	        
	        temp_ll.addView(etext);
	        temp_ll.addView(cbox);
	        
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
