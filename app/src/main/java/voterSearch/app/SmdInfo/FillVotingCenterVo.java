package voterSearch.app.SmdInfo;

import android.text.Spanned;

import java.io.Serializable;

/**
 * Created by Kamlesh on 12/4/2016.
 */
public class FillVotingCenterVo implements Serializable {

    public String mPrimaryId;
    public String mVotingCenter;
    public String mWardNo;
    public String mAddress;
    public String mSrFrom;
    public String mSrUpTo;

    public String getPrimaryId() {
        return mPrimaryId;
    }

    public void setPrimaryId(String mPrimaryId) {
        this.mPrimaryId = mPrimaryId;
    }

    public String getVotingCenter() {
        return mVotingCenter;
    }

    public void setVotingCenter(String mVotingCenter) {
        this.mVotingCenter = mVotingCenter;
    }

    public String getWardNo() {
        return mWardNo;
    }

    public void setWardNo(String mWardNo) {
        this.mWardNo = mWardNo;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getSrFrom() {
        return mSrFrom;
    }

    public void setSrFrom(String mSrFrom) {
        this.mSrFrom = mSrFrom;
    }

    public String getSrUpTo() {
        return mSrUpTo;
    }

    public void setSrUpTo(String mSrUpTo) {
        this.mSrUpTo = mSrUpTo;
    }
}
