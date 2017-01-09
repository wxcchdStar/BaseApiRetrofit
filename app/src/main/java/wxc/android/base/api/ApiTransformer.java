package wxc.android.base.api;

import android.text.TextUtils;
import android.util.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ApiTransformer<T> implements Observable.Transformer<ApiResult<T>, T> {

    @SuppressWarnings("unchecked")
    @Override
    public Observable<T> call(Observable<ApiResult<T>> observable) {
        return observable
                .map(new Func1<ApiResult<T>, T>() {
                    @Override
                    public T call(ApiResult<T> apiResult) {
                        if (apiResult.isOk() || apiResult.isIgnore()) {
                            return apiResult.mData;
                        }
                        if (TextUtils.isEmpty(apiResult.mMessage) && apiResult.mData instanceof String) {
                            apiResult.mMessage = (String) apiResult.mData;
                        }
                        throw new ApiResultException(apiResult.mStatus, apiResult.mMessage);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, T>() {
                    @Override
                    public T call(Throwable throwable) {
                        Log.e("Api", "OnError:" + throwable);
                        if (throwable instanceof Exception) {
                            throw new RuntimeException(throwable);
                        }
                        return null;
                    }
                });
    }
}
