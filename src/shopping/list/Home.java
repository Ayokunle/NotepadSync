package shopping.list;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
 
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class Home extends Activity{

	String user_name;
	public static final String PREFS_NAME = "NoteSync";
	String manufacturer = Build.MANUFACTURER;
	String model = Build.MODEL;
	String username = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		getActionBar().setTitle("Notepad Sync");
		
	}
	
	public void onNewListClick(final View v){
	    Intent startNewActivityOpen = new Intent(Home.this, NewList.class);
		startActivityForResult(startNewActivityOpen, 0);
    }
	
	public void onGetListClick(final View v){
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final EditText input = new EditText(Home.this); 
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                              LinearLayout.LayoutParams.MATCH_PARENT,
                              LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        adb.setView(input);
        adb.setTitle("Get shopping list");
        adb.setMessage("Enter the Access Code");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
            	//getWindow().getDecorView().findViewById(R.id.scrollview_list).invalidate();
                return;
            }
        });
        AlertDialog dialog = adb.show();
        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.app_info:
	            //openSettings();
	            Log.d("Action", "Settings");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
	/** Called when the activity is brought to front. */
    @Override
    protected void onResume() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String skipMessage = settings.getString("showMessage", "Yes");
        
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final EditText input = new EditText(Home.this);  
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                              LinearLayout.LayoutParams.MATCH_PARENT,
                              LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        adb.setView(input);
        adb.setMessage("Hi! What is your name?");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if(input.getText().toString().equals("")){
            		showToast();
            		
            		AccountManager manager = AccountManager.get(getBaseContext()); 
        	        Account[] accounts = manager.getAccountsByType("com.google"); 
        	        List<String> possibleEmails = new LinkedList<String>();

        	        for (Account account : accounts) {
        	          // TODO: Check possibleEmail against an email regex or treat
        	          // account.name as an email address only for certain account.type values.
        	          possibleEmails.add(account.name);
        	        }

        	        if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null){
        	            String email = possibleEmails.get(0);
        	            String[] parts = email.split("@");
        	            if(parts.length > 0 && parts[0] != null)
        	            	username =  parts[0];
        	            else
        	            	username =  "?";
        	        }else
        	        	username = "?";
        	        
        	        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    user_name = input.getText().toString();
                    editor.putString("username", username +" - "+ manufacturer + " - "+ model);
                    editor.putString("showMessage", "No");
                    editor.commit();
        	        
            		return;
            	}
            	
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                user_name = input.getText().toString();
                editor.putString("username", input.getText().toString());
                editor.putString("showMessage", "No");
                editor.commit();
                return;
            }
        });
        
        if(!skipMessage.equals("No")){
        	AlertDialog dialog = adb.show();
        	dialog.show();
        }else{
        	//Toast.makeText(this, "OnResume() call", Toast.LENGTH_LONG).show();
    		int list_index = settings.getInt("list_index", 0);
    		
    		System.out.println("list_index: " + list_index);
    		
    		if( list_index == 0){
    			TextView tv = (TextView) findViewById(R.id.nolist);
    			tv.setText("No Lists...");
    		}else{
    			TextView tv = (TextView) findViewById(R.id.nolist);
    			tv.setText("");
    			String list_title [] = new String [list_index];
    			String list_time  [] = new String [list_index];
    			String list_date  [] = new String [list_index];
    			final String messages   [] = new String [list_index];
    			
    			JSONObject obj;
    			String json;
    			
    			try{
    				for(int i =0; i < list_index; i++ ){
    					json = settings.getString(Integer.toString(i), "null");
    					obj = new JSONObject(json);
    					if(obj.getString("list_title").equals("")){
    						list_title[i] = obj.getJSONArray("messages").getString(i);//.getString("list_title");
    					}else{
    						list_title[i] = obj.getString("list_title");
    					}
    					list_time[i] = obj.getString("time");
    					list_date[i] = obj.getString("date");
    					messages[i] = obj.getJSONArray("messages").getString(i);
    					System.out.println("json: "+ obj);
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			
    			RelativeLayout rl = (RelativeLayout) findViewById(R.id.list);
    			CutomArrayAdapter caa = new CutomArrayAdapter(this, list_title, list_time, list_date);
    			ListView listview = new ListView(this);
    			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
    					RelativeLayout.LayoutParams.WRAP_CONTENT,
    					RelativeLayout.LayoutParams.WRAP_CONTENT);
    			listview.setLayoutParams(rlp);
    			listview.setAdapter(caa);
    			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    			      @Override
    			      public void onItemClick(AdapterView<?> parent, final View view,
    			          int position, long id) {
    			    	//open intent with extra
    			    	  Intent intent = new Intent(getBaseContext(), NewList.class);
    			    	  intent.putExtra("messages", messages[position]);
    			    	  startActivity(intent);
    			      }
    			});
    			rl.addView(listview);
    			
    			View newList = rl.findViewById(R.id.newList);
    			((RelativeLayout)newList.getParent()).removeView(newList);
    			rl.addView(newList);
    			
    			View getList = rl.findViewById(R.id.getList);
    			((RelativeLayout)getList.getParent()).removeView(getList);
    			rl.addView(getList);
    			
    		}
        }
        
        super.onResume();
    }
    
    void showToast(){
    	Toast.makeText(this, "Phone ID will be used.\n Change in Settings.", Toast.LENGTH_LONG).show();
    } 
    
	public class LoadData extends AsyncTask<Void, Void, Void> {
	   	    
		String line = null;
		
	    //declare other objects as per your need
	    @Override
	    protected void onPreExecute()
	    {
	        
	                      
	    };      
	    
	    @Override
	    protected Void doInBackground(Void... params){
	    	/*
	        //do loading operation here  
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	nameValuePairs.add(new BasicNameValuePair("access_code",access_code));
			nameValuePairs.add(new BasicNameValuePair("location",location));
			
		    try{
		    	HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://ayokunle.netsoc.ie/projects/ShoppingList/db.py");
			    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    HttpResponse response = httpclient.execute(httppost); 
			    HttpEntity entity = response.getEntity();
			    is = entity.getContent();
			    Log.e("pass 1", "events - connection success ");
			}catch(Exception e){
		        Log.e("Fail 1", e.toString());
			}
		    
		    try{
			       BufferedReader reader = new BufferedReader
			    		   (new InputStreamReader(is,"iso-8859-1"),8);
			       StringBuilder sb = new StringBuilder();
			       while ((line = reader.readLine()) != null){
			       		    sb.append(line + "\n");
			       }
			       is.close();
			       String result = sb.toString();
//			       int start = result.indexOf("{");
//			       result = result.substring(start);
			       //Log.e("pass 2.0", "start: "+  start);
			       result = result.replaceAll("\"","'");
			       Log.e("pass 2.1", "events - result: "+ result);
				   Log.e("pass 2.2", "events - connection success ");
				}catch(Exception e){
					Log.e("Fail 2", e.toString());
				}     
			  */  
	        return null;
	    }       

		@Override
	    protected void onPostExecute(Void result){
	
		}
	
	}

}