# BaseApiRetrofit
RxJava+Retrofit2+Okhttp3

```
// request api
Subscription subscription = Api.get().test(params)
  .compose(new ApiTransformer<String>())
  .subscribe(new ProgressSubscriber<>(this, new Observer<String>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onNext(String s) {
        Log.d("test", "Api result: " + s);
      }
}));
mCompositeSubscription.add(subscription);

// cancel api
mCompositeSubscription.unsubscribe();
```
