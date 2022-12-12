package voterSearch.app.SmdInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kamlesh on 12/4/2016.
 */

public class FillVotingCenterAdapter extends BaseAdapter  {

    ArrayList<FillVotingCenterVo> mList;
    Context mContext;
    FillVotingCentreIntrface mListener;

    public FillVotingCenterAdapter(Context context,  FillVotingCentreIntrface listener){
        this.mContext = context;
        this.mListener = listener;
    }

    public void setData(ArrayList<FillVotingCenterVo> list){
        this. mList = list;
    }

    @Override
    public int getCount() {
        if(mList.size() != 0) {
            return mList.size();
        }else{
            return 0;
        }
    }


    @Override
    public FillVotingCenterVo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        VoterViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    mContext).inflate(
                    R.layout.fillvotingcenter_listitem, null);
            holder = new VoterViewHolder();
            holder.txtVotingCenter = (TextView) convertView
                    .findViewById(R.id.itemVotingcenter);
            holder.txtWard = (TextView) convertView
                    .findViewById(R.id.itemWard);
            holder.txtAddress= (TextView) convertView
                    .findViewById(R.id.itemVotingAddress);
            holder.txtSrFrom = (TextView) convertView
                    .findViewById(R.id.itemSrFrom);
            holder.txtSrTo = (TextView) convertView
                    .findViewById(R.id.itemSrUpTo);
            holder.btnEdit= (Button) convertView
                    .findViewById(R.id.btnEdit);
            holder.btnDelete = (Button) convertView
                    .findViewById(R.id.btnDelete);
            Utility.setSelectorRoundedCorner(mContext, holder.btnEdit,1, R.color.transparent_bg,R.color.gray,R.color.DimGray,R.color.DimGray,6);
            Utility.setSelectorRoundedCorner(mContext, holder.btnDelete,1, R.color.transparent_bg,R.color.gray,R.color.DimGray,R.color.DimGray,4);

            convertView.setTag(holder);
        } else {
            holder = (VoterViewHolder) convertView.getTag();
        }
        holder.txtVotingCenter.setText(getItem(position).mVotingCenter);
        holder.txtWard.setText(getItem(position).mWardNo);
        holder.txtAddress.setText("Address : " + getItem(position).mAddress);
        holder.txtSrFrom.setText(getItem(position).mSrFrom);
        holder.txtSrTo.setText(getItem(position).mSrUpTo);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mListener.onEdit(getItem(position));
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDelete(getItem(position), position);
            }
        });

        return convertView;
    }

}

 final class VoterViewHolder {
    public TextView txtVotingCenter, txtWard, txtSrFrom, txtSrTo,txtAddress;
    public Button btnEdit, btnDelete;
}

