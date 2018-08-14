package com.what.does.date.says.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;
import com.what.does.date.says.Adapter.FlabbyListViewAdapter;
import com.what.does.date.says.R;
import com.what.does.date.says.Utils.CommonFunctions;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Nitin on 3/25/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener , Response.ErrorListener , Response.Listener<String> {
    JazzyListView jazzyListView;
    Button buttonSetDate , buttonGetEvents , buttonMoreEvents;
    TextView textViewDate;
    LinearLayout linearLayout;
    ArrayList<String> resultList;
    int counter = 0 ;
    boolean loadingFinished = true;
    ProgressDialog progressDialog;
    int day,month,year ;
    String fontPath = "font/BreeSerif-Regular.ttf";
    String date = "";
    int adCounter = 0;
    AdView adView ;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd rewardedVideoAd ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_fragment, container, false);
        String todayDate = getArguments().getString("Date");
        if (todayDate != ""){
            String[] parsedDate = todayDate.split("-");
            if(parsedDate.length == 3){
                day = Integer.parseInt(parsedDate[0]);
                month = Integer.parseInt(parsedDate[1]) + 1;
                year = Integer.parseInt(parsedDate[2]);
            }
        }
        MobileAds.initialize(getActivity() , "ca-app-pub-3498970195952528~3363403093");
        adView = (AdView) view.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        loadRewardedVideoAds();
        linearLayout = (LinearLayout) view.findViewById(R.id.childLayout);
        GradientDrawable bgShape = (GradientDrawable) linearLayout.getBackground();
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        int color = Color.parseColor("#000000");
        int textColor = Color.parseColor("#E18A07");
        int shadowColor = Color.parseColor("#336699");
        bgShape.setColor(color);
        buttonSetDate = (Button) view.findViewById(R.id.btSetDate);
        buttonGetEvents = (Button) view.findViewById(R.id.btGetEvents);
        buttonMoreEvents = (Button) view.findViewById(R.id.btMoreEvents);
        textViewDate = (TextView) view.findViewById(R.id.tvDate);
        buttonSetDate.setOnClickListener(this);
        buttonGetEvents.setOnClickListener(this);
        buttonMoreEvents.setOnClickListener(this);
        date = day + "-" + month + "-" +year;
        textViewDate.setText((Html.fromHtml("Getting Events for " + "<u>"+date+"</u>")));
        textViewDate.setTextColor(textColor);
        textViewDate.setShadowLayer(1.5f , 2 , 2 , shadowColor);
        textViewDate.setTypeface(tf);
        resultList = new ArrayList<>();
        jazzyListView = (JazzyListView) view.findViewById(R.id.jazzyList);
        jazzyListView.setTransitionEffect(JazzyHelper.SLIDE_IN);
        progressDialog=new ProgressDialog(getActivity(),R.style.MyTheme);
        progressDialog.setTitle("Loading Data for " + date + " ....");
        progressDialog.setMessage("This depends on your connection speed.Please be patient");
        progressDialog.setCancelable(false);
        if(CommonFunctions.isNetworkAvailable(getActivity())) {
            progressDialog.show();
            dismissProgressDialog();
            loadData();
        }else{
            resultList.add("There is problem with your connectivity.Please check your internet connection and try again.");
            jazzyListView.setAdapter(new FlabbyListViewAdapter(getActivity(), resultList));
        }

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        if (CommonFunctions.isNetworkAvailable(getActivity())) {
            switch (view.getId()) {
                case R.id.btSetDate:
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.setTargetFragment(this, 0);
                    newFragment.show(getFragmentManager(), "datePicker");
                    break;

                case R.id.btGetEvents:
                    adCounter++;
                    if(adCounter % 3 == 0 && adCounter % 12 != 0){
                        loadInterstitial();
                        showInterstitial();
                    }else if(adCounter % 12 == 0){
                        if (rewardedVideoAd.isLoaded()){
                            rewardedVideoAd.show();
                        }else{
                            loadRewardedVideoAds();
                            rewardedVideoAd.show();
                        }
                    }
                    progressDialog.setTitle("Loading Data for " + date + " ....");
                    progressDialog.show();
                    dismissProgressDialog();
                    loadData();
                    break;

                case R.id.btMoreEvents:
                    adCounter++;
                    if(adCounter % 3 == 0 && adCounter % 12 != 0){
                        loadInterstitial();
                        showInterstitial();
                    }else if(adCounter % 12 == 0){
                        if (rewardedVideoAd.isLoaded()){
                            rewardedVideoAd.show();
                        }else{
                            loadRewardedVideoAds();
                            rewardedVideoAd.show();
                        }
                    }
                    progressDialog.setTitle("Loading Data for " + date + " ....");
                    progressDialog.show();
                    dismissProgressDialog();
                    loadData();
                    break;
            }

        }else {
            resultList = new ArrayList<>();
            resultList.add("There is problem with your connectivity.Please check your internet connection and try again.");
            jazzyListView.setAdapter(new FlabbyListViewAdapter(getActivity(), resultList));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            String dateObtained = data.getStringExtra("DATE");
            if (dateObtained != ""){
                String[] parsedDate = dateObtained.split("/");
                if(parsedDate.length == 3){
                    day = Integer.parseInt(parsedDate[0]);
                    month = Integer.parseInt(parsedDate[1]);
                    year = Integer.parseInt(parsedDate[2]);
                    date = day + "-" + month + "-" +year;
                    textViewDate.setText((Html.fromHtml("Getting Events for " + "<u>"+date+"</u>")));
                }
            }
        }
    }

    private void loadData() {
        //http://numbersapi.com/3/10/date?json
        String URL_C = "http://numbersapi.com/"+month+"/"+day+"/date?json";
        resultList = new ArrayList<>();
        counter = 0;
        loadingFinished = true;
        for (int i = 0 ; i < 10 ; i++){
            StringRequest stringRequest = new StringRequest(Request.Method.GET , URL_C , this ,this);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(stringRequest);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        counter++;
        if(counter == 9){
            loadingFinished = false;
            System.out.println("Result list obtained : "+resultList);
            progressDialog.dismiss();
            jazzyListView.setAdapter(new FlabbyListViewAdapter(getActivity(), resultList));
        }
    }

    @Override
    public void onResponse(String s) {

        String text = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
            text = jsonObject.getString("text");
            if(!resultList.contains(text))
                resultList.add(text);
            if(counter == 9){
                loadingFinished = false;
                System.out.println("Result list obtained : "+resultList);
                progressDialog.dismiss();
                jazzyListView.setAdapter(new FlabbyListViewAdapter(getActivity(), resultList));
            }else
            {
                counter++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("The output at  "+counter+" is "+text);
    }

    public void dismissProgressDialog(){
        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity() , "There is problem with your connectivity.Please check your internet connection and try again" , Toast.LENGTH_SHORT).show();
                }
            }
        };

        mHandler.sendMessageDelayed(new Message(), 10000);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.

            }
        });
        return interstitialAd;
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadInterstitial();
        }
    }

    private void loadRewardedVideoAds(){
        rewardedVideoAd.loadAd(getString(R.string.video_ad_unit_id), new AdRequest.Builder().build());
    }

}
