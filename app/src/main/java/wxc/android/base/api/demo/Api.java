package wxc.android.base.api.demo;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;
import wxc.android.base.api.ApiResult;
import wxc.android.base.api.RequestParams;
import wxc.android.base.api.RxApiClient;

public class Api {

    private static volatile Api sInstance;

    private InternalApi mApi;

    private Api() {
        RxApiClient.setBaseUrl("http://api.demo.com/v1/");

        mApi = RxApiClient.get().createApi(InternalApi.class);
    }

    public static InternalApi get() {
        if (sInstance == null) {
            synchronized (Api.class) {
                if (sInstance == null) {
                    sInstance = new Api();
                }
            }
        }
        return sInstance.mApi;
    }

    public static class TestParams implements RequestParams {
        public String param1;
        public int param2;
    }

    public interface InternalApi {

        @POST("test")
        Observable<ApiResult<String>> test(@Body TestParams params);
    }

}
