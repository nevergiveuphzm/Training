package com.oucb303.training.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oucb303.training.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeftSlidMenu extends Fragment implements View.OnClickListener
{


    public FragmentLeftSlidMenu()
    {
        // Required empty public constructor
    }

    private View slidView;
    private LinearLayout llAlbum, llBaby;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        slidView = inflater.inflate(R.layout.fragment_fragment_left_slid_menu, container, false);

        initView();
        return slidView;
    }

    public void initView()
    {


    }

    @Override
    public void onClick(View view)
    {

    }
}
