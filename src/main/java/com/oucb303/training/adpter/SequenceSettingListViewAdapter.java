package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.widget.HorizontalListView;

import java.util.List;
import java.util.Map;

/**
 * Created by baichangcai on 2016/9/22.
 */
public class SequenceSettingListViewAdapter extends BaseAdapter
{
    private LayoutInflater inflater = null;
    private List<Map<String, Object>> list_sequence;
    private AddLightClickListener mListener;
    private Context mcontext;
    HorizontalListView mListView;
    private List<HorizonListViewAdapter> list_adapter;
    private HorizonListViewAdapter adapter;
    private AdapterView.OnItemClickListener itemClickListener;

    public SequenceSettingListViewAdapter(Context context, List<Map<String, Object>> list,
                                          List<HorizonListViewAdapter> list_adapter,
                                          AddLightClickListener listener,
                                          AdapterView.OnItemClickListener itemClickListener)
    {
        this.list_adapter = list_adapter;
        this.list_sequence = list;
        this.mcontext = context;
        this.mListener = listener;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount()
    {
        if (list_adapter == null)
            return 0;
        return list_adapter.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list_adapter.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_sequenceset_lv_vertical, null);
        TextView step = (TextView) convertView.findViewById(R.id.tv_name);
        mListView = (HorizontalListView) convertView.findViewById(R.id.horizontal_listview);
        TextView tv_add = (TextView) convertView.findViewById(R.id.tv_add);
        LinearLayout ll_ListView = (LinearLayout) convertView.findViewById(R.id.ll_ListView);
        LinearLayout ll_last = (LinearLayout) convertView.findViewById(R.id.ll_last);

        //将最后一个item显示“添加序列”
        if (position == list_adapter.size() - 1)
        {
            ll_ListView.setVisibility(View.GONE);
            ll_last.setVisibility(View.VISIBLE);
        } else
        {
            step.setText("步骤:" + (position + 1) + "  " +
                    "延迟时间:" + list_sequence.get(position).get("delay_time") + "秒");
            //添加按钮的点击事件
            tv_add.setOnClickListener(mListener);
            tv_add.setTag(position);
            mListView.setTag(position);
            mListView.setAdapter(list_adapter.get(position));
            mListView.setOnItemClickListener(itemClickListener);
        }
        return convertView;
    }

    //* 用于回调的抽象类
    public static abstract class AddLightClickListener implements View.OnClickListener
    {
        /**
         * 基类的onClick方法
         */
        public void onClick(View v)
        {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
