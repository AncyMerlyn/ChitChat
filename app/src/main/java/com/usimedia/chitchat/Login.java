package com.usimedia.chitchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.usimedia.chitchat.model.LoginModel;
import com.usimedia.chitchat.model.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email =(EditText) findViewById(R.id.login_activity_email_text);
        final EditText password = (EditText) findViewById(R.id.login_activity_password_text);
        final Button login = (Button) findViewById(R.id.login_activity_login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginModel model = new LoginModel();
                model.setUsername(email.getText().toString());
                model.setPassword(password.getText().toString());
                Signin sign = new Signin();
                sign.execute(model);
            }
        });
    }

    public String login(String email, String pass) {
        String username = email;
        String password = pass;
        String url = "http://192.168.2.175:8000/login";
        RequestBody request = new FormBody.Builder()
                            .add("email", username)
                            .add("password", password)
                            .build();
        String result =  post(url,request);
        return result;
    }

    public String post(String url, RequestBody body){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        String result = null;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    private class Signin extends AsyncTask<LoginModel, Void, String> {
        protected String doInBackground(LoginModel... models) {
          LoginModel model = models[0];
            String result = login(model.getUsername(), model.getPassword());
            return result;
        }

         protected void onPostExecute(String result) {
             JSONObject jsonResponse = null;
             boolean isSuccess=false;
             String username = null;
             try {
                 if(result != null) {
                     jsonResponse = new JSONObject(result);
                     isSuccess = jsonResponse.getBoolean("response");
                     username = jsonResponse.getString("name");
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
              //   isSuccess = false;
             }

             String message = isSuccess ? "Welcome ".concat(username) : "Login Failed";
             Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
             if(isSuccess == true){
                 Intent contact = new Intent(Login.this,Contacts.class);
                 startActivity(contact);
             }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
