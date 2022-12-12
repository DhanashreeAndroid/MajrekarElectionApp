package voterSearch.app.SmdInfo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import voterSearch.app.SmdInfo.Alpha_VoterList_Master;
import voterSearch.app.SmdInfo.ConnectionDetector;
import voterSearch.app.SmdInfo.Mail;
import voterSearch.app.SmdInfo.R;

public class Alpha_VoterList_Display extends Activity {

	
		
	TextView txt;
	Bundle extras;
	SharedPreferences pref;
	SharedPreferences master_pref;
	String Filter_Type;
	String PartNo, AgeRange, SrNo, WardNo;
	WebView webView_Display;	
	public ProgressDialog progressDialog;
	String Header;
	String Display;
	Alpha_VoterList_Master AVMaster;
	Button btnSendMail, btnSend, btnRefresh, btnEnglish, btnMarathi;
	Dialog dialog;
	final Context context = this;
	EditText edtMail;
	CheckBox chkCSV, chkPDF;
	String attachment = "";
	String vidhanName="";
	String htmlFormatData = "";
	ConnectionDetector cd;
	String language = "English";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alpha_voterlist_display);
		
		master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);				
		pref = getApplicationContext().getSharedPreferences("Alpha_VoterList_Types", 0);
		Filter_Type = pref.getString("Filter_Type","");
		
		webView_Display = (WebView) findViewById(R.id.webView2);
		btnSendMail = (Button) findViewById(R.id.btnSendMail);
		btnEnglish = (Button) findViewById(R.id.btnEnglish);
		btnMarathi = (Button) findViewById(R.id.btnMarathi);
		
		txt = (TextView) findViewById(R.id.txt4);
				
		final Typeface font1  = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
		txt.setTypeface(font1);
		
		//------------------------------------------------------------------------------
		
		extras = getIntent().getExtras();
		if(extras != null) {
			PartNo = extras.getString("Part_Nos");
			SrNo = extras.getString("Sr_no");
			WardNo = extras.getString("ward_no");
			AgeRange = extras.getString("AgeRange");
			vidhanName = extras.getString("vidhansabhaName");
		}
		//------------------------------------------------------------------------------
		
		AVMaster = new Alpha_VoterList_Master(this, PartNo,SrNo,WardNo, Filter_Type, AgeRange, vidhanName, true);

		btnEnglish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				language = "English";
				AVMaster = new Alpha_VoterList_Master(Alpha_VoterList_Display.this, PartNo,SrNo,WardNo, Filter_Type, AgeRange, vidhanName, true);
				LoadData ld = new LoadData();
				ld.execute();
			}
		});
		btnMarathi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				language = "Marathi";
				AVMaster = new Alpha_VoterList_Master(Alpha_VoterList_Display.this, PartNo,SrNo,WardNo, Filter_Type, AgeRange, vidhanName, false);
				LoadData ld = new LoadData();
				ld.execute();
			}
		});
		//------------------------------------------------------------------------------
		
		LoadData ld = new LoadData();
		ld.execute();
		
		//------------------------------------------------------------------------------
		
		dialog =  new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alpha_voterlist_sendmail_dialog );
		edtMail = (EditText)dialog.findViewById(R.id.edtMail );
		chkCSV = (CheckBox)dialog. findViewById(R.id.chkCSV );
		chkPDF = (CheckBox)dialog.findViewById(R.id.chkPDF );
		btnSend = (Button)dialog. findViewById(R.id.btnSend);
		btnRefresh =   (Button)dialog. findViewById(R.id.btnRefresh);
		
		//------------------------------------------------------------------------------
		
		btnSendMail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//dialog.show();

				cd = new ConnectionDetector(getApplicationContext());
				if (cd.isConnectingToInternet()) {

					int readexternalStoragePermission = ActivityCompat.checkSelfPermission(Alpha_VoterList_Display.this, Manifest.permission.READ_EXTERNAL_STORAGE);
					int writeexternalStoragePermission = ActivityCompat.checkSelfPermission(Alpha_VoterList_Display.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
					int smsPermission = ActivityCompat.checkSelfPermission(Alpha_VoterList_Display.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
					List<String> permissions = new ArrayList<>();
					if (readexternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
						permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
					}
					if (writeexternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
						permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
					}
					if (smsPermission != PackageManager.PERMISSION_GRANTED) {
						permissions.add(Manifest.permission.SEND_SMS);
					}
					if (!permissions.isEmpty()) {
						ActivityCompat.requestPermissions(Alpha_VoterList_Display.this,
								permissions.toArray(new String[permissions.size()]),
								1000);

					}

					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SEND_MULTIPLE);
					intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
					intent.setType("text/html"); /* This example is sharing html file. */

					ArrayList<Uri> files = new ArrayList<Uri>();


					File file1 = new File(saveHtmlFile());
					if(file1.exists()) {
						Uri uri1 = Uri.fromFile(file1);
						files.add(uri1);
					}

					intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_LONG);
				}
				
			}
		});
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String email = edtMail.getText().toString();
				
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
					async_For_Network_Operation(attachment);
					
					
					//async_for_SendMail(email, SanitizeXmlString(Display), master_pref.getString("Table_Name", ""), Filter_Type, attachment, SanitizeXmlString(AVMaster.Get_Data_For_CSV_Format()), PartNo);	
				}
					
			}
		});
		
		btnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				chkCSV.setChecked(false);
				chkPDF.setChecked(false);
				attachment = "";
			}
		});
		
	}
	private String saveHtmlFile() {

		String path = Environment.getExternalStorageDirectory().getPath();
		String fileName = DateFormat.format("dd_MM_yyyy_", System.currentTimeMillis()).toString()  +  PartNo + "_" + language;
		fileName = fileName + ".html";
		File file = new File(path, fileName);

		try {
			FileOutputStream out = new FileOutputStream(file);
			byte[] data = htmlFormatData.getBytes();
			out.write(data);
			out.close();
			Log.e("TAG", "File Save : " + file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	//--------------------------------------------------------------------------------------
	
	private class LoadData extends AsyncTask<String, Integer, String> {
	     
		
		@Override
        protected void onPreExecute()
        {
        	progressDialog = new ProgressDialog(Alpha_VoterList_Display.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
           
         }
                   
      
		@Override
		protected String doInBackground(String... params) 
        { 
			Display = AVMaster.Get_Display_Format();
        	return Display;
        }
        
        @Override
        protected void onPostExecute(String result) 
        
        
        {
        	try
			{
				//String res2 = android.util.Base64.encodeToString(Display.getBytes("UTF-8"), android.util.Base64.DEFAULT);
				//WebSettings websetting = webView_Display.getSettings();

				//websetting.setDefaultTextEncodingName("utf-8");
				webView_Display.clearHistory();
				webView_Display.clearCache(true);
				webView_Display.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null);
				//webView_Display.loadData(res2, "text/html; charset=utf-8", "base64");
				htmlFormatData = result;
			}
			catch (Exception e) 
			{
				// TODO: handle exception
			}
        	progressDialog.dismiss();
        	    		    		
         }
    }
	
	
	//---------------for Email------------------------------------------------------------------------------------------------------------
	
		private String sendEmail(String attachment)
			{
			
			Mail m = new Mail( master_pref.getString("Email_Id", ""),  master_pref.getString("Password_for_Email", ""));
			 
		      String[] toArr = {edtMail.getText().toString().trim()}; 
		      m.setTo(toArr); 
		      m.setFrom( master_pref.getString("Email_Id", "")); 
		      m.setSubject("Voter Details - " + AVMaster.getHeader()); 
		      m.setBody( "Please find attachment."); 
		 
		      try
		      { 
		    	  if(attachment.equalsIgnoreCase("CSV"))
		    	  {
		    		  m.addAttachment(AVMaster.exportDatabaseToCsv()); 
		    	  }
		    	  else if(attachment.equalsIgnoreCase("PDF"))
			   	  {
		    		  m.addAttachment(AVMaster. exportDatabaseToPdf());   
			      }
		    	  else
		    	  {
		    		  m.addAttachment(AVMaster.exportDatabaseToCsv()); 
		    		  m.addAttachment(AVMaster. exportDatabaseToPdf());   
		    	  }
		    			  
		 
		        if(m.send())
		        { 
		          return "true";
		        }
		        else
		        { 
		        	return "false";
		        } 
		      }
		        catch(Exception e) { 
		        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
		        	return "false";
		      } 
		      
			}
		
		
		
		//--------------------------------------------------------------------------------------------------------
		private void async_For_Network_Operation(final String attachment)
			{
				AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
					
					String result;
					
					@Override
					protected void onPreExecute() 
				{
						progressDialog = new ProgressDialog(Alpha_VoterList_Display.this);
			            progressDialog.setCancelable(false);
			            progressDialog.setMessage("Please Wait...");
			            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			            progressDialog.setProgress(0);
			            progressDialog.show();
					}
						
					@Override
					protected String doInBackground(String... arg0)
					{
						result = sendEmail(attachment);
			        	return result;
					}
					
					@Override
					protected void onPostExecute(String result)
					{
						progressDialog.dismiss();
						
						if(result.equalsIgnoreCase("False"))
						{
							Toast.makeText(Alpha_VoterList_Display.this, "Problem occurred while sending Email.\n" +
									"Check internet connection or entered email Id.", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(Alpha_VoterList_Display.this, "Mail Send Successfully.", Toast.LENGTH_SHORT).show();
						}
						
						
					}
				};
				
				task.execute();
				
			}
		
		
		
		class GMailAuthenticator extends Authenticator
		{
			 String user;
		     String pw;
		     public GMailAuthenticator (String username, String password)
		     {
		        super();
		        this.user = username;
		        this.pw = password;
		     }
		    public PasswordAuthentication getPasswordAuthentication()
		    {
		       return new PasswordAuthentication(user, pw);
		    }
		}
	//--------------------------------------------------------------------------------------
	/*
	  public void async_for_SendMail(final String Email,final String html_table,final String table_name, final String filter_type, final String attachment,final String Get_Data_For_CSV_Format, final String part_no)
		{
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
				
				String result;
				@Override
				protected void onPreExecute() 
				{
					 progressDialog = new ProgressDialog(Alpha_VoterList_Display.this);
			         progressDialog.setCancelable(false);
			         progressDialog.setMessage("Please wait...");
			         progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			         progressDialog.setProgress(0);
			         progressDialog.show();
				}
					
				@Override
				protected String doInBackground(String... arg0)
				{
					//esult = CallWebServiceForSendMail(Email,html_table,table_name,filter_type,attachment, Get_Data_For_CSV_Format, part_no);
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

	*/

	
}
