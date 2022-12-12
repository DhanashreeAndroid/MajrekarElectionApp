package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Alpha_VoterList_Filter extends Activity {
	
	TextView txt1, txt2, txt3, txt4;
	EditText edtSrNo, edtPartNo, edtAgefrom, edtAgeTo;
	Button btnNext, btnCombineNext;
	LinearLayout AgeLinearLayout;
	String Filter_Type;
	Spinner mSpnVidhasabha;
	VidhansabhaVo[] mVidhanList;
	String mVidhansabhaNo;

	SharedPreferences pref;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alpha_voterlist_filter);
		
				
		pref = getApplicationContext().getSharedPreferences("Alpha_VoterList_Types", 0);
				
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
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
		edtSrNo = (EditText) findViewById(R.id.edtSrNo);
		edtPartNo = (EditText) findViewById(R.id.edtPartNo);
		edtAgefrom = (EditText) findViewById(R.id.edtAgeFrom);
		edtAgeTo = (EditText) findViewById(R.id.edtAgeTO);
		
		btnNext = (Button) findViewById(R.id.btnNext);
		btnCombineNext  = (Button) findViewById(R.id.btnNext2);
		
		AgeLinearLayout = (LinearLayout) findViewById(R.id.AgeLinearLayout);
		
		final Typeface font1  = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
		txt1.setTypeface(font1);
		
		final Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);
		
		Filter_Type = pref.getString("Filter_Type","");
		
		if(Filter_Type.equals("Surname"))
		{
			txt4.setText("Surname Wise Alphabetical Voter List");
			AgeLinearLayout.setVisibility(View.GONE);
		}
		else if(Filter_Type.equals("Name"))
		{
			txt4.setText("Name Wise Alphabetical Voter List");
			AgeLinearLayout.setVisibility(View.GONE);
		}
		else if(Filter_Type.equals("Building"))
		{
			txt4.setText("Building Wise Alphabetical Voter List");
			AgeLinearLayout.setVisibility(View.GONE);
		}
		else if(Filter_Type.equals("Age"))
		{
			txt4.setText("Age Wise Alphabetical Voter List");
			AgeLinearLayout.setVisibility(View.VISIBLE);
		}
			
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(Filter_Type .equals("Age"))
				{
					Intent i = new Intent(Alpha_VoterList_Filter.this, Alpha_VoterList_Display.class);
					i.putExtra("Part_Nos", "PartNo-" + edtPartNo.getText().toString());
					i.putExtra("Sr_no", edtSrNo.getText().toString());
					i.putExtra("ward_no", mVidhansabhaNo) ;
					i.putExtra("vidhansabhaName",  mVidhanList[mSpnVidhasabha.getSelectedItemPosition()].getName());
					i.putExtra("AgeRange", edtAgefrom.getText().toString() + "-" + edtAgeTo.getText().toString());
					startActivity(i);
				}
				else
				{
					Intent i = new Intent(Alpha_VoterList_Filter.this, Alpha_VoterList_Display.class);
					i.putExtra("Part_Nos", "PartNo-" + edtPartNo.getText().toString());
					i.putExtra("Sr_no", edtSrNo.getText().toString());
					i.putExtra("ward_no", mVidhansabhaNo) ;
					i.putExtra("vidhansabhaName",  mVidhanList[mSpnVidhasabha.getSelectedItemPosition()].getName());
					i.putExtra("AgeRange", "");
					startActivity(i);
				}
				
			}
		});
		
		btnCombineNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(Alpha_VoterList_Filter.this, Alpha_VoterList_Combine_Filter.class);
				startActivity(i);
				
			}
		});
		
	}

 
}
