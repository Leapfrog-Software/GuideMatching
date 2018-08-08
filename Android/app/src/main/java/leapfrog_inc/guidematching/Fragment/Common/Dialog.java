package leapfrog_inc.guidematching.Fragment.Common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.MainActivity;
import leapfrog_inc.guidematching.R;

public class Dialog extends BaseFragment {

    public enum Style {
        success,
        error
    }

    private MainActivity mMainActivity;
    private Style mStyle;
    private String mTitle;
    private String mMessage;
    private DialogCallback mCallback;

    public static void show(FragmentActivity activity, Style style, String title, String message, DialogCallback callback) {

        if (!(activity instanceof MainActivity)) {
            return;
        }

        MainActivity mainActivity = (MainActivity)activity;
        Dialog loading = new Dialog();
        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(mainActivity.getSubContainerId(), loading);
        transaction.commitAllowingStateLoss();

        loading.mMainActivity = mainActivity;
        loading.mStyle = style;
        loading.mTitle = title;
        loading.mMessage = message;
        loading.mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_dialog, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        if (mStyle == Style.success) {
            ((ImageView)view.findViewById(R.id.successImageView)).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.errorImageView)).setVisibility(View.GONE);
            ((FrameLayout)view.findViewById(R.id.successButtonLayout)).setVisibility(View.VISIBLE);
            ((FrameLayout)view.findViewById(R.id.errorButtonLayout)).setVisibility(View.GONE);
        } else {
            ((ImageView)view.findViewById(R.id.successImageView)).setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.errorImageView)).setVisibility(View.VISIBLE);
            ((FrameLayout)view.findViewById(R.id.successButtonLayout)).setVisibility(View.GONE);
            ((FrameLayout)view.findViewById(R.id.errorButtonLayout)).setVisibility(View.VISIBLE);
        }

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mTitle);
        ((TextView)view.findViewById(R.id.messageTextView)).setText(mMessage);
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.successOkButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        ((Button)view.findViewById(R.id.errorOkButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    private void close() {

        if (mMainActivity == null) {
            return;
        }

        if (mCallback != null) {
            mCallback.didClose();
        }

        FragmentTransaction transaction = mMainActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commitAllowingStateLoss();
    }

    public interface DialogCallback {
        void didClose();
    }
}
