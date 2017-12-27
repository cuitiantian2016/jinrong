package com.honglu.future.ui.live.player;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.honglu.future.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zq on 2017/12/27.
 */

public class FaceImgAdapter extends BaseAdapter {
    private Context context;
    private List<Picture> mList = new ArrayList<>();
    private Picture mFace;

    public FaceImgAdapter(Map<String, Drawable> faceMap, Context context) {
        super();
        this.context = context;
        for (Map.Entry<String, Drawable> entry : faceMap.entrySet()) {
            mFace = new Picture(entry.getKey(), entry.getValue());
            mList.add(mFace);
        }
    }

    @Override
    public int getCount() {
        if (null != mList) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.face_item, null);

            // 初始化组件
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_face);

            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
        Picture picture = mList.get(position);
        viewHolder.image.setImageDrawable(picture.getImage());
        return convertView;
    }

    class ViewHolder {
        public ImageView image;
    }

    public class Picture {

        private String key;
        private Drawable image;

        public Picture(String key, Drawable image) {
            this.image = image;
            this.key = key;
        }

        public String getKey()

        {
            return key;
        }

        public Drawable getImage() {
            return image;
        }

    }
}
