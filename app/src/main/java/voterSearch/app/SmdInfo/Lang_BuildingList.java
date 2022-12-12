package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Lang_BuildingList extends Activity {

	public ArrayList<HashMap<String,String>> list =new ArrayList<HashMap<String,String>>();
	
	DBAdapter mDbAdapter;
	final Context context = this;
	
	TextView txt1,txt2,txt3,txt4, txtBuildingCount;
	EditText edtSearch;
	Button btnSearch;
	ListView lv;
	public ProgressDialog progressDialog;
	
	String table = "";	
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
		setContentView(R.layout.lang_building_list);

		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		txt4.setVisibility(View.VISIBLE);
		txtBuildingCount = (TextView) findViewById(R.id.txtBuildingCount);
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		lv = (ListView) findViewById(R.id.lv);
		
		Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
				
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);
		txt4.setTypeface(font2);

		txt4.setText("Building Wise Language Report");
		
		LoadData gd = new LoadData();
		gd.execute();
		
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

				Intent i = new Intent(Lang_BuildingList.this, Lang_BuildingTabs.class);
				i.putExtra("build_name",voter.Build_Name);
				startActivity(i);
				
			}
		});
		
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Lang_BuildingList.this.adapter.getFilter().filter(edtSearch.getText().toString().trim());
			
			}
		});
		
	}
	
	public void GenerateList() {
			cursor = mDbAdapter.GetBuildingNames();
			if (cursor.moveToFirst()) {
				do {
					String buildname = cursor.getString(0);

					Voter voter = new Voter();
					voter.Build_Name = buildname;

					listDatas.add(voter);
					listBackupDatas.add(voter);

				} while (cursor.moveToNext());

			}
			cursor.close();
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
								R.layout.building_address_listitem, null);
				holder = new VoterViewHolder();
				holder.txtAddress = (TextView) convertView
						.findViewById(R.id.txtAddress);
				holder.txtTotalVoter = (TextView) convertView
						.findViewById(R.id.txtTotalVoter);
				convertView.setTag(holder);
			} else {
				holder = (VoterViewHolder) convertView.getTag();
			}
			holder.txtAddress.setText(getItem(position).Build_Name);
			holder.txtTotalVoter.setVisibility(View.GONE);
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
					if ((row.Build_Name.toString()).toUpperCase().startsWith(constraint.toString().toUpperCase()))
					{
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
		public TextView txtAddress;
		public TextView txtTotalVoter;
	}
	
	public class Voter {
		public String Build_Name;
		
	}
	
	private class LoadData extends AsyncTask<String, Void, String> 
	 {

	     @Override
	     protected void onPreExecute()
	     {
	     	 progressDialog = new ProgressDialog(Lang_BuildingList.this);
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
