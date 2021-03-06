package com.example.adity.loginscreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by adity on 27/09/2016.
 */
public class getJSON extends AsyncTask<String,Void,String> {
    public static boolean loginflag;
    public static String user_role, Name, Email, u_id;
    ProgressDialog loading;
    String userName,passWord;
    Boolean flag;
Context c;
   public getJSON(Context context,Boolean b)
    {
        c=context;
        loginflag=false;
        user_role=Name=Email=null;
        flag=b;
    }
    @Override
    protected String doInBackground(String... arg0) {

        // URL to call
         userName = arg0[0];
         passWord=arg0[1];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result="";
        String resultToDisplay = "";
        try {

            data="?username="+URLEncoder.encode(userName,"UTF-8");
            data +="&password="+URLEncoder.encode(passWord,"UTF-8");
            String urlString = "http://issclibrary.esy.es/connn.php"+data;
            URL url = new URL(urlString);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();

            bufferedReader =new BufferedReader(new InputStreamReader(con.getInputStream()));
            result =bufferedReader.readLine();
    return result;
        } catch (Exception e) {

            return new String("Exception : " +e.getMessage());
        }


    }

    protected void onPostExecute(String result) {
            String jsonStr=result;
       // Toast.makeText(c, result, Toast.LENGTH_SHORT).show();
       if(jsonStr!=null)
       {
           try{
               JSONObject jsonObj = new JSONObject(jsonStr);
               String status=jsonObj.getString("status");
           //    Toast.makeText(c,"Sign-In Successful!",Toast.LENGTH_LONG).show();

               if(status.equals("Success"))
                {

                    if(flag==false)
                        MainActivity.progress.dismiss();

                     Toast.makeText(c,"Sign-In Successful!",Toast.LENGTH_LONG).show();
                    Name=""+jsonObj.getString("Name");
                    Email=""+jsonObj.getString("Email");
                    user_role=""+jsonObj.getString("user_role");
                    u_id = "" + jsonObj.getString("u_id");
                    loginflag=true;
                    if(flag==false) {

                        Intent intent = new Intent(MainActivity.fa, MainPage.class);
                        MainActivity.fa.startActivity(intent);
                        MainActivity.fa.finish();
                    }
                    new SessionManager(c).createLoginSession(userName,passWord);


                }
               else
                    if(status.equals("Unsuccessful"))
                    {if(flag==false)
                        MainActivity.progress.dismiss();
                        Toast.makeText(c,"Please Check Username Password!",Toast.LENGTH_LONG).show();

                    }
               else {
                        if(flag==false)
                        MainActivity.progress.dismiss();
                        Toast.makeText(c, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                    }catch(JSONException e)
           {
               e.printStackTrace();
               if(flag==false)
               MainActivity.progress.dismiss();
               Toast.makeText(c, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
           }
           }
        else
       {
           Toast.makeText(c, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
       }
       }
    }



