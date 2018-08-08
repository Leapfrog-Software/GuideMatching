package leapfrog_inc.guidematching;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import leapfrog_inc.guidematching.Fragment.Initial.SplashFragment;
import leapfrog_inc.guidematching.Http.Requester.LoginRequester;
import leapfrog_inc.guidematching.System.SaveData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SaveData.getInstance().initialize(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rootContainer, new SplashFragment());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();

        SaveData saveData = SaveData.getInstance();
        if ((saveData.guideId.length() > 0) || (saveData.guestId.length() > 0)) {
            LoginRequester.login();
        }
    }
}
