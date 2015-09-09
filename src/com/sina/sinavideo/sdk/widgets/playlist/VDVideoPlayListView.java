package com.sina.sinavideo.sdk.widgets.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.VDVideoViewListeners.OnVideoListListener;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.widgets.VDBaseWidget;
import com.sina.video_playersdkv2.R;

/**
 * 播放列表弹出界面，容器部分
 * 
 * @author liuqun
 */
public class VDVideoPlayListView extends ListView implements VDBaseWidget, OnVideoListListener {

    private VDVideoPlaylistContainerAdapter mAdapter = null;
    @SuppressLint("nouse")
    private final static String TAG = "VDVideoPlayListView";

    /**
     * 容器的适配器，因为不知道当前适配的界面，所以，ViewHolder没法弄了，短列表姑且认为没问题吧
     * 
     * @author sunxiao
     */
    class VDVideoPlaylistContainerAdapter extends VDVideoPlayListAdapter {

        private Context mContext;

        public VDVideoPlaylistContainerAdapter(Context context, int itemID) {
            super();
            VDLog.d(TAG, "VDVideoPlaylistContainerAdapter:" + itemID);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItemID = itemID;
            mContext = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            VDLog.d(TAG, "getCount:" + mVideoList.getRealVideoListSize());
            return mVideoList.getRealVideoListSize();
        }

        @Override
        public Object getItem(int arg0) {
            VDLog.d(TAG, "getItem:" + mVideoList.getRealVideoInfo(arg0));
            return mVideoList.getRealVideoInfo(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            VDLog.d(TAG, "getItemId:" + arg0);
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mItemID == -1) {
                return null;
            }
            // LogS.i("VDVideoPlayListView", "getView p = " + position);
            final int p = position;
            if (convertView == null) {
                convertView = mInflater.inflate(mItemID, null);
            }
            // 设置数据部分
            ViewGroup viewgroup = (ViewGroup) convertView;
            for (int i = 0; i < viewgroup.getChildCount(); i++) {
                if (viewgroup.getChildAt(i) instanceof VDVideoPlaylistBase) {
                    VDVideoPlaylistBase model = (VDVideoPlaylistBase) viewgroup.getChildAt(i);
                    model.setData(mVideoList.getRealVideoInfo(position));

                    model.setVideoInfo(position, mCurPlayIndex);

                }
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    VDLog.i("VDVideoPlayListView", "getView mCurPlayIndex = " + mCurPlayIndex + " , p = " + p);
                    if (mCurPlayIndex == p)
                        return;
                    VDVideoViewController controller = VDVideoViewController.getInstance(mContext);
                    if (null == controller) {
                        return;
                    }
                    VDVideoInfo info = controller.mVDVideoListInfo.getRealVideoInfo(p);
                    controller.getExtListener().notifyPlaylistListener(info, p);
                    controller.notifyHideVideoList();
                    mCurPlayIndex = p;
                }
            });

            return convertView;
        }

    }

    public VDVideoPlayListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 如果有自定义属性，加载
        int itemID = -1;
        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.VDVideoPlayListView);
        for (int i = 0; i < typedArr.getIndexCount(); i++) {
            if (typedArr.getIndex(i) == R.styleable.VDVideoPlayListView_listItem) {
                itemID = typedArr.getResourceId(R.styleable.VDVideoPlayListView_listItem, -1);
            }
        }
        typedArr.recycle();

        // 设置adapter
        mAdapter = new VDVideoPlaylistContainerAdapter(context, itemID);
        setAdapter(mAdapter);
        VDVideoViewController controller = VDVideoViewController.getInstance(context);
        if (controller != null)
            controller.addOnVideoListListener(VDVideoPlayListView.this);
        // VDVideoViewController.getInstance().addOnVideoListVisibleChangeListener(this);
        setOnScrollListener(new VDPlayListScrollListener(context));
    }

    @Override
    public void reset() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void onVideoList(VDVideoListInfo infoList) {
        // TODO Auto-generated method stub
        if (mAdapter != null) {
            mAdapter.setVideoList(infoList);
        }
    }
}
