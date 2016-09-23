package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.widget.HorizontalListView;

/**
 * Created by baichangcai on 2016/9/22.
 */
public class SequenceSetListViewAdapter extends BaseAdapter{
    private LayoutInflater inflater = null;
    private String [] data;
    private AddLightClickListener mListener;
    private Context mcontext;
    private HorizonListViewAdapter adapter;
    public SequenceSetListViewAdapter(Context context, String []data, HorizonListViewAdapter adapter, AddLightClickListener listener) {
        this.adapter =adapter;
        this.data = data;
        this.mcontext = context;
        this.mListener = listener;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            holder = new ViewHolder();
        convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_sequenceset_lv_vertical, null);
            TextView step = (TextView) convertView.findViewById(R.id.tv_name);
        HorizontalListView mListView = (HorizontalListView) convertView.findViewById(R.id.horizontal_listview);
           LinearLayout ll_last = (LinearLayout)convertView.findViewById(R.id.ll_last);
            LinearLayout ll_ListView = (LinearLayout) convertView.findViewById(R.id.ll_ListView);
            TextView tv_add = (TextView)convertView.findViewById(R.id.tv_add);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        //将最后一个item显示“添加序列”
        if(position == data.length-1){
            ll_ListView.setVisibility(View.GONE);
            ll_last.setVisibility(View.VISIBLE);
        }else {
            step.setText(data[position]);
        }
        //添加按钮的点击事件
        tv_add.setOnClickListener(mListener);
        tv_add.setTag(position);
        mListView.setAdapter(adapter);
        return convertView;
    }
//    class ViewHolder {
//        LinearLayout ll_last;//最后一个用于显示“添加序列”得条目
//        LinearLayout ll_ListView;//包含整个横向ListView和添加按钮
//        HorizontalListView mListView;//整个横向ListView
//        TextView step;//步骤
//        TextView tv_add;//添加按钮
//    }
    //* 用于回调的抽象类
    public static abstract class AddLightClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

}
