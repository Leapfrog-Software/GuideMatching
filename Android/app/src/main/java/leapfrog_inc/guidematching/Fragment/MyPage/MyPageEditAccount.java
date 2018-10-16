package leapfrog_inc.guidematching.Fragment.MyPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Fragment.Guide.GuideDetailFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.UpdateGuideRequester;
import leapfrog_inc.guidematching.MainActivity;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.SaveData;

public class MyPageEditAccount extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_mypage_edit_account, null);

        initAction(view);
        initContents(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapRegister();
            }
        });
    }

    private void initContents(View view) {

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);

        ((EditText)view.findViewById(R.id.nameEditText)).setText(myGuideData.bankAccount.name);
        ((EditText)view.findViewById(R.id.kanaEditText)).setText(myGuideData.bankAccount.kana);
        ((EditText)view.findViewById(R.id.bankEditText)).setText(myGuideData.bankAccount.bankName);
        ((EditText)view.findViewById(R.id.bankBranchEditText)).setText(myGuideData.bankAccount.bankBranchName);
        ((EditText)view.findViewById(R.id.accountTypeEditText)).setText(myGuideData.bankAccount.accountType);
        ((EditText)view.findViewById(R.id.accountNumberEditText)).setText(myGuideData.bankAccount.accountNumber);
    }

    private void onTapRegister() {

        View view = getView();

        String name = ((EditText)view.findViewById(R.id.nameEditText)).getText().toString();
        String kana = ((EditText)view.findViewById(R.id.kanaEditText)).getText().toString();
        String bank = ((EditText)view.findViewById(R.id.bankEditText)).getText().toString();
        String bankBranch = ((EditText)view.findViewById(R.id.bankBranchEditText)).getText().toString();
        String accountType = ((EditText)view.findViewById(R.id.accountTypeEditText)).getText().toString();
        String accountNumber = ((EditText)view.findViewById(R.id.accountNumberEditText)).getText().toString();

        GuideData myGuideData = FetchGuideRequester.getInstance().query(SaveData.getInstance().guideId);
        myGuideData.bankAccount.name = name;
        myGuideData.bankAccount.kana = kana;
        myGuideData.bankAccount.bankName = bank;
        myGuideData.bankAccount.bankBranchName = bankBranch;
        myGuideData.bankAccount.accountType = accountType;
        myGuideData.bankAccount.accountNumber = accountNumber;

        Loading.start(getActivity());

        UpdateGuideRequester.update(myGuideData, new UpdateGuideRequester.Callback() {
            @Override
            public void didReceiveData(final boolean resultUpdate) {

                FetchGuideRequester.getInstance().fetch(new FetchGuideRequester.Callback() {
                    @Override
                    public void didReceiveData(boolean resultFetch) {

                        Loading.stop(getActivity());

                        if ((resultUpdate) && (resultFetch)) {
                            success();
                        } else {
                            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", null);
                        }
                    }
                });
            }
        });
    }

    private void success() {

        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            BaseFragment fragment = (BaseFragment) fragments.get(i);
            if (fragment instanceof MyPagePaymentFragment) {
//                ((MyPagePaymentFragment)fragment).resetListView(null);
            }
        }

        Dialog.show(getActivity(), Dialog.Style.success, "確認", "登録が完了しました", new Dialog.DialogCallback() {
            @Override
            public void didClose() {
                popFragment(AnimationType.horizontal);
            }
        });
    }

}
