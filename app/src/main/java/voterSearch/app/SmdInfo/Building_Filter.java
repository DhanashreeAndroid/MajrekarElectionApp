package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Building_Filter extends Activity{

	final Context context  = this;
	public ProgressDialog progress;
	TextView txt1, txt2, txt3, txt4;
	Button btnPart, btnFullVidhan;
	
	SharedPreferences pref;
	Editor editor;

	Spinner mSpnVidhasabha;
	VidhansabhaVo[] mVidhanList;
	String mVidhansabhaNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.building_filter);
		
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		btnPart = (Button) findViewById(R.id.btnInPart);
		btnFullVidhan = (Button) findViewById(R.id.btnInFullvidhan);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
		 pref = getApplicationContext().getSharedPreferences("Search_type", 0);
		 editor = pref.edit();
		 
		final Typeface font1  = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
		txt1.setTypeface(font1);
		
		final Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);

		mSpnVidhasabha = (Spinner) findViewById(R.id.spnFeedbackType);
		String[] arrVidhan;
		SharedPreferences master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);
		String vidhan = master_pref.getString("vishansabhas", "");
		if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
			arrVidhan = vidhan.split(",");
			mVidhanList = new VidhansabhaVo[arrVidhan.length];
			for(int i =0; i< arrVidhan.length; i ++) {
				VidhansabhaVo vo5 = new VidhansabhaVo();
				vo5.setID(arrVidhan[i].substring(0,arrVidhan[i].indexOf("@")));
				vo5.setName(arrVidhan[i].replace("@", " "));
				mVidhanList[i] = vo5;
			}
		}else{
			mVidhanList = new VidhansabhaVo[1];
			VidhansabhaVo vo5 = new VidhansabhaVo();
			vo5.setID(vidhan.substring(0,vidhan.indexOf("@")));
			vo5.setName(vidhan.replace("@", " "));
			mVidhanList[0] = vo5;
		}
		SpinAdapter adapter = new SpinAdapter(this,
				android.R.layout.simple_spinner_item,mVidhanList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnVidhasabha.setAdapter(adapter);
		mSpnVidhasabha.setSelection(0);

		mSpnVidhasabha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mVidhansabhaNo = mVidhanList[position].getID();
				mSpnVidhasabha.setSelection(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnPart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(Building_Filter.this, Building_Part_1.class);
				i.putExtra("wardNo", mVidhansabhaNo);
				startActivity(i);
				
			}
		});
		
		btnFullVidhan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				editor.putString("type", "building_fullvidhasabha");
				
				editor.commit();
				
				Intent i = new Intent(Building_Filter.this, Alphabet_Listing.class);
				i.putExtra("wardNo", mVidhansabhaNo);
				startActivity(i);
				
			}
		});
		
		
		
	}

	
}
