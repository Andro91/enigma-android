package com.andro.enigma.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.activity.GetPackageActivity;
import com.andro.enigma.activity.MainActivity;
import com.andro.enigma.database.*;
import com.andro.enigma.database.Package;

import java.util.ArrayList;

/**
 * Created by Internet on 29-Jan-16.
 */
public class ListSelectPackageAdapter extends ArrayAdapter<Package> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Package> data = new ArrayList<>();

    public ListSelectPackageAdapter(Context context, int layoutResourceId,
                       ArrayList<Package> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.data.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        Package p = data.get(position);

        if (p != null) {
            TextView tt1 = (TextView) row.findViewById(R.id.text_list_item);

            if (tt1 != null) {
                tt1.setText(p.getTitle());
            }
        }

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, MainActivity.class);

                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("lang", data.get(position).getLang());
                intent.putExtra("type", data.get(position).getIdType());
                intent.putExtra("count", data.get(position).getEnigmaCount());
                intent.putExtra("solved", data.get(position).getSolvedCount());
                intent.putExtra("crosswordNumber",1);

                context.startActivity(intent);
            }
        });

        return row;
    }
}
