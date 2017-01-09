package wxc.android.base.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class RxApiClient {
    private static String sBaseUrl;

    private static volatile RxApiClient sInstance;

    private Retrofit mRetrofit;

    private RxApiClient() {
        if (sBaseUrl == null) {
            throw new IllegalArgumentException("The sBaseUrl is NULL!");
        }

        mRetrofit = createBuilder().build();
    }

    public static RxApiClient get() {
        if (sInstance == null) {
            synchronized (RxApiClient.class) {
                if (sInstance == null) {
                    sInstance = new RxApiClient();
                }
            }
        }
        return sInstance;
    }

    private Retrofit.Builder createBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logger);
        }
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(15, TimeUnit.SECONDS); // 默认10s

        return new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    public <T> T createApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public static void setBaseUrl(String url) {
        sBaseUrl = url;
    }

}
