package com.meridian.voiceofislam.videoaudioupload;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meridian.voiceofislam.Constant;
import com.meridian.voiceofislam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by libin on 9/19/2016.
 */
public class Loginfragment extends AppCompatActivity {
    EditText us,pd;
    Button login;
    ProgressBar progress;
    private static final String REGISTER_URL = Constant.BASE_URL+"services/response.php";
    ArrayList<LoginModel> logmodel;
    LoginModel lm;
    JSONArray mArray1;
    JSONObject mJsonObject1;
    String log_in;
    String us_nm,p_wd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        progress = (ProgressBar)findViewById(R.id.progress_bar);

        Toolbar tool= (Toolbar) findViewById(R.id.toolbar_topa);
        setSupportActionBar(tool);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        us= (EditText) findViewById(R.id.editText);
        pd= (EditText)findViewById(R.id.editText2);
        login=(Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                us_nm=us.getText().toString();

                p_wd=pd.getText().toString();
                registerUser1();
            }
        });

    }

    private void registerUser1() {

        if(us_nm.isEmpty())
        {
           us.setError("Enter Username");
        }
        else if(p_wd.isEmpty())
        {
            pd.setError("Enter Password");
        }
        else {
            progress.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
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
                                            Intent inte = new Intent(getApplicationContext(), Vis_Upload_Activity.class);
                                            startActivity(inte);
                                        } else {
                                            progress.setVisibility(ProgressBar.GONE);
                                            final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(Loginfragment.this).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setMessage("Invalid Username or Password");

                                            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();


                                                }
                                            });
                                            alertDialog.show();
                                        }


                                        progress.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {

                                {
                                    progress.setVisibility(ProgressBar.GONE);
                                    final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(Loginfragment.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Invalid Username or Password");

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
                    params.put("fid", "6");
                    params.put("username", us_nm);
                    params.put("password", p_wd);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}