package shopping.list;

import java.text.SimpleDateFormat;
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
	private final String[] titles;
	private final String[] times;
	private final String[] dates;
 
	public CutomArrayAdapter(Context context, String[] titles, String[] times, String[] dates) {
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
		
		label.setText(titles[position]);
		//if more than 24h, use date
		if(dates[position].equalsIgnoreCase(
				new SimpleDateFormat("dd/MM/yyyy").format(new Date()).toString()
				)){
			time.setText(times[position]);	
		}else{
			time.setText(dates[position]);
		}
 
//		// Change icon based on name
//		String s = titles[position];
// 
//		System.out.println(s);
		
		return rowView;
	}
}