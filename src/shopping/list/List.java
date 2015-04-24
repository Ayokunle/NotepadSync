package shopping.list;

import java.security.SecureRandom;
import java.util.ArrayList;

public class List {

	final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String NUM = "0123456789";
    
    SecureRandom  srnd = new SecureRandom ();
    
	int access_code;
	ArrayList<String> list;
	String list_name ;
	
	List(int access_code , String name){
		if(access_code == 0){
			
		}else{
			//generate access code;
			StringBuilder sb = new StringBuilder( 5 );
	        for( int i = 0; i < 2; i++ ) 
	            sb.append( ALPHA.charAt( srnd.nextInt(ALPHA.length()) ) );
	        for( int i = 0; i < 3; i++ ) 
	            sb.append( NUM.charAt( srnd.nextInt(NUM.length()) ) );
	        
	        access_code = Integer.parseInt( sb.toString());
	        list_name = name;
	        
	        
	       
		}
	}
	
	public long getId() {
		return 0;
	}

}
