package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Building_Part_1  extends Activity{

	TextView txt1,txt2,txt3,txt4;
	EditText edtPartNo;
	Button btnNext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.building_part_1);
		
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
		edtPartNo = (EditText) findViewById(R.id.edtPartNo);
				
		btnNext = (Button) findViewById(R.id.btnNext);
		
		final Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(Building_Part_1.this, Building_Part_2.class);
				i.putExtra("partno", edtPartNo.getText().toString());
				i.putExtra("wardNo", getIntent().getExtras().getString("wardNo"));
				startActivity(i);
					
			}
		});
	}
}
