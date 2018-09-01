package leapfrog_inc.guidematching.Fragment.Guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.StripePaymentSource;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import java.util.Calendar;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Common.Dialog;
import leapfrog_inc.guidematching.Fragment.Common.Loading;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.Requester.CreateReserveRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Stripe.StripeManager;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class CardInputFragment extends BaseFragment {

    private GuideData mGuideData;
    private Calendar mDate;
    private int mStartIndex;
    private int mEndIndex;
    private String mMeetingPlace;
    private int mGuideFee;
    private int mTransactionFee;

    public void set(GuideData guideData, Calendar date, int startIndex, int endIndex, String meetingPlace, int guideFee, int transactionFee) {
        mGuideData = guideData;
        mDate = date;
        mStartIndex = startIndex;
        mEndIndex = endIndex;
        mMeetingPlace = meetingPlace;
        mGuideFee = guideFee;
        mTransactionFee = transactionFee;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_card_input, null);

        initAction(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.payoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTapPayout();
            }
        });
    }

    private void onTapPayout() {

        DeviceUtility.hideSoftKeyboard(getActivity());

        CardMultilineWidget widget = getView().findViewById(R.id.cardWidget);
        Card card = widget.getCard();

        if (card == null) {
            Dialog.show(getActivity(), Dialog.Style.error, "Error", "Invalid card data", null);
            return;
        }

        Loading.start(getActivity());

        Stripe stripe = new Stripe(getActivity(), Constants.Stripe.Key);

        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                Loading.stop(getActivity());
                Dialog.show(getActivity(), Dialog.Style.error, "Error", error.getMessage(), null);
            }
            @Override
            public void onSuccess(Token token) {
                createReserve(token);
            }
        });
    }

    private void createReserve(final Token token) {

        CreateReserveRequester.create(SaveData.getInstance().guestId, mGuideData.id, mGuideFee, mTransactionFee, mMeetingPlace, mDate.getTime(), mStartIndex, mEndIndex, new CreateReserveRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    charge(token);
                } else {
                    Dialog.show(getActivity(), Dialog.Style.error, "Error", "Failed to communicate", null);
                }
            }
        });
    }

    private void charge(Token token) {

        GuestData guestData = FetchGuestRequester.getInstance().query(SaveData.getInstance().guestId);
        if (guestData == null) {
            Loading.stop(getActivity());
            return;
        }
        String customerId = guestData.stripeCustomerId;
        String cardId = token.getId();

        StripeManager.charge(customerId, cardId, mGuideFee, mTransactionFee, mGuideData.stripeAccountId, new StripeManager.ChargeCallback() {
            @Override
            public void didReceiveResponse(boolean result) {
                Loading.stop(getActivity());

                if (result) {
                    BookCompleteFragment fragment = new BookCompleteFragment();
                    fragment.set(mGuideData, mDate, mStartIndex, mEndIndex, mMeetingPlace, mGuideFee, mTransactionFee);
                    stackFragment(fragment, AnimationType.horizontal);
                } else {
                    Dialog.show(getActivity(), Dialog.Style.error, "Error", "Failed to communicate", null);
                }
            }
        });
    }
}
