package voterSearch.app.SmdInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Family_Member_Listing extends Activity {

    SharedPreferences master_pref;

    //---------------------------------------------------------------------------------------------------------------

    DBAdapter mDbAdapter;
    final Context context = this;

    TextView txt1, txt2, txt3, txt4;
    ListView lv;
    String house_no, build_name, area_name, last_name, table, page_name;
    Cursor cursor;
    String vidhanNo, SMS;
    public ProgressDialog progressDialog;
    EditText edtSMS, edtEmail;
    Button btnSendSMS, btnSendEmail, btnPrint, btnShare, btnCall;
    String Message1, Message2,Message3, MsgForEmail, mob_no;

    public ArrayList<Voter> listDatas = new ArrayList<Voter>();
    public VoterAdapter adapter;

    SharedPreferences pref;
    String type;

    SharedPreferences pref_for_Printing;
    Editor editor;
    String last_date;
    String strDate;
    SimpleDateFormat date_format;
    Date Current_Date;
    Date DB_lastDate;
    String str;

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    HashMap<String, String> temp_for_backup = new HashMap<String, String>();

    String[] backup_feilds = {"part_no", "sr_no", "mob_no", "email_id", "sms_sent", "email_sent", "mob_call",
            "f_mob_no", "f_mail_id", "f_sms_sent", "f_mail_sent", "f_mob_call", "slip", "voted", "shifted",
            "found", "not_found"};

    String strMobNoHolder, strEmailHolder;
    String partno, serialno, wardno;

    HashMap<String, String> temp = new HashMap<String, String>();

    Cursor cursorY;
    int date_compare;
    boolean isSlipAttach = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setContentView(R.layout.family_member_listing);

        //**************************
        value_Initializer();
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


        //---------------------------------------------------------------------------------------------------------------------------------

    }

    public void value_Initializer() {
        master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        edtSMS.clearFocus();
        edtEmail.clearFocus();
        // Toast.makeText(getApplicationContext(), "Record Write Successfully", Toast.LENGTH_SHORT).show();

        return super.onKeyDown(keyCode, event);
    }


    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void eventHandler_declarations() {
        pref = getApplicationContext().getSharedPreferences("Search_type", 0);
        type = pref.getString("type", "");

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt1.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        txt3.setVisibility(View.VISIBLE);
        txt4.setVisibility(View.VISIBLE);
        lv = (ListView) findViewById(R.id.lv);

        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        edtSMS = (EditText) findViewById(R.id.edtSms);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnCall = (Button) findViewById(R.id.btnCall);


        Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
        txt1.setTypeface(font1);

        Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
        txt2.setTypeface(font2);
        txt3.setTypeface(font2);
        txt4.setTypeface(font2);

        Bundle extras = getIntent().getExtras();
        house_no = extras.getString("house_no");
        build_name = extras.getString("biuld_name");
        area_name = extras.getString("area_name");
        last_name = extras.getString("last_name");
        table = extras.getString("table");
        partno = extras.getString("Part_no");
        serialno = extras.getString("Sr_no");
        wardno = extras.getString("ward_no");
        page_name = extras.getString("Page_Name");

        //---------------because family details getting from surname tables only------------------------------------------------------
        //char c = last_name.charAt(0);
        //table = "Stable" + c;
        table = "FullVidhansabha";

        strMobNoHolder = edtSMS.getText().toString();
        strEmailHolder = edtEmail.getText().toString();
    }

    public void data_retrieval() {
        //----------------------------------------------------------------------------------------------------------------------------
        cursor = mDbAdapter.fetchVoterFamilyPersonName(table, house_no, build_name, area_name, last_name);

        int count = cursor.getCount();
        txt4.setText(last_name + " family total " + count + " voters");

        cursor.close();

        date_format = new SimpleDateFormat("dd/MM/yyyy");

        //---------------------------------------------------------------------------------------------------------------------------------
        //getting  record from voteAdd table for Vidhan_no and SMS and last_date

        Cursor cur = mDbAdapter.GetAllVoterAdd();

        if (cur != null && cur.moveToFirst()) {
            int count1 = cur.getCount();

            if (cur.moveToPosition(count1 - 4)) {
                vidhanNo = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
                SMS = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_VOTE_ADD));
            }

            if ((cur.moveToPosition(count1 - 1))) ;
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
                    /* for testing purpose only
			    	btnPrint.setText(Current_Date + "----" + DB_lastDate);
			    	
			    	if(date_compare == 0 || date_compare == 1)
			    	{
			    		btnfamilySearch.setText("last date is greater or equal");
			    	}
			    	*/
        } catch (Exception e) {

        }

    }

    public void button_click_event() {

        lv.setTextFilterEnabled(true);
        lv.setFastScrollEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

                Voter voter = (Voter) lv.getItemAtPosition(position);

                Cursor cursor = mDbAdapter.CheckForVoterDeleted(table, voter.LastName, voter.FirstName, voter.Sr);

                //	Toast.makeText(getApplicationContext(),String.valueOf( lv.getCount()) , Toast.LENGTH_LONG).show();
                if (page_name.equals("Search_for_Part")) {
                    if (cursor != null && cursor.moveToFirst()) {
                        Intent i = new Intent(Family_Member_Listing.this, Search_Details_For_Deleted.class);
                        i.putExtra("Last", voter.LastName);
                        i.putExtra("First", voter.FirstName);
                        i.putExtra("Sr_no", voter.Sr);
                        i.putExtra("table", table);
                        i.putExtra("PartNo", voter.PartNo);
                        i.putExtra("ward_no", voter.WardNo);
                        i.putExtra("Search_type", "Part_Sr_Search");
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Family_Member_Listing.this, Search_Details_Part_Sr.class);
                        i.putExtra("PartNo", voter.PartNo);
                        i.putExtra("ward_no", voter.WardNo);
                        i.putExtra("Sr_no", voter.Sr);
                        startActivity(i);
                    }
                } else {
                    if (cursor != null && cursor.moveToFirst()) {
                        Intent i = new Intent(Family_Member_Listing.this, Search_Details_For_Deleted.class);
                        i.putExtra("Last", voter.LastName);
                        i.putExtra("First", voter.FirstName);
                        i.putExtra("Sr_no", voter.Sr);
                        i.putExtra("table", table);
                        i.putExtra("PartNo", voter.PartNo);
                        i.putExtra("ward_no", voter.WardNo);
                        i.putExtra("Search_type", "Surname_name_Search");
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Family_Member_Listing.this, Search_Details.class);
                        i.putExtra("Last", voter.LastName);
                        i.putExtra("First", voter.FirstName);
                        i.putExtra("Sr_no", voter.Sr);
                        i.putExtra("table", table);
                        startActivity(i);
                    }
                }

                cursor.close();

            }


        });

        lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

                lv.setScrollbarFadingEnabled(true);
            }


        });

        btnSendSMS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mob_no = edtSMS.getText().toString();

                if (edtSMS.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                } else if (edtSMS.length() < 10) {
                    Toast.makeText(getApplicationContext(), "You entered " + edtSMS.length() + " digit only.", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = mDbAdapter.fetchVoterFamilyPersonDetails(table, house_no, build_name, area_name, last_name);

                    if (cursor.moveToFirst()) {
                        do {

                            String first_name_marathi = cursor.getString(11);
                            String last_name_marathi = cursor.getString(12);
                            String name = "", strNameMarathi = "";


                            if (type.equals("surname") || type.equalsIgnoreCase("easy")) {
                                name = cursor.getString(1) + " " + cursor.getString(2);
                                strNameMarathi = last_name_marathi + " " + first_name_marathi;

                            } else if (type.equals("name")) {
                                name = cursor.getString(2) + " " + cursor.getString(1);
                                strNameMarathi = first_name_marathi + " " + last_name_marathi;

                            }

                            String vidhan = master_pref.getString("vishansabhas", "");
                            if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
                                String[] arrVidhan = vidhan.split(",");
                                for (int i = 0; i < arrVidhan.length; i++) {
                                    if (arrVidhan[i].contains(cursor.getString(0))) {
                                        vidhan = arrVidhan[i].replace("@", " ");
                                        break;
                                    }
                                }
                            } else {
                                vidhan = vidhan.replace("@", " ");
                            }
                            Message1 = name + ",Vidhansabha :  " + vidhan + ", "
                                    + "Part No : " + cursor.getString(3) + ", " + "Serial No : " + cursor.getString(4) + "\n";

                            Message2 = "Voting Center : " + cursor.getString(10);
                            Message3 = strNameMarathi + " मतदान केंद्र : " + cursor.getString(15);

                            if (!(cursor.getString(5).equalsIgnoreCase("D"))) {
                                sendSMS(mob_no, Message1);
                            }

                        } while (cursor.moveToNext());

                        cursor.close();
                    }

                    sendSMS(mob_no, Message2);
                    sendSMS(mob_no, Message3);

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
                        StringBuilder str = new StringBuilder();

                        Cursor cursor = mDbAdapter.fetchVoterFamilyPersonDetails(table, house_no, build_name, area_name, last_name);

                        if (cursor.moveToFirst()) {
                            do {

                                String name_for_mail = "";
                                String first_name_marathi = cursor.getString(11);
                                String last_name_marathi = cursor.getString(12);
                                String strNameMarathi = "";
                                if (cursor.getString(4).equalsIgnoreCase("D")) {
                                    if (type.equals("surname") || type.equalsIgnoreCase("easy")) {
                                        name_for_mail = "<font color='#FF0000'>" + cursor.getString(0) + " " + cursor.getString(1) +
                                                "---- DELETED </font>";
                                    } else if (type.equals("name")) {
                                        name_for_mail = "<font color='#FF0000'>" + cursor.getString(1) + " " + cursor.getString(0) +
                                                "---- DELETED </font>";
                                    }
                                } else {
                                    if (type.equals("surname") || type.equalsIgnoreCase("easy")) {
                                        name_for_mail = cursor.getString(1) + " " + cursor.getString(2);
                                        strNameMarathi = last_name_marathi + " "+ first_name_marathi;
                                    } else if (type.equals("name")) {
                                        name_for_mail = cursor.getString(2) + " " + cursor.getString(1);
                                        strNameMarathi = first_name_marathi + " "+ last_name_marathi;

                                    }
                                }
                                String vidhan = master_pref.getString("vishansabhas", "");
                                if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
                                    String[] arrVidhan = vidhan.split(",");
                                    for (int i = 0; i < arrVidhan.length; i++) {
                                        if (arrVidhan[i].contains(cursor.getString(0))) {
                                            vidhan = arrVidhan[i].replace("@", " ");
                                            break;
                                        }
                                    }
                                } else {
                                    vidhan = vidhan.replace("@", " ");
                                }

                                MsgForEmail = name_for_mail + ", <br></br>"
                                        +strNameMarathi + ", <br></br>"
                                        + "Vidhansabha : " + vidhan + ", <br></br>"
                                        + "Part No : " + cursor.getString(3) + ", <br></br>"
                                        + "Serial No : " + cursor.getString(4) + ", <br></br>"
                                        + "Voting Center : " + cursor.getString(10) + ", <br></br>"
                                        + "मतदान केंद्र : " + cursor.getString(15) + ", <br></br>";


                                str.append(MsgForEmail);
                                str.append("------------------------------------------------------------------------------------------------------- <br></br>");

                            } while (cursor.moveToNext());

                            cursor.close();
                        }

                        if (date_compare == 0 || date_compare == 1) {
                            //str.append(SMS + "ImageAttachment");
                            str.append(SMS);
                        }

                        //String FromEmailId = master_pref.getString("Email_Id", "");
                        // String password = master_pref.getString("Password_for_Email", "");
                        //String ToEmailId = edtEmail.getText().toString().trim();
                        // String strSubject = "Family Voter Details";
                        // String strBody = str.toString();
                        //String strImage =  master_pref.getString("Image_Name_for_mail", "");

                        async_For_Network_Operation(str.toString());
                    }

                } else {
                    showAlertDialog(Family_Member_Listing.this, "Unable to connect", "To proceed,\nPlease enabled internet connection.", false);
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

                editor.putString("Print_Data", printData(false));
                editor.putString("Print_Activity", "Family_Member_Listing");
                editor.commit();

                Intent i = new Intent(Family_Member_Listing.this, Printer_CIE_DYNO_4807.class);
                startActivity(i);
            }
        });


        btnShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editor.putString("Print_Data", printData(true));
                editor.putString("Print_Activity", "Family_Member_Listing");
                editor.commit();

                Intent i = new Intent(Family_Member_Listing.this, Share_Preview.class);
                i.putExtra("isSlipAttach", isSlipAttach);
                i.putExtra("vidhansabha", wardno);
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


    //---------------for Print and Share button--------------------------------------------------------------

    private String printData(boolean isShare) {
        String print_data = "";
        String Print_Data_Share = "";
        String election_date = "";
        String election_time = "";
        String limited_ad = "";
        String advertise = "";

        StringBuilder str = new StringBuilder();
        StringBuilder strShare = new StringBuilder();
        //---------------------------------------------------------------------------------------------------------------------------------
        //getting  record from voteAdd table
        String election_dateHtml = "", election_timeHtml = "", advertiseHtml = "";
        Cursor cur = mDbAdapter.GetAllVoterAdd();

        if (cur != null && cur.moveToFirst()) {
            int count = cur.getCount();

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
                    election_timeHtml = time;
                }
            }

            if ((cur.moveToPosition(count - 2))) ;
            {
                limited_ad = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_VOTE_ADD));
            }

            cur.close();
        }
        //---------------------------------------------------------------------------------------------------------------------------------
        if (limited_ad.equalsIgnoreCase("null")) {
            advertise = "";
        } else {
            // last date is greater or equal to current date
            if (date_compare == 0 || date_compare == 1) {
                isSlipAttach = true;
                advertise = "----------------------------\n" +
                        limited_ad + "\n\n";

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


        //Toast.makeText(getApplicationContext(), last_date + "-----------\n" + strDate, Toast.LENGTH_SHORT).show();
        //---------------------------------------------------------------------------------------------------------------------------------
        Cursor cursor = mDbAdapter.fetchVoterFamilyPersonDetails(table, house_no, build_name, area_name, last_name);

        if (cursor.moveToFirst()) {
            do {

                String name = "";
                String first_name_marathi = cursor.getString(11);
                String last_name_marathi = cursor.getString(12);
                String strNameMarathi = "";

                if (type.equals("surname") || type.equalsIgnoreCase("easy")) {
                    name = cursor.getString(1) + " " + cursor.getString(2);
                    strNameMarathi = last_name_marathi + " " + first_name_marathi;
                } else if (type.equals("name")) {
                    name = cursor.getString(2) + " " + cursor.getString(1);
                    strNameMarathi = first_name_marathi + " " + last_name_marathi;
                }

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
                    if (votingCenterAddress.length() > 50) {
                        votingCenterAddress = votingCenterAddress.substring(0, 50) + "-\n"
                                + votingCenterAddress.substring(50, votingCenterAddress.length());
                    }
                }

                String votingCenterAddressHTML = cursor.getString(10);
                if (!TextUtils.isEmpty(votingCenterAddress)) {
                    if (votingCenterAddress.length() > 50) {
                        votingCenterAddress = votingCenterAddress.substring(0, 50) + "-<br>&nbsp;"
                                + votingCenterAddress.substring(50, votingCenterAddress.length());
                    }
                }

                String votingAddressMarathi = cursor.getString(15);
                if (!TextUtils.isEmpty(votingAddressMarathi)) {
                    if (votingAddressMarathi.length() > 40) {
                        votingAddressMarathi = votingAddressMarathi.substring(0, 40) + "\n"
                                + votingAddressMarathi.substring(40, votingAddressMarathi.length());
                    }
                }
                String votingAddressMarathiHtml = cursor.getString(15);
                if (!TextUtils.isEmpty(votingAddressMarathiHtml)) {
                    if (votingAddressMarathiHtml.length() > 40) {
                        votingAddressMarathiHtml = votingAddressMarathiHtml.substring(0, 40) + "-<br>&nbsp;"
                                + votingAddressMarathiHtml.substring(40, votingAddressMarathiHtml.length());
                    }
                }

                String address_marathi_html =  cursor.getString(14) + "," +cursor.getString(13);
                if (!TextUtils.isEmpty(address_marathi_html)) {
                    if (address_marathi_html.length() > 50) {
                        address_marathi_html = address_marathi_html.substring(0, 50) + "-<br>&nbsp;" + address_marathi_html.substring(50, address_marathi_html.length());
                    }
                }

                String address_marathi =  cursor.getString(14) + "," +cursor.getString(13);
                if (!TextUtils.isEmpty(address_marathi)) {
                    if (address_marathi.length() > 50) {
                        address_marathi = address_marathi.substring(0, 50) + "\n" + address_marathi.substring(50, address_marathi.length());
                    }
                }

                String vidhan = master_pref.getString("vishansabhas", "");
                if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
                    String[] arrVidhan = vidhan.split(",");
                    for (int i = 0; i < arrVidhan.length; i++) {
                        if (arrVidhan[i].contains(cursor.getString(0))) {
                            vidhan = arrVidhan[i].replace("@", " ");
                            break;
                        }
                    }
                } else {
                    vidhan = vidhan.replace("@", " ");
                }
                /*print_data = "Voter's Name : " + name + "\n" +
                        "नाव : " + strNameMarathi + "\n" +
                        "Assembly : " + vidhan + "\n" +
                        "Part No : " + cursor.getString(3) + "      " +
                        "Sr. No : " + cursor.getString(4) + "\n" +
                        "Gender : " + cursor.getString(5) + "      " +
                        "Age : " + cursor.getString(6) + "\n" +
                        "Address : " + cursor.getString(9) + "," + "\n" + areaName + "," + "\n" + cursor.getString(8) + "\n" +
                        "पत्ता : " + address_marathi + "\n" +
                        "----------------------------\n\n" +
                        "Voting Center : " + votingCenterAddress + "\n" +
                        "मतदान केंद्र पत्ता : " + votingAddressMarathi + "\n" +
                        election_date + " " + election_time + "\n" +
                        advertise + "\n" +
                        "---------- Cut Here --------\n\n";
*/
                print_data = "Voter's Name : " + name + "\n" +
                       "Assembly : " + vidhan + "\n" +
                        "Part No : " + cursor.getString(3) + "      " +
                        "Sr. No : " + cursor.getString(4) + "\n" +
                        "Gender : " + cursor.getString(5) + "      " +
                        "Age : " + cursor.getString(6) + "\n" +
                        "Address : " + cursor.getString(9) + "," + "\n" + areaName + "," + "\n" + cursor.getString(8) + "\n" +
                        "----------------------------\n\n" +
                        "Voting Center : " + votingCenterAddress + "\n" +
                        election_date + " " + election_time + "\n" +
                        advertise + "\n" +
                        "---------- Cut Here --------\n\n";

                Print_Data_Share = "<br><font color='blue'>&nbsp;Voter's Name :</font>  <b><font color='blue'>" + name + "</font></b><br>" +
                        "<font color='blue'>&nbsp;नाव :</font>  <b><font color='blue'>" + strNameMarathi + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Assembly :</font> <b><font color='red'>" + vidhan + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Part No :</font> <b><font color='red'>" + cursor.getString(3) + "</font></b>    " +
                        "<font color='blue'>&nbsp;&nbsp;Sr. No :</font> <b><font color='red'>" + cursor.getString(4) + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Gender :</font> <b><font color='blue'>" + cursor.getString(5) + "</font></b>      " +
                        "<font color='blue'>&nbsp;&nbsp;Age :</font> <b><font color='blue'>" + cursor.getString(6) + "</font></b><br>" +
                        "<font color='blue'>&nbsp;Address :</font> <font color='blue'>" + cursor.getString(9) + "," + " " + areaNameHTML + "," + "<br>" + cursor.getString(8) + "</font><br>" +
                        "<font color='blue'>&nbsp;पत्ता :</font> <b><font color='blue'>" + address_marathi_html + "</font></b>  <br>    " +
                        "<font color='red'>&nbsp;-------------------------------------------------------</font><br>" +
                        "<font color='blue'>&nbsp;Voting Center :</font> <font color='red'>" + votingCenterAddressHTML + "</font><br>" +
                        "<font color='blue'>&nbsp;मतदान केंद्र पत्ता :</font> <font color='red'>" + votingAddressMarathiHtml + "</font><br>" +

                        election_dateHtml + " " + election_timeHtml + "<br>" +
                        advertiseHtml;

                str.append(print_data);
                strShare.append(Print_Data_Share);

            } while (cursor.moveToNext());

            cursor.close();
        }
        if (isShare) {
            return strShare.toString();
        } else {
            return str.toString();
        }
    }

    //---------------for Email--------------------------------------------------------------------------------

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


    //--------------------------------------------------------------------------------------------------------

    private void async_For_Network_Operation(final String strBody) {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            String result;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(Family_Member_Listing.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... arg0) {
                result = sendEmail(strBody);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                progressDialog.dismiss();

                if (result.equals("False")) {
                    Toast.makeText(Family_Member_Listing.this, "Problem occurred while sending Email.\n" +
                            "Check internet connection or entered email Id.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Family_Member_Listing.this, "Mail Send Successfully.", Toast.LENGTH_SHORT).show();
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
                msg.setSubject("Family Voter Details");
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

        cursor = mDbAdapter.fetchVoterFamilyPersonName(table, house_no, build_name, area_name, last_name);

        if (cursor.moveToFirst()) {
            do {

                String last = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_LASTNAME));
                String first = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_FIRST_NAME));
                String sr_no = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_SR_NO));
                String deleted = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_SEX));
                String part_no = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
                String ward_no = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_WARD_NO));
                String first_marathi = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_MFIRST_NAME));
                String last_marathi = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.key_MLASTNAME));


                Voter voter = new Voter();
                voter.LastName = last;
                voter.FirstName = first;
                voter.LastNameMarathi = last_marathi;
                voter.FirstNameMarathi = first_marathi;
                voter.Sr = sr_no;
                voter.PartNo = part_no;
                voter.WardNo = ward_no;
                voter.Sr = sr_no + "," + part_no + "," + ward_no;

                if (deleted.equalsIgnoreCase("D")) {
                    if (type.equals("surname") || type.equals("building_fullvidhasabha") || type.equalsIgnoreCase("easy")) {
                        voter.Name = Html.fromHtml("<font color='#FF0000'>" + voter.LastName + " " + voter.FirstName + "- Deleted</font>");
                        voter.NameMarathi = Html.fromHtml("<font color='#FF0000'>" + voter.LastNameMarathi + " " + voter.FirstNameMarathi + "- Deleted</font>");

                    } else if (type.equals("name")) {
                        voter.Name = Html.fromHtml("<font color='#FF0000'>" + voter.FirstName + " " + voter.LastName + "- Deleted</font>");
                        voter.NameMarathi = Html.fromHtml("<font color='#FF0000'>" + voter.FirstNameMarathi + " " + voter.LastNameMarathi + "- Deleted</font>");

                    }
                } else {
                    if (type.equals("surname") || type.equals("building_fullvidhasabha") || type.equalsIgnoreCase("easy")) {
                        voter.Name = Html.fromHtml(voter.LastName + " " + voter.FirstName);
                        voter.NameMarathi = Html.fromHtml(voter.LastNameMarathi + " " + voter.FirstNameMarathi);
                    } else if (type.equals("name")) {
                        voter.Name = Html.fromHtml(voter.FirstName + " " + voter.LastName);
                        voter.NameMarathi = Html.fromHtml(voter.FirstNameMarathi + " " + voter.LastNameMarathi);
                    }
                }


                listDatas.add(voter);

            } while (cursor.moveToNext());

        }
        cursor.close();


        //adapter = new ArrayAdapter<String>(this, R.layout.list_item_surname_search, R.id.txtRawName, results);

    }


    private class VoterAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return listDatas.size();
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
                holder.nameMarathi = (TextView) convertView
                        .findViewById(R.id.txtRawNameMarathi);
                convertView.setTag(holder);
            } else {
                holder = (VoterViewHolder) convertView.getTag();
            }
            holder.name.setText(getItem(position).Name);
            holder.sr.setText(getItem(position).Sr);
            holder.nameMarathi.setText(getItem(position).NameMarathi);
            return convertView;
        }

    }


    private static class VoterViewHolder {
        public TextView name, nameMarathi;
        public TextView sr;
    }


    public class Voter {

        public String LastName,LastNameMarathi;
        public String FirstName, FirstNameMarathi;
        public Spanned Name, NameMarathi;
        public String Sr;
        public String PartNo;
        public String WardNo;
    }


    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Family_Member_Listing.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            GenerateList();
            adapter = new VoterAdapter();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            lv.setAdapter(adapter);
            lv.setTextFilterEnabled(true);
            lv.setFastScrollEnabled(true);
            progressDialog.dismiss();
            //for setting the size of listView
            setListViewHeightBasedOnChildren(lv);

        }
    }


}
