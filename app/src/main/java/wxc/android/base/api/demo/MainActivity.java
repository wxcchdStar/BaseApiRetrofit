package wxc.android.base.api.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import wxc.android.base.api.ApiTransformer;
import wxc.android.base.api.ProgressSubscriber;
import wxc.android.base.api.R;

public class MainActivity extends AppCompatActivity {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api.TestParams params = new Api.TestParams();
        params.param1 = "param1";
        params.param2 = 2;

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
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
