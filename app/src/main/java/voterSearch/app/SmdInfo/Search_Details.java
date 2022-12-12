package voterSearch.app.SmdInfo;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import android.Manifest;
import android.annotation.SuppressLint;
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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Search_Details extends Activity {

    SharedPreferences master_pref;

    //---------------------------------------------------------------------------------------------------------------

    DBAdapter mDbAdapter;
    final Context context = this;
    TextView txtName, txtNameMarathi,txtAddress, txtAddressMarathi,txtAgeGender, txtVidhansabha, txtPartNo, txtSerialNo, txtVotingCenterAdd,txtVotingCenterAddMarathi;
    TextView txt1, txt2, txt3, txtmsg1, txtmsg2, txtsms, txtemail;
    EditText edtSMS, edtEmail;
    Button btnfamilySearch, btnSendSMS, btnSendEmail, btnPrint, btnShare, btnCall;

    String name,name_marathi, add,add_marathi, age, Gender, vidhansabha, partno, serialno, votingadd, votingAddMarathi;
    String house_no, biuld_name, area_name, last_name, SMS;
    String Message1, Message2,Message3, mob_no, MsgForEmail;
    String VoterName, alpha, vidhan_no;
    String strFirstName, strLastName, strNumber, strPartNo, strWardNo, strSrNo, strtable;

    HashMap<String, String> temp = new HashMap<String, String>();

    public ProgressDialog progressDialog;

    SharedPreferences pref;

    SharedPreferences pref_for_Printing;
    Editor editor;
    String Print_Data, textForSpeech;
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
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setContentView(R.layout.search_details);


        //---------------------------------------------------------------------------------------------------------------------------------

        eventHandler_declarations();

        //---------------------------------------------------------------------------------------------------------------------------------

        data_retrieval();

        //---------------------------------------------------------------------------------------------------------------------------------

        LoadData load = new LoadData();
        load.execute();

        //---------------------------------------------------------------------------------------------------------------------------------

        button_click_event();


        //---------------------------------------------------------------------------------------------------------------------------------


    }//......... onCreate closing....................................................................................................


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        edtSMS.clearFocus();
        edtEmail.clearFocus();
        // Toast.makeText(getApplicationContext(), "Record Write Successfully", Toast.LENGTH_SHORT).show();

        return super.onKeyDown(keyCode, event);
    }

    public void eventHandler_declarations() {
        master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);
        pref = getApplicationContext().getSharedPreferences("Search_type", 0);

        Bundle extras = getIntent().getExtras();
        strLastName = extras.getString("Last");
        strFirstName = extras.getString("First");
        strNumber = extras.getString("Sr_no");
        strtable = extras.getString("table");

        if (!TextUtils.isEmpty(strNumber)) {
            String[] str = strNumber.split(",");
            strSrNo = str[0];
            strPartNo = str[1];
            strWardNo = str[2];
        }
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
        txtNameMarathi = (TextView) findViewById(R.id.txtNameMarathi);
        txtAddressMarathi = (TextView) findViewById(R.id.txtAddressMarathi);
        txtAgeGender = (TextView) findViewById(R.id.txtAgeGender);
        txtVidhansabha = (TextView) findViewById(R.id.txtVidhansabha);
        txtPartNo = (TextView) findViewById(R.id.txtPartNo);
        txtSerialNo = (TextView) findViewById(R.id.txtSerialNo);
        txtVotingCenterAdd = (TextView) findViewById(R.id.txtVotingCenterAddress);
        txtVotingCenterAddMarathi = (TextView) findViewById(R.id.txtVotingCenterAddressMarathi);

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

        int readexternalStoragePermission = ActivityCompat.checkSelfPermission(Search_Details.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeexternalStoragePermission = ActivityCompat.checkSelfPermission(Search_Details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int smsPermission = ActivityCompat.checkSelfPermission(Search_Details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<>();
        if (readexternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeexternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (smsPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.SEND_SMS);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(Search_Details.this,
                    permissions.toArray(new String[permissions.size()]),
                    1000);

        }
    }

    private void getAddress_part_ward_wise() {
        Cursor cur = mDbAdapter.getAddressByPartWardWise(strPartNo, strWardNo);

        if (cur != null && cur.moveToFirst()) {
            vidhan_no = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_WARD_NO));
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void data_retrieval() {
        //---------------------------------------------------------------------------------------------------------------------------------
        //getting  record from voteAdd table

        //getAddress_part_ward_wise();

        Cursor cur = mDbAdapter.GetAllVoterAdd();
        String election_dateHtml = "", election_timeHtml = "", advertiseHtml = "";
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
                    election_dateHtml = "&nbsp;----------------------------<br>&nbsp;" + date + "<br>&nbsp;";
                    election_timeHtml = time;
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

                String addForHtml = limited_ad;
                if (!TextUtils.isEmpty(addForHtml)) {
                    if (addForHtml.length() > 50) {
                        addForHtml = addForHtml.substring(0, 50) + "-<br>&nbsp;"
                                + addForHtml.substring(50, addForHtml.length());
                    }
                }

                advertiseHtml = "&nbsp;----------------------------<br>&nbsp;<font color='red'>" +
                        addForHtml + "</font><br>";


            } else {
                advertise = "";
            }
        }

        //btnfamilySearch.setText(Current_Date + "---" + DB_lastDate + "---" +  last_date + "---" + strDate + "---(" + date_compare +")");
        //---------------------------------------------------------------------------------------------------------------------------------

        Cursor cursor = mDbAdapter.fetchVoterDetailsByName(strtable, strLastName, strFirstName, strSrNo, strPartNo, strWardNo);
        if (cursor.moveToFirst()) {
            do {
                //key_VIDHAN_NO,key_PART_NO,key_SR_NO,key_VOTE_ADD,
                // key_SEX,key_AGE,key_BUILD_NAME,key_AREA_NAME,key_HOUSE_NO,key_SMS
                String vidhan = master_pref.getString("vishansabhas", "");
                if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
                    String[] arrVidhan = vidhan.split(",");
                    for (int i = 0; i < arrVidhan.length; i++) {
                        if (arrVidhan[i].contains(cursor.getString(0))) {
                            vidhan = arrVidhan[i].replace("@", " ");
                            ;
                            break;
                        }
                    }
                } else {
                    vidhan = vidhan.replace("@", " ");
                }
                String first_name_marathi = cursor.getString(9);
                String last_name_marathi = cursor.getString(10);
                String strname = "", strNameMarathi = "";
                //--------------------------------------------------------------------------------------------------------------------
                if (pref.getString("type", "").equals("surname") || pref.getString("type", "").equalsIgnoreCase("easy")) {
                    strname = strLastName + " " + strFirstName;
                    strNameMarathi = last_name_marathi + " " + first_name_marathi;
                } else if (pref.getString("type", "").equals("name")) {
                    strname = strFirstName + " " + strLastName;
                    strNameMarathi = first_name_marathi + " " + last_name_marathi;
                }
                //--------------------------------------------------------------------------------------------------------------------

                Message1 = strname + ", Vidhansabha : " + vidhan + ", "
                        + "Part No : " + cursor.getString(1) + ", " + "Serial No : " + cursor.getString(2);

                Message2 = "Voting Center : " + cursor.getString(8);
                Message3 = strNameMarathi + " मतदान केंद्र : " + cursor.getString(13);


                //--------------------------------------------------------------------------------------------------------------------
                if (date_compare == 0 || date_compare == 1) {
                    MsgForEmail = strname + ", <br></br>" +
                                    strNameMarathi + ", <br></br>"
                            + "Vidhansabha : " + vidhan + ", <br></br>"
                            + "Part No : " + cursor.getString(1) + ", <br></br>"
                            + "Serial No : " + cursor.getString(2) + ", <br></br>"
                            + "Voting Center : " + cursor.getString(8) + " <br></br>"
                            + "मतदान केंद्र : " + cursor.getString(13) + " <br></br>"
                            + "----------------------------<br>"
                            + SMS;
                } else {

                    MsgForEmail = strname + ", <br></br>"
                            + strNameMarathi + ", <br></br>"
                            + "Vidhansabha : " + vidhan + ", <br></br>"
                            + "Part No : " + cursor.getString(1) + ", <br></br>"
                            + "Serial No : " + cursor.getString(2) + ", <br></br>"
                            + "Voting Center : " + cursor.getString(8) + " <br></br>"
                            + "मतदान केंद्र : " + cursor.getString(13) + " <br></br>"
                            + "----------------------------<br>";
                }


                //--------------------------------------------------------------------------------------------------------------------

                //--------------------------------------------------------------------------------------------------------------------
                String areaName = cursor.getString(5);
                if (!TextUtils.isEmpty(areaName)) {
                    if (areaName.length() > 60) {
                        areaName = areaName.substring(0, 60) + "-\n" + areaName.substring(60, areaName.length());
                    }
                }

                String areaNameHTML = cursor.getString(5);
                if (!TextUtils.isEmpty(areaNameHTML)) {
                    if (areaName.length() > 50) {
                        areaNameHTML = areaNameHTML.substring(0, 50) + "-<br>&nbsp;" + areaNameHTML.substring(50, areaNameHTML.length());
                    }
                }

                String address_marathi_html =  cursor.getString(12) + "," +cursor.getString(11);
                if (!TextUtils.isEmpty(address_marathi_html)) {
                    if (address_marathi_html.length() > 50) {
                        address_marathi_html = address_marathi_html.substring(0, 50) + "-<br>&nbsp;" + address_marathi_html.substring(50, address_marathi_html.length());
                    }
                }

                String address_marathi =  cursor.getString(12) + "," +cursor.getString(11);
                if (!TextUtils.isEmpty(address_marathi)) {
                    if (address_marathi.length() > 50) {
                        address_marathi = address_marathi.substring(0, 50) + "\n" + address_marathi.substring(50, address_marathi.length());
                    }
                }
                String votingCenterAddress = cursor.getString(8);
                if (!TextUtils.isEmpty(votingCenterAddress)) {
                    if (votingCenterAddress.length() > 40) {
                        votingCenterAddress = votingCenterAddress.substring(0, 40) + "-\n"
                                + votingCenterAddress.substring(40, votingCenterAddress.length());
                    }
                }

                String votingCenterAddressHTML = cursor.getString(8);
                if (!TextUtils.isEmpty(votingCenterAddressHTML)) {
                    if (votingCenterAddressHTML.length() > 40) {
                        votingCenterAddressHTML = votingCenterAddressHTML.substring(0, 40) + "-<br>&nbsp;"
                                + votingCenterAddressHTML.substring(40, votingCenterAddressHTML.length());
                    }
                }
                String votingAddressMarathi = cursor.getString(13);
                if (!TextUtils.isEmpty(votingAddressMarathi)) {
                    if (votingAddressMarathi.length() > 40) {
                        votingAddressMarathi = votingAddressMarathi.substring(0, 40) + "\n"
                                + votingAddressMarathi.substring(40, votingAddressMarathi.length());
                    }
                }
                String votingAddressMarathiHtml = cursor.getString(13);
                if (!TextUtils.isEmpty(votingAddressMarathiHtml)) {
                    if (votingAddressMarathiHtml.length() > 40) {
                        votingAddressMarathiHtml = votingAddressMarathiHtml.substring(0, 40) + "-<br>&nbsp;"
                                + votingAddressMarathiHtml.substring(40, votingAddressMarathiHtml.length());
                    }
                }
               /* Print_Data = "Voter's Name : " + strname + "\n" +
                        "नाव : " + strNameMarathi + "\n" +
                        "Assembly : " + vidhan + "\n" +
                        "Part No : " + cursor.getString(1) + "    " +
                        "Sr. No : " + cursor.getString(2) + "\n" +
                        "Gender : " + cursor.getString(3) + "      " +
                        "Age : " + cursor.getString(4) + "\n" +
                        "Address : " + cursor.getString(7) + "," + "\n" + areaName + "," + "\n" + cursor.getString(6) + "\n" +
                        "पत्ता : " + address_marathi + "\n" +
                        "----------------------------\n\n" +
                        "Voting Center : " + votingCenterAddress + "\n" +
                        "मतदान केंद्र पत्ता : " + votingAddressMarathi + "\n" +
                        election_date + " " + election_time + "\n" +
                        advertise;
*/
                Print_Data = "Voter's Name : " + strname + "\n" +
                        "Assembly : " + vidhan + "\n" +
                        "Part No : " + cursor.getString(1) + "    " +
                        "Sr. No : " + cursor.getString(2) + "\n" +
                        "Gender : " + cursor.getString(3) + "      " +
                        "Age : " + cursor.getString(4) + "\n" +
                        "Address : " + cursor.getString(7) + "," + "\n" + areaName + "," + "\n" + cursor.getString(6) + "\n" +
                        "----------------------------\n\n" +
                        "Voting Center : " + votingCenterAddress + "\n" +
                        election_date + " " + election_time + "\n" +
                        advertise;

                Print_Data_Share =
                        "<br><font color='blue'>&nbsp;Voter's Name :</font>  <b><font color='blue'>" + strname + "</font></b><br>" +
                        "<font color='blue'>&nbsp;नाव :</font>  <b><font color='blue'>" + strNameMarathi + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Assembly :</font> <b><font color='red'>" + vidhan + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Part No :</font> <b><font color='red'>" + cursor.getString(1) + "</font></b>    " +
                        "<font color='blue'>&nbsp;&nbsp;Sr. No :</font> <b><font color='red'>" + cursor.getString(2) + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Gender :</font> <b><font color='blue'>" + cursor.getString(3) + "</font></b>      " +
                        "<font color='blue'>&nbsp;&nbsp;Age :</font> <b><font color='blue'>" + cursor.getString(4) + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Address :</font> <font color='blue'>" + cursor.getString(7) + "," + " " + areaNameHTML + "," + "<br>" + cursor.getString(6) + "</font><br>" +
                         "<font color='blue'>&nbsp;पत्ता :</font> <b><font color='blue'>" + address_marathi_html + "</font></b>  <br>    " +
                        "<font color='red'>&nbsp;----------------------------</font><br>" +
                        "<font color='blue'>&nbsp;Voting Center :</font> <font color='red'>" + votingCenterAddressHTML + "</font><br>" +
                        "<font color='blue'>&nbsp;मतदान केंद्र पत्ता :</font> <font color='red'>" + votingAddressMarathiHtml + "</font><br>" +

                                election_dateHtml + " " + election_timeHtml + "<br>" +
                        advertiseHtml;
                
                textForSpeech = strname + " Assembly " + vidhan + " Part Number " + cursor.getString(1) +
                        " Serial Number " + cursor.getString(2) + " Age " + cursor.getString(4) +
                        " Voting Center Address " + cursor.getString(8) + " " + limited_ad;
            }
            while (cursor.moveToNext());

            cursor.close();

        }

