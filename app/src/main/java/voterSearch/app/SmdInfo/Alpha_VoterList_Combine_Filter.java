package voterSearch.app.SmdInfo;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Alpha_VoterList_Combine_Filter extends Activity {

	//test commithhg
	public ProgressDialog progressDialog;
	SharedPreferences master_pref;
	String Display;
	Alpha_VoterList_Master AVMaster;
	
	TextView txt1;
	Button btnNext;
	ListView listView;  
	SharedPreferences pref;
	String Filter_Type;
	MyCustomAdapter dataAdapter = null;        
	ArrayList<Items> PartNoList = new ArrayList<Items>();      
	 
	DBAdapter mdbAdapter;
	
	Button  btnSend, btnRefresh;
	Dialog dialog;
	final Context context = this;
	EditText edtMail;
	CheckBox chkCSV, chkPDF;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mdbAdapter = new DBAdapter(this);
		mdbAdapter.open();
		setContentView(R.layout.alpha_voterlist_combine_filter);
		
			
		
		pref = getApplicationContext().getSharedPreferences("Alpha_VoterList_Types", 0);
		Filter_Type = pref.getString("Filter_Type","");
		
		txt1 = (TextView) findViewById(R.id.txt1);
		listView = (ListView) findViewById(R.id.lv);
		btnNext = (Button) findViewById(R.id.btnNext);
		
		final Typeface font1  = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
		txt1.setTypeface(font1);
			
		dataAdapter = new MyCustomAdapter(Alpha_VoterList_Combine_Filter.this,R.layout.list_item_with_checkbox, PartNoList);
	  	listView.setAdapter(dataAdapter);
	  	listView.setFastScrollEnabled(true);
	 
	  //------------------------------------------------------------------------------
		
		dialog =  new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alpha_voterlist_sendmail_dialog );
		edtMail = (EditText)dialog.findViewById(R.id.edtMail );
		chkCSV = (CheckBox)dialog. findViewById(R.id.chkCSV );
		chkPDF = (CheckBox)dialog.findViewById(R.id.chkPDF );
		btnSend = (Button)dialog. findViewById(R.id.btnSend);
		btnRefresh =   (Button)dialog. findViewById(R.id.btnRefresh);
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Intent i = new Intent(Alpha_VoterList_Combine_Filter.this, Alpha_VoterList_Display.class);
				//i.putExtra("Part_Nos", getSelectedPartNo());
				//i.putExtra("AgeRange", "");
				//startActivity(i);
				
				dialog.show();
				
				
			}
		});
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String email = edtMail.getText().toString();
				String attachment = "";
				if(chkCSV.isChecked() && chkPDF.isChecked())
				{
					attachment = "BOTH";
				}
				else if(chkCSV.isChecked() && chkPDF.isChecked() == false)
				{
					attachment = "CSV";
				}
				else if(chkPDF.isChecked() && chkCSV.isChecked() == false)
				{
					attachment = "PDF";
				}
				
				if(email.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
				}
				else if(attachment.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please select atleast one option", Toast.LENGTH_SHORT).show();
				}
				else
				{
					//async_for_SendMail(email, SanitizeXmlString(AVMaster.Get_Display_Format()), master_pref.getString("Table_Name", ""), Filter_Type, attachment, SanitizeXmlString(AVMaster.Get_Data_For_CSV_Format()));	
				}
					
			}
		});
	
		

	}
	
	

	
	//---------------------------Array adapter------------------------------------------------------------------
	
	 private class MyCustomAdapter extends ArrayAdapter<Items> {
		  
		  private ArrayList<Items> sList;
		
		  public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Items> sList) {
		   super(context, textViewResourceId, sList);
		   this.sList = new ArrayList<Items>();
		   this.sList.addAll(sList);
		  }
		  
		  private class ViewHolder {
		   TextView txtPartno;
		   CheckBox chk;
		  }
		  
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		  
		   ViewHolder holder = null;
		   Log.v("ConvertView", String.valueOf(position));
		  
		   if (convertView == null)
		   {
			   
		   LayoutInflater vi = (LayoutInflater)getSystemService(
		     Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.list_item_with_checkbox, null);
		  
		   holder = new ViewHolder();
		   holder.txtPartno = (TextView) convertView.findViewById(R.id.txtPartNo);
		   holder.chk = (CheckBox) convertView.findViewById(R.id.chkBox);
		   convertView.setTag(holder);
		  
	      holder.chk.setOnClickListener( new OnClickListener() {
		     @Override
			public void onClick(View v) { 
		        CheckBox cb = (CheckBox) v ; 
		        Items item = (Items) cb.getTag(); 
			    item.setSelected(cb.isChecked());
		     } 
		    });  
		   
		   }
		 
		   else 
		   {
		    holder = (ViewHolder) convertView.getTag();
		   }
		  
		   Items item = sList.get(position);
		   
		   holder.txtPartno.setText(item.getPartNo());
		   holder.chk.setChecked(item.isSelected());
		   holder.chk.setTag(item);
		  
		   return convertView;
		  
		  }
		  
		 }
	 
			
		//--------------------------------------------------------------------------------------
		
		  public void async_for_SendMail(final String Email,final String html_table,final String table_name, final String filter_type, final String attachment,final String Get_Data_For_CSV_Format)
			{
				AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
					
					String result;
					@Override
					protected void onPreExecute() 
					{
						 progressDialog = new ProgressDialog(Alpha_VoterList_Combine_Filter.this);
				         progressDialog.setCancelable(false);
				         progressDialog.setMessage("Please wait...");
				         progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				         progressDialog.setProgress(0);
				         progressDialog.show();
					}
						
					@Override
					protected String doInBackground(String... arg0)
					{
						//result = CallWebServiceForSendMail(Email,html_table,table_name,filter_type,attachment, Get_Data_For_CSV_Format);
			        	return result;
					}
					
					@Override
					protected void onPostExecute(String result)
					{
						if(result.equals("Mail Send Successfully"))
						{
							Toast.makeText(getApplicationContext(), "Mail Send Successfully", Toast.LENGTH_LONG).show();
							
						}
						else
						{
							Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
						}
						progressDialog.dismiss();
						dialog.dismiss();
					}
				};
				
				task.execute();
			}

		

			   
			    
}
