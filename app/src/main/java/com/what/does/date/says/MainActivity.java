package com.what.does.date.says;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.what.does.date.says.fragments.HomeFragment;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    String day = "";
    String month = "";
    String year = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();
        final Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH) + "";
        month = c.get(Calendar.MONTH) + "";
        year = c.get(Calendar.YEAR)+"";
        ActionBar actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFA000'>On This Day In History</font>"));
        String todayDate = day+"-"+month+"-"+year;
        bundle.putString("Date" , todayDate);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, homeFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i=new Intent(MainActivity.this,About.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.what.does.date.says")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.what.does.date.says")));
                    }
                }
            });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
