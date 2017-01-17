package com.oucb303.training.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by huzhiming on 2016/11/18.
 */

public abstract class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener
{

    private Spinner spinner;

    private ArrayAdapter<String> adapter;
    private String item[];

    public SpinnerItemSelectedListener(Context context, Spinner spinner, String []items)
    {
        this.spinner = spinner;
        this.item = items;
        this.adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

}
