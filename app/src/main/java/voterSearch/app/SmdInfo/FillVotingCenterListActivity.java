package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FillVotingCenterListActivity extends Activity implements FillVotingCentreIntrface, View.OnClickListener {

    DBAdapter mDbAdapter;
    ListView lv;
    FillVotingCenterAdapter mAdapter;
    ArrayList<FillVotingCenterVo> list;
    Button mBtnShare, mBtnUpdate;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();

        setContentView(R.layout.activity_fill_voting_center_list);

        Initialization();
        setListener();
        LoadData load = new LoadData();
        load.execute();
    }

    private void Initialization() {
        lv = (ListView) findViewById(R.id.lv_votingCenter);
        mAdapter = new FillVotingCenterAdapter(FillVotingCenterListActivity.this, this);
        mBtnUpdate = (Button) findViewById(R.id.btnVotingCenterUpdate);
        mBtnShare = (Button) findViewById(R.id.btnVotingCenterShare);
    }

    private void setListener() {
        mBtnUpdate.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
    }


    @Override
    public void onEdit(FillVotingCenterVo vo) {
        Intent i = new Intent(this, FillVotingCenterActivity.class);
        i.putExtra("FillVotingCenterVo", vo);
        startActivity(i);
    }

    @Override
    public void onDelete(FillVotingCenterVo vo, int position) {
        mDbAdapter.delete_VotingAddress_centerWise(vo.getPrimaryId());
        list.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnShare) {

            Uri u1 = null;
            u1 = Uri.fromFile(exportDatabaseToCsv());

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Voting Address file");
            sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
            sendIntent.setType("text/html");
            startActivity(Intent.createChooser(sendIntent, "Send your image"));

        } else if (v == mBtnUpdate) {
            Update_All_VotingCenter_Ward update = new Update_All_VotingCenter_Ward();
            update.execute();
        }
    }

    private class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            list = mDbAdapter.getFillVotingCenter();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            mAdapter.setData(list);
            lv.setAdapter(mAdapter);
            lv.setTextFilterEnabled(true);
            lv.setFastScrollEnabled(true);

            if(list != null && list.size() > 0){
                mBtnShare.setEnabled(true);
                mBtnShare.setBackgroundColor(Color.BLACK);
            }else{
                mBtnShare.setEnabled(false);
                mBtnShare.setBackgroundColor(Color.GRAY);
            }
        }
    }

    private class Update_All_VotingCenter_Ward extends AsyncTask<String, Integer, String> {

        int count = 1;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FillVotingCenterListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Updating...(1)");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... Values) {
            super.onProgressUpdate(Values);

            progressDialog.setMessage("Updating...( " + Values[0] + ")");
        }


        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < list.size(); i++) {
                //  mDbAdapter.mDb.beginTransaction();
                // mDbAdapter.mDb.setLockingEnabled(false);
                mDbAdapter.updateAllVotingCenter_WardNo(list.get(i).getVotingCenter(),
                        list.get(i).getWardNo(),
                        list.get(i).getSrFrom(),
                        list.get(i).getSrUpTo());
                //mDbAdapter.mDb.setTransactionSuccessful();
                //mDbAdapter.mDb.endTransaction();
                count = count + 1;
                publishProgress(count);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            progressDialog.dismiss();
        }
    }

    public File exportDatabaseToCsv() {


        /**First of all we check if the external storage of the device is available for writing.
         * Remember that the external storage is not necessarily the sd card. Very often it is
         * the device storage.
         */
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return null;
        } else {
            //We use the Download directory for saving our .csv file.
            File exportDir = Environment.getExternalStorageDirectory();
            exportDir = new File(exportDir + "/CreatedVotingAdreess");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = null;
            PrintWriter printWriter = null;
            try {
                file = new File(exportDir, "UpdateVotingAddress.csv");
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));

                /**This is our database connector class that reads the data from the database.
                 * The code of this class is omitted for brevity.
                 */

                //for combine part No
                ArrayList<FillVotingCenterVo> list = mDbAdapter.getFillVotingCenter();
                //PART_NO,WARD_NO,VOTE_ADD,SR_FROM,SR_UPTO

                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        String address = list.get(i).getAddress();
                        if (address.contains(",")) {
                            address = address.replace(",", ".");
                        }
                        String record = list.get(i).getVotingCenter()
                                + "," + list.get(i).getWardNo()
                                + "," + address
                                + "," + list.get(i).getSrFrom()
                                + "," + list.get(i).getSrUpTo();
                        printWriter.println(record); //write the record in the .csv file
                    }
                }

            } catch (Exception exc) {
                //if there are any exceptions, return false

            } finally {
                if (printWriter != null) printWriter.close();
            }

            return file;
            //If there are no errors, return true.

        }
    }


}
