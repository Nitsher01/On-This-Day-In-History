package com.what.does.date.says.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.what.does.date.says.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nitin on 2/18/2017.
 */

public class FlabbyListViewAdapter extends BaseAdapter {
    Context context;
    String[] titles ;
    int[] myColor ;
    Typeface tf ;
    String fontPath = "font/BreeSerif-Regular.ttf";
    public FlabbyListViewAdapter(Context context , ArrayList<String> data ) {
        this.context = context ;
        this.titles = new String[data.size()] ;
        this.titles = data.toArray(titles);
        myColor = new int[2];
        myColor[0] = Color.parseColor("#E18A07");
        myColor[1] = Color.parseColor("#000000");
        tf = Typeface.createFromAsset(context.getAssets(), fontPath);

    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.flabby_row,viewGroup,false);
        TextView result = (TextView) rowView.findViewById(R.id.tvText);
        //TextView description = (TextView) rowView.findViewById(R.id.tvJobDescription);
        Button shareResult = (Button) rowView.findViewById(R.id.btShare);
        shareResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,  titles[i] +"\n" + "Download this app to know more interesting events:\n  " + "https://play.google.com/store/apps/details?id=com.what.does.date.says");
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

        int color = myColor[(i % 2)];
        int textColor = myColor[((i+1) % 2)];
        result.setTextColor(textColor);//
        int shadowColor = color == myColor[0] ? Color.parseColor("#336699") : Color.parseColor("#FFCC00");
        result.setShadowLayer(1.5f , 2 , 2 ,shadowColor);
        GradientDrawable bgShape = (GradientDrawable)rowView.getBackground();
        bgShape.setColor(color);
        //((LinearLayout)rowView).setBackgroundColor(color);
        result.setTypeface(tf);
        result.setText(titles[i]);
        if(titles[i].contains("internet connection"))
            shareResult.setVisibility(View.GONE);
        //description.setText(descriptions[i]);
        return rowView;
    }
}
