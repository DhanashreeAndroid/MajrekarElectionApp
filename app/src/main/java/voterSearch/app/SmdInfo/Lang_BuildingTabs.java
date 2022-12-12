package voterSearch.app.SmdInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lang_BuildingTabs extends FragmentActivity {


	DBAdapter mDbAdapter;
	final Context context = this;
	
	TextView txt1,txt2,txt3;

	Cursor cursor;
	public String buildName= "";
	TabLayout tabLayout;
	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		setContentView(R.layout.activity_building_lang_tab);

		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt1.setVisibility(View.VISIBLE);
		txt2.setVisibility(View.VISIBLE);
		txt3.setVisibility(View.VISIBLE);
		Typeface font1 = Typeface.createFromAsset(getAssets(), "FUTURALC.TTF");  
		txt1.setTypeface(font1); 
				
		Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.loto_font));
		txt2.setTypeface(font2);
		txt3.setTypeface(font2);

		tabLayout = findViewById(R.id.tabs_boards);
		viewPager = findViewById(R.id.viewpager_courses);
		buildName = getIntent().getExtras().getString("build_name");
		TextView txtBuild = findViewById(R.id.txt_build_name);
		txtBuild.setText(buildName);
		GenerateList();

		tabLayout.getTabAt(0).select();

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				viewPager.setCurrentItem(tab.getPosition());
				selectTab(tab);
			}

			private void selectTab(TabLayout.Tab tab) {
				((Lang_Fragment) ((ViewPagerAdapterPerformance) viewPager.getAdapter())
						.getItem(tab.getPosition())).refresh(tab.getText().toString());

			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				if (tab.getPosition() == 0) {
					selectTab(tab);
					tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
						@Override
						public void onTabSelected(TabLayout.Tab tab) {
							selectTab(tab);
						}

						@Override
						public void onTabReselected(TabLayout.Tab arg0) {
						}

						@Override
						public void onTabUnselected(TabLayout.Tab arg0) {
						}
					});

				}
			}
		});


	}



	public void GenerateList() {
			cursor = mDbAdapter.GetLanguages(buildName);
		ViewPagerAdapterPerformance adapterPerformance = new ViewPagerAdapterPerformance(getSupportFragmentManager());
		ArrayList<String> arrTabs = new ArrayList<>();
		if (cursor.moveToFirst()) {
				do {
					String lang = cursor.getString(0);
					adapterPerformance.addFragment(new Lang_Fragment(), lang);
					arrTabs.add(lang);
				} while (cursor.moveToNext());

			}
			cursor.close();
		viewPager.setAdapter(adapterPerformance);
		tabLayout.setupWithViewPager(viewPager);
		for(int i =0; i<arrTabs.size(); i++){
			tabLayout.getTabAt(i).setTag(arrTabs.get(i));
		}
	}


	class ViewPagerAdapterPerformance extends FragmentStatePagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public ViewPagerAdapterPerformance(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title) {
			if (title.contains("_")) {
				title = title.replace("_", " ");
			}
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}


		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}
}
