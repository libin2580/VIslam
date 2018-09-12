package com.meridian.voiceofislam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meridian.voiceofislam.videoaudioupload.LoginModel;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user1 on 25-May-17.
 */

public class Login extends AppCompatActivity {
    MKLoader progressBar;
    EditText user,email,phone;
    Button btn;
    TextView skip;
    String st_usr,st_email,st_phone;
    ArrayList<LoginModel> logmodel;
    LoginModel lm;
    JSONArray mArray1;
    JSONObject mJsonObject1;
    String log_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_main);

        user= (EditText) findViewById(R.id.editText);
        email= (EditText) findViewById(R.id.editText2);
        phone= (EditText) findViewById(R.id.editText3);
        btn= (Button) findViewById(R.id.button);
        skip= (TextView) findViewById(R.id.skip);
        progressBar= (MKLoader) findViewById(R.id.progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_topa);
        setSupportActionBar(toolbar);

try {
    SharedPreferences myPrefs = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    String reg_status = myPrefs.getString("reg_status", null);
    if(reg_status.equalsIgnoreCase("success"))
    {
        Intent in=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }
}catch (Exception e){
    e.printStackTrace();
}


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_usr=user.getText().toString();
                st_email=email.getText().toString();
                st_phone=phone.getText().toString();
                log();
            }

            private void log() {
                if(st_usr.isEmpty())
                {
                    user.setError("Enter Username");
                }
                else if (st_phone.isEmpty())
                {
                    email.setError("Enter Phone Number");
                }
                else if(st_email.isEmpty())
                {
                    phone.setError("Enter Email");
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL+"services/response.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response != null) {
                                        logmodel = new ArrayList<LoginModel>();
                                        try {
                                            mArray1 = new JSONArray(response);
                                            for (int i = 0; i < mArray1.length(); i++) {
                                                lm = new LoginModel();
                                                mJsonObject1 = mArray1.getJSONObject(i);
                                                Log.d("OutPut", mJsonObject1.getString("login"));


                                                log_in = mJsonObject1.getString("login");

                                                lm.setLog_in(log_in);


                                                logmodel.add(lm);

                                                System.out.println("<<<22222222222222video_cat_id>>>>" + log_in);
                                                if (log_in.equalsIgnoreCase("success")) {

                                                    SharedPreferences sharedpref=getSharedPreferences("MyPref",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor=sharedpref.edit();
                                                    editor.putString("reg_status","success");
                                                    editor.putString("username", st_usr);
                                                    editor.putString("email", st_email);
                                                    editor.putString("phone", st_phone);
                                                    editor.commit();


                                                    Intent inte = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(inte);
                                                    finish();
                                                } else {
                                                    progressBar.setVisibility(ProgressBar.GONE);
                                                    final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(Login.this).create();
                                                    alertDialog.setTitle("Alert");
                                                    alertDialog.setMessage("Invalid Fields");

                                                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.dismiss();


                                                        }
                                                    });
                                                    alertDialog.show();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {

                                        {
                                            progressBar.setVisibility(ProgressBar.GONE);
                                            final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(Login.this).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setMessage("Invalid Fields");

                                            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();


                                                }
                                            });
                                            alertDialog.show();

                                        }
                                    }
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("fid", "19");
                            params.put("username", st_usr);
                            params.put("email", st_email);
                            params.put("phone", st_phone);

                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }

        });
    }
}