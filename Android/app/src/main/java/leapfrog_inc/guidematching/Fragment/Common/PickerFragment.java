package leapfrog_inc.guidematching.Fragment.Common;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.R;
import leapfrog_inc.guidematching.System.DeviceUtility;

public class PickerFragment extends BaseFragment {

    private String mTitle;
    private int mCurrentIndex;
    private ArrayList<String> mDataList;
    private  PickerFragmentCallback mCallback;

    public void set(String title, int defaultIndex, ArrayList<String> dataList, PickerFragmentCallback callback) {
        mTitle = title;
        mCurrentIndex = defaultIndex;
        mDataList = dataList;
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_picker, null);

        initAction(view);
        initContent(view);
        resetListView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view == null) {
            return;
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        ViewGroup.LayoutParams listViewParams = listView.getLayoutParams();
        listViewParams.height = getListViewHeight();
        listView.setLayoutParams(listViewParams);

        LinearLayout contentsLayout = (LinearLayout)view.findViewById(R.id.contentsLayout);
        int fromYDelta = getListViewHeight() + (int)(51 * DeviceUtility.getDeviceDensity(getActivity()));
        TranslateAnimation animation = new TranslateAnimation(0, 0, fromYDelta, 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        contentsLayout.startAnimation(animation);
    }

    private int getListViewHeight() {
        return (int)(50 * DeviceUtility.getDeviceDensity(getActivity())) * mDataList.size();
    }


    private void initContent(View view) {
        ((TextView)view.findViewById(R.id.titleTextView)).setText(mTitle);
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.cancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    private void resetListView(View view) {

        PickerAdapater adapter = new PickerAdapater(getActivity());

        for (int i = 0; i < mDataList.size(); i++) {
            PickerAdapterData data = new PickerAdapterData();
            data.title = mDataList.get(i);
            data.selected = (mCurrentIndex == i);
            adapter.add(data);
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentIndex = i;
                resetListView(getView());
                mCallback.didSelect(i);
                close();
            }
        });
    }

    private void close() {

        View view = getView();
        if (view == null) return;

        LinearLayout contentsLayout = (LinearLayout)view.findViewById(R.id.contentsLayout);
        int toYDelta = getListViewHeight() + (int)(51 * DeviceUtility.getDeviceDensity(getActivity()));
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, toYDelta);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                popFragment(AnimationType.none);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        contentsLayout.startAnimation(animation);
    }

    private class PickerAdapterData {
        boolean selected;
        String title;
    }

    private class PickerAdapater extends ArrayAdapter<PickerAdapterData> {

        LayoutInflater mInflater;
        Context mContext;

        public PickerAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_picker, parent, false);

            PickerAdapterData data = getItem(position);

            if (data.selected) {
                convertView.findViewById(R.id.selectedImageView).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.selectedImageView).setVisibility(View.INVISIBLE);
            }

            ((TextView) convertView.findViewById(R.id.titleTextView)).setText(data.title);

            return convertView;
        }
    }

    public interface PickerFragmentCallback {
        void didSelect(int index);
    }
}
