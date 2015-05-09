package shopping.list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CutomArrayAdapter extends ArrayAdapter<String> {

	
	private final Context context;
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<String> times = new ArrayList<String>();
	ArrayList<String> dates = new ArrayList<String>();
 
	public CutomArrayAdapter(Context context, ArrayList<String> titles, ArrayList<String> times, ArrayList<String> dates) {
		super(context, R.layout.list_title_time, titles);
		this.context = context;
		this.titles = titles;
		this.times = times;
		this.dates = dates;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_title_time, parent, false);
		TextView label = (TextView) rowView.findViewById(R.id.label);
		TextView time = (TextView) rowView.findViewById(R.id.time);
		
		label.setText(titles.get(position));
		//if more than 24h, use date
		try{
			System.out.println("dates: " + dates.size());
			System.out.println("position: " + position);
			if(dates.get(position).equalsIgnoreCase(
				new SimpleDateFormat("dd/MM/yyyy").format(new Date()).toString()
				)){
				time.setText(times.get(position));	
			}else{
				time.setText(dates.get(position));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
 
//		// Change icon based on name
//		String s = titles[position];
// 
//		System.out.println(s);
		
		return rowView;
	}
}