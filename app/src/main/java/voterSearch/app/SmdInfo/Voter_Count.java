package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Voter_Count extends Activity {
	AutoCompleteTextView actSurname, actName;
	Button btnSurnameCount, btnNameCount;
	String table = "FullVidhansabha";
	DBAdapter mDbAdapter;
	final Context context = this;
	String alpha ;
	String strVidhan;
	TextView txt1,txt2,txt3;

	ArrayAdapter<String> dataAdapter;
	Spinner mSpnVidhasabha;
	VidhansabhaVo[] mVidhanList;
	String mVidhansabhaNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		setContentView(R.layout.voter_count);
		
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		actSurname = (AutoCompleteTextView) findViewById(R.id.actName);
		btnSurnameCount = (Button) findViewById(R.id.btnCount);
		actSurname.setSingleLine();
		
		Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		
		Bundle extras=getIntent().getExtras();
		strVidhan = extras.getString("Vidhan");

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
				if(!TextUtils.isEmpty(alpha)) {
					//table = "Stable" + alpha;
					dataAdapter.clear();
					dataAdapter.notifyDataSetChanged();
					displayVoterName(table);
					actSurname.setAdapter(dataAdapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line);
		
		//---------------------------------------------------------------------------------------------------------------------------------	
				actSurname.addTextChangedListener(new TextWatcher() {
			 		 
			  	    @Override
			  	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
			  	        // When user changed the Text
			  	    }
			  	 
			  	    @Override
			  	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			  	            int arg3) {
			  	        // TODO Auto-generated method stub
			  	 	  	    }
			  	 
			  	    @Override
			  	    public void afterTextChanged(Editable s) {
			  	        // TODO Auto-generated method stub
			  	    	
			  	    	if (s.toString().length() == 1)
			  	    	{
			  	    		alpha = s.toString();
			  	    		//table ="Stable" + alpha;
			  	    		displayVoterName(table);
			  	  			actSurname.setAdapter(dataAdapter);
			  	    	}
			  	     }
			  	    
			  	 });  
		
				//---------------------------------------------------------------------------------------------------------------------------------	  
				  btnSurnameCount.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							int count = 0;
							Cursor cursor = mDbAdapter.fetchVoterSurNameForCount(table,mVidhansabhaNo,  actSurname.getText().toString());
							count = cursor.getCount();
							showAlertDialog(Voter_Count.this, mVidhanList[mSpnVidhasabha.getSelectedItemPosition()].getName() , actSurname.getText().toString().toUpperCase() + " Total Voters " + count);
						}
					});


	}
	
	 private void displayVoterName(String table) 
	 {
		  		   
		   Cursor cursor = mDbAdapter.fetchVoterNameForBindingToACT(table, mVidhansabhaNo);
		   
		   if (cursor.moveToFirst()) {
				do {
					
					String last = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_LASTNAME));
					
					dataAdapter.add(last);
					dataAdapter.notifyDataSetChanged();
					
			   	   } while (cursor.moveToNext());
				
			} 
			cursor.close();
		  
	 }
	 
	 @SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message) 
	 {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	 
	        // Setting Dialog Title
	        alertDialog.setTitle(title);
	 
	        // Setting Dialog Message
	        alertDialog.setMessage(message);
	 
	       
	        // Setting OK Button
	        alertDialog.setButton("Exit", new DialogInterface.OnClickListener() {
	            @Override
				public void onClick(DialogInterface dialog, int which) {
	            	
	            	
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
	    }
}
