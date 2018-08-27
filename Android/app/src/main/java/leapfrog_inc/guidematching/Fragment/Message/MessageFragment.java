package leapfrog_inc.guidematching.Fragment.Message;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideDetailFragment;
import leapfrog_inc.guidematching.Fragment.Guide.GuideFragment;
import leapfrog_inc.guidematching.Fragment.Search.SearchFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.MessageData;
import leapfrog_inc.guidematching.Http.Requester.FetchEstimateRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchMessageRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchReserveRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.CommonUtility;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MessageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_message, null);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fetchMessage();
                new Handler().postDelayed(this, 10000);
            }
        };
        new Handler().post(runnable);

        fetchMessage();

        return view;
    }

    private void fetchMessage() {

        FetchMessageRequester.getInstance().fetch(new FetchMessageRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    resetListView();
                }
            }
        });
    }

    private void resetListView() {

        View view = getView();
        if (view == null) return;

        String myUserId = "";
        if (SaveData.getInstance().guestId.length() > 0) {
            myUserId = SaveData.getInstance().guestId;
        } else {
            myUserId = SaveData.getInstance().guideId;
        }

        ArrayList<MessageData> messageList = FetchMessageRequester.getInstance().query(myUserId);
        ArrayList<String> targetIds = new ArrayList<String>();
        for (int i = 0; i < messageList.size(); i++) {
            String targetId = "";
            MessageData messageData = messageList.get(i);
            if (messageData.senderId.equals(myUserId)) {
                targetId = messageData.receiverId;
            } else {
                targetId = messageData.senderId;
            }
            if (!targetIds.contains(targetId)) {
                targetIds.add(targetId);
            }
        }

        if (targetIds.size() == 0) {
            view.findViewById(R.id.listView).setVisibility(View.GONE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.listView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.GONE);
        }

        MessageAdapter adapter = new MessageAdapter(getActivity());
        for (int i = 0; i < targetIds.size(); i++) {
            adapter.add(targetIds.get(i));
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String targetId = (String)adapterView.getItemAtPosition(i);
                MessageDetailFragment fragment = new MessageDetailFragment();
                fragment.set(targetId);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private class MessageAdapter extends ArrayAdapter<String> {

        LayoutInflater mInflater;
        Context mContext;

        public MessageAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_message, parent, false);

            String userId = getItem(position);

            if (userId.contains("guide_")) {
                GuideData guideData = FetchGuideRequester.getInstance().query(userId);
                PicassoUtility.getFaceImage((Activity)mContext, Constants.ServerGuideImageDirectory + userId + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));
                ((TextView)convertView.findViewById(R.id.nameTextView)).setText(guideData.name);
                String loginTime = DateUtility.dateToString(guideData.loginDate, "MM/dd");
                ((TextView)convertView.findViewById(R.id.timeTextView)).setText(loginTime);
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(guideData.message);
                convertView.findViewById(R.id.timeTextView).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.messageTextView).setVisibility(View.VISIBLE);
            } else {
                GuestData guestData = FetchGuestRequester.getInstance().query(userId);
                PicassoUtility.getFaceImage((Activity)mContext, Constants.ServerGuestImageDirectory + userId + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));
                ((TextView)convertView.findViewById(R.id.nameTextView)).setText(guestData.name);
                convertView.findViewById(R.id.timeTextView).setVisibility(View.GONE);
                convertView.findViewById(R.id.messageTextView).setVisibility(View.GONE);
            }
            return convertView;
        }
    }
}
