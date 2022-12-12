package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Alphabet_Listing extends Activity {
	
	TextView txt1,txt2,txt3,txt4,txt5;
	ListView list;
	String alpha;
	String vidhanNo, SMS;
	
	static final String[] alphabets = new String[] {"A","B","C","D","E","F",
													"G","H","I","J","K","L",
													"M","N","O","P","Q","R",
													"S","T","U","V","W","X",
													"Y","Z"};
	
	static final String[] alphabets2 = new String[] {"A","B","C","D","E","F",
													"G","H","I","J","K","L",
													"M","N","O","P","Q","R",
													"S","T","U","V","W","X",
													"Y","Z","Others"};
	
	SharedPreferences pref;
	String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alphabet_listing);
		
		pref = getApplicationContext().getSharedPreferences("Search_type", 0);
				
		type = pref.getString("type", "");
					
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt5 = (TextView) findViewById(R.id.txt5);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
		final Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);
		txt5.setTypeface(font2);
		
		list = (ListView) findViewById(R.id.list_alpha);
		
		if(type.equals("surname"))
		{
			txt4.setText("Select first alphabet of Surname for");
		}
		else if(type.equals("name"))
		{
			txt4.setText("Select first alphabet of Name for");
		}
		else
		{
			txt4.setText("Select first alphabet of Address for");
		}
		//--------------------------------------------------------------------------------------------------------------
		if(type.equals("building_fullvidhasabha"))
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_alphabet, alphabets2);
		    list.setAdapter(adapter);
		}
		else
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_alphabet, alphabets);
		    list.setAdapter(adapter);
		}
		
		

	list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			if(type.equals("building_fullvidhasabha"))
			{
				if(list.getItemAtPosition(position).equals("Others"))
				{
					Intent i = new Intent(Alphabet_Listing .this, Building_Part_2.class);
					i.putExtra("partno","Blank");
					i.putExtra("wardNo", getIntent().getExtras().getString("wardNo"));
					startActivity(i);
				}
				else
				{
					alpha = ((TextView) view).getText().toString();
					Intent i = new Intent (Alphabet_Listing.this,Building_Fullvidhan_2.class);
					i.putExtra("alphabet", alpha);
					i.putExtra("partno","Blank");
					i.putExtra("wardNo", getIntent().getExtras().getString("wardNo"));
					startActivity(i);
				}
				
			}
			else
			{
				alpha = ((TextView) view).getText().toString();
				Intent i = new Intent (Alphabet_Listing.this,Voter_Listing.class);
				i.putExtra("alphabet", alpha);
				startActivity(i);
			}
			
			
		}
	});
	
	}
	

}
