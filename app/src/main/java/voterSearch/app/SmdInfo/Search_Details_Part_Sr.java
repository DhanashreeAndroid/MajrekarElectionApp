package voterSearch.app.SmdInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import voterSearch.app.SmdInfo.Search_Details.GMailAuthenticator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Search_Details_Part_Sr extends Activity {

    SharedPreferences master_pref;
    DBAdapter mDbAdapter;
    final Context context = this;
    TextView txtName, txtAddress, txtAgeGender, txtVidhansabha, txtPartNo, txtSerialNo, txtVotingCenterAdd;
    TextView txt1, txt2, txt3, txtmsg1, txtmsg2, txtsms, txtemail;
    EditText edtSMS, edtEmail;
    Button btnfamilySearch, btnSendSMS, btnSendEmail, btnPrint, btnShare, btnCall, btnNext, btnPrevious;

    String name, add, age, Gender, vidhansabha, partno, serialno, votingadd;
    String house_no, biuld_name, area_name, last_name, SMS;
    String Message1, Message2, mob_no, MsgForEmail;
    String VoterName, alpha, vidhan_no;
    String strSrNo, strPartNo, strLastName, strFirstName, strTableName, strWardNo;
    HashMap<String, String> temp = new HashMap<String, String>();

    public ProgressDialog progressDialog;

    SharedPreferences pref;

    SharedPreferences pref_for_Printing;
    Editor editor;
    String Print_Data;
    String Print_Data_Share;
    String election_date = "";
    String election_time = "";
    String limited_ad = "";
    String last_date;
    String advertise = "";
    String strDate;
    SimpleDateFormat date_format;
    Date Current_Date;
    Date DB_lastDate;


    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    String strMobNoHolder, strEmailHolder;

    Cursor cursorY;

    int date_compare;
    boolean isSlipAttach = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setContentView(R.layout.search_details_part_sr);


        eventHandler_declarations();

        //---------------------------------------------------------------------------------------------------------------------------------

        async_for_Navigation();

        //---------------------------------------------------------------------------------------------------------------------------------

        data_retrieval();

        //---------------------------------------------------------------------------------------------------------------------------------

        button_click_event();

        //---------------------------------------------------------------------------------------------------------------------------------


    }//-------------------------------------------------------------------------------------------------------------


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        edtSMS.clearFocus();
        edtEmail.clearFocus();
        // Toast.makeText(getApplicationContext(), "Record Write Successfully", Toast.LENGTH_SHORT).show();

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        //rbFound.setChecked(false);
        //rbNotFound.setChecked(false);

        super.onResume();
    }


    public void eventHandler_declarations() {
        master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);
        pref = getApplicationContext().getSharedPreferences("Search_type", 0);

        Bundle extras = getIntent().getExtras();

        strSrNo = extras.getString("Sr_no");
        strPartNo = extras.getString("PartNo");
        strWardNo = extras.getString("ward_no");

        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt1.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        txt3.setVisibility(View.VISIBLE);
        txtName = (TextView) findViewById(R.id.txtName);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtAgeGender = (TextView) findViewById(R.id.txtAgeGender);
        txtVidhansabha = (TextView) findViewById(R.id.txtVidhansabha);
        txtPartNo = (TextView) findViewById(R.id.txtPartNo);
        txtSerialNo = (TextView) findViewById(R.id.txtSerialNo);
        txtVotingCenterAdd = (TextView) findViewById(R.id.txtVotingCenterAddress);

        txtmsg1 = (TextView) findViewById(R.id.txtmsg1);
        txtmsg2 = (TextView) findViewById(R.id.txtmsg2);
        txtsms = (TextView) findViewById(R.id.txtSms);
        txtemail = (TextView) findViewById(R.id.txtEmail);

        edtSMS = (EditText) findViewById(R.id.edtSms);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        btnfamilySearch = (Button) findViewById(R.id.btnFamilySearch);
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);


        Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
        txt1.setTypeface(font1);
        txtName.setTypeface(font1);
        txtAddress.setTypeface(font1);
        txtAgeGender.setTypeface(font1);
        txtVidhansabha.setTypeface(font1);
        txtPartNo.setTypeface(font1);
        txtSerialNo.setTypeface(font1);
        txtVotingCenterAdd.setTypeface(font1);

        txtmsg1.setTypeface(font1);
        txtmsg2.setTypeface(font1);
        txtsms.setTypeface(font1);
        txtemail.setTypeface(font1);

        edtSMS.setTypeface(font1);
        edtEmail.setTypeface(font1);

        Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
        txt2.setTypeface(font2);
        txt3.setTypeface(font2);

        date_format = new SimpleDateFormat("dd/MM/yyyy");


        strMobNoHolder = edtSMS.getText().toString();
        strEmailHolder = edtEmail.getText().toString();


    }

    public void data_retrieval() {
        //---------------------------------------------------------------------------------------------------------------------------------
        //getting  record from voteAdd table

        Cursor cur = mDbAdapter.GetAllVoterAdd();
        String election_dateHtml = "", election_timeHtml = "",  advertiseHtml = "";
        if (cur != null && cur.moveToFirst()) {
            int count = cur.getCount();
            cur.moveToPosition(count - 4);

            vidhan_no = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
            SMS = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_VOTE_ADD));

            if ((cur.moveToPosition(count - 3))) {
                String date = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
                String time = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_VOTE_ADD));

                if (date.equalsIgnoreCase("null") && time.equalsIgnoreCase("null")) {
                    election_date = "";
                    election_time = "";
                } else {
                    election_date = "----------------------------\n" + date + "  ";
                    election_time = time + "\n";
                    election_dateHtml = "&nbsp;----------------------------<br>&nbsp;" + date + "<br>&nbsp; ";
                    election_timeHtml = time ;
                }
            }

            if ((cur.moveToPosition(count - 2))) ;
            {
                limited_ad = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_VOTE_ADD));
            }

            if ((cur.moveToPosition(count - 1))) ;
            {
                last_date = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_VOTE_ADD));
            }

            cur.close();
        }

        //-----------------------getting current date-------

        Date now = new Date();
        strDate = new SimpleDateFormat("dd/MM/yyyy").format(now);

        try {
            // this lines convert the string into date
            Current_Date = date_format.parse(strDate);
            DB_lastDate = date_format.parse(last_date);
            date_compare = DB_lastDate.compareTo(Current_Date);
                            /*
					    	// for testing purpose only
					    	btnPrint.setText(Current_Date + "----" + DB_lastDate);
					    	
					    	if(date_compare == 0 || date_compare == 1)
					    	{
					    		btnfamilySearch.setText("last date is greater or equal");
					    	}
					    	*/
        } catch (Exception e) {

        }

        //---------------------------------------------------------------------------------------------------------------------------------
        if (limited_ad.equalsIgnoreCase("null")) {
            advertise = "";
        } else {
            // last date is greater or equal to current date
            if (date_compare == 0 || date_compare == 1) {
                isSlipAttach = true;
                advertise = "----------------------------\n" +
                        limited_ad + "\n";
                advertiseHtml = "&nbsp;----------------------------<br>&nbsp;<font color='red'>" +
                        limited_ad + "</font><br>";

            } else {
                advertise = "";
            }
        }

        //---------------------------------------------------------------------------------------------------------------------------------

        Cursor cursor = mDbAdapter.fetchVoterDetailsByPartSr(strSrNo, strPartNo, strWardNo);
        if (cursor.moveToFirst()) {
            do {
                //key_VIDHAN_NO,key_PART_NO,key_SR_NO,key_VOTE_ADD,
                // key_SEX,key_AGE,key_BUILD_NAME,key_AREA_NAME,key_HOUSE_NO,key_SMS
                strWardNo = cursor.getString(0);
                String strname = "";
                //--------------------------------------------------------------------------------------------------------------------

                String vidhan = master_pref.getString("vishansabhas", "");
                if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
                    String[] arrVidhan = vidhan.split(",");
                    for (int i = 0; i < arrVidhan.length; i++) {
                        if (arrVidhan[i].contains(strWardNo)) {
                            vidhan = arrVidhan[i].replace("@", " ");
                            break;
                        }
                    }
                }else{
                    vidhan = vidhan.replace("@", " ");
                }

                strname = cursor.getString(3) + " " + cursor.getString(4);

                //--------------------------------------------------------------------------------------------------------------------
                Message1 = strname + ", Vidhansabha : " + vidhan + ", "
                        + "Part No : " + cursor.getString(1) + ", " + "Serial No : " + cursor.getString(2);

                Message2 = "Voting Center : " + cursor.getString(10);

                //--------------------------------------------------------------------------------------------------------------------
                if (date_compare == 0 || date_compare == 1) {
                    MsgForEmail = strname + ", <br></br>"
                            + "Vidhansabha : " + vidhan + ", <br></br>"
                            + "Part No : " + cursor.getString(1) + ", <br></br>"
                            + "Serial No : " + cursor.getString(2) + ", <br></br>"
                            + "Voting Center : " + cursor.getString(10) + " <br></br>"
                            + "----------------------------</br>"
                            + SMS + "ImageAttachment";
                } else {

                    MsgForEmail = strname + ", <br></br>"
                            + "Vidhansabha : " + vidhan + ", <br></br>"
                            + "Part No : " + cursor.getString(1) + ", <br></br>"
                            + "Serial No : " + cursor.getString(2) + ", <br></br>"
                            + "Voting Center : " + cursor.getString(10) + " <br></br>"
                            + "----------------------------</br>";
                }
                //--------------------------------------------------------------------------------------------------------------------

                //--------------------------------------------------------------------------------------------------------------------

                String areaName = cursor.getString(7);
                if (!TextUtils.isEmpty(areaName)) {
                    if (areaName.length() > 60) {
                        areaName = areaName.substring(0, 60) + "-\n" + areaName.substring(60, areaName.length());
                    }
                }
                String areaNameHTML = cursor.getString(7);
                if (!TextUtils.isEmpty(areaNameHTML)) {
                    if (areaName.length() > 50) {
                        areaNameHTML = areaNameHTML.substring(0, 50) + "-<br>&nbsp;" + areaNameHTML.substring(50, areaNameHTML.length());
                    }
                }

                String votingCenterAddress = cursor.getString(10);
                ;
                if (!TextUtils.isEmpty(votingCenterAddress)) {
                    if (votingCenterAddress.length() > 80) {
                        votingCenterAddress = votingCenterAddress.substring(0, 80) + "-\n"
                                + votingCenterAddress.substring(80, votingCenterAddress.length());
                    }
                }

                String votingCenterAddressHTML = cursor.getString(10);
                if (!TextUtils.isEmpty(votingCenterAddress)) {
                    if (votingCenterAddress.length() > 50) {
                        votingCenterAddress = votingCenterAddress.substring(0, 50) + "-<br>&nbsp;"
                                + votingCenterAddress.substring(50, votingCenterAddress.length());
                    }
                }

                Print_Data = "Voter's Name : " + strname + "\n" +
                        "Assembly : " + vidhan + "\n" +
                        "Part No : " + cursor.getString(1) + "      " +
                        "Sr. No : " + cursor.getString(2) + "\n" +
                        "Gender : " + cursor.getString(5) + "      " +
                        "Age : " + cursor.getString(6) + "\n" +
                        "Address : " + cursor.getString(9) + "," + "\n" + areaName + "," + "\n" + cursor.getString(8) + "\n" +
                        "----------------------------\n\n" +
                        "Voting Center : " + votingCenterAddress + "\n" +
                        election_date + " " + election_time + "\n" +
                        advertise;
                Print_Data_Share =  "<font color='blue'>&nbsp;Voter's Name :</font>  <b><font color='blue'>" + strname + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Assembly :</font> <b><font color='red'>" + vidhan + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Part No :</font> <b><font color='red'>" + cursor.getString(1) + "</font></b>    " +
                        "<font color='blue'>&nbsp;&nbsp;Sr. No :</font> <b><font color='red'>" + cursor.getString(2) + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Gender :</font> <b><font color='blue'>" + cursor.getString(5) + "</font></b>      " +
                        "<font color='blue'>&nbsp;&nbsp;Age :</font> <b><font color='blue'>" + cursor.getString(6) + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Address :</font> <font color='blue'>" + cursor.getString(9) + "," + " " + areaNameHTML + "," + "<br>" + cursor.getString(8) + "</font><br>" +
                        "<font color='red'>&nbsp;-------------------------------------------------------</font><br>" +
                        "<font color='blue'>&nbsp;Voting Center :</font> <font color='red'>" + votingCenterAddressHTML + "</font><br>" +
                        election_dateHtml + " " + election_timeHtml + "<br>" +
                        advertiseHtml;

            }
            while (cursor.moveToNext());

            cursor.close();

        }
    }

    public void button_click_event() {
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // creating connection detector class instance

                cd = new ConnectionDetector(getApplicationContext());
                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();

                Cursor cursor = mDbAdapter.Get_serial_In_Part(partno, vidhansabha);
                int total_sr_no = cursor.getCount();

                int current_sr_no = Integer.parseInt(serialno);
                current_sr_no = current_sr_no + 1;

                if (current_sr_no > total_sr_no) {
                    current_sr_no = 1;
                }

                String SrNo = String.valueOf(current_sr_no);

                Cursor cur = mDbAdapter.fetchVoterDetailsByPartSr(SrNo, partno , cursor.getString(1));
                cursor.close();
                if (cur.getString(5).equalsIgnoreCase("D")) {
                    Intent i = new Intent(Search_Details_Part_Sr.this, Search_Details_For_Deleted.class);
                    i.putExtra("Last", cur.getString(3));
                    i.putExtra("First", cur.getString(4));
                    i.putExtra("Sr_no",cur.getString(2));
                    i.putExtra("table",cur.getString(11));
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("ward_no", cur.getString(0)) ;
                    i.putExtra("Search_type","Part_Sr_Search" );
                    startActivity(i);
                } else {
                    finish();
                    Intent i = new Intent(Search_Details_Part_Sr.this, Search_Details_Part_Sr.class);
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("Sr_no", SrNo);
                    i.putExtra("ward_no", cur.getString(0)) ;
                    startActivity(i);
                }


            }
        });

        btnPrevious.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // creating connection detector class instance
                cd = new ConnectionDetector(getApplicationContext());
                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();

                Cursor cursor = mDbAdapter.Get_serial_In_Part(partno, vidhansabha);
                int total_sr_no = cursor.getCount();

                int current_sr_no = Integer.parseInt(serialno);
                current_sr_no = current_sr_no - 1;

                if (Integer.parseInt(serialno) == 1) {
                    current_sr_no = total_sr_no;
                }

                String SrNo = String.valueOf(current_sr_no);

                Cursor cur = mDbAdapter.fetchVoterDetailsByPartSr(SrNo, partno , cursor.getString(1));
                cursor.close();
                if (cur.getString(5).equalsIgnoreCase("D")) {
                    Intent i = new Intent(Search_Details_Part_Sr.this, Search_Details_For_Deleted.class);
                    i.putExtra("Last", cur.getString(3));
                    i.putExtra("First", cur.getString(4));
                    i.putExtra("Sr_no",cur.getString(2));
                    i.putExtra("table",cur.getString(11));
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("ward_no", cur.getString(0)) ;
                    i.putExtra("Search_type","Part_Sr_Search" );
                    startActivity(i);
                } else {
                    finish();
                    Intent i = new Intent(Search_Details_Part_Sr.this, Search_Details_Part_Sr.class);
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("Sr_no", SrNo);
                    i.putExtra("ward_no", cur.getString(0)) ;
                    startActivity(i);
                }

            }
        });

        btnfamilySearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent i = new Intent(Search_Details_Part_Sr.this, Family_Member_Listing.class);
                i.putExtra("house_no", house_no);
                i.putExtra("biuld_name", biuld_name);
                i.putExtra("area_name", area_name);
                i.putExtra("last_name", strLastName);
                i.putExtra("table", strTableName);
                i.putExtra("Part_no", partno);
                i.putExtra("Sr_no", serialno);
                i.putExtra("ward_no", strWardNo);
                i.putExtra("Page_Name", "Search_for_Part");
                startActivity(i);


            }
        });

        btnSendSMS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (edtSMS.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                } else if (edtSMS.length() < 10) {
                    Toast.makeText(getApplicationContext(), "You entered " + edtSMS.length() + " digit only.", Toast.LENGTH_SHORT).show();
                } else {

                    mob_no = edtSMS.getText().toString();

                    sendSMS(mob_no, Message1);
                    sendSMS(mob_no, Message2);


                    if (date_compare == 0 || date_compare == 1) {
                        sendSMS(mob_no, SMS);
                    }
                }
            }
        });

        btnSendEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    if (edtEmail.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter E-Mail ID.", Toast.LENGTH_SHORT).show();
                    } else {

                        async_For_Network_Operation(MsgForEmail);

                    }
                } else {
                    showAlertDialog(Search_Details_Part_Sr.this, "Unable to connect", "To proceed,\nPlease enabled internet connection.", false);
                }
            }
        });


        //---------------------------------------------------------------------------------------------------------------------------------
        pref_for_Printing = getApplicationContext().getSharedPreferences("Printer", 0);
        editor = pref_for_Printing.edit();
        //---------------------------------------------------------------------------------------------------------------------------------

        btnPrint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(), last_date + "\n-----------\n" + strDate, Toast.LENGTH_SHORT).show();

                editor.putString("Print_Data", Print_Data);
                editor.putString("Print_Activity", "Search_Details");
                editor.commit();

                Intent i = new Intent(Search_Details_Part_Sr.this, Printer_CIE_DYNO_4807.class);
                startActivity(i);
            }
        });


        btnShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editor.putString("Print_Data", Print_Data_Share);
                editor.putString("Print_Activity", "Search_Details");
                editor.commit();

                Intent i = new Intent(Search_Details_Part_Sr.this, Share_Preview.class);
                i.putExtra("isSlipAttach", isSlipAttach);
                i.putExtra("vidhansabha", vidhansabha);
                startActivity(i);
            }
        });

        edtSMS.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                if (!hasFocus) {
                    if (edtSMS.length() < 10) {
                        Toast.makeText(getApplicationContext(), "You entered " + edtSMS.length() + " digit only.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        //-----------------------------------------------------------------------------------------------------------------------
        btnCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (edtSMS.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                } else {
                    String mob = edtSMS.getText().toString();

                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_CALL);
                    in.setData(Uri.parse("tel:" + mob));
                    startActivity(in);


                }
            }
        });


        //---------------------------------------------------------------------------------------------------------------------------------
    }

    //---------------for Email------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();
            }
        });

        // Setting OK Button
        alertDialog.setButton2("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

/*  public String CallWebServiceFor_SendMail(final String FromEmailId,final String password,final String ToEmailId,final String strSubject,final String strBody, final String strImage)
	    {
	    	String res = "";
	    	SoapObject request;
	        try
	        {
	        	
	        request = new SoapObject(NAMESPACE, METHOD_NAME5); 
	        
	        request.addProperty("FromEmailId", FromEmailId);
	        request.addProperty("password", password );
	        request.addProperty("ToEmailId", ToEmailId);
	        request.addProperty("strSubject", strSubject);
	        request.addProperty("strBody", strBody);
	        request.addProperty("strImage", strImage);
	        
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

			try 
			{
				androidHttpTransport.call(SOAP_ACTION5, envelope);
				SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
				res = response.toString();
				return res;
			
			} 
			catch (Exception e) 
			{
				res = e.getMessage();
			}
	        }
	        catch(Exception ex)
	        {
	        	res = ex.getMessage();
	        }       
	        return res;
	    }
	
	//--------------------------------------------------------------------------------------------------------
	private void async_For_Network_Operation(final String FromEmailId, final String password, final String ToEmailId,
			 								 final String strSubject, final String strBody, final String strImage)
		{
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
				
				String result;
				
				@Override
				protected void onPreExecute() 
				{
					progressDialog = new ProgressDialog(Search_Details_Part_Sr.this);
		            progressDialog.setCancelable(false);
		            progressDialog.setMessage("Please Wait...");
		            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		            progressDialog.setProgress(0);
		            progressDialog.show();
				}
					
				@Override
				protected String doInBackground(String... arg0)
				{
					result = CallWebServiceFor_SendMail(FromEmailId, password, ToEmailId, strSubject, strBody, strImage);
		        	return result;
				}
				
				@Override
				protected void onPostExecute(String result)
				{
					progressDialog.dismiss();
					
					if(result.equals("Mail Send Successfully"))
					{
						Toast.makeText(Search_Details_Part_Sr.this, result, Toast.LENGTH_SHORT).show();
						
						// check for Internet status
			            if (isInternetPresent)
			            {
			            	async_for_Update("email_sent", "y");
			            	
			            	Cursor c = mDbAdapter.FetchBackup(partno, serialno);
	            			
			            	  if( c != null && c.moveToFirst())
				              {
			            		  mDbAdapter.UpdateBackup(0, partno, serialno, "email_sent", "y");             
				              }
				              else
				              {
				            	//part_no,sr_no,mob_no,email_id,sms_sent,email_sent,mob_call,f_mob_no,f_mail_id,f_sms_sent,f_mail_sent,f_mob_call,slip,voted,shifted,found,not_found,bth_head1,bhead1_mob,bth_head2,bhead2_mob
				            	  mDbAdapter.createBackup(0, partno, serialno, "", "","", "y", "", "", "", "", "", "", "", "", "", "", "","");
				              }
			            	  
			            	c.close();
			            	
			            }	
			            else 
			            {
			            	Cursor c = mDbAdapter.FetchBackup(partno, serialno);
			            			
			            	  if( c != null && c.moveToFirst())
				              {
			            		  mDbAdapter.UpdateBackup(1, partno, serialno, "email_sent", "y");             
				              }
				              else
				              {
				            	//part_no,sr_no,mob_no,email_id,sms_sent,email_sent,mob_call,f_mob_no,f_mail_id,f_sms_sent,f_mail_sent,f_mob_call,slip,voted,shifted,found,not_found,bth_head1,bhead1_mob,bth_head2,bhead2_mob
				            	  mDbAdapter.createBackup(1, partno, serialno, "", "","", "y", "", "", "", "", "", "", "", "", "", "", "","");
				              }
			            	  
			            	c.close();
			            }
						txtemailSent.setText("Email already sent to this voter");
						txtemailSent.setTextColor(getResources().getColor(R.color.green));
						
						
					}
					else
					{
						Toast.makeText(Search_Details_Part_Sr.this, "Problem occurred while sending Email.\n" +
															"Check internet connection or entered email Id.", Toast.LENGTH_SHORT).show();
					}
					
					
				}
			};
			
			task.execute();
			
		}
		*/


    class GMailAuthenticator extends Authenticator {

        String user;
        String pw;

        public GMailAuthenticator(String username, String password) {
            super();
            this.user = username;
            this.pw = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, pw);
        }
    }

    //---sends a SMS message to another device---
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    public void GenerateList() {

        Cursor cursor = mDbAdapter.fetchVoterDetailsByPartSr(strSrNo, strPartNo, strWardNo);
        strWardNo = cursor.getString(0);
        strLastName = cursor.getString(3);
        strTableName = cursor.getString(11);
        if (cursor.moveToFirst()) {
            do {
                //key_VIDHAN_NO,key_PART_NO,key_SR_NO,key_VOTE_ADD,
                // key_SEX,key_AGE,key_BUILD_NAME,key_AREA_NAME,key_HOUSE_NO,key_SMS

                temp.put("Name", cursor.getString(3) + " " + cursor.getString(4));
                temp.put("House_no", cursor.getString(9));
                temp.put("Build_name", cursor.getString(7));
                temp.put("Area_name", cursor.getString(8));
                temp.put("Age", cursor.getString(6));
                temp.put("Gender", cursor.getString(5));
                temp.put("Vidhansabha", cursor.getString(0));
                temp.put("PartNo", cursor.getString(1));
                temp.put("SerialNo", cursor.getString(2));
                temp.put("VotingCenterAdd", cursor.getString(10));

            } while (cursor.moveToNext());
        }

        cursor.close();
        // mDbAdapter.close();

        name = temp.get("Name");
        add = temp.get("House_no") + "," + temp.get("Build_name") + "," + temp.get("Area_name");
        age = temp.get("Age");
        Gender = temp.get("Gender");
        vidhansabha = temp.get("Vidhansabha");
        partno = temp.get("PartNo");
        serialno = temp.get("SerialNo");
        votingadd = temp.get("VotingCenterAdd");

        house_no = temp.get("House_no");
        biuld_name = temp.get("Build_name");
        area_name = temp.get("Area_name");

    }


    public void setData() {
        txtName.setText(Html.fromHtml("Name : " + "<b>" + name + "</b> "));
        txtAddress.setText(Html.fromHtml("Address : " + "<b>" + add + "</b> "));
        txtAgeGender.setText(Html.fromHtml("Age/Gender : " + "<b>" + age + " / " + Gender + "</b> "));
        String vidhan = master_pref.getString("vishansabhas", "");
        if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
            String[] arrVidhan = vidhan.split(",");
            for (int i = 0; i < arrVidhan.length; i++) {
                if (arrVidhan[i].contains(vidhansabha)) {
                    vidhan = arrVidhan[i].replace("@", " ");
                    break;
                }
            }
        }else{
            vidhan = vidhan.replace("@", " ");
        }
        txtVidhansabha.setText(Html.fromHtml("Vidhansabha : " + "<b>" + vidhan + "</b> "));
        txtPartNo.setText(Html.fromHtml("Part No : " + "<b>" + partno + "</b> "));
        txtSerialNo.setText(Html.fromHtml("Serial No : " + "<b>" + serialno + "</b> "));
        txtVotingCenterAdd.setText(Html.fromHtml("<u>" + "Voting Center Address : " + "</u>" + "<br> <b>" + votingadd + "</b> "));
    }


    public void async_for_Navigation() {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {


            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(Search_Details_Part_Sr.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... arg0) {
                GenerateList();

                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                progressDialog.dismiss();
                setData();

            }
        };

        task.execute();
    }

    //--------------------------------------------------------------------------------------------------------
    private void async_For_Network_Operation(final String Message) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            String result;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(Search_Details_Part_Sr.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... arg0) {
                result = sendEmail(Message);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                progressDialog.dismiss();

                if (result.equals("False")) {
                    Toast.makeText(Search_Details_Part_Sr.this, "Problem occurred while sending Email.\n" +
                            "Check internet connection or entered email Id.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Search_Details_Part_Sr.this, "Mail Send Successfully.", Toast.LENGTH_SHORT).show();
                }


            }
        };

        task.execute();

    }

    private String sendEmail(String str) {
        String status = "false";
        String host = "smtp.gmail.com";
        String from = master_pref.getString("Email_Id", "");
        String pass = master_pref.getString("Password_for_Email", "");

        String to = edtEmail.getText().toString().trim();
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        //props.put("mail.smtp.port", "465");
        //props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new GMailAuthenticator(from, pass));

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (to.matches(emailPattern)) {

            try {
                // Instantiate a message
                Message msg = new MimeMessage(session);

                //Set message attributes
                msg.setFrom(new InternetAddress(from));
                InternetAddress[] address = {new InternetAddress(to)};
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject("Voter Details");
                msg.setSentDate(new Date());
                msg.setContent(str, "text/html");
                // Set message content
                // msg.setText(str);

                //Send the message
                Transport.send(msg);


                status = "true";

            } catch (MessagingException mex) {
                // Prints all nested (chained) exceptions as well
                mex.printStackTrace();
                status = "false";
            }
        } else {
            status = "false";
        }

        return status;

    }
}
