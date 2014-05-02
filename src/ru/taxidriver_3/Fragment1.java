package ru.taxidriver_3;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.widget.CompoundButton;

public class Fragment1 extends Fragment implements CompoundButton.OnCheckedChangeListener{
	CheckBox cb;
	  Button  btnSave, btnLoad;
	  EditText etLgn, etPsw;
	  SharedPreferences sPref;
	  final String LOG_TAG = "myLogs";
	  
	  final String SAVED_TEXT_LGN = "saved_text_lgn";
	  final String SAVED_TEXT_PSW = "saved_text_psw";
	  final String SAVED_TEXT_RADIO = "saved_text_radio";
	  final String BRIGHTNESS_LOW_HIGH = "brightness_low_high";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment1, container, false);
		
		cb=(CheckBox) view.findViewById(R.id.checkBox1);
		cb.setOnCheckedChangeListener(this);
		
		etLgn = (EditText) view.findViewById(R.id.etLgn);
	    etPsw = (EditText) view.findViewById(R.id.etPsw);
	    
		btnSave = (Button) view.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  saveText();
		      }
		    });
		
		btnLoad = (Button) view.findViewById(R.id.btnLoad);
		btnLoad.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  loadText();
		      }
		    });
		
		RadioGroup RadioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
		RadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio1:
					saveRadio(1);
					break;
				case R.id.radio2:
					saveRadio(2);
					break;
				case R.id.radio3:
					saveRadio(3);
					break;
				}
			}
		});
		
		sPref = this.getActivity().getSharedPreferences("pref",0);
		MyVariables.SAVED_TEXT_1 = sPref.getString(SAVED_TEXT_LGN, "");
		MyVariables.SAVED_TEXT_2 = sPref.getString(SAVED_TEXT_PSW, "");
		MyVariables.SAVED_TEXT_3 = sPref.getString(SAVED_TEXT_RADIO, "");
		//при загрузке включаем настройку не понижать €ркость экрана
				sPref = this.getActivity().getSharedPreferences("pref",0);
				Editor ed = sPref.edit();
				ed.putString(BRIGHTNESS_LOW_HIGH, "1");
				ed.commit();
				MyVariables.BRIGTNESSHIGH=true;
		
		return view;
	}
	
	private void saveText() {
		// TODO Auto-generated method stub
		//sPref = getPreferences(MODE_PRIVATE);
		sPref = this.getActivity().getSharedPreferences("pref",0);
		Editor ed = sPref.edit();
		ed.putString(SAVED_TEXT_LGN, etLgn.getText().toString());
		ed.putString(SAVED_TEXT_PSW, etPsw.getText().toString());
		ed.commit();
		Toast.makeText(this.getActivity(), "—охранено", Toast.LENGTH_SHORT).show();
		
		MyVariables.SAVED_TEXT_1=etLgn.getText().toString();
		MyVariables.SAVED_TEXT_2=etPsw.getText().toString();
		//Log.d(LOG_TAG, "Button click btnSave");
	}
	
	private void loadText() {
		// TODO Auto-generated method stub
		sPref = this.getActivity().getSharedPreferences("pref",0);
		String savedTextLgn = sPref.getString(SAVED_TEXT_LGN, "");
		String savedTextPsw = sPref.getString(SAVED_TEXT_PSW, "");
		String savedTextRadio = sPref.getString(SAVED_TEXT_RADIO, "");
		etLgn.setText(savedTextLgn);
		etPsw.setText(savedTextPsw);
		Toast.makeText(this.getActivity(), "«агружено " + savedTextRadio, Toast.LENGTH_SHORT).show();
		
		MyVariables.SAVED_TEXT_1=savedTextLgn;
		MyVariables.SAVED_TEXT_2=savedTextPsw;
		MyVariables.SAVED_TEXT_3=savedTextRadio;			
	}
	
	private void saveRadio(int RadioArg) {
		//сохран€ем номер мелодии
		sPref = this.getActivity().getSharedPreferences("pref",0);
		Editor ed = sPref.edit();
		ed.putString(SAVED_TEXT_RADIO, "radio" + String.valueOf(RadioArg));
		ed.commit();
		MyVariables.SAVED_TEXT_3="radio" + String.valueOf(RadioArg);
		Toast.makeText(this.getActivity(), "radio" + String.valueOf(RadioArg), Toast.LENGTH_SHORT).show();
	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{//провер€ем CheckBox что бы притушивать экран или нет
	if (isChecked) { //€ркость понижать
		sPref = this.getActivity().getSharedPreferences("pref",0);
		Editor ed = sPref.edit();
		ed.putString(BRIGHTNESS_LOW_HIGH, "0");
		ed.commit();
		MyVariables.BRIGTNESSHIGH=false;
		Toast.makeText(this.getActivity(), "Brightness LOW", Toast.LENGTH_SHORT).show();	
	}
	else { //€ркость не понижать
		sPref = this.getActivity().getSharedPreferences("pref",0);
		Editor ed = sPref.edit();
		ed.putString(BRIGHTNESS_LOW_HIGH, "1");
		ed.commit();
		MyVariables.BRIGTNESSHIGH=true;
		Toast.makeText(this.getActivity(), "Brightness HIGH", Toast.LENGTH_SHORT).show();
	}
	}
}
