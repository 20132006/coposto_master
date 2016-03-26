/*import android.support.v7.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UploadProfilePic = (EditText) findViewById(R.id.profile_pic);
        UploadPassportPic = (EditText) findViewById(R.id.passport_pic);
        UploadProfilePic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                profile = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        UploadPassportPic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passport = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && requestCode == 1 && data != null && passport == 1){
            Uri selectedImage = data.getData();
            UploadProfilePic.setText(selectedImage.toString());
            //ImageView imageView = (ImageView) getActivity().findViewById(R.id.icon);
            //imageView.setImageURI(selectedImage);
            passport = 0;
        }
        else if (requestCode == RESULT_LOAD_IMAGE && requestCode == 1 && data != null && profile == 1){
            Uri selectedImage = data.getData();
            UploadPassportPic.setText(selectedImage.toString());
            //ImageView imageView = (ImageView) getActivity().findViewById(R.id.icon);
            //imageView.setImageURI(selectedImage);
            profile = 0;
        }
    }
    public static final int RESULT_LOAD_IMAGE=1;
    EditText UploadProfilePic;
    EditText UploadPassportPic;

    EditText ET_NAME,ET_PHON,ET_USER_PASS;
    String name,user_phon,user_pass;
    ImageButton SignUpButton;
    int passport = 0;
    int profile = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ET_NAME = (EditText)findViewById(R.id.etName);
        ET_PHON= (EditText)findViewById(R.id.etPhonenumber);
        ET_USER_PASS = (EditText)findViewById(R.id.etPassword);
        SignUpButton = (ImageButton) findViewById(R.id.signup_ImageButton);

        UploadProfilePic = (EditText) findViewById(R.id.profile_pic);
        UploadPassportPic = (EditText) findViewById(R.id.passport_pic);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });

        UploadProfilePic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                profile = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        UploadPassportPic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passport = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

    }
    public void userSignUp()
    {

        name = ET_NAME.getText().toString();
        user_phon = ET_PHON.getText().toString();
        user_pass =ET_USER_PASS.getText().toString();
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,name,user_phon,user_pass);
        finish();

    }*/
package com.coposto.RegistrationProcess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class SignUpActivity extends Activity {
    EditText ET_FIRST_NAME,ET_PASS,ET_LAST_NAME,ET_EMAIL;
    String first_name,last_name,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ET_FIRST_NAME = (EditText)findViewById(R.id.etFirstName);
        ET_LAST_NAME = (EditText) findViewById(R.id.etLastName);
        ET_EMAIL = (EditText) findViewById(R.id.etEmail);
        ET_PASS = (EditText)findViewById(R.id.etPassword);


    }
    public void userReg(View view)
    {

        first_name = ET_FIRST_NAME.getText().toString();
        last_name = ET_LAST_NAME.getText().toString();
        email =ET_EMAIL.getText().toString();
        password =ET_PASS.getText().toString();
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, first_name, last_name, email,password);
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

        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

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
            final HttpPost httppost = new HttpPost("http://www.coposto.com/api/register");
            String method = params[0];
            if (method == "register") {
                String first_name = params[1];
                String last_name = params[2];
                String email = params[3];
                String password = params[4];

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("first_name", first_name));
                nameValuePairs.add(new BasicNameValuePair("last_name", last_name));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));
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

                    String result = null;

                    result = "Registration Success...";
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
            if (result == "Registration Success...") {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.putExtra("arg", first_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                alertDialog.setMessage(result);
                alertDialog.show();
            }

        }
    }

}
