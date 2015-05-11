package shopping.list;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity {

	String user_name;
	public static final String PREFS_NAME = "NoteSync";
	String manufacturer = Build.MANUFACTURER;
	String model = Build.MODEL;
	String username = "";
	int lv_id = -404;
	int requestCode = 0, resultCode = 0;
	Intent data = null;
	int onCreate = 0;

	final ArrayList<String> list_title = new ArrayList<String>();
	final ArrayList<String> list_time = new ArrayList<String>();
	final ArrayList<String> list_date = new ArrayList<String>();
	CutomArrayAdapter caa = null;
	
	String code = "";
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		getActionBar().setTitle("Notepad Sync");
		RefreshNotes task = new RefreshNotes();
		task.execute();
	}

	public void onNewListClick(final View v) {
		Intent startNewActivityOpen = new Intent(Home.this, NewNote.class);
		startActivity(startNewActivityOpen);
	}

	public void onGetListClick(final View v) {
		System.out.println("onGetListClick()");
		AlertDialog.Builder adb = new AlertDialog.Builder(Home.this,
				AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		final EditText input = new EditText(Home.this);
		input.setBackgroundColor(Color.WHITE);
		input.requestFocus();
		input.setSelection(input.getText().length());
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		//input.setFilters(new InputFilter[] { new InputFilter.AllCaps() });
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		adb.setView(input);
		
		String chars = "Enter the Access Code";
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
				0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		adb.setMessage(str);
		
		chars = "OK";
		str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
				0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		adb.setPositiveButton(str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if (!input.getText().toString().equals("")) {
            		System.out.println("setPositiveButton()");
					code = input.getText().toString();
					SelectData task = new SelectData();
					task.execute();
				}
                return;
            }
        });
		AlertDialog dialog = adb.show();
		TextView messageText = (TextView) dialog
				.findViewById(android.R.id.message);
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
		case R.id.app_setting:
			final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			String uname = settings.getString("username", "");

			AlertDialog.Builder adb = new AlertDialog.Builder(Home.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
			final EditText input = new EditText(Home.this);
			input.setText(uname);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			input.setLayoutParams(lp);
			input.setBackgroundColor(Color.WHITE);
			
			String chars = "Here, you can change your name.";
			SpannableString str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
					0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			adb.setView(input);
			adb.setMessage(str);
			adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(final DialogInterface dialog) {
					if (input.getText().toString().equals("")) {
						//showToast();
						user_name = input.getText().toString();
						StoreUsername task = new StoreUsername();
						task.execute();
					}
				}
			});
			
			chars = "OK";
			str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
					0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			adb.setPositiveButton(str, new DialogInterface.OnClickListener() {
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
			
			AlertDialog dialog = adb.show();
			dialog.show();
			TextView messageText = (TextView) dialog
						.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
			return true;
		case R.id.app_info:
			ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.ic_launcher);
            LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(150,150);
            layoutParams.setMargins(Gravity.CENTER, 10, Gravity.CENTER, 10);
            image.setLayoutParams(layoutParams);
            
        	AlertDialog.Builder builder = new AlertDialog.Builder(Home.this,
    				AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        	chars = "Version 1.0 \n Developer: Ayokunle Adeosun \n adeosua@tcd.ie";
			str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
					0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       
        	// set dialog message
        	TextView msg = new TextView(this);
        	msg.setText(str);
        	msg.setGravity(Gravity.CENTER_HORIZONTAL);
        	
        	LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            
            layout.addView(image);
            
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.setMargins(Gravity.CENTER, 10, Gravity.CENTER, 10);
            
            layout.addView(msg, lp);
            
        	builder.setView(layout);
        	
        	//create alert dialog
        	AlertDialog alertDialog = builder.create();

        	//show it
        	alertDialog.show();
			return true;
		case R.id.action_refresh:
			// openSettings();
			Log.d("Action", "Refresh");
			RefreshNotes task = new RefreshNotes();
			task.execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Called when the activity is brought to front. */
	@Override
	protected void onResume(){
		setContentView(R.layout.home);
		
		System.out.println("onResume()");
		
		final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String skipMessage = settings.getString("showMessage", "Yes");

		AlertDialog.Builder adb = new AlertDialog.Builder(Home.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		final EditText input = new EditText(Home.this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		input.setBackgroundColor(Color.WHITE);
		
		String chars = "Hi! What is your name?";
		SpannableString str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
				0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		adb.setView(input);
		adb.setMessage(str);
		adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(final DialogInterface dialog) {
				if (input.getText().toString().equals("")) {
					showToast();
					user_name = input.getText().toString();
					StoreUsername task = new StoreUsername();
					task.execute();
				}
			}
		});
		
		chars = "OK";
		str = new SpannableString(chars);
		str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
				0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		adb.setPositiveButton(str, new DialogInterface.OnClickListener() {
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
		if (lv_id != -404) {
			System.out.println("Old Id: " + lv_id);
			try {
				View oldListView = rl.findViewById(lv_id);
				((RelativeLayout) oldListView.getParent())
						.removeView(oldListView);
			} catch (Exception e) {
				System.out.println("No old view to deleted. Line 196.");
				//e.printStackTrace();
			}
		}
		if (!skipMessage.equals("No")) {
			AlertDialog dialog = adb.show();
			dialog.show();
			TextView messageText = (TextView) dialog
					.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER);
		} else {
			// Toast.makeText(this, "OnResume() call",
			// Toast.LENGTH_LONG).show();
			int list_index = settings.getInt("list_index", 0);

			System.out.println("list_index: " + list_index);

			if (list_index == 0) {
				TextView tv = (TextView) findViewById(R.id.nolist);
				tv.setText("No Lists...");
			} else {
				final TextView tv = (TextView) findViewById(R.id.nolist);
				tv.setText("");
				
				list_title.clear();
				list_time.clear(); 
				list_date.clear();
				
				final String messages[] = new String[list_index];

				JSONObject obj;
				String json;

				try {
					for (int i = 0; i < list_index; i++) {
						json = settings.getString(Integer.toString(i), "null");
						obj = new JSONObject(json);

						
						if (obj.get("list_title").toString().equals("")) {
							String temp = "";
							for (int j = 0; j < obj.getJSONArray("messages").length(); j++) {
								temp = obj.getJSONArray("messages").getString(j);
								// System.out.println("temp: " + temp);
								if (!temp.equals("")) {
									break;
								}
								j++;
							}
							if (temp.length() > 20) {
								temp = temp.substring(0, 20);// +"...";
//								for( int k = 20; k > 18; k--){
//									if(temp.charAt(k) == ' '){
//										temp = temp.substring(0, k);
//									}
//								}
								list_title.add(temp + "...");
								
							} else {
								list_title.add(temp.substring(0, temp.length()));
							}
						} else {
							list_title.add(obj.get("list_title").toString());
						}

						list_time.add(obj.getString("time"));
						list_date.add(obj.getString("date"));
						
						// System.out.println("json: "+ obj);
						// messages[i] =
						// obj.getJSONArray("messages").toString();

						// System.out.println("messages["+i+"]: "+ messages[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				caa = new CutomArrayAdapter(this,
						list_title, list_time, list_date);
				final ListView listview = (ListView) findViewById(R.id.listView);
				
				listview.setAdapter(caa);
				listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {
						// open intent with extra
						Intent intent = new Intent(getBaseContext(),
								NewNote.class);
						intent.putExtra("index", position);
						startActivityForResult(intent, requestCode);
					}
				});
				listview.setLongClickable(true);
				listview.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int pos, long id) {
						// TODO Auto-generated method stub
						AlertDialog.Builder adb2 = new AlertDialog.Builder(
								Home.this,
								AlertDialog.THEME_DEVICE_DEFAULT_DARK);
						adb2.setIcon(R.drawable.ic_launcher);

						ListView lst = new ListView(Home.this);
						String[] arr = { "Open", "Get Note Code", "Delete" };

						final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
								Home.this, R.layout.alert_dialog_text, arr);
						lst.setAdapter(arrayAdapter);

						adb2.setAdapter(arrayAdapter,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int position) {
										if (position == 0) {
											// open
											Intent intent = new Intent(
													getBaseContext(),
													NewNote.class);
											intent.putExtra("index", pos);
											//startActivity(intent);
											startActivityForResult(intent, requestCode);
										} else if (position == 1) {
											// show code
											show_code(pos);
										} else if (position == 2) {
											// delete
											AlertDialog.Builder adb = new AlertDialog.Builder(Home.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
											
											String chars = "Confirm delete?";
											SpannableString str = new SpannableString(chars);
											str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")), 0, chars.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											
											adb.setMessage(str);
											
											chars = "Yes";
											str = new SpannableString(chars);
											str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")), 0, chars.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											adb.setPositiveButton(str, new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													list_title.remove(pos);
													list_time.remove(pos);
													list_date.remove(pos);
													int list_index = settings.getInt("list_index", 0);
													for (int i = pos; i != list_index; i++) {
														if (list_index != 1) {
															
															String json = settings.getString(Integer.toString(i + 1),"null");
															settings.edit().remove(Integer.toString(i + 1)).commit();
															settings.edit().putString(Integer.toString(i),json).commit();

														} else {
															// System.out.println("i: "+i);
															settings.edit().remove(Integer.toString(i)).commit();
															String json = settings.getString(Integer.toString(i),"null");
															// System.out.println("Deleted. \n json: "+json);
															tv.setText("No Lists...");
															rl.removeView(listview);
														}
													}
													list_index = list_index - 1;
													settings.edit().putInt("list_index",list_index).commit();

													list_index = settings.getInt("list_index", -1);
													System.out.println("list_index: "+ list_index);

													caa.notifyDataSetChanged();
													Toast.makeText(Home.this, "Note deleted", Toast.LENGTH_LONG).show();
													return;
												}
											});
											
											chars = "No";
											str = new SpannableString(chars);
											str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")), 0, chars.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											adb.setNegativeButton(str, new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													dialog.dismiss();
													return;
												}
											});
											AlertDialog dialog2 = adb.show();
											dialog2.show();
										}
									}
								});
						adb2.show();

						return true;
					}
				});
				listview.setId(generateViewId());
				lv_id = listview.getId();
				System.out.println("New Id: " + lv_id);

				rl.removeView(listview);
				rl.addView(listview);

				View newList = rl.findViewById(R.id.newList);
				((RelativeLayout) newList.getParent()).removeView(newList);
				rl.addView(newList);

				View getList = rl.findViewById(R.id.getList);
				((RelativeLayout) getList.getParent()).removeView(getList);
				rl.addView(getList);
			}
		}

		super.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult()");
		
		if(requestCode == 0){
			if(resultCode == RESULT_OK){
				int result = data.getIntExtra("result", 0);
	            System.out.println("result: " + result);
	            if(result == 1){
	            	Toast.makeText(Home.this, "Saved", Toast.LENGTH_LONG).show();
	            }else if(result == 2){
	            	Toast.makeText(Home.this, "Updated.", Toast.LENGTH_LONG).show();
	            }else if(result == 404){
	            	Toast.makeText(Home.this, "Note Deleted.", Toast.LENGTH_LONG).show();
	            }
	        }
		}
    }
	
	public void showToast() {
		Toast.makeText(this, "Phone ID will be used.\n Change in Settings.",
				Toast.LENGTH_LONG).show();
	}

	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	/**
	 * Generate a value suitable for use in {@link #setId(int)}. This value will
	 * not collide with ID values generated at build time by aapt for R.id.
	 * 
	 * @return a generated ID value
	 */
	public static int generateViewId() {
		for (;;) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range
			// under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF)
				newValue = 1; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				return result;
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Read values from the "savedInstanceState"-object and put them in your
		// textview
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save the values you need from your textview into "outState"-object
		super.onSaveInstanceState(outState);
	}

	public void show_code(int position) {
		try {
			AlertDialog.Builder show_code = new AlertDialog.Builder(Home.this,
					AlertDialog.THEME_DEVICE_DEFAULT_DARK);
			show_code.setIcon(R.drawable.ic_launcher);

			String chars = "Copy & Share Code";
			SpannableString str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFACD")),
					0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			show_code.setTitle(str);

			JSONObject obj;
			String json;

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			json = settings.getString(Integer.toString(position), "null");
			obj = new JSONObject(json);
			String code = obj.getString("code");

			chars = code;
			str = new SpannableString(chars);
			str.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),
					0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			show_code.setMessage(str);
			// show_code.show();

			AlertDialog dialog = show_code.show();

			// Must call show() prior to fetching views
			TextView messageView = (TextView) dialog
					.findViewById(android.R.id.message);
			messageView.setGravity(Gravity.CENTER);
			messageView.setTextIsSelectable(true);
			messageView.setBackgroundColor(Color.WHITE);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class StoreUsername extends AsyncTask<Void, Void, Void> {

		String line = null;

		// declare other objects as per your need
		@Override
		protected void onPreExecute() {

		};

		@Override
		protected Void doInBackground(Void... params) {
			AccountManager manager = AccountManager.get(getBaseContext());
			Account[] accounts = manager.getAccountsByType("com.google");
			List<String> possibleEmails = new LinkedList<String>();

			for (Account account : accounts) {
				// TODO: Check possibleEmail against an email regex or treat
				// account.name as an email address only for certain
				// account.type values.
				possibleEmails.add(account.name);
			}

			if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
				String email = possibleEmails.get(0);
				String[] parts = email.split("@");
				if (parts.length > 0 && parts[0] != null)
					username = parts[0];
				else
					username = "?";
			} else
				username = "?";

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", username + " - " + manufacturer
					+ " - " + model);
			editor.putString("showMessage", "No");
			editor.commit();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

	}

	public class SelectData extends AsyncTask<Void, Void, Void> {

		String line = null;
		InputStream is = null;
		int found = -404;
		String json;
		String result;

		// System.out.println("list_title: "+ title);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		JSONObject obj;
		String pref_code = "";

		// declare other objects as per your need
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(Home.this, "", "");
			progressDialog.setCancelable(false);
			progressDialog.setContentView(R.layout.progressdialog);
			progressDialog.show();
		};

		@Override
		protected Void doInBackground(Void... params) {
			/*
			 * Check if the EditTexts are empty
			 */
			System.out.println("In background...");
			
			try{
				int list_index = settings.getInt("list_index", 0);
				for (int index =0; index < list_index; index++){
					json = settings.getString(Integer.toString(index), "null");
					obj = new JSONObject(json);
					pref_code = obj.getString("code");
					//System.out.println("pref_code: " + pref_code);
					if(pref_code.equals(code)){
						found = 2;
						return null;
					}
					
				}
			}catch(Exception ex){
				
			}
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("code", code));

			try {
				long startTime = System.nanoTime();
				HttpParams para = new BasicHttpParams();
				//this how tiny it might seems, is actually absoluty needed. otherwise http client lags for 2sec.
				HttpProtocolParams.setVersion(para, HttpVersion.HTTP_1_1);
				//HttpClient httpClient = new DefaultHttpClient(params);
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://ayokunle.netsoc.ie/notepad_sync/select.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				long endTime = System.nanoTime();
				System.out.println("It took " + (endTime - startTime)/ 1000000000.0 + " seconds.");
				Log.e("pass 1", "Connection success ");
			} catch (Exception e) {
				Log.e("Fail 1", e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				result = sb.toString();
				if (result.equals("0 results")) {
					System.out.println("Error - Result: " + result);
					return null;
				}
				found = 1;
				//Log.e("pass 2", "Result: " + result);
			} catch (Exception e) {
				Log.e("Fail 2", e.toString());
			}

			
			int list_index = settings.getInt("list_index", 0);
			
			try {

				json = result;
				obj = new JSONObject(json);

				editor.putString(Integer.toString(list_index), json);
				list_index = list_index + 1;
				System.out.println("list_index: " + list_index);
				editor.putInt("list_index", list_index);
				editor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("Out background...");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// update arraylist
			progressDialog.dismiss();
			if(found == 1){
				Toast.makeText(Home.this, "New note added.", Toast.LENGTH_LONG).show();
				onResume();
			}else if(found == -404){
				Toast.makeText(Home.this, "Note not found.", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(Home.this, "Note already exists.", Toast.LENGTH_LONG).show();
			}
			
		}

	}

	public class RefreshNotes extends AsyncTask<Void, Void, Void> {

		JSONObject obj;
		String json = "", pref_code = "", line = "", result = "";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		InputStream is = null;
		int list_index = settings.getInt("list_index", 0);
		
		// declare other objects as per your need
		@Override
		protected void onPreExecute() {
			//Toast.makeText(Home.this, "This may take a while...", Toast.LENGTH_LONG).show();
			progressDialog = ProgressDialog.show(Home.this, "", "");
			progressDialog.setCancelable(false);
			progressDialog.setContentView(R.layout.progressdialog);
			progressDialog.show();
		};

		@Override
		protected Void doInBackground(Void... params) {
			try{
				
				for (int index =0; index < list_index; index++){
					json = settings.getString(Integer.toString(index), "null");
					obj = new JSONObject(json);
					pref_code = obj.getString("code");
					System.out.println("pref_code: " + pref_code);
					
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("code", pref_code));

					try {
						long startTime = System.nanoTime();
						HttpParams para = new BasicHttpParams();
						//this how tiny it might seems, is actually absoluty needed. otherwise http client lags for 2sec.
						HttpProtocolParams.setVersion(para, HttpVersion.HTTP_1_1);
						//HttpClient httpClient = new DefaultHttpClient(params);
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://ayokunle.netsoc.ie/notepad_sync/select.php");
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						long endTime = System.nanoTime();
						System.out.println("It took " + (endTime - startTime)/ 1000000000.0 + " seconds.");
						Log.e("pass 1", "Connection success ");
					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "iso-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						is.close();
						result = sb.toString();
						if (result.equals("0 results")) {
							System.out.println("Error - Result: " + result);
							System.out.println("insertData()");
							insertData();
							return null;
						}
						//Log.e("pass 2", "Result: " + result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());
					}

					
					try {

						json = result;
						obj = new JSONObject(json);

						editor.putString(Integer.toString(index), json);
						System.out.println("list_index: " + list_index);
						editor.putInt("list_index", list_index);
						editor.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return null;
		}

		public void insertData(){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("code", pref_code));
			nameValuePairs.add(new BasicNameValuePair("json", json));

			try {
				long startTime = System.nanoTime();
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://ayokunle.netsoc.ie/notepad_sync/insert.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				long endTime = System.nanoTime();
				System.out.println("It took " + (endTime - startTime)/ 1000000000.0 + " seconds.");
				Log.e("pass 1", "Connection success ");
				
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				is.close();
				String result = sb.toString();
				if (!result.equals("Success!")) {
					System.out.println("Error - Result: " + result);
				}
				Log.e("pass 2", "Result: " + result);
			} catch (Exception e) {
				Log.e("Fail 1", e.toString());
			}
		}
		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
				
			if(list_index != 0){
				if(onCreate != 0){
					Toast.makeText(Home.this, "Refresh Completed.", Toast.LENGTH_LONG).show();
				}
				onCreate++;
				Home.this.onResume();
			}else{
				if(onCreate != 0){
					Toast.makeText(Home.this, "Nothing to refresh.", Toast.LENGTH_LONG).show();
				}
				onCreate++;
			}
		}

	}
}