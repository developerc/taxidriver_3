package ru.taxidriver_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
//import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.Fragment;

public class Fragment3 extends Fragment {
	int i=0;
	HTTPATask httpatask;  //обьявили класс  для метода POST
	HTTGATask httgatask;  //обьявили класс для метода GEt
	
	String ArgPost = "2";
	String resultPOST;
	String UvedStr ="";
	String strAdr,strZak,strTlf,strKod,strDat,strTim,strCar,strUve;
	boolean flagZapros = true;  //делать ли запрос на заказ
	boolean flagMusic = false;  //играть ли оповещение о приходе заказа
	String httpPath;
	final Handler myHandler = new Handler();
	Button btnAcc, btnRjc, btnCallAbon;
	private static final String ERROR = "error";
	private static final String RESULT = "result";
	private static final String ADRES = "adres";
	private static final String ZAKAZ = "zakaz";
	private static final String TELEFON = "telefon";
	private static final String KODE = "kode";
	private static final String DAT = "dat";
	private static final String TIM = "tim";
	private static final String CAR = "car";
	private static final String UVEDOMLEN = "uvedomlen";
	private static final String TAG = "myLogs";
	final int MAX_STREAMS = 1;
	//SoundPool sp;
	//int soundIdShot;
	//int streamIDShot;
	//private int brightness;
	MediaPlayer mediaPlayer;
	MusicTask mt;
	boolean flagbrightness = true;
	boolean flagendmusic = true;
	String[] zakaz = { "Адрес:", "Заказ", "Телефон абонента", "Код абонента", "Дата", "Время","Машина", "Уведомлен"};
	String strTlfCall="";
	ListView lvMain;
	Runnable runnable;
	ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment3, container, false);
	
		lvMain = (ListView) view.findViewById(R.id.lvMain);
		 adapter = new ArrayAdapter<String>(this.getActivity(),
		        android.R.layout.simple_list_item_1, zakaz);
		lvMain.setAdapter(adapter);
		
		btnAcc = (Button) view.findViewById(R.id.btnAcc);
		btnAcc.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	//здесь будем поднимать флаг на выдачу POST запроса
			    	flagZapros=false;
			    	ArgPost = "2";
		      }
		    });
		
		btnRjc = (Button) view.findViewById(R.id.btnRjc);
		btnRjc.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	//здесь будем поднимать флаг на выдачу POST запроса
			    	flagZapros=false;
			    	ArgPost = "3";
		      }
		    });
		
		btnCallAbon = (Button) view.findViewById(R.id.btnCallAbon);
		btnCallAbon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		    	//здесь будем вызывать окно звонилки
		    	//PlayZakazMusic();
	    	  Intent intent = new Intent(Intent.ACTION_DIAL);
	    	  switch (strTlfCall.length()) {
	    	  case 0:
	          intent.setData(Uri.parse("tel:"+"88619670000"));
	          break;
	    	  case 5:
		          intent.setData(Uri.parse("tel:"+"886196"+strTlf));
		          break;
	    	  case 10:
		          intent.setData(Uri.parse("tel:"+"8"+strTlf));
		          break;
		      default:
		    	  intent.setData(Uri.parse("tel:"+"88619670000"));
	    	  }
	          startActivity(intent);
	    	  Log.d(TAG, strTlfCall);
	    	  Log.d(TAG, String.valueOf(strTlfCall.length()));
	      }
		    });
	
		Timer myTimer = new Timer();
	    myTimer.schedule(new TimerTask(){
	    	 @Override
	         public void run() {UpdateGUI();}	
	    }, 0, 15000);
		
	    //httpPath ="http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/order";
	    httpPath =MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/order";
	    
		return view;
	}
	
	private void UpdateGUI() {
	      i++;
	      myHandler.post(myRunnable);
	   }
	final Runnable myRunnable = new Runnable() {
	      public void run() {
	    	 if ((i > 3) && (flagbrightness == false) && MyVariables.BRIGTNESSHIGH == false)  {  //притушиваем экран
					setBrightness(10);
				}
	    	  else {
	    		  setBrightness(100);
	    	  }
	 //-------------------------------------------------
	 if (flagZapros) {
		 httgatask = new HTTGATask();  //выполняем запрос на получение заказа
		 httgatask.execute(ArgPost);
		 } 
		 else {
		 httpatask = new HTTPATask();  //выполняем запрос на уведомление заказа
		 httpatask.execute(ArgPost);	 
		 }
	 //-------------------------------------------------
	 }
	};
	
	class HTTGATask extends AsyncTask<String, Void, Void> {
		@Override
	    protected void onPreExecute() {
	      super.onPreExecute();
	      //etZak.setText(String.valueOf(i));
	    }
		
		@Override
	    protected Void doInBackground(String... httpPath) {  //выполняем запрос
		try {
			ShedZapros();	
		} catch (Exception e) {
	        e.printStackTrace(); }
			
			
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {  //выводим результат в GUI
	      super.onPostExecute(result);
	      zakaz[0]=strAdr;
	      zakaz[1]=strZak;
	      zakaz[2]=strTlf;
	      zakaz[3]=strKod;
	      zakaz[4]=strDat;
	      zakaz[5]=strTim;
	      zakaz[6]=strCar;
	      zakaz[7]=strUve;
	      adapter.notifyDataSetChanged();
	      Log.d(TAG, "Обработался GET");
	      if (flagMusic){ PlayZakazMusic(); }
	    }
	}  //конец класса HTTGATask
	
	class HTTPATask extends AsyncTask<String, Void, Void> {
		
		@Override
	    protected void onPreExecute() {
	      super.onPreExecute();
	     // etUve.setText("Begin");
	      zakaz[7]="Begin";
	    }
		@Override
	    protected Void doInBackground(String... httpPath) {
		try {
			PostZapros();	
		} catch (Exception e) {
	        e.printStackTrace(); }
			
			
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      //etUve.setText(UvedStr);
	      zakaz[7]=strUve;
	    }
		
	} //конец класса HTTPATask
	
	
	private void ShedZapros() { //зашедуленый запрос на прием заказа
		 HttpClient httpclient = new DefaultHttpClient();
		 HttpGet httpget = new HttpGet(httpPath);
		 try {
			 HttpEntity httpEntity = httpclient.execute(httpget).getEntity();
		     
			   if (httpEntity != null){
			    InputStream inputStream = httpEntity.getContent();
			    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			    StringBuilder stringBuilder = new StringBuilder();
			        
			    String line = null;
			        
			    while ((line = bufferedReader.readLine()) != null) {
			     stringBuilder.append(line + "\n");
			    }
			  
			    inputStream.close();	    
//---------- разбираем JSON строку
try{
	  JSONObject jo =  new JSONObject(stringBuilder.toString());
	  if(jo.getString(ERROR).equals("none")) {
		//разбираем строку RESULT
	    	if (jo.getString(RESULT).equals("null")) {
	    		strAdr = String.valueOf(i)+" "+"Нет заказов";
	    	strZak = "Заказ";
   			strTlf = "Телефон";
   			strKod = "Код";
   			strDat = "Дата";
   			strTim = "Время";
   			strCar = "Машина";
   			strUve = "Уведомлен";
   			flagMusic = false;
   			flagbrightness = false;
	    	}
	    	else {
	    	//разбираем строку RESULT
	    		try{
	    		JSONObject joRESULT = new JSONObject(jo.getString(RESULT));
	    		strAdr = String.valueOf(i)+" "+joRESULT.getString(ADRES);
	    		strZak = "Зак: " + joRESULT.getString(ZAKAZ);
	    		MyVariables.SAVED_TEXT_4 = joRESULT.getString(ADRES);
	    		strTlfCall = joRESULT.getString(TELEFON);
	    		strTlf = "Тел: " + joRESULT.getString(TELEFON);
	    		strKod = "Код: " + joRESULT.getString(KODE);
	    		strDat = "Дата: " + joRESULT.getString(DAT);
	    		strTim = "Время: " + joRESULT.getString(TIM);
	    		strCar = "Машина: " + joRESULT.getString(CAR);
	    		strUve = joRESULT.getString(UVEDOMLEN);
	            if(strUve.equals("null")) { flagMusic = true; } 
	            else {flagMusic = false;}
	            flagbrightness = true;
	    		} catch (JSONException e) {
	    	    	// TODO Auto-generated catch block
	    	    	e.printStackTrace();
	    	    	strAdr = String.valueOf(i)+" "+"ERROR JSON STRING!";  //выводим если приходит не строка JSON
	    	    	strZak = "";
	    			  strTlf = "";
	    			  strKod = "";
	    			  strDat = "";
	    			  strTim = "";
	    			  strCar = "";
	    			  strUve = "";
	    			flagMusic = false;
	    			flagbrightness = false;
	    	    }	
	    	}  
	  } else {
		  strAdr = String.valueOf(i)+" "+"Нет заказов";
		  strZak = "Заказ";
 			strTlf = "Телефон";
 			strKod = "Код";
 			strDat = "Дата";
 			strTim = "Время";
 			strCar = "Машина";
 			strUve = "Уведомлен";
		  flagMusic = false;
		  flagbrightness = false;
	         }
	  
} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	strAdr = String.valueOf(i)+" "+"ERROR JSON STRING!";  //выводим если приходит не строка JSON
	strZak = "";
	strTlf = "";
	strKod = "";
	strDat = "";
	strTim = "";
	strCar = "";
	strUve = "";
	flagMusic = false;
	flagbrightness = false;
}

//---------- конец разбора JSON строки		    
			   }
		 } catch (ClientProtocolException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  } catch (IOException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();				  
				   }
	} //конец ShedZapros
	
	private void PostZapros() {

		try{
	          HttpClient client = 
	              new DefaultHttpClient();
	          HttpPost post = 
	             new HttpPost(httpPath);
	          List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	          pairs.add(new BasicNameValuePair("uvedomlen", ArgPost));
	          post.setEntity(new UrlEncodedFormEntity(pairs));
	          HttpResponse response = client.execute(post);
	          
	          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"windows-1251"));
	          StringBuilder sb = new StringBuilder();
	          String line = null;
	          while ((line = reader.readLine()) != null) {
	          sb.append(line + System.getProperty("line.separator"));
	          }    
	                 resultPOST = sb.toString();
	                
	    //разбираем JSON UVEDOMLEN
	    try {
			JSONObject joPOST = new JSONObject(resultPOST);
			if (joPOST.getString("error").equals("none")) {
				UvedStr = joPOST.getString("result");
			}
			else {
				UvedStr = joPOST.getString("result");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	          
		}
		catch (org.apache.http.client.ClientProtocolException e) {
            // TODO Auto-generated catch block
            resultPOST = "ClientProtocolException: " + e.getMessage();
    }
	 catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		
		flagZapros=true;
	}  //конец PostZapros
	
	 public enum CheckRadio {
			radio1,
			radio2,
			radio3
		}
	 private void releaseMP() {
		    if (mediaPlayer != null) {
		      try {
		        mediaPlayer.release();
		        mediaPlayer = null;
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
		  }
	 
	 class MusicTask extends AsyncTask<Void, Void, Void> {

		    @Override
		    protected void onPreExecute() {
		      super.onPreExecute();
		     // releaseMP();
		    }

		    protected Void doInBackground(Void... params) {
		    	if (flagendmusic) {
		    	flagendmusic = false;
		    	mediaPlayer.start();}
		    	
		    	Log.d(TAG, "Играем музыку");
		        return null;
		    }

		    @Override
		    protected void onPostExecute(Void result) {
		      super.onPostExecute(result);
		      flagendmusic = true;
		    }
		  }
	
	private void PlayZakazMusic() {
		releaseMP();
    	 //проверяем радио
    	CheckRadio myradio = CheckRadio.valueOf(MyVariables.SAVED_TEXT_3);
    	 switch (myradio) {
    	 case radio1 : 
    		 mediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.bumbai);	
             mt = new MusicTask();
             mt.execute();
    	 break;
    	 case radio2 : 
    		 mediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.krasivyj);	
             mt = new MusicTask();
             mt.execute();
    	 break;
    	 case radio3 : 
    		 mediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.laia);	
             mt = new MusicTask();
             mt.execute();
    	 break;
    	 default:
    		 mediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.bumbai);	
             mt = new MusicTask();
             mt.execute();
    	 }	    	     	
	}
	
	private void setBrightness(int brightness) {
        Window w = this.getActivity().getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.screenBrightness = (float)brightness/100;
        if (lp.screenBrightness<.01f) lp.screenBrightness=.01f;
        w.setAttributes(lp);
}	//регулируем яркость экрана

}
