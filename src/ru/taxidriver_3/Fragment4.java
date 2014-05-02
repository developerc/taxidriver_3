package ru.taxidriver_3;

//import ru.taxidriver_1.ActivityFour.HTTPATask;
//import ru.taxidriver_1.R;
//import ru.taxidriver_1.R;
//import ru.taxidriver_1.ActivityFour.HTTPATask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

//import ru.taxidriver_1.MyVariables;




import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class Fragment4 extends Fragment {
	TextView tvProc;
	 ListView lvMain;
	  String[] stay;
	  Button btnGobackwards, btnReg;
	  String ResQuery, StrState, StrDelzak;
	  String resultPOST, resultJSON;
	  String ArgPost = "2";
	  String httpPath;
	  String httpDestr;
	  String httpStCh;
	  boolean statechyes;
	  HTTPATask httpatask;  //обьявили класс  для метода POST

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment4, container, false);
		
		tvProc = (TextView) view.findViewById(R.id.tvProc);
		 lvMain = (ListView) view.findViewById(R.id.lvMain);	
		    lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		            this.getActivity(), R.array.stay,
		            android.R.layout.simple_list_item_single_choice);
		    lvMain.setAdapter(adapter);
	    btnReg = (Button) view.findViewById(R.id.btnReg);
	    btnReg.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
			    	//flagZapros=false;
			    	//ArgPost = "2";
		    	  switch (lvMain.getCheckedItemPosition()+2)
			        {
			        case 2:
			        case 3:
			        case 4:
			        case 5:
			        case 6:
			        case 7:
			        case 8:
			        case 9:
			        case 10:
			        	ResQuery=String.valueOf(lvMain.getCheckedItemPosition()+2);
			        	StrState = "4";
			        	StrDelzak= "0";
			        	statechyes=false;
			        	break;
			        case 11:
			        	//ResQuery="-1";
			        	StrState = "5";
			        	//StrDelzak= "0";
			        	statechyes=true;
			        	break;
			        case 12:
			        	//ResQuery="-1";
			        	StrState = "6";
			        	//StrDelzak= "0";
			        	statechyes=true;
			        	break;
			        }
			    	 httpatask = new HTTPATask();  //выполняем запрос на перестановку машины в очереди
					 httpatask.execute(ResQuery);
		      }
		    });
		
		return view;
	}
	
	class HTTPATask extends AsyncTask<String, Void, Void> {
		
		@Override
	    protected void onPreExecute() {
	      super.onPreExecute();
	      tvProc.setText("Begin");
	    }
		@Override
	    protected Void doInBackground(String... ResQuery) {
		try {
			httpPath =MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/qpush";
			//httpPath ="http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/qpush";
			httpDestr =MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/orderdestroy";
			//httpDestr ="http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/orderdestroy";
			httpStCh =MyVariables.HTTPAdress+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/stateupd";
			//httpStCh ="http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/stateupd";
			if (statechyes) { 
				StateChange();	
			}
			else{
			PostZapros();
			PostDestroy();
			}
		} catch (Exception e) {
	        e.printStackTrace(); }
			
		// httpPath ="http://pchelka.teleknot.ru/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/qpush";	
		 //httpPath ="http://192.168.28.19/api/"+MyVariables.SAVED_TEXT_1+"/"+MyVariables.SAVED_TEXT_2+"/qpush";
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      tvProc.setText(resultJSON);
	    }
		
	} //конец класса HTTPATask
	
	private void PostZapros() {
		try {
		HttpClient client = 
	              new DefaultHttpClient();
	          HttpPost post = 
	             new HttpPost(httpPath);
	          List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	       /*  pairs.add(new BasicNameValuePair("row", ResQuery));
	          pairs.add(new BasicNameValuePair("state", StrState));
	          pairs.add(new BasicNameValuePair("delzak", StrDelzak));*/
	          
	          pairs.add(new BasicNameValuePair("point_id", ResQuery));
	          pairs.add(new BasicNameValuePair("state", StrState));
	          post.setEntity(new UrlEncodedFormEntity(pairs));
	          HttpResponse response = client.execute(post);
	          
	          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"windows-1251"));
	          StringBuilder sb = new StringBuilder();
	          String line = null;
	          while ((line = reader.readLine()) != null) {
	          sb.append(line + System.getProperty("line.separator"));
	          }    
	                 resultPOST = sb.toString();
	     //Разбираем JSON строку
	     try {
			JSONObject joPOST = new JSONObject(resultPOST);
			if (joPOST.getString("error").equals("none")) {
			 resultJSON = "Готово";	
			}
			else {
				resultJSON = "Ошибка!";	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	               //  Toast.makeText(this, resultPOST, Toast.LENGTH_SHORT).show();
		}
		catch (org.apache.http.client.ClientProtocolException e) {
            // TODO Auto-generated catch block
            resultPOST = "ClientProtocolException: " + e.getMessage();
    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
	
	private void PostDestroy() {
		try {
		HttpClient client = 
	              new DefaultHttpClient();
	          HttpPost post = 
	             new HttpPost(httpDestr);
	          List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	         
	          post.setEntity(new UrlEncodedFormEntity(pairs));
	          HttpResponse response = client.execute(post);
	          
	          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"windows-1251"));
	          StringBuilder sb = new StringBuilder();
	          String line = null;
	          while ((line = reader.readLine()) != null) {
	          sb.append(line + System.getProperty("line.separator"));
	          }    
	                 resultPOST = sb.toString();
	     //Разбираем JSON строку
	     try {
			JSONObject joPOST = new JSONObject(resultPOST);
			if (joPOST.getString("error").equals("none")) {
			 resultJSON = "Готово";	
			}
			else {
				resultJSON = "Ошибка!";	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	               //  Toast.makeText(this, resultPOST, Toast.LENGTH_SHORT).show();
		}
		catch (org.apache.http.client.ClientProtocolException e) {
            // TODO Auto-generated catch block
            resultPOST = "ClientProtocolException: " + e.getMessage();
    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
	
	private void StateChange() {
		try {
		HttpClient client = 
	              new DefaultHttpClient();
	          HttpPost post = 
	             new HttpPost(httpStCh);
	          List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	          pairs.add(new BasicNameValuePair("state", StrState));
	          post.setEntity(new UrlEncodedFormEntity(pairs));
	          HttpResponse response = client.execute(post);
	          
	          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"windows-1251"));
	          StringBuilder sb = new StringBuilder();
	          String line = null;
	          while ((line = reader.readLine()) != null) {
	          sb.append(line + System.getProperty("line.separator"));
	          }    
	                 resultPOST = sb.toString();
	     //Разбираем JSON строку
	     try {
			JSONObject joPOST = new JSONObject(resultPOST);
			if (joPOST.getString("error").equals("none")) {
			 resultJSON = "Готово";	
			}
			else {
				resultJSON = "Ошибка!";	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	               //  Toast.makeText(this, resultPOST, Toast.LENGTH_SHORT).show();
		}
		catch (org.apache.http.client.ClientProtocolException e) {
            // TODO Auto-generated catch block
            resultPOST = "ClientProtocolException: " + e.getMessage();
    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}	
	
}

