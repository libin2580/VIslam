package com.meridian.voiceofislam;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user1 on 25-May-17.
 */

public class Feedback extends AppCompatActivity
{
    EditText user,email,feedback;
    Button submit;
    String st_user,st_email,st_feedback;
    ProgressBar progressBar;
    private static final String REGISTER_URL = "http://app.knm.com.php56-9.dfw3-2.websitetestlink.com/services/response.php";
    boolean edittexterror=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_topa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user= (EditText) findViewById(R.id.username);
        email= (EditText) findViewById(R.id.email);
        feedback= (EditText) findViewById(R.id.feedback);
        submit= (Button) findViewById(R.id.button);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_user=user.getText().toString();
                st_email=email.getText().toString();
                st_feedback=feedback.getText().toString();
                log();
            }

            private void log() {
                if(st_user.isEmpty())
                {
                    user.setError("Enter Username");
                }
                else if (st_email.isEmpty())
                {
                    email.setError("Enter Email");
                }
                else if(st_feedback.isEmpty())
                {
                    feedback.setError("Enter Feedback");
                }
                else
                {
                    NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(Feedback.this);
                    boolean i = networkCheckingClass.ckeckinternet();
                    if (i == true) {

                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url = "http://app.knm.com.php56-9.dfw3-2.websitetestlink.com/services/response.php?fid=20&name="+st_user+"&email="+st_email+"&message="+st_feedback;
                        System.out.println(url);
                        StringRequest stringRequest3 = new StringRequest
                                (Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        //  tv.setText("Response is: "+ response);
//pd.dismiss();
                                        System.out.println("++++++++++++++RESPONSE+++++++++++++++ gallery :" + response);
                                        if (response != null) {

                                            JSONArray jsonarray;
                                            JSONObject jsonobject = null;

                                            try {

                                               /* jsonobject = new JSONObject(response);



                                                System.out.println("g3");

*/


                                                String status = "";
                                            /*    System.out.println("statussssscours"+status);*/
                                                if (response.contentEquals("success"))
                                                {

                                                    // Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(ProgressBar.GONE);

                                                  /*  final SweetAlertDialog dialog = new SweetAlertDialog(FeedBackActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                                                    dialog.setTitleText("Feedback Submitted")
                                                            //  .setContentText("You have given your feedback")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    Intent is = new Intent(getApplicationContext(), MainActivity.class);
                                                                    startActivity(is);
                                                                    finish();
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            .show();


                                                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#10315a"));*/



                                                    final AlertDialog alertDialog = new AlertDialog.Builder(Feedback.this).create();
                                                    alertDialog.setTitle("Success");
                                                    alertDialog.setMessage("You have given your feedback");
                                                    alertDialog.setCancelable(false);
                                                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                           /* Intent is = new Intent(getApplicationContext(), MainActivity.class);
                                                            startActivity(is);*/
                                                            finish();
                                                            dialog.dismiss();


                                                        }
                                                    });
                                                    alertDialog.show();
                                                    Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                                                    //     nbutton.setTextColor(getResources().getColor(R.color.Orange));
//


                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(ProgressBar.GONE);

                                                }





                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            System.out.println("nothing to displayy");
                                        }
                                    }


                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //tv.setText("That didn't work!");

                                    }
                                });


                        queue.add(stringRequest3);


                    } else {




                        final AlertDialog alertDialog = new AlertDialog.Builder(Feedback.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Oops Your Connection Seems Off..");

                        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                            }
                        });
                        alertDialog.show();

                    }
                }
            }

        });


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