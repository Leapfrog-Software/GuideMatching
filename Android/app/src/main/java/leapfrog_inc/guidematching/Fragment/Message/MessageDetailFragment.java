package leapfrog_inc.guidematching.Fragment.Message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.Http.DataModel.GuestData;
import leapfrog_inc.guidematching.Http.DataModel.GuideData;
import leapfrog_inc.guidematching.Http.DataModel.MessageData;
import leapfrog_inc.guidematching.Http.Requester.FetchGuestRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchGuideRequester;
import leapfrog_inc.guidematching.Http.Requester.FetchMessageRequester;
import leapfrog_inc.guidematching.Http.Requester.PostMessageRequester;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.Constants;
import leapfrog_inc.guidematching.System.DateUtility;
import leapfrog_inc.guidematching.System.DeviceUtility;
import leapfrog_inc.guidematching.System.PicassoUtility;
import leapfrog_inc.guidematching.System.SaveData;

public class MessageDetailFragment extends BaseFragment {

    private String mTargetId;

    public void set(String targetId) {
        mTargetId = targetId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_message_detail, null);

        initTimer();
        initContents(view);
        initAtion(view);

        return view;
    }

    private void initContents(View view) {

        if (mTargetId.contains("guide_")) {
            GuideData guideData = FetchGuideRequester.getInstance().query(mTargetId);
            ((TextView)view.findViewById(R.id.headerNameTextView)).setText(guideData.name);
        } else {
            GuestData guestData = FetchGuestRequester.getInstance().query(mTargetId);
            ((TextView)view.findViewById(R.id.headerNameTextView)).setText(guestData.name);
        }
    }

    private void initAtion(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSend();
            }
        });
    }

    private void initTimer() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fetchMessage();
                new Handler().postDelayed(this, 10000);
            }
        };
        new Handler().post(runnable);

        fetchMessage();
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

        String myUserId = (SaveData.getInstance().guestId.length() > 0) ? SaveData.getInstance().guestId : SaveData.getInstance().guideId;

        ArrayList<MessageData> messageList = FetchMessageRequester.getInstance().query(myUserId, mTargetId);
        messageList.sort(new Comparator<MessageData>() {
            @Override
            public int compare(MessageData m1, MessageData m2) {
                return m1.datetime.compareTo(m2.datetime);
            }
        });

        MessageDetailAdapater adapter = new MessageDetailAdapater(getActivity());
        for (int i = 0; i < messageList.size(); i++) {
            adapter.add(messageList.get(i));
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private void onClickSend() {

        DeviceUtility.hideSoftKeyboard(getActivity());

        String message = ((EditText)getView().findViewById(R.id.messageEditText)).getText().toString();

        String messageId = DateUtility.dateToString(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
        PostMessageRequester.post(messageId, mTargetId, message, new PostMessageRequester.Callback() {
            @Override
            public void didReceiveData(boolean result) {
                fetchMessage();
            }
        });

        ((EditText)getView().findViewById(R.id.messageEditText)).setText("");
    }

    private class MessageDetailAdapater extends ArrayAdapter<MessageData> {

        LayoutInflater mInflater;
        Context mContext;

        public MessageDetailAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MessageData messageData = getItem(position);

            String myUserId = (SaveData.getInstance().guestId.length() > 0) ? SaveData.getInstance().guestId : SaveData.getInstance().guideId;

            if (messageData.senderId.equals(SaveData.getInstance().equals(myUserId))) {

                convertView = mInflater.inflate(R.layout.adapter_message_detail_right, parent, false);

                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(messageData.message);

                Calendar dateTime = Calendar.getInstance();
                dateTime.setTime(messageData.datetime);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(getDateString(dateTime));

            } else {
                convertView = mInflater.inflate(R.layout.adapter_message_detail_left, parent, false);

                if (messageData.receiverId.contains("guide_")) {
                    PicassoUtility.getFaceImage(mContext, Constants.ServerGuideImageDirectory + messageData.senderId + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));
                } else {
                    PicassoUtility.getFaceImage(mContext, Constants.ServerGuestImageDirectory + messageData.senderId + "-0", (ImageView)convertView.findViewById(R.id.faceImageView));
                }

                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(messageData.message);

                Calendar dateTime = Calendar.getInstance();
                dateTime.setTime(messageData.datetime);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(getDateString(dateTime));
            }

            return convertView;
        }

        private String getDateString(Calendar calendar) {

            Calendar today = Calendar.getInstance();

            if ((today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                    && (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                    && (today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))) {
                SimpleDateFormat format = new SimpleDateFormat("kk:mm");
                return format.format(calendar.getTime());
            } else if (today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                SimpleDateFormat format = new SimpleDateFormat("M月d日 kk:mm");
                return format.format(calendar.getTime());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日 kk:mm");
                return format.format(calendar.getTime());
            }
        }
    }
}
