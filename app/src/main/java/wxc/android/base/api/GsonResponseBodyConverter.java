package wxc.android.base.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type adapter) {
        this.gson = gson;
        this.type = adapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(ResponseBody value) throws IOException {
        String responseStr = value.string();

        if ("class java.lang.String".equals(type.toString())) {
            return (T) responseStr;
        }

        T t = null;
        try {
            t = gson.fromJson(responseStr, type);
        } catch (Exception e) {
            Log.w("Api", "parse response error: " + e);
        } finally {
            value.close();
        }
        if (t != null) {
            return t;
        }

        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            ApiResult apiResult = new ApiResult();
            apiResult.mStatus = jsonObject.optString("status");
            apiResult.mMessage = jsonObject.optString("results");
            return (T) apiResult;
        } catch (JSONException e) {
            Log.w("Api", "parse response error again: " + e);
            throw new JsonSyntaxException(e);
        }
    }

}
