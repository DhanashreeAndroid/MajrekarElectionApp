package voterSearch.app.SmdInfo;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Building_Part_3 extends Activity {

public ArrayList<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
	
	DBAdapter mDbAdapter;
	final Context context = this;
	
	TextView txt1,txt2,txt3,txt4;
	EditText edtSearch;
	Button btnSearch;
	ListView lv;
	
	public ProgressDialog progressDialog;
	
	String table = "", BuildingName, AreaName, partNo;	
	Cursor cursor;
	
	public ArrayList<Voter> listDatas = new ArrayList<Voter>();
	public ArrayList<Voter> listBackupDatas = new ArrayList<Voter>();
	public VoterAdapter adapter;

	Bundle extras;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		setContentView(R.layout.building_part_3);
		
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		lv = (ListView) findViewById(R.id.lv);
		
		Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
				
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);
		
		extras = getIntent().getExtras();
		BuildingName = extras.getString("buildname");
		AreaName = extras.getString("areaname");
		partNo = extras.getString("partno");
			
		LoadData load = new LoadData();
		load.execute();
		
		edtSearch.addTextChangedListener(new TextWatcher() {
	 		 
	  	    @Override
	  	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	  	        // When user changed the Text
	  	    	if(cs.toString().length() >= 1)
	  	    	{
	  	    		adapter.getFilter().filter(cs.toString());	
	  	    	}
	        }
	  	 
	  	    @Override
	  	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	  	            int arg3) {
	  	        // TODO Auto-generated method stub
	  	 	  	    }
	  	 
	  	    @Override
	  	    public void afterTextChanged(Editable s) {
	  	        // TODO Auto-generated method stub
	  	    	
	  	     }
	  	    
	  	 });     
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				Voter voter = (Voter) lv.getItemAtPosition(position);
				Character chr;
				chr = voter.LastName.charAt(0);
				table = "Stable" + chr.toString();
				table = "FullVidhansabha";

				try {
					Cursor cursor = mDbAdapter.CheckForVoterDeleted(table, voter.LastName, voter.FirstName, voter.Sr);

					if (cursor != null && cursor.moveToFirst()) {
						Intent i = new Intent(Building_Part_3.this, Search_Details_For_Deleted.class);
						i.putExtra("Last", voter.LastName);
						i.putExtra("First", voter.FirstName);
						i.putExtra("Sr_no", voter.Sr);
						i.putExtra("table", table);
						//for part wise search only, not for surname and name search
						i.putExtra("Search_type", "Surname_name_Search");

						startActivity(i);
						//cursor.close();
					} else //if(type.equals("surname") || type.equals("name"))
					{
						Intent i = new Intent(Building_Part_3.this, Search_Details.class);
						i.putExtra("Last", voter.LastName);
						i.putExtra("First", voter.FirstName);
						i.putExtra("Sr_no", voter.Sr);
						i.putExtra("table", table);
						startActivity(i);
						//cursor.close();
					}
				}catch (Exception ex){
					ex.printStackTrace();
					Toast.makeText(Building_Part_3.this, "Sorry, Surname not found.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Building_Part_3.this.adapter.getFilter().filter(edtSearch.getText().toString().trim());
			
			}
		});
		
	
	}

	public void GenerateList()
	{
		if(partNo.equals("Blank"))
		{
			cursor = mDbAdapter.GetNames_In_BlankBuilding(BuildingName, AreaName);
			
			if (cursor.moveToFirst()) 
			{
					do {
						String last = cursor.getString(0);
				   		String first = cursor.getString(1);
				   		String sr_no = cursor.getString(2);				   		
				   		String part_no = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
						String ward_no = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_WARD_NO));
						String lastM = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME));
						String firstM = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME));

				   		Voter voter = new Voter();
				   		voter.LastName = last;
				   		voter.FirstName = first;
				   		voter.Sr = sr_no + "," + part_no + "," + ward_no;
						voter.LastNameM = lastM;
						voter.FirstNameM = firstM;

						voter.Name = voter.LastName + " " + voter.FirstName;
						voter.NameM = voter.LastNameM + " " + voter.FirstNameM;

						listDatas.add(voter);
						listBackupDatas.add(voter);
				   	 			   			  	
				   	} while (cursor.moveToNext());
					
				} 
				cursor.close();
		}
		else
		{
			cursor = mDbAdapter.GetNames_BuildingWise(BuildingName, AreaName, partNo);
			
			if (cursor.moveToFirst()) 
			{
					do {
						String last = cursor.getString(0);
				   		String first = cursor.getString(1);
				   		String sr_no = cursor.getString(2);
				   		String part_no = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
						String ward_no = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_WARD_NO));
						String lastM = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME));
						String firstM = cursor.getString(cursor
								.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME));

				   		Voter voter = new Voter();
				   		voter.LastName = last;
				   		voter.FirstName = first;
				   		voter.Sr = sr_no + "," + part_no + "," + ward_no;
						voter.LastNameM = lastM;
						voter.FirstNameM = firstM;

						voter.Name = voter.LastName + " " + voter.FirstName;
						voter.NameM = voter.LastNameM + " " + voter.FirstNameM;

						listDatas.add(voter);
						listBackupDatas.add(voter);
				   	 			   			  	
				   	} while (cursor.moveToNext());
					
				} 
				cursor.close();
		}
			
	}
		
	private class VoterAdapter extends BaseAdapter implements Filterable {
		
		private VoterFilter voterFilter;

		@Override
		public int getCount() {
			return listDatas.size();
		}

		@Override
		public Filter getFilter() {
			if (voterFilter == null)
				voterFilter = new VoterFilter();
			return voterFilter;
		}

		@Override
		public Voter getItem(int position) {
			return listDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			VoterViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(
						getApplicationContext()).inflate(
								R.layout.list_item_surname_search, null);
				holder = new VoterViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.txtRawName);
				holder.sr = (TextView) convertView
						.findViewById(R.id.txtSerial);
				holder.nameMarathi = (TextView) convertView.findViewById(R.id.txtRawNameMarathi);

				convertView.setTag(holder);
			} else {
				holder = (VoterViewHolder) convertView.getTag();
			}
			holder.name.setText(getItem(position).Name);
			holder.nameMarathi.setText(getItem(position).NameM);
			holder.sr.setText(getItem(position).Sr);
			return convertView;
		}
		
	}
	
	
	private class VoterFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			ArrayList<Voter> filters = new ArrayList<Voter>();
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				for (Voter player : listBackupDatas) {
					filters.add(player);
				}
				results.values = filters;
				results.count = filters.size();
			} else {
				// We perform filtering operation
				for (Voter row : listBackupDatas) {
					if (row.Name.toUpperCase().startsWith(
							constraint.toString().toUpperCase())) {
						filters.add(row);
					}
				}
				results.values = filters;
				results.count = filters.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results.count == 0) {
				listDatas.clear();
				adapter.notifyDataSetChanged();
			} else {
				listDatas.clear();
				@SuppressWarnings("unchecked")
				ArrayList<Voter> resultList = (ArrayList<Voter>) results.values;
				for (Voter row : resultList) {
					listDatas.add(row);
				}
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private static class VoterViewHolder {
		public TextView name, nameMarathi;
		public TextView sr;
	}
	
	public class Voter {

		public String LastName,LastNameM;
		public String FirstName,FirstNameM ;
		public String Name, NameM;
		public String Sr;
	}
	
	private class LoadData extends AsyncTask<String, Void, String> 
	 {

	     @Override
	     protected void onPreExecute()
	     {
	     	 progressDialog = new ProgressDialog(Building_Part_3.this);
	         progressDialog.setCancelable(false);
	         progressDialog.setMessage("Please wait...");
	         progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	         progressDialog.setProgress(0);
	         progressDialog.show();
	     }
	     
	     
	     @Override
		protected String doInBackground(String... params) 
	     {    	
	    	 GenerateList();
			 adapter = new VoterAdapter();
	     	return "";
	     }

	     @Override
	     protected void onPostExecute(String result) 
	     {
	    	 
	    	 lv.setAdapter(adapter);
		     lv.setTextFilterEnabled(true);
			 lv.setFastScrollEnabled(true);
	    	
	       progressDialog.dismiss();
	      
	    
	     
	     }
	 }
}
