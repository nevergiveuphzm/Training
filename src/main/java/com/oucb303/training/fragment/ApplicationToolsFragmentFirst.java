package com.oucb303.training.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.oucb303.training.R;
import com.oucb303.training.activity.GroupResistActivity;
import com.oucb303.training.activity.RandomTimeActivity;
import com.oucb303.training.activity.RandomTimesModuleActivity;
import com.oucb303.training.activity.SitUpsActivity;
import com.oucb303.training.activity.TimeKeeperActivity;
import com.oucb303.training.activity.TimingModuleActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BaiChangCai on 2017/7/1.
 * 组合练习的fragment
 */

public class ApplicationToolsFragmentFirst extends Fragment implements AdapterView.OnItemClickListener{

    GridView gvFirst;
    private int[] images = { R.drawable.iv_combined_first, R.drawable.iv_combined_second,
            R.drawable.iv_combined_third, R.drawable.iv_combined_forth, R.drawable.iv_combined_fifth,R.drawable.iv_combined_six };
    private String[] text = { "交替", "限时", "计时", "次数随机", "时间随机","分组对抗"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combined,container,false);
        gvFirst = (GridView) view.findViewById(R.id.gv_first);
        initView();
        return view;
    }

    private void initView() {
        ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();
        // 使用HashMap将图片添加到一个数组中，注意一定要是HashMap<String,Object>类型的，因为装到map中的图片要是资源ID，而不是图片本身
        // 如果是用findViewById(R.drawable.image)这样把真正的图片取出来了，放到map中是无法正常显示的
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("images", images[i]);
            map.put("text", text[i]);
            imagelist.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
                imagelist, R.layout.layout_fragment_gridview_item, new String[] { "images",
                "text" }, new int[] { R.id.iv_itemGradView, R.id.tv_itemTextView });
        gvFirst.setAdapter(simpleAdapter);
        gvFirst.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                Toast.makeText(getActivity(), "交替", Toast.LENGTH_SHORT)
                        .show();
                intent = new Intent();
                intent.setClass(getActivity(), SitUpsActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case 1:
                Toast.makeText(getActivity(), "限时", Toast.LENGTH_SHORT)
                        .show();
                intent = new Intent();
                intent.setClass(getActivity(), TimingModuleActivity.class);
                startActivity(intent);
                break;
            case 2:
                Toast.makeText(getActivity(), "计时", Toast.LENGTH_SHORT)
                        .show();
                intent = new Intent();
                intent.setClass(getActivity(), TimeKeeperActivity.class);
                startActivity(intent);
                break;
            case 3:
                Toast.makeText(getActivity(), "次数随机", Toast.LENGTH_SHORT)
                        .show();
                intent = new Intent();
                intent.setClass(getActivity(), RandomTimesModuleActivity.class);
                startActivity(intent);
                break;
            case 4:
                Toast.makeText(getActivity(), "时间随机", Toast.LENGTH_SHORT)
                        .show();
                intent = new Intent();
                intent.setClass(getActivity(), RandomTimeActivity.class);
                startActivity(intent);
                break;
            case 5:
                Toast.makeText(getActivity(), "分组对抗", Toast.LENGTH_SHORT)
                        .show();
                intent = new Intent();
                intent.setClass(getActivity(), GroupResistActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
