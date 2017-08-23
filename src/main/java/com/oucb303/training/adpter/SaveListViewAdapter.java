package com.oucb303.training.adpter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.List;

/**
 * Created by BaiChangCai on 2017/1/8.
 */
public class SaveListViewAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mList;
    private String[] arrTemp;
    public SaveListViewAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mList = list;
        arrTemp = new String[list.size()];
    }
    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_save_student_num, null);
            holder.tv_groupNum = (TextView) view.findViewById(R.id.tv_groupNum);
            holder.et_studentNum = (EditText) view.findViewById(R.id.et_studentNum);
            view.setTag(holder);

        }else {
            holder = (ViewHolder) view.getTag();
        }
        final String strHint=mContext.getResources().getString(R.string.save_hint);
        holder.et_studentNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    ((TextView) view).setHint(strHint);
                }else
                    ((TextView) view).setHint("");
            }
        });
        holder.ref = position;
        holder.tv_groupNum.setText(mList.get(position));
        holder.et_studentNum.setText(arrTemp[position]);
        holder.et_studentNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTemp[holder.ref] = s.toString();
            }
        });
        return view;
    }
    private class ViewHolder {
        TextView tv_groupNum;
        EditText et_studentNum;
        int ref;
    }
    public String[] getStudentNum(){
        return arrTemp;
    }

}
