package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Alpha_VoterList_Types extends Activity {

	TextView txt1, txt2, txt3, txt4;
	Button btnSurname, btnName, btnBuilding, btnAge;
	
	SharedPreferences pref;
	Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alpha_voterlist_types);
		
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
		btnSurname = (Button) findViewById(R.id.btnAvlSurname);
		btnName = (Button) findViewById(R.id.btnAvlName);
		btnBuilding = (Button) findViewById(R.id.btnAvlBuilding);
		btnAge = (Button) findViewById(R.id.btnAvlAge);
		
		final Typeface font1  = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
		txt1.setTypeface(font1);
		
		final Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);
		
		pref = getApplicationContext().getSharedPreferences("Alpha_VoterList_Types", 0);
		editor = pref.edit();
		
		btnSurname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				editor.putString("Filter_Type", "Surname");
				editor.commit();
				Intent i = new Intent(Alpha_VoterList_Types.this, Alpha_VoterList_Filter.class);
				startActivity(i);
				
			}
		});
		
		btnName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				editor.putString("Filter_Type", "Name");
				editor.commit();
				Intent i = new Intent(Alpha_VoterList_Types.this, Alpha_VoterList_Filter.class);
				startActivity(i);
				
			}
		});

		btnBuilding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				editor.putString("Filter_Type", "Building");
				editor.commit();
				Intent i = new Intent(Alpha_VoterList_Types.this, Alpha_VoterList_Filter.class);
				startActivity(i);
				
			}
		});		
		
		btnAge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				editor.putString("Filter_Type", "Age");
				editor.commit();
				Intent i = new Intent(Alpha_VoterList_Types.this, Alpha_VoterList_Filter.class);
				startActivity(i);
				
			}
		});
	}

}
