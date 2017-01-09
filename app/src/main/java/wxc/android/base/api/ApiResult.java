package wxc.android.base.api;

import com.google.gson.annotations.SerializedName;

public class ApiResult<T> {
    public static final String CODE_IGNORE = "99999";

    @SerializedName("status")
    public String mStatus;

    @SerializedName("message")
    public String mMessage;

    @SerializedName("results")
    public T mData;

    public boolean isOk() {
        return "1".equals(mStatus);
    }

    public boolean isIgnore() {
        return CODE_IGNORE.equalsIgnoreCase(mStatus);
    }

    public void setIgnore() {
        mStatus = CODE_IGNORE;
    }
}