//--------------------------------------------------------------------------------------------------------------------    
        // Display mobile no and EmailID from database
        Cursor cur1 = mDbAdapter.FetchBackup(strtable, strSrNo);
        if (cur1 != null && cur1.moveToFirst()) {
            edtSMS.setText(cur1.getString(1));
            edtEmail.setText(cur1.getString(2));
        }


    }

    public void button_click_event() {
        btnfamilySearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent i = new Intent(Search_Details.this, Family_Member_Listing.class);
                i.putExtra("house_no", house_no);
                i.putExtra("biuld_name", biuld_name);
                i.putExtra("area_name", area_name);
                i.putExtra("last_name", strLastName);
                i.putExtra("table", strtable);
                i.putExtra("Part_no", partno);
                i.putExtra("Sr_no", serialno);
                i.putExtra("ward_no", strWardNo);
                i.putExtra("Page_Name", "Search_for_Surname_name");
                startActivity(i);
            }
        });

        btnSendSMS.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                    byte[] bytes = Message3.getBytes(StandardCharsets.UTF_16);
                    sendSMS(mob_no, new String(bytes, StandardCharsets.UTF_16));

                    if (date_compare == 0 || date_compare == 1) {
                        sendSMS(mob_no, SMS);
                    }

                    // save mobile no in database
                    Cursor cur = mDbAdapter.FetchBackup(strtable, serialno);
                    if (cur != null && cur.moveToFirst()) {
                        mDbAdapter.UpdateBackup(strSrNo, "mob_no", mob_no);
                    } else {
                        mDbAdapter.createBackup(strSrNo, mob_no, "");
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

                        // save mobile no in database
                        Cursor cur = mDbAdapter.FetchBackup(strtable, serialno);
                        if (cur != null && cur.moveToFirst()) {
                            mDbAdapter.UpdateBackup(strSrNo, "email_id", edtEmail.getText().toString());
                        } else {
                            mDbAdapter.createBackup(strSrNo, "", edtEmail.getText().toString());
                        }

                    }
                } else {
                    showAlertDialog(Search_Details.this, "Unable to connect", "To proceed,\nPlease enabled internet connection.", false);
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

                Intent i = new Intent(Search_Details.this, Printer.class);
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

                Intent i = new Intent(Search_Details.this, Share_Preview.class);
                i.putExtra("isSlipAttach", isSlipAttach);
                i.putExtra("vidhansabha", vidhansabha);
                i.putExtra("textForSpeech", textForSpeech);
                startActivity(i);
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

    }

    //---------------for Email------------------------------------------------------------------------------------------------------------

    private String sendEmail(String str) {
        String status = "false";
        String host = "smtp.gmail.com";
        final String from = master_pref.getString("Email_Id", "");
        final String pass = master_pref.getString("Password_for_Email", "");

        String to = edtEmail.getText().toString().trim();
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        //props.put("mail.smtp.port", "587");
        props.put("mail.smtp.port", "465");
        //props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");

        //Session session = Session.getInstance(props, new GMailAuthenticator(from, pass));

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

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

            } catch (Exception mex) {
                // Prints all nested (chained) exceptions as well
                mex.printStackTrace();
                status = "false";
            }
        } else {
            status = "false";
        }

        return status;

    }

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


    class GMailAuthenticator extends Authenticator {
        String user;
        String pw;

        public GMailAuthenticator(String username, String password) {
            super();
            this.user = username;
            this.pw = password;
        }

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

        Cursor cursor = mDbAdapter.fetchVoterDetailsByName(strtable, strLastName, strFirstName, strSrNo, strPartNo, strWardNo);

        if (cursor.moveToFirst()) {
            do {
                //key_VIDHAN_NO,key_PART_NO,key_SR_NO,key_VOTE_ADD,
                // key_SEX,key_AGE,key_BUILD_NAME,key_AREA_NAME,key_HOUSE_NO,key_SMS
                String first_name_marathi = cursor.getString(9);
                String last_name_marathi = cursor.getString(10);
                if (pref.getString("type", "").equals("surname") || pref.getString("type", "").equalsIgnoreCase("easy")) {
                    temp.put("Name", strLastName + " " + strFirstName);
                    temp.put("NameMarathi", last_name_marathi + " " + first_name_marathi);
                } else if (pref.getString("type", "").equals("name")) {
                    temp.put("Name", strFirstName + " " + strLastName);
                    temp.put("NameMarathi", first_name_marathi + " " + last_name_marathi);
                } else {
                    temp.put("Name", strLastName + " " + strFirstName);
                    temp.put("NameMarathi", last_name_marathi + " " + first_name_marathi);
                }
                String address_marathi =  cursor.getString(12) + "," +cursor.getString(11);
                temp.put("Address_marathi", address_marathi);

                temp.put("House_no", cursor.getString(7));
                temp.put("Build_name", cursor.getString(5));
                temp.put("Area_name", cursor.getString(6));
                temp.put("Age", cursor.getString(4));
                temp.put("Gender", cursor.getString(3));
                temp.put("Vidhansabha", cursor.getString(0));
                temp.put("PartNo", cursor.getString(1));
                temp.put("SerialNo", cursor.getString(2));
                temp.put("VotingCenterAdd", cursor.getString(8));
                temp.put("VotingCenterAddMarathi", cursor.getString(13));

            } while (cursor.moveToNext());
        }

        cursor.close();
        // mDbAdapter.close();

        name = temp.get("Name");
        name_marathi = temp.get("NameMarathi");
        add = temp.get("House_no") + "," + temp.get("Build_name") + "," + temp.get("Area_name");
        add_marathi = temp.get("Address_marathi");
        age = temp.get("Age");
        Gender = temp.get("Gender");
        vidhansabha = temp.get("Vidhansabha");
        partno = temp.get("PartNo");
        serialno = temp.get("SerialNo");
        votingadd = temp.get("VotingCenterAdd");
        votingAddMarathi = temp.get("VotingCenterAddMarathi");

        house_no = temp.get("House_no");
        biuld_name = temp.get("Build_name");
        area_name = temp.get("Area_name");


    }

    public void setData() {
        txtName.setText(Html.fromHtml("Name : " + "<b>" + name + "</b> "));
        txtNameMarathi.setText(Html.fromHtml("नाव : " + "<b>" + name_marathi + "</b> "));
        txtAddress.setText(Html.fromHtml("Address : " + "<b>" + add + "</b> "));
        txtAddressMarathi.setText(Html.fromHtml("पत्ता  : " + "<b>" + add_marathi + "</b> "));
        txtAgeGender.setText(Html.fromHtml("Age/Gender : " + "<b>" + age + " / " + Gender + "</b> "));
        String vidhan = master_pref.getString("vishansabhas", "");
        if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
            String[] arrVidhan = vidhan.split(",");
            for (int i = 0; i < arrVidhan.length; i++) {
                if (arrVidhan[i].contains(vidhansabha)) {
                    vidhan = arrVidhan[i].replace("@", " ");
                    ;
                    break;
                }
            }
        } else {
            vidhan = vidhan.replace("@", " ");
        }
        txtVidhansabha.setText(Html.fromHtml("Ward : " + "<b>" + vidhansabha + "</b> "));
        txtPartNo.setText(Html.fromHtml("Part No : " + "<b>" + partno + "</b> "));
        txtSerialNo.setText(Html.fromHtml("Serial No : " + "<b>" + serialno + "</b> "));
        txtVotingCenterAdd.setText(Html.fromHtml("<u>" + "Voting Center Address : " + "</u>" + "<br> <b>" + votingadd + "</b> "));
        txtVotingCenterAddMarathi.setText(Html.fromHtml("<u>" + "मतदान केंद्र पत्ता : " + "</u>" + "<br> <b>" + votingAddMarathi + "</b> "));


    }

    private class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Search_Details.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            GenerateList();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            setData();

        }
    }


    //--------------------------------------------------------------------------------------------------------
    private void async_For_Network_Operation(final String Message) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            String result;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(Search_Details.this);
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
                    Toast.makeText(Search_Details.this, "Problem occurred while sending Email.\n" +
                            "Check internet connection or entered email Id.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Search_Details.this, "Mail Send Successfully.", Toast.LENGTH_SHORT).show();
                }


            }
        };

        task.execute();

    }


}
