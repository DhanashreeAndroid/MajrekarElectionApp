package voterSearch.app.SmdInfo;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class First_Page extends Activity {

    SharedPreferences master_pref;

    String str;
    DBAdapter mDbAdapter;
    TextView txt1, txt2, txt3, txtVidhan;
    Button btnSurNameWiseSearch, btnNameWiseSearch, btnEasySearch, btnPartSrSearch,
            btnBuildingSearch, btnPass, btnAlphabeticalVoter,btnSearchByLang;
    final Context context = this;
    String WARD_NO, PART_NO, SR_NO, SEX, AGE, BUILD_NAME, AREA_NAME, HOUSE_NO, FIRST_NAME, LASTNAME;
    String PART_NO1, VOTE_ADD;
    public ProgressDialog progressDialog;
    Cursor cursor;
    Dialog dialog;
    Dialog dialogSmsSetting;
    EditText edtPass, edtSms1, edtSms2, edtSms3;
    Button btnSms1, btnSms2, btnSms3, btnNext, btnOtherInfo;

    CheckBox chkMain, chkSms1, chkSms2, chkSms3;
    int total = 0;

    ArrayAdapter<String> dataAdapter;

    static final String[] alphabets = new String[]{"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    SharedPreferences pref;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.first_page);

        master_pref = getApplicationContext().getSharedPreferences("Master_Values", 0);

        //---------------------------------------------------------------------------------------------------------------------------------
        txt1 = (TextView) findViewById(R.id.txtHeader);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt1.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        txt3.setVisibility(View.VISIBLE);
        txtVidhan = (TextView) findViewById(R.id.txtVidhan);
        btnSurNameWiseSearch = (Button) findViewById(R.id.btnSurNameWiseSearch);
        btnNameWiseSearch = (Button) findViewById(R.id.btnNameWiseSearch);
        btnEasySearch = (Button) findViewById(R.id.btnEasySearch);
        btnPartSrSearch = (Button) findViewById(R.id.btnPartSrNoWiseSearch);
        btnBuildingSearch = (Button) findViewById(R.id.btnBuildingWiseSearch);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnOtherInfo = (Button) findViewById(R.id.btnOtherInfo);
        btnAlphabeticalVoter = (Button) findViewById(R.id.btnAlphabeticalVoter);
        btnSearchByLang = (Button) findViewById(R.id.btnSearchByLang);
        btnPartSrSearch.setVisibility(View.GONE);
        //---------------------------------------------------------------------------------------------------------------------------------
        Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
        txt1.setTypeface(font1);
        txtVidhan.setTypeface(font1);

        Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
        txt2.setTypeface(font2);
        txt3.setTypeface(font2);

        //---------------------------------------------------------------------------------------------------------------------------------
        pref = getApplicationContext().getSharedPreferences("Search_type", 0);
        editor = pref.edit();
        //---------------------------------------------------------------------------------------------------------------------------------
        // event handlers for try Dialog for password
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password_initial);

        edtPass = (EditText) dialog.findViewById(R.id.edtPass);
        btnPass = (Button) dialog.findViewById(R.id.btnPass);

        //---------------------------------------------------------------------------------------------------------------------------------
        Cursor cur = mDbAdapter.GetAllVoterAdd();

        if (cur != null && cur.moveToFirst()) {
		/*	int count = cur.getCount();
			cur.moveToPosition(count - 4);
			txtVidhan.setText(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO)) + " Voters Search");*/

            String vidhan = master_pref.getString("vishansabhas", "");
            vidhan = vidhan.replace("@", " ");
            txtVidhan.setText(vidhan);

            cur.close();
            btnSurNameWiseSearch.setVisibility(View.VISIBLE);
            btnNameWiseSearch.setVisibility(View.VISIBLE);
            btnEasySearch.setVisibility(View.VISIBLE);
            btnPartSrSearch.setVisibility(View.GONE);
            btnBuildingSearch.setVisibility(View.VISIBLE);
            btnAlphabeticalVoter.setVisibility(View.VISIBLE);
            btnOtherInfo.setVisibility(View.VISIBLE);
            btnSearchByLang.setVisibility(View.GONE);
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnSurNameWiseSearch.setVisibility(View.INVISIBLE);
            btnNameWiseSearch.setVisibility(View.INVISIBLE);
            btnEasySearch.setVisibility(View.INVISIBLE);
            btnPartSrSearch.setVisibility(View.INVISIBLE);
            btnBuildingSearch.setVisibility(View.INVISIBLE);
            btnAlphabeticalVoter.setVisibility(View.INVISIBLE);
            btnOtherInfo.setVisibility(View.INVISIBLE);
            btnSearchByLang.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }


        //---------------------------------------------------------------------------------------------------------------------------------

        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                cursor = mDbAdapter.GetAllVoterAdd();

                if (cursor != null && cursor.moveToFirst()) {
                    cursor.close();
                    LoadData gd = new LoadData();
                    gd.execute();
                } else {
                    dialog.show();
                }

            }
        });

        //---------------------------------------------------------------------------------------------------------------------------------
        btnSurNameWiseSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                editor.putString("type", "surname");
                editor.commit();

                /*Intent i = new Intent(First_Page.this, Alphabet_Listing.class);
                startActivity(i);*/
                Intent i = new Intent (First_Page.this,Voter_Listing.class);
                i.putExtra("alphabet", "a");
                startActivity(i);
            }
        });

        //---------------------------------------------------------------------------------------------------------------------------------

        btnNameWiseSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editor.putString("type", "name");

                editor.commit();

                /*Intent i = new Intent(First_Page.this, Alphabet_Listing.class);
                startActivity(i);*/
                Intent i = new Intent (First_Page.this,Voter_Listing.class);
                i.putExtra("alphabet", "a");
                startActivity(i);
            }
        });
        btnSearchByLang.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent (First_Page.this,Lang_BuildingList.class);
                startActivity(i);
            }
        });

        //---------------------------------------------------------------------------------------------------------------------------------

        btnEasySearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editor.putString("type", "easy");

                editor.commit();

                Intent i = new Intent(First_Page.this, EasySearchFilter.class);
                startActivity(i);
            }
        });

        //---------------------------------------------------------------------------------------------------------------------------------
        btnPartSrSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                editor.putString("type", "surname");

                editor.commit();

                Intent i = new Intent(First_Page.this, Search_Details_Part_Sr_Filter.class);
                startActivity(i);

            }
        });

        btnBuildingSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                editor.putString("type", "surname");

                editor.commit();

                Intent i = new Intent(First_Page.this, Building_Filter.class);
                startActivity(i);

            }
        });

        btnAlphabeticalVoter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent i = new Intent(First_Page.this, Alpha_VoterList_first_Filter.class);
                startActivity(i);

            }
        });
        //---------------------------------------------------------------------------------------------------------------------------------
        btnPass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String pass = edtPass.getText().toString();
                if (pass.equalsIgnoreCase(master_pref.getString("Password_for_Next", ""))) {
                    dialog.dismiss();
                    edtPass.setText("");

                    LoadData gd = new LoadData();
                    gd.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Sorry password not correct", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnOtherInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(First_Page.this, Other_Imp_Info.class);
                startActivity(i);


            }
        });

    }//-----------------------------------onCreate closing----------------------------------------------------


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            return true;
        }
        return false;

    }


    //--------------------------------------------------------------------------------------------------------
    private int count() {
        int total1 = 0;
        int total2 = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("EnglishMarathiData.csv")));
            while (br.readLine() != null) {
                total1 = total1 + 1;
            }

            BufferedReader br27 = new BufferedReader(new InputStreamReader(getAssets().open("VotingAddress.csv")));
            while (br27.readLine() != null) {
                total2 = total2 + 1;
            }

        } catch (Exception e) {

        }

        int total = total1 + total2 ;

        return total;

    }


    //--------------------------------------------------------------------------------------------------------------------
    private class LoadData extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(First_Page.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
            cursor = mDbAdapter.GetAllVoterAdd();


        }

        @Override
        protected void onProgressUpdate(Integer... Values) {
            super.onProgressUpdate(Values);

            progressDialog.setMessage("Initializing...( " + (Values[0] * 100) / total + " % )");
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            total = count();
            if (cursor != null && cursor.moveToFirst()) {
                cursor.close();
            } else {

                try {

                    mDbAdapter.mDb.beginTransaction();
                    mDbAdapter.mDb.setLockingEnabled(false);

                    //-----------(TABLE - Surname Data Import)-----------------------------------------------------------------

                    int count1 = 0;

					/*for(int i = 0; i<alphabets.length; i++)
					{
						
						String strCsv = "S"+ alphabets[i] + ".CSV";
						String table = "Stable" + alphabets[i];
										
								BufferedReader br1=new BufferedReader(new 
							    InputStreamReader(getAssets().open(strCsv)));
								String line1 = "";
								while ((line1 = br1.readLine()) != null) {
									line1 = line1.replace("\"", "");
									String[] str1 = line1.split(",");
								    
									WARD_NO 	= str1[0];
								    PART_NO 	= str1[1];
								    SR_NO		= str1[2];
								    SEX			= str1[3];
								    AGE 		= str1[4];
								    BUILD_NAME 	= str1[5];
								    AREA_NAME	= str1[6];
								    HOUSE_NO	= str1[7];
								   	// if last name is empty			   			    
								    if(str1.length == 9)
								    {
								       	FIRST_NAME	= str1[8]; 
								    }
								    // if last name and first name both empty
								    if(str1.length == 10)
								    {
								    	FIRST_NAME	= str1[8]; 
								      	LASTNAME	= str1[9];
								    }
								    				    
								     mDbAdapter.createTable(table,WARD_NO,PART_NO, SR_NO, SEX, AGE, BUILD_NAME, AREA_NAME, HOUSE_NO,FIRST_NAME,LASTNAME) ;
								     count1 = count1 + 1;
								     publishProgress(count1);
								   }
					
					}
				
					//-----------(TABLE - Name Data Import)-----------------------------------------------------------------
					
					for(int i = 0; i<alphabets.length; i++)
					{
						
						String strCsv = "N"+ alphabets[i] + ".CSV";
						String table = "Ntable" + alphabets[i];
										
								BufferedReader br1=new BufferedReader(new 
							    InputStreamReader(getAssets().open(strCsv)));
								String line1 = "";
								while ((line1 = br1.readLine()) != null) {
									line1 = line1.replace("\"", "");
									String[] str1 = line1.split(",");
								    
									WARD_NO 	= str1[0];
								    PART_NO 	= str1[1];
								    SR_NO		= str1[2];
								    SEX			= str1[3];
								    AGE 		= str1[4];
								    BUILD_NAME 	= str1[5];
								    AREA_NAME	= str1[6];
								    HOUSE_NO	= str1[7];
								   	// if last name is empty			   			    
								    if(str1.length == 9)
								    {
								       	FIRST_NAME	= str1[8]; 
								    }
								    // if last name and first name both empty
								    if(str1.length == 10)
								    {
								    	FIRST_NAME	= str1[8]; 
								      	LASTNAME	= str1[9];
								    }
								    				    
								     mDbAdapter.createTable(table,WARD_NO,PART_NO, SR_NO, SEX, AGE, BUILD_NAME, AREA_NAME, HOUSE_NO,FIRST_NAME,LASTNAME) ;
								     count1 = count1 + 1;
								     publishProgress(count1);
								   }
					
					}*/


                    //------------ (Table - FullVidhansabha) ----------------------------------------------
                    String key_MFIRST_NAME, key_MLASTNAME, key_MADDRESS,
                            key_MHOUSE_NO, key_ACC_NO, key_ORG_PART_NO,
                            key_ORG_SERIAL_NO, key_CARD_NO, key_SECTION_NO,
                            key_MOBILE_NO, key_LANG;

                    BufferedReader br1 = new BufferedReader(new
                            InputStreamReader(getAssets().open("EnglishMarathiData.csv"), Charset.forName("UTF-16")));
                    String line1 = "";
                    while ((line1 = br1.readLine()) != null) {
                        line1 = line1.replace("\"", "");
                        String[] str1 = line1.split(",");
                        Log.d("line" , line1);
                        WARD_NO = str1[0];
                        PART_NO = str1[1];
                        SR_NO = str1[2];
                        SEX = str1[3];
                        AGE = str1[4];
                        BUILD_NAME = str1[5];
                        AREA_NAME = ".";
                        HOUSE_NO = str1[6];
                        FIRST_NAME = str1[7];
                        LASTNAME = str1[8];
                        key_MADDRESS = str1[9];
                        key_MHOUSE_NO = str1[10];
                        key_MFIRST_NAME = str1[11];
                        key_MLASTNAME = str1[12];
                        key_ACC_NO = str1[13];
                        key_ORG_PART_NO = str1[14];
                        key_ORG_SERIAL_NO = str1[15];
                        key_CARD_NO = str1[16];
                        key_SECTION_NO = str1[17];
                        key_MOBILE_NO = str1[18];
                        key_LANG = str1[19];

                        // mDbAdapter.createTableFullVidhansabha("FullVidhansabha",WARD_NO,PART_NO, SR_NO, SEX, AGE, BUILD_NAME, AREA_NAME, HOUSE_NO,FIRST_NAME,LASTNAME,MfirstName, MLastName,MAddress) ;
                        mDbAdapter.createTable("FullVidhansabha", WARD_NO, PART_NO, SR_NO, SEX, AGE, BUILD_NAME, AREA_NAME, HOUSE_NO, FIRST_NAME, LASTNAME,
                                key_MFIRST_NAME, key_MLASTNAME, key_MADDRESS,
                                key_MHOUSE_NO, key_ACC_NO, key_ORG_PART_NO,
                                key_ORG_SERIAL_NO, key_CARD_NO, key_SECTION_NO,
                                key_MOBILE_NO, key_LANG);
                        count1 = count1 + 1;
                        publishProgress(count1);
                    }

                    //--------------Voting Address---------------------------------------------

                    BufferedReader br = new BufferedReader(new
                            InputStreamReader(getAssets().open("VotingAddress.csv")));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        line = line.replace("\"", "");
                        String[] str = line.split(",");
                        Log.d("line" , line);
                        if (str.length == 3) {
                            WARD_NO = str[0];
                            PART_NO = str[1];
                            VOTE_ADD = str[2];

                            mDbAdapter.createVoteAdd(WARD_NO, PART_NO, VOTE_ADD, "n");
                            count1 = count1 + 1;
                        } else {
                            WARD_NO = "";
                            PART_NO = str[0];
                            VOTE_ADD = str[1];

                            mDbAdapter.createVoteAdd(WARD_NO, PART_NO, VOTE_ADD, "x");
                            count1 = count1 + 1;

                        }
                        publishProgress(count1);
                    }
                    //previous code
						/*try
							{
								int myNum = 0;
							    myNum = Integer.parseInt(str[0]);
							    WARD_NO		=str[0];
							    PART_NO		=str[1];
								VOTE_ADD	=str[2];
												   
								mDbAdapter.createVoteAdd(WARD_NO,PART_NO ,VOTE_ADD, "n") ;
								count1 = count1 + 1;
							    
							} 
							catch(NumberFormatException nfe) 
							{
								 WARD_NO		=str[0];
								 PART_NO		=str[1];
								 VOTE_ADD		=str[2];
													   
								 mDbAdapter.createVoteAdd(WARD_NO,PART_NO ,VOTE_ADD, "x") ;
								 count1 = count1 + 1;
							}*/


                    //-----------------------------------------
                  //  if(!isFileExists()){
                    BufferedReader brU = new BufferedReader(new
                            InputStreamReader(getAssets().open("UpdateVotingAddress.csv"), Charset.forName("UTF-16")));
                    String lineU = "";
                    while ((lineU = brU.readLine()) != null) {
                        lineU = lineU.replace("\"", "");
                        String[] str1 = lineU.split(",");
                        Log.d("line", lineU);
                        String WARD_NO = str1[0];
                        String PART_NO = str1[1];
                        String VOTE_ADD = str1[4];
                        String SR_FROM = str1[2];
                        String SR_UPTO = str1[3];
                        String VOTE_ADD_MARATHI = str1[5];

                        mDbAdapter.insert_UpdateVotingAddress(WARD_NO, PART_NO, VOTE_ADD, SR_FROM, SR_UPTO, VOTE_ADD_MARATHI);
                        count1 = count1 + 1;
                        publishProgress(count1);
                    }

                    BufferedReader brB = new BufferedReader(new
                            InputStreamReader(getAssets().open("UpdateVotingAddress.csv"), Charset.forName("UTF-16")));
                    String lineB = "";
                    while ((lineB = brB.readLine()) != null) {
                        lineB = lineB.replace("\"", "");
                        String[] str1 = lineB.split(",");
                        Log.d("line", lineB);
                        String WARD_NO = str1[0];
                        String PART_NO = str1[1];
                        String VOTE_ADD = str1[4];
                        String SR_FROM = str1[2];
                        String SR_UPTO = str1[3];
                        String VOTE_ADD_MARATHI = str1[5];

                        mDbAdapter.updateBoothNo(PART_NO,SR_FROM,SR_UPTO);
                        count1 = count1 + 1;
                        publishProgress(count1);
                    }
                   // }
                    mDbAdapter.mDb.setTransactionSuccessful();
                    mDbAdapter.mDb.endTransaction();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Sorry problem occurred", Toast.LENGTH_SHORT).show();
                }

            }

            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        	
        	/*Cursor cur = mDbAdapter.GetAllVoterAdd();
    		
        	if(cur != null && cur.moveToFirst())
    		{
    			
        		int count = cur.getCount();
    			cur.moveToPosition(count - 4);
    			txtVidhan.setText(cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO)) + " Voters Search");
    			cur.close();
    		}*/

            String vidhan = master_pref.getString("vishansabhas", "");
            vidhan = vidhan.replace("@", " ");
            txtVidhan.setText(vidhan);

            btnSurNameWiseSearch.setVisibility(View.VISIBLE);
            btnNameWiseSearch.setVisibility(View.VISIBLE);
            btnEasySearch.setVisibility(View.VISIBLE);
            btnPartSrSearch.setVisibility(View.GONE);
            btnBuildingSearch.setVisibility(View.VISIBLE);
            btnAlphabeticalVoter.setVisibility(View.VISIBLE);
            btnOtherInfo.setVisibility(View.VISIBLE);
            btnSearchByLang.setVisibility(View.GONE);
            btnNext.setVisibility(View.INVISIBLE);

        }
    }

    private Boolean isFileExists(){
        File root = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String bt = root.getPath() +  "/UpdateVotingAddress.csv";
        BufferedReader br1 = null;
        int count =0;
        try {
            File file = new File(bt);
            if(file.exists()) {
                //first delete all record
                mDbAdapter.delete_UpdateVotingAddress();
                br1 = new BufferedReader(new InputStreamReader(
                        new FileInputStream(bt), Charset.forName("UTF-16")) );
                String line1 = "";
                while ((line1 = br1.readLine()) != null) {
                    line1 = line1.replace("\"", "");
                    String[] str1 = line1.split(",");

                    String PART_NO = str1[1];
                    String WARD_NO = str1[0];
                    String VOTE_ADD = str1[4];
                    String SR_FROM = str1[2];
                    String SR_UPTO = str1[3];
                    String VOTE_ADD_MARATHI = str1[5];

                    mDbAdapter.insert_UpdateVotingAddress(WARD_NO, PART_NO, VOTE_ADD, SR_FROM, SR_UPTO,VOTE_ADD_MARATHI);
                 }

                while ((line1 = br1.readLine()) != null) {
                    line1 = line1.replace("\"", "");
                    String[] str1 = line1.split(",");

                    String PART_NO = str1[1];
                    String WARD_NO = str1[0];
                    String VOTE_ADD = str1[4];
                    String SR_FROM = str1[2];
                    String SR_UPTO = str1[3];
                    String VOTE_ADD_MARATHI = str1[5];
                    mDbAdapter.updateBoothNo(PART_NO,SR_FROM,SR_UPTO);
                }
                return true  ;
            }else{
                return false  ;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
