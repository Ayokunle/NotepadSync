package shopping.list;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class NewNote extends Activity {

	int len = 25;
	final ArrayList<CheckBox > cbox = new ArrayList<CheckBox>();
	final ArrayList<EditText > etext = new ArrayList<EditText>();
	boolean new_note = true, editted = false;
	String[] text_check;
	String PREFS_NAME = "NoteSync";
	int index = -1;
	EditText list_title;
	String code = "";
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		if (index != -1) {// not new
			try {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				json = settings.getString(Integer.toString(index), "null");
				obj = new JSONObject(json);
				// System.out.println("json: "+ obj);
				JSONArray listdata = new JSONArray();

				listdata = obj.getJSONArray("messages");

				if (listdata != null) {
					for (int i = 0; i < listdata.length(); i++) {
						list.add(listdata.get(i).toString());
					}
				}
				code = obj.getString("code");
				// System.out.println("code: "+code);
				new_note = false;
			} catch (JSONException e) {
				// e.printStackTrace();
			}
		}

		LinearLayout.LayoutParams lp;

		LinearLayout ll = (LinearLayout) findViewById(R.id.newList_layout); // newList_layout
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(lp);

		ScrollView sc = new ScrollView(this);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		sc.setLayoutParams(lp);

		LinearLayout ll2 = new LinearLayout(this);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		ll2.setOrientation(LinearLayout.VERTICAL);
		ll2.setLayoutParams(lp);

		int al_i = 0;
		for (int i = 0; i < len; i++) {
			LinearLayout temp_ll = new LinearLayout(this);
			lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			temp_ll.setLayoutParams(lp);

			cbox.add(i, new CheckBox(this));
			etext.add(i, new EditText(this));

			etext.get(i).setFocusableInTouchMode(true);
			cbox.get(i).setAlpha((float) 1.0);
			cbox.get(i).setEnabled(true);

			if (new_note == true) {
				etext.get(i).setText("");
			} else {
				try {
					if (al_i % 2 == 0) {// if even number
						etext.get(i).setText(list.get(al_i));
						// System.out.println("list.get(al_i  ): "+list.get(al_i));
						// System.out.println("list.get(al_i+1): "+list.get(al_i+1));
						if (list.get(al_i + 1).equals("Checked")) {
							cbox.get(i).setAlpha((float) 1.0);
							cbox.get(i).setEnabled(true);
							cbox.get(i).setChecked(true);
							etext.get(i).setFocusableInTouchMode(true);
							etext.get(i).setPaintFlags(etext.get(i).getPaintFlags()
									| Paint.STRIKE_THRU_TEXT_FLAG);
						} else {
							cbox.get(i).setAlpha((float) 1.0);
							cbox.get(i).setEnabled(true);
							cbox.get(i).setChecked(false);
							etext.get(i).setFocusableInTouchMode(true);
						}
					} else {
						al_i++;
						etext.get(i).setText(list.get(al_i));
						// System.out.println("list.get(al_i  ): "+list.get(al_i));
						// System.out.println("list.get(al_i+1): "+list.get(al_i+1));
						if (list.get(al_i + 1).equals("Checked")) {
							cbox.get(i).setAlpha((float) 1.0);
							cbox.get(i).setEnabled(true);
							cbox.get(i).setChecked(true);
							etext.get(i).setFocusableInTouchMode(true);
							etext.get(i).setPaintFlags(etext.get(i).getPaintFlags()
									| Paint.STRIKE_THRU_TEXT_FLAG);
						} else {
							cbox.get(i).setAlpha((float) 1.0);
							cbox.get(i).setEnabled(true);
							cbox.get(i).setChecked(false);
							etext.get(i).setFocusableInTouchMode(true);
						}
					}
				} catch (Exception e) {
					// e.printStackTrace();
					etext.get(i).setText("");
				}
			}
			etext.get(i).setBackground(null);
			etext.get(i).setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
					| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			etext.get(i).setSelection(etext.get(i).getText().length());

			final int x = i;

			lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			cbox.get(i).setLayoutParams(lp);
			cbox.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
						etext.get(x).setPaintFlags(etext.get(x).getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
					} else {
						etext.get(x).setPaintFlags(etext.get(x).getPaintFlags()
								& (~Paint.STRIKE_THRU_TEXT_FLAG));
					}
				}
			});

			lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.weight = 80;
			etext.get(i).setLayoutParams(lp);

			final int y = i;
			etext.get(i).setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {

					try {
						if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

							etext.get(y+1).requestFocus();

							return true;

						} else if ((event.getAction() == KeyEvent.ACTION_DOWN)
								&& (keyCode == KeyEvent.KEYCODE_DEL)) {

							System.out.println("DEL pressed: "
									+ etext.get(y).getText().toString());
							if (etext.get(y).getText().length() == 1
									|| etext.get(y).getText().length() == 0) {
								System.out.println("No text.");
								etext.get(y).setText("");
								etext.get(y -1).requestFocus();// setSelection(etext[y+1].getText().length());
								cbox.get(y).setChecked(false);
								return true;
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}
			});

			etext.get(i).addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable mEdit) {
					editted = true;
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}
			});

			cbox.get(i).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					editted = true;
				}
			});

			temp_ll.addView(etext.get(i));
			temp_ll.addView(cbox.get(i));

			al_i++;

			ll2.addView(temp_ll);

			View ruler = new View(this);
			ruler.setBackgroundColor(Color.GRAY);
			ll2.addView(ruler, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 2));
		}

		sc.addView(ll2);
		ll.addView(sc);
		// ll.setBackgroundColor(0x00ffff);
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
		list_title = (EditText) v.findViewById(R.id.list_name_edit);
		Intent intent = getIntent();
		index = intent.getIntExtra("index", -1);
		String json = "";
		JSONObject obj;
		String title = "";

		try {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			json = settings.getString(Integer.toString(index), "null");
			obj = new JSONObject(json);
			// System.out.println("json: "+ obj);

			title = obj.get("list_title").toString();
			// System.out.println("title: "+ title);
			if (!title.equals("")) {
				list_title.setText(title);
			}

			new_note = false;
		} catch (JSONException e) {
			// e.printStackTrace();
		}

		int maxLength = 30;
		list_title.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxLength) });
		list_title.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		list_title.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable mEdit) {
				editted = true;

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		/** Setting an action listener */
		// txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
		//
		// @Override
		// public boolean onEditorAction(TextView v, int actionId, KeyEvent
		// event) {
		// Toast.makeText(getBaseContext(), "Search : " + v.getText(),
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });
		return super.onCreateOptionsMenu(menu);
	}

	public String shuffle(String input) {
		List<Character> characters = new ArrayList<Character>();
		for (char c : input.toCharArray()) {
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(input.length());
		while (characters.size() != 0) {
			int randPicker = (int) (Math.random() * characters.size());
			output.append(characters.remove(randPicker));
		}
		System.out.println(output.toString());
		return output.toString();
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

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
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
			// openSettings();
			Log.d("Action", "share_list");
			return true;
		case R.id.view_log:
			// openSettings();
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

	public void goBack() {
		try {
			InsertData task = new InsertData();
			task.execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Override
	// public void onPause(){

	public class InsertData extends AsyncTask<Void, Void, Void> {

		String line = null;
		InputStream is = null;
		boolean done = false;
		boolean empty = true;
		String text = "";
		String checkbox = "";
		String title = list_title.getText().toString();
		String json;
		int tries = 0;

		// System.out.println("list_title: "+ title);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		Pattern p = Pattern.compile("[a-zA-Z0-9]");
		Matcher m;

		// declare other objects as per your need
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(NewNote.this, "", "");
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
			for (int i = 0; i < len; i++) {
				text = etext.get(i).getText().toString();

				m = p.matcher(text);
				// System.out.println("1. text: " + text);
				if (m.find()) {
					// System.out.println("2. text: " + text);
					empty = false;
				}
			}
 
			// if the title is not empty
			if (!list_title.getText().toString().equals("")) {
				empty = false;
			}

			// if both are empty, finish
			System.out.println("empty: " + empty);
			if (empty == true) {
				// System.out.println("after finish()" );
			} else {
				System.out.println("Not empty");
				if (editted == false) {
					System.out.println("Not editted");
				}
			}
			if (editted == true) {
				System.out.println("Editted");
				GenCode();
				postData();
				int list_index = settings.getInt("list_index", 0);

				if (new_note == true) {
					editor.putString(Integer.toString(list_index), json);
					list_index = list_index + 1;
					// System.out.println("list_index: " + list_index);
					editor.putInt("list_index", list_index);
				} else {
					if (editted == true) {
						// System.out.println("index: " + index);
						editor.putString(Integer.toString(index), json); // use original index of the note
					}
				}
				editor.commit();
			}
			System.out.println("Out background...");
			return null;
		}

		public void postData() {
			tries++;
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("code", code));
			nameValuePairs.add(new BasicNameValuePair("json", json));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://ayokunle.netsoc.ie/notepad_sync/insert.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				Log.e("pass 1", "Connection success ");
				getResult();
			} catch (Exception e) {
				Log.e("Fail 1", e.toString());
			}
		}

		public void getResult() {
			try {
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
					if(tries <= 3){//only try 3 times or less
						GenCode();
						postData();
					}
				}
				Log.e("pass 2", "Result: " + result);
			} catch (Exception e) {
				Log.e("Fail 2", e.toString());
			}
		}

		public void GenCode() {
			/*
			 * By now, it is clear that the note is not empty and it has been
			 * editted
			 */
			try {
				JSONObject obj = new JSONObject();
				JSONArray list = new JSONArray();
				JSONArray log = new JSONArray();

				String username = settings.getString("username", "null");

				JSONObject old_obj;
				JSONArray old_log = null;

				if (new_note == true) {
					code = randomString();
					obj.put("code", code);
				} else {
					obj.put("code", code);
					json = settings.getString(Integer.toString(index), "null");
					old_obj = new JSONObject(json);
					// System.out.println("->old_obj: "+old_obj);
					old_log = old_obj.getJSONArray("log");
				}

				for (int i = 0; i < len; i++) {
					text = etext.get(i).getText().toString();

					// System.out.println(text);

					if (cbox.get(i).isChecked()) {
						checkbox = "Checked";
					} else {
						checkbox = "NotChecked";
					}

					list.put(text);
					list.put(checkbox);
				}

				if (new_note == true) {
					log.put(username);
					log.put(new SimpleDateFormat("hh:mm a").format(new Date()));
					log.put(new SimpleDateFormat("dd/MM/yyyy")
							.format(new Date()));
					obj.put("log", log);
				} else {
					old_log.put(username);
					old_log.put(new SimpleDateFormat("hh:mm a")
							.format(new Date()));
					old_log.put(new SimpleDateFormat("dd/MM/yyyy")
							.format(new Date()));
					obj.put("log", old_log);
				}

				obj.put("messages", list);

				obj.put("time",
						new SimpleDateFormat("hh:mm a").format(new Date()));
				obj.put("date",
						new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

				obj.put("list_title", title);

				System.out.println("json: " + obj.toString());
				json = obj.toString();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		/*
		 * This method randomly generates 5 alphabetic chars and 4 number
		 */
		final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String NUM = "0123456789";

		SecureRandom srnd = new SecureRandom();

		public String randomString() {
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < 2; i++)
				sb.append(ALPHA.charAt(srnd.nextInt(ALPHA.length())));
			for (int i = 0; i < 3; i++)
				sb.append(NUM.charAt(srnd.nextInt(NUM.length())));

			return shuffle(sb.toString());
		}

		@Override
		protected void onPostExecute(Void result) {
//			if (new_note == true) {
//				Toast.makeText(NewNote.this, "Saved", Toast.LENGTH_LONG).show();
//			} else {
//				if (editted == true) {
//					Toast.makeText(NewNote.this, "Updated", Toast.LENGTH_LONG)
//							.show();
//				}
//			}
			//

			progressDialog.dismiss();
			System.out.println("Committed.");
			//if(done == true)
			finish();
		}

	}
}