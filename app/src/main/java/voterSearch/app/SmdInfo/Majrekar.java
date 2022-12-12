package voterSearch.app.SmdInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Majrekar extends Activity {

	
	//String Email_Id = "voter.details@gmail.com";
	//String Password_for_Email = "tgb159mju";
	String Email_Id = "voterinfo4u@gmail.com";
	String Password_for_Email = "shree123";
	//String Email_Id = "dhanashree1113@gmail.com";
	//String Password_for_Email = "sumanvishnu";

	String Password_for_Next = "dev99";
	String Image_Name_for_mail = "image1.jpg";

	String vishansabhas = "1@Dombivali";
	//--------------------------------------------------------------------------------------
	
	SharedPreferences master_pref;
	Editor editor;
	TextView txt1,txt2,txt3;
	String vidhanNo,SMS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		//---------------------------------------------------------------------------------------------------------------------------------	
			master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);
			editor = master_pref.edit();
		//---------------------------------------------------------------------------------------------------------------------------------		
			editor.putString("Email_Id", Email_Id);
			editor.putString("Password_for_Email", Password_for_Email);
			editor.putString("Password_for_Next", Password_for_Next);
			editor.putString("Image_Name_for_mail", Image_Name_for_mail);
			editor.putString("vishansabhas", vishansabhas);
			editor.commit();
		 
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		
		Thread td = new Thread()
		{
			@Override
			public void run ()
			{
				super.run();
				try {
					sleep(700);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
					Intent inte = new Intent(Majrekar.this,First_Page.class);
					startActivity(inte);
					//mDbAdapter.close();
					finish();
				}
			
		};td.start();
	}



}
