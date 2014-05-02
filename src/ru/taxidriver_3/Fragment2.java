package ru.taxidriver_3;

//import ru.taxidriver_1.R;
//import ru.taxidriver_1.MyVariables;
//import ru.taxidriver_1.MyVariables;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.support.v4.app.Fragment;

public class Fragment2 extends Fragment {
	WebView browser;
	Button  btnPostHTTP;
	
	 @SuppressLint("SetJavaScriptEnabled")
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment2, container, false);
		
		browser=(WebView) view.findViewById(R.id.webkit);
		btnPostHTTP = (Button) view.findViewById(R.id.btnPostHTTP);
		btnPostHTTP.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  //browser.loadUrl("http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/queue");
		    	  browser.loadUrl(MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/queue");
		      }
		    });
		WebSettings webSettings = browser.getSettings();
		webSettings.setSavePassword(true);
	    webSettings.setSaveFormData(true);
	    //browser.loadUrl("http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/queue");
	    browser.loadUrl(MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/queue");
	    browser.getSettings().setJavaScriptEnabled(true);
		
		return view;
	}
}