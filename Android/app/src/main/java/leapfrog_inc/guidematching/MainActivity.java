package leapfrog_inc.guidematching;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import leapfrog_inc.guidematching.Fragment.Initial.SplashFragment;
import leapfrog_inc.guidematching.Http.Requester.LoginRequester;
import leapfrog_inc.guidematching.System.GalleryManager;
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

    public int getSubContainerId() {
        return R.id.subContainer;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GalleryManager.requestCodePermission) {
            GalleryManager.getInstance().didGrantPermission(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == GalleryManager.requestCodeGallery)
                && (resultCode == RESULT_OK)) {
            GalleryManager.getInstance().didSelectImage(data);
        }
    }
}
