package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Search_Details_Part_Sr_Filter extends Activity{

	final Context context = this;
	public ProgressDialog progressDialog;
	DBAdapter mDbAdapter;
	TextView txt1,txt2,txt3,txt4;
	EditText edtSrNo, edtPartNo;
	Button btnNext;
	Spinner mSpnVidhasabha;
	VidhansabhaVo[] mVidhanList;
	String mVidhansabhaNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		setContentView(R.layout.search_details_part_sr_filter);
		
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
		btnNext = (Button) findViewById(R.id.btnPartSrNext);
		
		final Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String sr =  edtSrNo.getText().toString();
				String part =  edtPartNo.getText().toString();
				Cursor cursor = mDbAdapter.fetchVoterDetailsByPartSr(sr,part, mVidhansabhaNo);
				
				if(sr.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please Enter Both Number.", Toast.LENGTH_SHORT).show();
				}
				else if(cursor != null && cursor.moveToFirst())
				{
					if(cursor.getString(5).equalsIgnoreCase("D"))
					{
						Intent i = new Intent(Search_Details_Part_Sr_Filter.this, Search_Details_For_Deleted.class);
						i.putExtra("Last", cursor.getString(3));
						i.putExtra("First", cursor.getString(4));
						i.putExtra("Sr_no",cursor.getString(2));
						i.putExtra("table",cursor.getString(11));
						i.putExtra("PartNo", cursor.getString(1));
					    i.putExtra("ward_no", cursor.getString(0)) ;
						i.putExtra("Search_type","Part_Sr_Search" );
						startActivity(i); 
					}
					else
					{
						Intent i = new Intent(Search_Details_Part_Sr_Filter.this, Search_Details_Part_Sr.class);
					   	i.putExtra("PartNo", cursor.getString(1));
						i.putExtra("Sr_no", sr);
						i.putExtra("ward_no", cursor.getString(0)) ;
						startActivity(i);
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Record not available for this details.", Toast.LENGTH_SHORT).show();
				}
				
				cursor.close();
				
			}
		});
	}

	
}
