package com.horizon.myapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    //to store the list of countries
    private final List<String> nameUser;

    //to store the list of countries
    private final List<Object> countArray;

    public CustomListAdapter(Activity context, List<String> nameUser, List<Object> countArray) {
        super(context,R.layout.simple, nameUser);
        this.context = context;
        this.nameUser = nameUser;
        this.countArray = countArray;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.simple, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.textViewUsername);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.textViewCount);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameUser.get(position));
        infoTextField.setText(String.valueOf(countArray.get(position)));

        return rowView;

    };
}
