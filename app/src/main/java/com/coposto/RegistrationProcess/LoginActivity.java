package com.coposto.RegistrationProcess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.coposto.Coposto.MainActivity;
import com.coposto.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText ET_PHONE,ET_PASS;
    String login_phone,login_pass;
    Boolean correct_login=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ET_PHONE = (EditText)findViewById(R.id.etPhonenumber);
        ET_PASS = (EditText)findViewById(R.id.etPassword);

    }
    public void userReg(View view)
    {

        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 1) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Close COPOSTO?")
                .setMessage("Do you want to exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void userLogin(View view)
    {
        login_phone = ET_PHONE.getText().toString();
        login_pass = ET_PASS.getText().toString();
        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, login_phone, login_pass);
        if (correct_login) {
            finish();
        }
    }

    public class BackgroundTask extends AsyncTask<String,Void,String> {
        AlertDialog alertDialog;
        Context ctx;

        BackgroundTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Login Information....");
        }

        @Override
        protected String doInBackground(String... params) {

            final HttpClient httpclient = new DefaultHttpClient();
            final HttpPost httppost = new HttpPost("http://www.coposto.com/api/login");
            String method = params[0];
            if (method == "login") {
                String login_email = params[1];
                String login_pass = params[2];
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("email", login_email));
                nameValuePairs.add(new BasicNameValuePair("password", login_pass));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Execute HTTP Post Request
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if ((response.getStatusLine().toString()).equals("HTTP/1.0 200 OK"))
                {
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StringBuilder builder = new StringBuilder();
                    try {
                        for (String line = null; (line = reader.readLine()) != null; ) {
                            builder.append(line).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONObject finalResult = null;
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    try {
                        finalResult = new JSONObject(tokener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String value = null;
                    String first;
                    JSONArray second;
                    JSONObject third,fourth = null;
                    try {
                        first = finalResult.get("data").toString();
                        second = new JSONArray(first);
                        third = (JSONObject) second.get(0);
                        fourth = (JSONObject) third.get("fields");
                        value = fourth.get("first_name").toString();
                        }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String result = null;

                    try {
                        result = "Success<"+fourth.get("first_name").toString() + ">";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return result;
                }
                else
                {
                    return "Fail to login";
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            int login_success = result.indexOf("Success");
            if (login_success >= 0) {
                int name_start_index = result.indexOf("<")+1;
                int name_end_index = result.indexOf(">");
                String USER_NAME = result.substring(name_start_index,name_end_index);
                Toast.makeText(ctx, "WELCOME TO COPOSTO", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("arg", USER_NAME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Fail To Login")
                        .setMessage("The password or phone number you entered is incorrect. Please try again.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        }
    }

}

