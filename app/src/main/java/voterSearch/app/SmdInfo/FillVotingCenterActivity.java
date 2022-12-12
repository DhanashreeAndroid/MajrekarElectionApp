package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FillVotingCenterActivity extends Activity implements View.OnClickListener {

    DBAdapter mDbAdapter;
    EditText edtVotingcenter, edtVotingAdd, edtWardNo, edtSrFrom, edtSrTo;
    Button btnSave, btnViewAll;
    TextView txtSaveMessage;
    String mPrimaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();

        setContentView(R.layout.activity_fill_voting_center);

        Initialization();
        setListeners();
        FillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillData();
    }

    private void Initialization() {
        edtVotingcenter = (EditText) findViewById(R.id.edtVotingcenter);
        edtVotingAdd = (EditText) findViewById(R.id.edtVotingAdd);
        edtWardNo = (EditText) findViewById(R.id.edtWardNo);
        edtSrFrom = (EditText) findViewById(R.id.edtSrFrom);
        edtSrTo = (EditText) findViewById(R.id.edtSrTo);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnViewAll = (Button) findViewById(R.id.btnViewAll);
        txtSaveMessage = (TextView) findViewById(R.id.txtSaveMessage);
      /*  Typeface font1 = Typeface.createFromAsset(getAssets(), "LIP01N.TTF");
        txtSaveMessage.setTypeface(font1);
        txtSaveMessage.setText("am¬ti rimQi¬>gir rimQi[>gir bi gieSvim}");*/

    }

    private void  saveData(){
        String votingCenter = edtVotingcenter.getText().toString();
        String votingAdd = edtVotingAdd.getText().toString();
        if (votingAdd.contains(",")) {
            votingAdd = votingAdd.replace(",", ".");
        }
        String wardNo = edtWardNo.getText().toString();
        String srFrom = edtSrFrom.getText().toString();
        String srTo = edtSrTo.getText().toString();
        FillVotingCenterVo vo = new FillVotingCenterVo();
        vo.setPrimaryId(mPrimaryId);
        vo.setVotingCenter(votingCenter);
        vo.setWardNo(wardNo);
        vo.setAddress(votingAdd);
        vo.setSrFrom(srFrom);
        vo.setSrUpTo(srTo);
        txtSaveMessage.setMovementMethod(new ScrollingMovementMethod());
        if(TextUtils.isEmpty(votingCenter) || TextUtils.isEmpty(votingAdd) || TextUtils.isEmpty(wardNo) ||
                TextUtils.isEmpty(srFrom) || TextUtils.isEmpty(srTo)){
            Toast.makeText(this,"Please fill all details", Toast.LENGTH_SHORT).show();
        }else {
            String fromToAvailability = mDbAdapter.checkSerialNoAvailability(srFrom,srTo);
            if(TextUtils.isEmpty(fromToAvailability)) {
                long insertCount = mDbAdapter.save_UpdateVotingAddress(vo);
                if (insertCount != -1) {
                    txtSaveMessage.setText(Html.fromHtml("Voting Center No: " + "<b>" + votingCenter + "</b> " + "<br>" +
                            "Ward No:" + "<b>" + wardNo + "</b>" + "<br>" +
                            "Voting Address:" + "<b>" + votingAdd + "</b>" + "<br>" +
                            "Sr. From: " + "<b>" + srFrom + "</b>" + "<br>" +
                            "Sr. To: " + "<b>" + srTo + "</b>" + "<br>" + "Successfully saved."));
                    edtVotingcenter.setText("");
                    edtVotingAdd.setText("");
                    edtWardNo.setText("");
                    edtSrFrom.setText("");
                    edtSrTo.setText("");
                } else {
                    txtSaveMessage.setText("Some error occured, Data not saved.");
                }
            }else{
                txtSaveMessage.setText(fromToAvailability);
            }
        }
    }

    private void setListeners(){
        btnSave.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSave){
            saveData();
        }else if(v == btnViewAll){
            Intent i = new Intent(FillVotingCenterActivity.this, FillVotingCenterListActivity.class);
            startActivity(i);
        }
    }

    private void FillData(){
        Bundle extras;
        extras = getIntent().getExtras();
        if(extras != null) {
            FillVotingCenterVo vo = (FillVotingCenterVo) extras.getSerializable("FillVotingCenterVo");
            edtVotingcenter.setText(vo.getVotingCenter());
            edtSrFrom.setText(vo.getSrFrom());
            edtSrTo.setText(vo.getSrUpTo());
            edtVotingAdd.setText(vo.getAddress());
            edtWardNo.setText(vo.getWardNo());
            mPrimaryId = vo.getPrimaryId();
            //edtVotingcenter.setEnabled(false);
            //edtWardNo.setEnabled(false);
        }
    }

}
