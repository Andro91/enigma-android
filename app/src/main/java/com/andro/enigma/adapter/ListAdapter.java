//package com.andro.enigma.adapter;
//
///**
// * Created by Internet on 28-Jan-16.
// */
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.media.Image;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.andro.enigma.R;
//import com.andro.enigma.activity.GetPackageActivity;
//import com.andro.enigma.database.CrosswordDbHelper;
//import com.andro.enigma.database.Package;
//import com.andro.enigma.helper.Helper;
//
//public class ListAdapter extends ArrayAdapter<Package> {
//
//    private Context context;
//    private int layoutResourceId;
//    private ArrayList<Package> data = new ArrayList<>();
//    private int[] idList;
//    private CrosswordDbHelper db;
//    private int packageType;
//
//    public ListAdapter(Context context, int layoutResourceId,
//                       ArrayList<Package> data, int packageType) {
//        super(context, layoutResourceId, data);
//        this.layoutResourceId = layoutResourceId;
//        this.context = context;
//        this.data = data;
//        this.packageType = packageType;
//        db = new CrosswordDbHelper(context);
//
//        Cursor c = db.getAllPackages(Helper.getLocale(context), packageType);
//
//        idList = new int[c.getCount()];
//
//        if (c != null) {
//            while(c.moveToNext()) {
//                idList[c.getPosition()] = c.getInt(0);
//                Log.d("CURSOR","idList " + c.getPosition() + " " + c.getInt(0));
//            }
//            c.close();
//        }
//
//    }
//
//    @Override
//    public int getCount() {
//        return this.data.size();
//    }
//
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        View row = convertView;
//
//        if (row == null) {
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//        }
//
//        Package p = data.get(position);
//
//        if (p != null) {
//            TextView textTitle = (TextView) row.findViewById(R.id.text_list_item);
//            TextView textPrice = (TextView) row.findViewById(R.id.text_list_item_price);
//            TextView textNumber = (TextView) row.findViewById(R.id.text_list_item_number);
//            ImageView imageTick = (ImageView) row.findViewById(R.id.image_list_item);
//
//            if (textTitle != null) {
//                textTitle.setText(p.getTitle());
//            }
//            if (textPrice != null) {
//                String priceText = String.format("%s %s", p.getPrice(), "\u20ac");
//                textPrice.setText(priceText);
//            }
//            if (textNumber != null){
//                String numberText = String.format("%d %s", p.getEnigmaCount(), context.getResources().getString(R.string.enigmas_text));
//                textNumber.setText(numberText);
//            }
//            try {
//                if(p.getPurchased() == 1){
//                    imageTick.setImageResource(R.mipmap.ic_check_black);
//                }
//            }catch (NullPointerException ex){
//                Log.d("MYTAG", ex.getMessage());
//            }
//
//        }
//
//        row.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                // TODO Auto-generated method stub
//
//                Intent intent = new Intent(context, GetPackageActivity.class);
//
//                intent.putExtra("id", data.get(position).getId());
//                intent.putExtra("title", data.get(position).getTitle());
//                intent.putExtra("lang", data.get(position).getLang());
//                intent.putExtra("type", data.get(position).getIdType());
//                intent.putExtra("price", data.get(position).getPrice());
//                intent.putExtra("count", data.get(position).getEnigmaCount());
//                intent.putExtra("purchased", data.get(position).getPurchased());
//
//                context.startActivity(intent);
//            }
//        });
//
//        return row;
//    }
//}
//
