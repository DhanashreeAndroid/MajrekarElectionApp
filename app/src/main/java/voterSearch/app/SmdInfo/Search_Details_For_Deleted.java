package voterSearch.app.SmdInfo;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Search_Details_For_Deleted extends Activity {
    DBAdapter mDbAdapter;
    final Context context = this;
    TextView txtName, txtAddress, txtAgeGender, txtVidhansabha, txtPartNo, txtSerialNo, txtVotingCenterAdd;
    TextView txt1, txt2, txt3, txtmsg1, txtmsg2, txtsms, txtemail;

    String name, add, age, Gender, vidhansabha, partno, serialno, votingadd;
    String house_no, biuld_name, area_name, last_name, SMS;
    String strFirstName, strLastName, strSrNo, strNumber, strPartNo, strWardNo, strtable, vidhan_no, Search_type;

    HashMap<String, String> temp = new HashMap<String, String>();

    public ProgressDialog progressDialog;

    SharedPreferences pref;

    Button btnNext, btnPrevious;
    TextView txtNext, txtPrevious;

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setContentView(R.layout.search_details_for_deleted);

        pref = getApplicationContext().getSharedPreferences("Search_type", 0);

        Bundle extras = getIntent().getExtras();
        strLastName = extras.getString("Last");
        strFirstName = extras.getString("First");
        strNumber = extras.getString("Sr_no");
        strtable = extras.getString("table");
        Search_type = extras.getString("Search_type");

        strSrNo = extras.getString("Sr_no");
        strPartNo = extras.getString("PartNo");
        strWardNo = extras.getString("ward_no");

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

        txtNext = (TextView) findViewById(R.id.txtNext);
        txtPrevious = (TextView) findViewById(R.id.txtPrevious);

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

        Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
        txt2.setTypeface(font2);
        txt3.setTypeface(font2);

        Cursor cur = mDbAdapter.GetAllVoterAdd();

        if (cur != null && cur.moveToFirst()) {
            int count = cur.getCount();
            cur.moveToPosition(count - 4);

            vidhan_no = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
        }
        cur.close();

        LoadData load = new LoadData();
        load.execute();

        if (Search_type.equals("Surname_name_Search")) {
            txtNext.setVisibility(View.INVISIBLE);
            txtPrevious.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            btnPrevious.setVisibility(View.INVISIBLE);
        } else {
            txtNext.setVisibility(View.VISIBLE);
            txtPrevious.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrevious.setVisibility(View.VISIBLE);
        }

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

                Cursor cur = mDbAdapter.fetchVoterDetailsByPartSr(SrNo, partno, cursor.getString(1));
                cursor.close();
                if (cur.getString(5).equalsIgnoreCase("D")) {
                    Intent i = new Intent(Search_Details_For_Deleted.this, Search_Details_For_Deleted.class);
                    i.putExtra("Last", cur.getString(3));
                    i.putExtra("First", cur.getString(4));
                    i.putExtra("Sr_no", cur.getString(2));
                    i.putExtra("table", cur.getString(11));
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("ward_no", cur.getString(0));
                    i.putExtra("Search_type", "Part_Sr_Search");
                    startActivity(i);
                } else {
                    finish();
                    Intent i = new Intent(Search_Details_For_Deleted.this, Search_Details_Part_Sr.class);
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("Sr_no", SrNo);
                    i.putExtra("ward_no", cur.getString(0));
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

                Cursor cur = mDbAdapter.fetchVoterDetailsByPartSr(SrNo, partno, cursor.getString(1));
                cursor.close();
                if (cur.getString(5).equalsIgnoreCase("D")) {
                    Intent i = new Intent(Search_Details_For_Deleted.this, Search_Details_For_Deleted.class);
                    i.putExtra("Last", cur.getString(3));
                    i.putExtra("First", cur.getString(4));
                    i.putExtra("Sr_no", cur.getString(2));
                    i.putExtra("table", cur.getString(11));
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("ward_no", cur.getString(0));
                    i.putExtra("Search_type", "Part_Sr_Search");
                    startActivity(i);
                } else {
                    finish();
                    Intent i = new Intent(Search_Details_For_Deleted.this, Search_Details_Part_Sr.class);
                    i.putExtra("PartNo", cur.getString(1));
                    i.putExtra("Sr_no", SrNo);
                    i.putExtra("ward_no", cur.getString(0));
                    startActivity(i);
                }

            }
        });

    }


    public void GenerateList() {

        Cursor cursor = mDbAdapter.fetchVoterDetailsByName(strtable, strLastName, strFirstName, strSrNo, strPartNo, strWardNo);

        if (cursor.moveToFirst()) {
            do {
                //key_VIDHAN_NO,key_PART_NO,key_SR_NO,key_VOTE_ADD,
                // key_SEX,key_AGE,key_BUILD_NAME,key_AREA_NAME,key_HOUSE_NO,key_SMS

                if (pref.getString("type", "").equals("surname") || pref.getString("type", "").equals("easy")) {
                    temp.put("Name", strLastName + " " + strFirstName);
                } else if (pref.getString("type", "").equals("name")) {
                    temp.put("Name", strFirstName + " " + strLastName);
                } else {
                    temp.put("Name", strLastName + " " + strFirstName);
                }

                temp.put("House_no", cursor.getString(7));
                temp.put("Build_name", cursor.getString(5));
                temp.put("Area_name", cursor.getString(6));
                temp.put("Age", cursor.getString(4));
                temp.put("Gender", cursor.getString(3));
                temp.put("Vidhansabha", cursor.getString(0));
                temp.put("PartNo", cursor.getString(1));
                temp.put("SerialNo", cursor.getString(2));
                temp.put("VotingCenterAdd", cursor.getString(8));

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
        SharedPreferences master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);
        String vidhan = master_pref.getString("vishansabhas", "");
        if (!TextUtils.isEmpty(vidhan) && vidhan.contains(",")) {
            String[] arrVidhan = vidhan.split(",");
            for (int i = 0; i < arrVidhan.length; i++) {
                if (arrVidhan[i].contains(vidhansabha)) {
                    vidhan = arrVidhan[i].replace("@", " ");
                    break;
                }
            }
        } else {
            vidhan = vidhan.replace("@", " ");
        }
        txtVidhansabha.setText(Html.fromHtml("Vishansabha : " + "<b>" + vidhan + "</b> "));
        txtPartNo.setText(Html.fromHtml("Part No : " + "<b>" + partno + "</b> "));
        txtSerialNo.setText(Html.fromHtml("Serial No : " + "<b>" + serialno + "</b> "));
        txtVotingCenterAdd.setText(Html.fromHtml("<u>" + "Voting Center Address : " + "</u>" + "<br> <b>" + votingadd + "</b> "));


    }

    private class LoadData extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Search_Details_For_Deleted.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            GenerateList();


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            setData();

        }
    }
}
