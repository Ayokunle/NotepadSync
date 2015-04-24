package shopping.list;

import android.app.Activity;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

public class CheckedEditable extends Activity{
	
	CheckBox cbox;
    EditText etext;
    
	CheckedEditable(Context context){
		cbox = new CheckBox(context);
	    etext = new EditText(context);
	}
	
}
