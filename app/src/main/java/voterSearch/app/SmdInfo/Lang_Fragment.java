package voterSearch.app.SmdInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Lang_Fragment extends Fragment {

    public Lang_Fragment() {
        // Required empty public constructor
    }

    ListView lv;
    public ProgressDialog progressDialog;
    public ArrayList<Voter> listDatas = new ArrayList<Voter>();
    public VoterAdapter adapter;
    DBAdapter mDbAdapter;
    Cursor cursor;
    String langName;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lang_tab , container , false);
        lv = view.findViewById(R.id.lv);
        mDbAdapter = new DBAdapter(getActivity());
        mDbAdapter.open();
        pref = requireContext().getSharedPreferences("Search_type", 0);
        editor = pref.edit();
        editor.putString("type", "language");



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

                Voter voter = (Voter) lv.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), Voter_Listing.class);
                editor.putString("surname", voter.LastName);
                editor.commit();
                i.putExtra("alphabet", "a");//not used
                startActivity(i);

            }
        });

        return view;
    }

    public void refresh(String Lang) {
        langName = Lang;
        listDatas.clear();
        editor.putString("language", langName);
        editor.putString("buildingName", ((Lang_BuildingTabs)getActivity()).buildName);
        editor.commit();
        LoadData load = new LoadData();
        load.execute();
    }

    public void GenerateList() {

        cursor = mDbAdapter.getSurnameAndCountForLang(
                ((Lang_BuildingTabs)getActivity()).buildName,langName
        );

        if (cursor.moveToFirst()) {
            do {
                String last = cursor.getString(0);
                String count = cursor.getString(1);


                Voter voter = new Voter();
                voter.LastName = last;
                voter.count = count;

                listDatas.add(voter);

            } while (cursor.moveToNext());

        }
        cursor.close();

    }


    private class VoterAdapter extends BaseAdapter  {


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
                convertView = LayoutInflater.from(requireActivity())
                        .inflate(R.layout.list_item_surname_count_lang, null);
                holder = new VoterViewHolder();
                holder.name = (TextView) convertView
                        .findViewById(R.id.txtRawName);
                holder.count = (TextView) convertView.findViewById(R.id.txtCount);
                convertView.setTag(holder);
            } else {
                holder = (VoterViewHolder) convertView.getTag();
            }
            //Typeface font1 = Typeface.createFromAsset(getAssets(), "LIP01N.TTF");
            holder.name.setText(getItem(position).LastName);
            holder.count.setText("Total Voters : " + getItem(position).count);
            holder.name.setTextColor(Color.BLACK);
            return convertView;
        }

    }

    private static class VoterViewHolder {
        public TextView name, count;
    }

    public class Voter {

        public String LastName,count;

    }

    private class LoadData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
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

        }
    }


}