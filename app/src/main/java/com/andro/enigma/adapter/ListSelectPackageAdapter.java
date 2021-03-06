package com.andro.enigma.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andro.enigma.R;
import com.andro.enigma.activity.GetPackageActivity;
import com.andro.enigma.activity.MainActivity;
import com.andro.enigma.database.*;
import com.andro.enigma.database.Package;
import com.andro.enigma.helper.Helper;

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
            TextView textTitle = (TextView) row.findViewById(R.id.text_list_item);
            TextView textPrice = (TextView) row.findViewById(R.id.text_list_item_price);
            TextView textNumber = (TextView) row.findViewById(R.id.text_list_item_number);
            ImageView imageTick = (ImageView) row.findViewById(R.id.image_list_item);
            Log.d("COUNT","EnigmaCount = " + p.getEnigmaCount());
            if (textTitle != null) {
                textTitle.setText(p.getTitle());
            }
            if (textPrice != null) {
                textPrice.setText("");
            }
            if (textNumber != null){
                String headerText = String.format("%d %s", p.getEnigmaCount(), context.getResources().getString(R.string.enigmas_text));
                textNumber.setText(headerText);
            }
            try {
//                if(Helper.contains(idList, Integer.parseInt(p.getId()))){
//                    imageTick.setImageResource(R.mipmap.checked);
//                }
            }catch (NullPointerException ex){
                Log.d("MYTAG", ex.getMessage());
            }
        }

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, MainActivity.class);

                intent.putExtra("id", data.get(position).getId());
                Log.d("MYTAG","Intent extra" + data.get(position).getId());
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
