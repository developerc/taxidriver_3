package ru.taxidriver_3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Fragment5 extends Fragment {
	WebView browser;
	Button  btnPostHTTP;
	Button btnPostHTTPru;
	String StrFindAdrMap;
	String[] adress;
	String street;
	String house;
	String URLFind;
	private static final String TAG = "myLogs";
	
	 @SuppressLint("SetJavaScriptEnabled")

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment5, container, false);
		
		browser=(WebView) view.findViewById(R.id.webkit);
		btnPostHTTP = (Button) view.findViewById(R.id.btnPostHTTP);
		btnPostHTTP.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  //browser.loadUrl("http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/queue");
		    	  browser.loadUrl(MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/goal");
		    	  
		      }
		    });
		
		btnPostHTTPru = (Button) view.findViewById(R.id.btnPostHTTPru);
		btnPostHTTPru.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  FindAdrMap(); 
		    	 Log.d(TAG, URLFind);
		    	  browser.loadUrl("http://openstreetmap.ru/frame.php?noscreenshot=1#map=18/45.84979/40.11891&layer=SU&q=ÒÈÕÎÐÅÖÊ, Îêòÿáðüñêàÿ, 2&qmap=");
		    	  browser.getSettings().setJavaScriptEnabled(true);
		      }
		    });
		
		WebSettings webSettings = browser.getSettings();
		webSettings.setSavePassword(true);
	    webSettings.setSaveFormData(true);
	    browser.loadUrl(MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/goal");
	    //browser.loadUrl("http://192.168.28.19/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/goal");
	    browser.getSettings().setJavaScriptEnabled(true);
		
		return view;
	}
	 
	 private void FindAdrMap() {
		StrFindAdrMap = MyVariables.SAVED_TEXT_4; 
		adress = StrFindAdrMap.split(" ");
		street = adress[0];
		house = adress[1];
		URLFind = "http://openstreetmap.ru/frame.php?noscreenshot=1#map=18/45.84979/40.11891&layer=SU&q=ÒÈÕÎÐÅÖÊ, Îêòÿáðüñêàÿ, 2&qmap=";
	 }
}
