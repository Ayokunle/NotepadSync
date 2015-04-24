package shopping.list;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	    
	    
	    RelativeLayout.LayoutParams  lp;
	    
	    RelativeLayout rl = new RelativeLayout(this);
	    lp = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	    rl.setLayoutParams(lp);
	    
	    for(int i =0; i < 3; i++){
	    	
	    	CheckBox cbox = new CheckBox(this);
		    EditText etext = new EditText(this);
		    
		    cbox.setId(i);
		    
		    etext.setText("TextViewTextViewTextViewTextViewTex");
	        //etext.setBackground(null);
	        etext.setId(i+5);
	        
	    	lp = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
	        lp.addRule(RelativeLayout.LEFT_OF, etext.getId());
	        if(i != 0){
	        	lp.addRule(RelativeLayout.BELOW, i-1);
	        }
	        cbox.setLayoutParams(lp);
	        
	        lp = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	        lp.addRule(RelativeLayout.RIGHT_OF, cbox.getId());
	        if(i != 0){
	        	lp.addRule(RelativeLayout.BELOW, etext.getId()-1);
	        }
	        etext.setLayoutParams(lp);
	        
	        rl.addView(cbox);
	        rl.addView(etext);
	    }
	    
        this.setContentView(rl);
        
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
