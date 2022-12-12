package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EasySearchFilter extends Activity {
	EditText edtSurname, edtName;
	Button btnSearch;
	DBAdapter mDbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		setProgressBarIndeterminateVisibility(true);
		setContentView(R.layout.easy_search_filter);

		edtSurname = (EditText) findViewById(R.id.edtSurname);
		edtName = (EditText) findViewById(R.id.edtName);		
		btnSearch = (Button) findViewById(R.id.btnSearch);

		edtSurname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (edtSurname.length() < 3) {
					edtSurname.setError(Html
							.fromHtml("<font color =\'black\'>Please enter minimum three character</font>"));
				}else{
					edtSurname.setError(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		edtName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (edtName.length() < 3) {
					edtName.setError(Html
							.fromHtml("<font color =\'black\'>Please enter minimum three character</font>"));
				}else{
					edtName.setError(null);
					
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edtSurname.length() < 3
						|| edtName.length() < 3) {
					Toast.makeText(getApplicationContext(), "Please enter minimum three character.", Toast.LENGTH_SHORT).show();
				} else {
				
					SharedPreferences pref;
					Editor editor;

					pref = getApplicationContext().getSharedPreferences("easy",
							0);
					editor = pref.edit();
					editor.putString("surname", edtSurname.getText().toString());
					editor.putString("name", edtName.getText().toString());
					
					editor.commit();

					Intent i = new Intent(EasySearchFilter.this,
							Voter_Listing.class);
					startActivity(i);
				}

			}
		});
	}
}
