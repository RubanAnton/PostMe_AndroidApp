package com.example.antonruban.postme;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.antonruban.postme.Database.MyDAO;
import com.example.antonruban.postme.Model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import static com.example.antonruban.postme.HostClass.URL.HOST;

public class SignUpActivity extends AppCompatActivity {


    EditText login,password,email;
    public static String username;
    public static String password_;
    public static String email_;
    public static int NO_OPTIONS=0;
    public static String SHAHash;
    public static String SOAP_RESP="";
    private Context context;
    //    MyDBHelper dbHelper = new MyDBHelper(this);
//    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//

        setContentView(R.layout.activity_sign_up);

        context = this;

        login = (EditText) findViewById(R.id.loginSign);
        password = (EditText) findViewById(R.id.passwordSign);
        email = (EditText)findViewById(R.id.emailSign);
        String title_1 = null;
        try {
            title_1= new SoapAsyncTask().execute("offline").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, SOAP_RESP, Toast.LENGTH_SHORT).show();
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onClickSingUp(View view) throws NoSuchAlgorithmException {
        username = login.getText().toString();
        password_ = password.getText().toString();
        email_ = email.getText().toString();
        //computeSHAHash(SHAHash);
        if(username.trim().length() == 0){
            Toast.makeText(this, "Enter login", Toast.LENGTH_SHORT).show();
        }else if(password_.trim().length() ==0){
            // computeSHAHash(password_);
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }else if(email_.trim().length() ==0){
            Toast.makeText(this, "Enter login", Toast.LENGTH_SHORT).show();
        }
        else if(MyDAO.get(context).getUser(username)!=null){
            Toast.makeText(SignUpActivity.this, "Such user exists in local db.",Toast.LENGTH_SHORT ).show();
        }else {
            // User user = new User(username ,password_);
            User user = new User(username,password_);
            user.setEmail(email_);
            SaveUser save=new SaveUser();
            save.execute();
            MyDAO.get(context).addUser(user);
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
    //SHAHash password
//    @NonNull
//    private static String convertToHex(byte[] data) throws java.io.IOException {
//        StringBuffer sb = new StringBuffer();
//        String hex=null;
//        hex= Base64.encodeToString(data, 0, data.length, NO_OPTIONS);
//        sb.append(hex);
//        return sb.toString();
//    }
//    public void computeSHAHash(String password_) {
//        MessageDigest mdSha1 = null;
//        //password_ = password.getText().toString();
//        try {
//            mdSha1 = MessageDigest.getInstance("SHA-1");
//        } catch (NoSuchAlgorithmException e1) {}
//        try {
//            mdSha1.update(SignUpActivity.password_.getBytes("ASCII"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        byte[] data = mdSha1.digest();
//        try {
//            SHAHash=convertToHex(data);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    class SaveUser extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("login", username);
                jsonObject.accumulate("password", password_);
                jsonObject.accumulate("email", email_);

                String data = jsonObject.toString();
                Log.d("json data", data);
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(HOST + "UC/saveUser");
                StringEntity se = new StringEntity(data);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);
                // 9. receive response as inputStream
                Log.d("----!!!!_----", httpResponse.getEntity().getContent().toString());


            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class SoapAsyncTask extends AsyncTask<String, Void, String> {

        String request;

        @Override
        protected String doInBackground(String... strings) {
            request = SOAPRequest(strings[0]);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }

        private String SOAPRequest(String option) {
            String SOAP_ACTION = "http://ruban.com/soap/getNameRequest";
            String METHOD_NAME = "getNameRequest";
            String NAMESPACE = "http://ruban.com/soap";
            String URL = HOST+"soapws/name.wsdl";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            PropertyInfo info = new PropertyInfo();
            info.setName("name_project");
            info.setValue(option);
            info.setType(String.class);
            request.addProperty(info);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
            try {
                httpTransportSE.debug = true;
                httpTransportSE.call(SOAP_ACTION, envelope);
                String str = "Запрос\n" + httpTransportSE.requestDump + "\n" +
                        "Ответ\n" + httpTransportSE.responseDump;
                SoapPrimitive response = (SoapPrimitive ) envelope.getResponse();
                Log.d("SOAP ___ ",str+ response.toString());
                SOAP_RESP=response.toString();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
