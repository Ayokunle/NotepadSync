package shopping.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity{

	String user_name;
	public static final String PREFS_NAME = "NoteSync";
	String manufacturer = Build.MANUFACTURER;
	String model = Build.MODEL;
	String username = "";
	int lv_id = -404;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		getActionBar().setTitle("Notepad Sync");
		
	}
	
	public void onNewListClick(final View v){
	    Intent startNewActivityOpen = new Intent(Home.this, NewNote.class);
		startActivity(startNewActivityOpen);
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
    	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String skipMessage = settings.getString("showMessage", "Yes");
        
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final EditText input = new EditText(Home.this);  
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                              LinearLayout.LayoutParams.MATCH_PARENT,
                              LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        adb.setView(input);
        adb.setMessage("Hi! What is your name?");
        adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
        	@Override
        	public void onDismiss(final DialogInterface dialog) {
        		if(input.getText().toString().equals("")){
        			showToast();
        			
        			AccountManager manager = AccountManager.get(getBaseContext()); 
        			Account[] accounts = manager.getAccountsByType("com.google"); 
        			List<String> possibleEmails = new LinkedList<String>();
        			
        			for (Account account : accounts) {
    	          // 	TODO: Check possibleEmail against an email regex or treat
    	          // 	account.name as an email address only for certain account.type values.
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
        		}
        	}
        });
        
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                user_name = input.getText().toString();
                editor.putString("username", input.getText().toString());
                editor.putString("showMessage", "No");
                editor.commit();
                return;
            }
        });
        
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.list);
        if(lv_id != -404){
        	//System.out.println("Old Id: " + id);
        	try{
        		View oldListView = rl.findViewById(lv_id);
        		((RelativeLayout)oldListView.getParent()).removeView(oldListView);
        	}catch(Exception e){
        		
        	}
        }
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
    			final TextView tv = (TextView) findViewById(R.id.nolist);
    			tv.setText("");
    			final ArrayList<String> list_title = new ArrayList<String>();
    			final ArrayList<String> list_time = new ArrayList<String>();
    			final ArrayList<String> list_date = new ArrayList<String>();
    			final String messages   [] = new String [list_index];
    			
    			JSONObject obj;
    			String json;
    			
    			try{
    				for(int i =0; i < list_index; i++ ){
    					json = settings.getString(Integer.toString(i), "null");
    					obj = new JSONObject(json);
    					
    					if(obj.get("list_title").toString().equals("")){
    						String temp = "";
    						for(int j = 0; j < obj.getJSONArray("messages").length(); j++){
    							temp = obj.getJSONArray("messages").getString(j);
    							System.out.println("temp: " + temp);
    							if(!temp.equals("")){
    								break;
    							}
    							j++;
    						}
    						if(temp.length() > 20){
    							list_title.add(temp.substring(0, 20));//+"...";
    							
    							//if last char is a space char, remove it.
    							if(list_title.get(i).charAt(list_title.get(i).length()-1) == ' '){
    								list_title.add(list_title.get(i).substring(0, 19)+"...");
    							}else{
    								list_title.add(list_title.get(i)+"...");
    							}
    						}else{
    							list_title.add(temp.substring(0, temp.length()));
    						}
    					}else{
    						list_title.add(obj.get("list_title").toString());
    					}
    					
    					list_time.add(obj.getString("time"));
    					list_date.add(obj.getString("date"));
    					//System.out.println("json: "+ obj);    					
    					//messages[i] = obj.getJSONArray("messages").toString();
    					
    					//System.out.println("messages["+i+"]: "+ messages[i]);
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    			
    			final CutomArrayAdapter caa = new CutomArrayAdapter(this, list_title, list_time, list_date);
    			final ListView listview = new ListView(this);
    			
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
    			    	  Intent intent = new Intent(getBaseContext(), NewNote.class);
    			    	  intent.putExtra("index", position);
    			    	  startActivity(intent);
    			      }
    			});
    			listview.setLongClickable(true);
    			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

    	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
    	                    final int pos, long id) {
    	                // TODO Auto-generated method stub
    	            	AlertDialog.Builder adb2 = new AlertDialog.Builder(Home.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
    	            	adb2.setIcon(R.drawable.ic_launcher);
    	            	
    	            	String chars = "Notepad Sync";
    	                SpannableString str = new SpannableString(chars);
    	                str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
    	            	adb2.setTitle(str);
    	            	
    	            	ListView lst = new ListView(Home.this);
    	            	String[] arr = {"Open","Get Note Code","Delete"};
    	            	
    	                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
    	                        Home.this,
    	                        R.layout.alert_dialog_text, arr);
    	                lst.setAdapter(arrayAdapter);
    	                
    	                adb2.setAdapter(arrayAdapter,
    	                        new DialogInterface.OnClickListener() {
    	                	@Override
    	                    public void onClick(DialogInterface dialog, int position) {
    	                		if(position == 0){
    	                			//open
    	                			Intent intent = new Intent(getBaseContext(), NewNote.class);
    	      			    	  	intent.putExtra("index", pos);
    	      			    	  	startActivity(intent);
    	                		}else if( position == 1){
    	                			//show code
    	                			show_code(pos);
    	                		}else if(position == 2){
    	                			//delete 
    	                			//listview.removeViewAt(position);
    	                			//caa.remove(caa.getItem(pos));
    	                			
    	                			list_title.remove(pos);
    	                			list_time.remove(pos);
    	                			list_date.remove(pos);
    	                			int list_index = settings.getInt("list_index", 0);
    	                			for(int i = pos; i != list_index; i++){
    	                				if(list_index != 1){//more than one item in list
    	                					String json = settings.getString(Integer.toString(i+1), "null");
    	                					settings.edit().remove(Integer.toString(i+1)).commit();
    	                					
    	                					settings.edit().putString(Integer.toString(i), json).commit();
    	                					
    	                				}else{
    	                					// System.out.println("i: "+i);
    	                					settings.edit().remove(Integer.toString(i)).commit();
    	                					String json = settings.getString(Integer.toString(i), "null");
    	                					//System.out.println("Deleted. \n json: "+json);
    	                					tv.setText("No Lists...");
    	                					rl.removeView(listview);
    	                				}
    	                			}
    	                			list_index = list_index - 1;
    	                			settings.edit().putInt("list_index", list_index).commit();
    	                			
    	                			list_index = settings.getInt("list_index", -1);
    	                			System.out.println("list_index: " + list_index);
    	                			
    	                			caa.notifyDataSetChanged();
    	                			
    	                			/*
    	                			listview.setId( generateViewId() );
    	                			lv_id = listview.getId();
    	                			//System.out.println("New Id: " + id);
    	                			
    	                			rl.removeView(listview);
    	                			rl.addView(listview);
    	                			
    	                			View newList = rl.findViewById(R.id.newList);
    	                			((RelativeLayout)newList.getParent()).removeView(newList);
    	                			rl.addView(newList);
    	                			
    	                			
    	                			View getList = rl.findViewById(R.id.getList);
    	                			((RelativeLayout)getList.getParent()).removeView(getList);
    	                			rl.addView(getList);
    	                			*/
    	                		}
    	                	}
    	                });
    	                adb2.show();

    	                return true;
    	            }
    	        });
    			listview.setId( generateViewId() );
    			lv_id = listview.getId();
    			//System.out.println("New Id: " + id);
    			
    			rl.removeView(listview);
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
    
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    
    /**
	 * Generate a value suitable for use in {@link #setId(int)}.
	 * This value will not collide with ID values generated at build time by aapt for R.id.
	 *
	 * @return a generated ID value
	 */
	public static int generateViewId() {
	    for (;;) {
	        final int result = sNextGeneratedId.get();
	        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
	        int newValue = result + 1;
	        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
	        if (sNextGeneratedId.compareAndSet(result, newValue)) {
	            return result;
	        }
	    }
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
	
	public void show_code(int position){
		try{
			AlertDialog.Builder show_code = new AlertDialog.Builder(Home.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
			show_code.setIcon(R.drawable.ic_launcher);
			
			String chars = "Get Note Code";
			SpannableString str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
			show_code.setTitle(str);
			
			JSONObject obj;
			String json;
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			json = settings.getString(Integer.toString(position), "null");
			obj = new JSONObject(json);
			String code = obj.getString("code");
			
			chars = code;
			str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
			show_code.setMessage(str);
			//show_code.show();
			
			AlertDialog dialog = show_code.show();

			// Must call show() prior to fetching views
			TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
			messageView.setGravity(Gravity.CENTER);
			  
		}catch(Exception ex){
			ex.printStackTrace();
		}
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