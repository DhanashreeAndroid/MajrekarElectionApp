package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Other_Imp_Info extends Activity {
    DBAdapter mDbAdapter;

    Button btnCounter, btnFillVotingCenter, btnUpdateVotingCenter;
    String Vidhan;

    TextView txt1, txt2, txt3;

    final Context context = this;

    public ProgressDialog progressDialog;

    String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();
        setContentView(R.layout.other_imp_info);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt1.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        txt3.setVisibility(View.VISIBLE);
        btnCounter = (Button) findViewById(R.id.btnVoterCount);
        btnFillVotingCenter = (Button) findViewById(R.id.btnFillVotingCenter);
        btnUpdateVotingCenter = (Button) findViewById(R.id.btnUpdateVotingCenter);

        Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");
        txt1.setTypeface(font1);

        Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
        txt2.setTypeface(font2);
        txt3.setTypeface(font2);

        btnFillVotingCenter.setVisibility(View.GONE);
        btnUpdateVotingCenter.setVisibility(View.VISIBLE);
        /*progressDialog = new ProgressDialog(Other_Imp_Info.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();*/

        //--------------------------------------------------------------------------------------------------------
        //LoadData load = new LoadData();
        //load.execute();
        //--------------------------------------------------------------------------------------------------------

        Cursor cur = mDbAdapter.GetAllVoterAdd();

        if (cur != null && cur.moveToFirst()) {
            int count = cur.getCount();
            cur.moveToPosition(count - 4);
            Vidhan = cur.getString(cur.getColumnIndexOrThrow(DBAdapter.key_PART_NO));
            cur.close();
        } else {
            Vidhan = "";
        }


        //-----------------------------------------------------------------

        Button_Click();

    }//----------------------------------------------------------------------------------------------------------------------------


    public void Button_Click() {

        btnCounter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Vidhan.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please import records first", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Other_Imp_Info.this, Voter_Count.class);
                    i.putExtra("Vidhan", Vidhan);
                    startActivity(i);
                }
            }
        });
        //-------------------------------------------------------------------------------------------------------------------

        btnFillVotingCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Other_Imp_Info.this, FillVotingCenterActivity.class);
               startActivity(i);

                //String dbName = "MajrekarDB";
               /* File dbFile = getApplicationContext().getDatabasePath(dbName);
                if (dbFile.exists()) {
                    try {
                        File internalDbPath = new File(Environment.getExternalStorageDirectory(), "internalDB");
                        if (!internalDbPath.exists()) {
                            internalDbPath.mkdir();
                        }
                        String destinationPath = internalDbPath.getAbsolutePath() + File.separator + dbName;
                        copyFileFromSourceToDestn(dbFile.getAbsolutePath(), destinationPath, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
*/

            }
        });



        btnUpdateVotingCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadUpdateVotingAddress load = new LoadUpdateVotingAddress();
                load.execute();

            }
        });


    }

    public static boolean copyFileFromSourceToDestn(String sPath, String dPath, boolean isDeleteSourceDir) {
        try {
            FileInputStream fis = new FileInputStream(new File(sPath));
            File dir = new File(dPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outfile = new File(dPath);
            outfile.delete();
            if (!outfile.exists()) {
                outfile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outfile);
            byte[] block = new byte[1000];
            int i;
            while ((i = fis.read(block)) != -1) {
                fos.write(block, 0, i);
            }
            fos.close();
            if (isDeleteSourceDir) {
                deleteDir(new File(sPath));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }


    private class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... params) {
            return searchForFile();
        }

        @Override
        protected void onPostExecute(final String result) {
            progressDialog.dismiss();
            mFilePath = result;
            if (TextUtils.isEmpty(result)) {
                btnUpdateVotingCenter.setEnabled(false);
                btnUpdateVotingCenter.setBackgroundColor(Color.GRAY);
            } else {
                btnUpdateVotingCenter.setEnabled(true);
                btnUpdateVotingCenter.setBackgroundColor(Color.BLACK);
            }
        }
    }

    private class LoadUpdateVotingAddress extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Other_Imp_Info.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... Values) {
            super.onProgressUpdate(Values);

            progressDialog.setMessage("Initializing...( " + (String.valueOf(Values)) + ")" );
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            File root = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String bt = root.getPath() +  "/UpdateVotingAddress.csv";
            BufferedReader br1 = null;
            int count =0;
            try {
                mDbAdapter.mDb.beginTransaction();
                mDbAdapter.mDb.setLockingEnabled(false);
                File file = new File(bt);
                if(file.exists()) {
                    //first delete all record
                    mDbAdapter.delete_UpdateVotingAddress();
                    br1 = new BufferedReader(new InputStreamReader(
                            new FileInputStream(bt), StandardCharsets.UTF_16) );
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
                        count = count + 1;
                        publishProgress(count);
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
                        count = count + 1;
                        publishProgress(count);
                    }

                }
                mDbAdapter.mDb.setTransactionSuccessful();
                mDbAdapter.mDb.endTransaction();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final String result) {

            progressDialog.dismiss();
        }
    }


    public String searchForFile() {
        String splitchar = "/";
        File root = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        List<File> btFolder = null;
        String bt = "UpdateVotingAddress.csv";
        String filePath = "";
        try {
            //btFolder = folderSearchBT(root, bt);
            filePath = getFilePath(root, bt);
        } catch (FileNotFoundException e) {
            Log.e("FILE: ", e.getMessage());
        }

       /* for (int i = 0; i < btFolder.size(); i++) {

            String g = btFolder.get(i).toString();

            String[] subf = g.split(splitchar);

            String s = subf[subf.length - 1].toUpperCase();
            boolean equals = false;
            if(!s.equalsIgnoreCase("CreatedVotingAdreess")) {
                 equals = s.equalsIgnoreCase(bt);
            }

            if (equals)
                return g;
        }*/
        return filePath; // not found
    }

    public List<File> folderSearchBT(File src, String folder)
            throws FileNotFoundException {

        List<File> result = new ArrayList<File>();

        File[] filesAndDirs = src.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);

        for (File file : filesDirs) {
            result.add(file); // always add, even if directory
            if (!file.isFile()) {
                List<File> deeperList = folderSearchBT(file, folder);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    public String getFilePath(File src, String folder)
            throws FileNotFoundException {

        String filePath  = "";

        String filesAndDirs[] = src.list();
        for(int i=0; i<filesAndDirs.length; i++) {
            if(filesAndDirs[i].equalsIgnoreCase("UpdateVotingAddress.csv")){
                filePath = src.getPath() + "/UpdateVotingAddress.csv";
            }
        }
        return filePath;
    }


}
