package com.loancare.lakeview;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.loancare.lakeview.GetSet.ForgetPasswordDetails;
import com.loancare.lakeview.GetSet.ForgetUIDDetails;
import com.loancare.lakeview.GetSet.LoginDetails;
import com.loancare.lakeview.GetSet.ResponseDetails;
import com.loancare.lakeview.Utils.Consts;
import com.loancare.lakeview.Utils.CustomProgressBar;
import com.loancare.lakeview.Utils.Dbhelper;
import com.loancare.lakeview.Utils.InternetStatus;
import com.loancare.lakeview.Utils.Util;
import com.loancare.lakeview.Utils.Validation;
import com.loancare.lakeview.retrofitt.ApiClient;
import com.loancare.lakeview.retrofitt.ForgetPasswordInput;
import com.loancare.lakeview.retrofitt.GetPDFStatementInterface;
import com.loancare.lakeview.retrofitt.RetrofitInterface;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {
    // Login layout declaration
    Activity activity;
    EditText userid_edit, pwd_edit;
    LinearLayout sigin_btn, register, help, forget_uid, forgot_pwd;
    CheckBox check_userid, check_enabletouch;
    Dbhelper mHelper;
    SQLiteDatabase database;
    boolean bool_userid = false, bool_pwd = false;
    String Checked = "checked";
    String status = "";
    boolean username = false, pass = false;
    String Username, Password, Enable_Touch;
    boolean doubleBackToExitPressedOnce = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String userid_edit_length, pwd_edit_length;
    private Dialog validationdialog = null;
    private Dialog forgot_uid_dialog = null;
    TextView
            Error_resp_txt,
            touch_enable,
            forgot_userid_txt,
            forgot_pwd_txt,
            save_user_id,
            sign_in_txt,
            register_txt,
            needhelp_txt,
            legal_txt,
            copy_rights_txt,
            nmls_txt,
            nmlsCompay_txt,
            welcome_txt;


    final Context context = this;
    SharedPreferences sharedpreferences;
    //font assigning
    Context ctx = LoginActivity.this;
    // Finger print declaration
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "Loancare";
    private Cipher cipher;
    private TextView textView;
    public KeyguardManager keyguardManager;
    public FingerprintManager fingerprintManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String Count;
    int reqestType = 0;
    public static String TAG = LoginActivity.class.getSimpleName();
    String[] permissions = new String[]
            {
                    Manifest.permission.INTERNET,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    //Manifest.permission.USE_FINGERPRINT,
                    Manifest.permission.ACCESS_NETWORK_STATE,

            };

    RetrofitInterface apiInterface;

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 2500;
    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;
    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        checkPermissions();
        mHelper = new Dbhelper(getApplicationContext());
        database = mHelper.getWritableDatabase();
        Intilization();
        web_screens();
        setfont();


        if (Consts.enable_Enable1.equals("false")) {
            check_enabletouch.setChecked(false);
        } else {
            check_enabletouch.setChecked(true);

        }


        if (Consts.check_userid.equals("false")) {
            check_userid.setChecked(false);
        } else {
            check_userid.setChecked(true);
            userid_edit.setText(Consts.UNAME);

        }


        if (InternetStatus.InternetStatus(LoginActivity.this)) {

        } else {


            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Message");
            alertDialog.setMessage("Please check Internet Connection !!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }


        sigin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetStatus.InternetStatus(LoginActivity.this)) {

                    Validation();

                }
                else
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("Please check Internet Connection !!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        check_userid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    status = "true";
                    Consts.check_userid = "true";
                } else {
                    status = "false";
                    Consts.check_userid = "false";
                }
            }
        });


        check_enabletouch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (check_enabletouch.isChecked()) {
                    status = "true";

                    Consts.enable_Enable = "true";
                    Consts.enable_Enable1 = "true";
                    //enableTouch();
                    checkFingerPrintScanner();

                } else {
                    status = "false";
                    Consts.enable_Enable = "false ";
                    Consts.enable_Enable1 = "false";

                }
            }
        });


        forget_uid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgot_uid_dialog = new Dialog(context);
                forgot_uid_dialog.setContentView(R.layout.custom_dialog);

                TextView dialog_for_userID = (TextView) forgot_uid_dialog.findViewById(R.id.dialog_for_userid);
                TextView dialog_txt = (TextView) forgot_uid_dialog.findViewById(R.id.dialog_txt);
                TextView cancel = (TextView) forgot_uid_dialog.findViewById(R.id.dialogue_cancel_txt);
                TextView submit = (TextView) forgot_uid_dialog.findViewById(R.id.dialogue_submit_txt);

                final TextView Error_loan_no = (TextView) forgot_uid_dialog.findViewById(R.id.Error_loan_no);
                final TextView Error_ssn_no = (TextView) forgot_uid_dialog.findViewById(R.id.Error_ssn_no);

                EditText edit_loan_no = (EditText) forgot_uid_dialog.findViewById(R.id.dialog_loan_no);
                EditText edit_loan_pwd = (EditText) forgot_uid_dialog.findViewById(R.id.dialog_loan_pwd);
                // edit_loan_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edit_loan_pwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


                edit_loan_no.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        Error_loan_no.setVisibility(View.INVISIBLE);
                    }
                });


                edit_loan_pwd.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        Error_ssn_no.setVisibility(View.INVISIBLE);
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forgot_uid_dialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edit_loan_no = (EditText) forgot_uid_dialog.findViewById(R.id.dialog_loan_no);
                        EditText edit_loan_pwd = (EditText) forgot_uid_dialog.findViewById(R.id.dialog_loan_pwd);
                        TextView Error_loan_no = (TextView) forgot_uid_dialog.findViewById(R.id.Error_loan_no);
                        TextView Error_ssn_no = (TextView) forgot_uid_dialog.findViewById(R.id.Error_ssn_no);

                        Consts.loan_no = edit_loan_no.getText().toString();
                        Consts.ssn_no = edit_loan_pwd.getText().toString();

                        int loan_no_count1 = Consts.loan_no.length();
                        int ssn_no_count1 = Consts.ssn_no.length();


                        if (loan_no_count1 == 0 && ssn_no_count1 == 0) {
                            Log.e("Both are empty", "Both are empty");
                            Error_loan_no.setVisibility(View.VISIBLE);
                            Error_loan_no.setText("Please enter Loan Number");
                            Error_ssn_no.setVisibility(View.VISIBLE);
                            Error_ssn_no.setText("Please enter SSN ");

                        } else if (loan_no_count1 == 0 && ssn_no_count1 != 0) {
                            Log.e("Loan no empty", "Loan no empty");
                            Error_loan_no.setVisibility(View.VISIBLE);
                            Error_loan_no.setText("Please enter Loan Number");
                            if (ssn_no_count1 < 4) {
                                Error_ssn_no.setVisibility(View.VISIBLE);
                                Error_ssn_no.setText("SSN must be 4 digits ");
                            } else {
                                Error_ssn_no.setVisibility(View.INVISIBLE);
                                Error_ssn_no.setText("");
                            }

                        } else if (loan_no_count1 != 0 && ssn_no_count1 == 0)

                        {
                            Log.e("SSN  empty", "SSN  empty");
                            Error_ssn_no.setVisibility(View.VISIBLE);
                            Error_ssn_no.setText("Please enter SSN");

                            if (loan_no_count1 < 6 || loan_no_count1 > 14) {
                                Error_loan_no.setVisibility(View.VISIBLE);
                                Error_loan_no.setText("Loan number must be 6 through 10 or 14 digits");
                            } else {

                                Error_loan_no.setVisibility(View.INVISIBLE);
                                Error_loan_no.setText("");
                            }

                        } else if (loan_no_count1 != 0 && ssn_no_count1 != 0) {

                            Log.e("Both are not empty", "Both are not empty");
                            if (loan_no_count1 < 6 || loan_no_count1 > 14) {
                                Error_loan_no.setVisibility(View.VISIBLE);
                                Error_loan_no.setText("Loan number must be 6 through 10 or 14 digits");
                            } else {
                                Error_loan_no.setVisibility(View.INVISIBLE);
                                Error_loan_no.setText("");
                            }

                            if (ssn_no_count1 < 4) {
                                Error_ssn_no.setVisibility(View.VISIBLE);
                                Error_ssn_no.setText("SSN must be 4 digits ");

                            } else {
                                Error_ssn_no.setVisibility(View.INVISIBLE);
                                Error_ssn_no.setText("");
                            }

                            if ((loan_no_count1 >= 6 && loan_no_count1 <= 14) && ssn_no_count1 == 4) {
                                Log.e("correct validation", "correct validation");

                                new Forgot_UID().execute();

                        /*        if(InternetConnection.checkConnection(LoginActivity.this))
                                ForgotUIDService();
                                else
                                    Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                   */         }

                        }

                    }

                });

                forgot_uid_dialog.show();
            }
        });


        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog1);

                TextView dialog_for_pwd = (TextView) dialog.findViewById(R.id.dialog_for_pwd);
                TextView dialog_alert_txt = (TextView) dialog.findViewById(R.id.alert_txt);
                TextView dialog_reset_txt = (TextView) dialog.findViewById(R.id.dialog_reset_txt);
                EditText edit_loan_id = (EditText) dialog.findViewById(R.id.dialog_edit_loan_id);
                EditText edit_ssn_no = (EditText) dialog.findViewById(R.id.dialog_edit_ssn_no);

                //  edit_ssn_no.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edit_ssn_no.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

                Consts.loan_no1 = edit_loan_id.getText().toString();
                Consts.ssn_no1 = edit_ssn_no.getText().toString();

                final TextView Error1_loan_no = (TextView) dialog.findViewById(R.id.Error1_loan_no);
                final TextView Error1_ssn_no = (TextView) dialog.findViewById(R.id.Error1_ssn_no);
                TextView cancel = (TextView) dialog.findViewById(R.id.dial_cancel_txt);
                TextView submit = (TextView) dialog.findViewById(R.id.dial_submit_txt);


                edit_loan_id.addTextChangedListener(new TextWatcher()
                {
                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        Error1_loan_no.setVisibility(View.INVISIBLE);
                    }
                });


                edit_ssn_no.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        Error1_ssn_no.setVisibility(View.INVISIBLE);
                    }
                });


                // Click cancel to dismiss android custom dialog box
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edit_ssn_no = (EditText) dialog.findViewById(R.id.dialog_edit_ssn_no);
                        EditText edit_loan_id = (EditText) dialog.findViewById(R.id.dialog_edit_loan_id);
                        edit_loan_id.setText("");
                        edit_ssn_no.setText("");
                        dialog.dismiss();
                    }
                });


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextView Error1_loan_no = (TextView) dialog.findViewById(R.id.Error1_loan_no);
                        TextView Error1_ssn_no = (TextView) dialog.findViewById(R.id.Error1_ssn_no);

                        EditText edit_loan_id = (EditText) dialog.findViewById(R.id.dialog_edit_loan_id);
                        EditText edit_ssn_no = (EditText) dialog.findViewById(R.id.dialog_edit_ssn_no);

                        Consts.loan_no1 = edit_loan_id.getText().toString();
                        Consts.ssn_no1 = edit_ssn_no.getText().toString();

                        int loan_no_count = Consts.loan_no1.length();
                        int ssn_no_count = Consts.ssn_no1.length();


                        if (loan_no_count == 0 && ssn_no_count == 0) {
                            Log.e("Both are empty", "Both are empty");
                            Error1_loan_no.setVisibility(View.VISIBLE);
                            Error1_loan_no.setText("Please enter Loan Number");
                            Error1_ssn_no.setVisibility(View.VISIBLE);
                            Error1_ssn_no.setText("Please enter SSN ");

                        } else if (loan_no_count == 0 && ssn_no_count != 0) {
                            Log.e("Loan no empty", "Loan no empty");
                            Error1_loan_no.setVisibility(View.VISIBLE);
                            Error1_loan_no.setText("Please enter Loan Number");
                            if (ssn_no_count < 9) {
                                Error1_ssn_no.setVisibility(View.VISIBLE);
                                Error1_ssn_no.setText("SSN must be 9 digits ");
                            } else {
                                Error1_ssn_no.setVisibility(View.INVISIBLE);
                                Error1_ssn_no.setText("");
                            }

                        } else if (loan_no_count != 0 && ssn_no_count == 0)

                        {
                            Log.e("SSN  empty", "SSN  empty");
                            Error1_ssn_no.setVisibility(View.VISIBLE);
                            Error1_ssn_no.setText("Please enter SSN");

                            if (loan_no_count < 6 || loan_no_count > 14) {
                                Error1_loan_no.setVisibility(View.VISIBLE);
                                Error1_loan_no.setText("Loan number must be 6 through 10 or 14 digits");
                            } else {
                                Error1_loan_no.setVisibility(View.INVISIBLE);
                                Error1_loan_no.setText("");
                            }

                        } else if (loan_no_count != 0 && ssn_no_count != 0) {

                            Log.e("Both are not empty", "Both are not empty");
                            if (loan_no_count < 6 || loan_no_count > 14) {
                                Error1_loan_no.setVisibility(View.VISIBLE);
                                Error1_loan_no.setText("Loan number must be 6 through 10 or 14 digits");
                            } else {
                                Error1_loan_no.setVisibility(View.INVISIBLE);
                                Error1_loan_no.setText("");
                            }

                            if (ssn_no_count < 9) {
                                Error1_ssn_no.setVisibility(View.VISIBLE);
                                Error1_ssn_no.setText("SSN must be 9 digits ");

                            } else {
                                Error1_ssn_no.setVisibility(View.INVISIBLE);
                                Error1_ssn_no.setText("");
                            }


                            if ((loan_no_count >= 6 && loan_no_count <= 14) && ssn_no_count == 9) {
                                // new Forgot_PWD().execute();
                                Volley_service();
                            }

                        }


                    }
                });

                dialog.show();

            }
        });


        nmls_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.nmlsconsumeraccess.org"));
                startActivity(i);
            }
        });


    }

    private  void ForgotUIDService()
    {

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        CustomProgressBar.showDialog(LoginActivity.this);
        String url = Consts.forgotUserIdUrl;
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // response
                        Log.d("Response", response);

                        Gson gson = new Gson();

                        ForgetUIDDetails responseDetails = gson.fromJson(response,ForgetUIDDetails.class);
                        if(responseDetails.status.CustomErrorCode.equals("0"))
                        {
                            Intent i = new Intent(LoginActivity.this, WebActivity.class);
                            Consts.JSONResponse = response;
                            Log.e("Consts.JSONResponse", "Consts.JSONResponse==>" + Consts.JSONResponse);
                            //LoginActivity.this.finish();
                            startActivity(i);
                        }
                        else
                        {

                            Error_resp_txt.setVisibility(View.VISIBLE);
                            Error_resp_txt.setText(responseDetails.status.Message);

                        }


                        //responseDetails.data


                        CustomProgressBar.hideDialog();

                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // error
                        Log.d("Error.Response", error.toString());
                        CustomProgressBar.hideDialog();

                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(error.toString());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();


                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loanNumber",Consts.loan_no);
                params.put("ssn", Consts.ssn_no);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        queue.add(postRequest);
       /* postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
    }


    public void Intilization() {

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        //fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        userid_edit = (EditText) findViewById(R.id.input_userid);
        pwd_edit = (EditText) findViewById(R.id.input_password);
        sigin_btn = (LinearLayout) findViewById(R.id.sign_in_layout);
        forget_uid = (LinearLayout) findViewById(R.id.linear_forget_UID);
        forgot_pwd = (LinearLayout) findViewById(R.id.linear_forget_pwd);
        check_userid = (CheckBox) findViewById(R.id.check_userid);
        check_enabletouch = (CheckBox) findViewById(R.id.check_enabletouch);
        touch_enable = (TextView) findViewById(R.id.enable_touch_txt);
        forgot_userid_txt = (TextView) findViewById(R.id.for_user_id_txt);
        forgot_pwd_txt = (TextView) findViewById(R.id.for_pwd_txt);
        save_user_id = (TextView) findViewById(R.id.save_user_id_txt);
        sign_in_txt = (TextView) findViewById(R.id.sign_in_txt);
        register_txt = (TextView) findViewById(R.id.register_txt);
        needhelp_txt = (TextView) findViewById(R.id.needhelp_txt);
        copy_rights_txt = (TextView) findViewById(R.id.copy_rights_txt);
        nmls_txt = (TextView) findViewById(R.id.nmls);
        nmlsCompay_txt = (TextView) findViewById(R.id.nmlsCompany);
        legal_txt = (TextView) findViewById(R.id.legal_txt);
        register = (LinearLayout) findViewById(R.id.linear_register);
        help = (LinearLayout) findViewById(R.id.linear_help);
        welcome_txt = (TextView) findViewById(R.id.welcome_txt);
        Error_resp_txt = (TextView) findViewById(R.id.Error_resp_txt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        }

        validationdialog = new Dialog(context);
    }

    public void setfont()

    {
        Util.setFont(1, ctx, forgot_pwd_txt, "Forgot Password");
        Util.setFont(1, ctx, forgot_userid_txt, "Forgot User ID");
        Util.setFont(1, ctx, save_user_id, "Save User ID");
        Util.setFont(1, ctx, touch_enable, "Enable Touch ID");
        Util.setFont(3, ctx, sign_in_txt, "Sign In");
        Util.setFont(1, ctx, register_txt, "Register");
        Util.setFont(1, ctx, needhelp_txt, "Need Help ?");
        Util.setFont(1, ctx, legal_txt, "Legal/Privacy");
        Util.setFont(1, ctx, copy_rights_txt, "© 2017 Lakeview Loan Servicing, LLC. All Rights Reserved.");
        String nmlsText="NMLS#391521 <u>www.nmlsconsumeraccess.org </u>";
        nmls_txt.setText(Html.fromHtml(nmlsText),TextView.BufferType.SPANNABLE);
        /* Util.setFont(1, ctx, nmlsCompay_txt, "Company ");
        Util.setFont(1, ctx, nmls_txt, "NMLS#2916");*/
        Util.setFont(2, ctx, welcome_txt, "Welcome");

    }

    public void Validation() {

        Username = userid_edit.getText().toString().length() > 0 ? userid_edit.getText().toString() : Consts.UNAME;
        Password = pwd_edit.getText().toString().length() > 0 ? pwd_edit.getText().toString() : Consts.PWD;

        Consts.UNAME = Username;
        Consts.PWD = Password;

        userid_edit.setText(Consts.UNAME);
        pwd_edit.setText(Consts.PWD);

        if (!Validation.isEmpty(Username)) {
            username = true;
        } else {
            username = false;
            userid_edit.setError("Please check your UserID !!");
        }
        if (!Validation.isEmpty(Password)) {
            pass = true;
        } else {
            pass = false;
            pwd_edit.setError("Please Enter Valid Password");
        }

        if (username && pass) {
            // login Success


            String sql = "delete from " + Dbhelper.TABLE_NAME;
            database.execSQL(sql);
            ContentValues value = new ContentValues();
            value.put(Dbhelper.KEY_UNAME, Username);
            value.put(Dbhelper.KEY_CHECKED, status);
            database.insert(Dbhelper.TABLE_NAME, null, value);

            if (username && pass && Consts.enable_Enable1.equals("true")) {
                Consts.Touch_status = "true";
            } else if (username && pass && Consts.enable_Enable1.equals("false")) {
                new SendPostRequest().execute();
                // Volley_Login_service();
                //Login_webservice();
            }

            if (Consts.Touch_status.equals("true")) {

                validationdialog.setContentView(R.layout.custom_touch_id);
                ImageView thump_img = (ImageView) validationdialog.findViewById(R.id.thump_img);
                TextView finger_txt_1 = (TextView) validationdialog.findViewById(R.id.dia_txt1);
                TextView finger_txt_2 = (TextView) validationdialog.findViewById(R.id.dia_txt2);
                TextView cancel_txt = (TextView) validationdialog.findViewById(R.id.dia_txt3);

                cancel_txt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Log.e("cancel_txt", "cancel_txt");

                        check_enabletouch.setChecked(false);

                        if (validationdialog != null) {
                            validationdialog.dismiss();
                        }


                    }
                });


                if(!validationdialog.isShowing()){
                    validationdialog.show();
                }

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FingerprintManager fingerprintManager = (FingerprintManager)
                        context.getSystemService(Context.FINGERPRINT_SERVICE);
                checkFingerPrintScanner();
            }

        }
    }


    public void Getdata() {
        String sql = "SELECT * FROM " + Dbhelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            userid_edit.setText(cursor.getString(cursor.getColumnIndex(Dbhelper.KEY_UNAME)));
            pwd_edit.setText(cursor.getString(cursor.getColumnIndex(Dbhelper.KEY_PASSWORD)));
            status = cursor.getString(cursor.getColumnIndex(Dbhelper.KEY_CHECKED));
            //Consts.enable_Enable = cursor.getString(cursor.getColumnIndex(Dbhelper.KEY_ENABLE));

            if (status.equals("true")) {
                check_userid.setChecked(true);
                check_userid.setClickable(true);
            } else {
                check_userid.setChecked(false);
                check_userid.setClickable(true);
            }

            if (Consts.enable_Enable.equals("true")) {
                check_enabletouch.setChecked(true);
                check_enabletouch.setClickable(true);
            } else {
                check_enabletouch.setChecked(false);
                check_enabletouch.setClickable(true);
            }
        }
    }

    // finger print coding deployment
    protected void checkFingerPrintScanner() {
        /*
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(getApplication(), "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_LONG).show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplication(), "Fingerprint authentication permission not enabled", Toast.LENGTH_LONG).show();

            } else {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Toast.makeText(getApplication(), "Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show();

                } else {
                    if (!keyguardManager.isKeyguardSecure()) {
                        Toast.makeText(getApplication(), "Lock screen security not enabled in Settings", Toast.LENGTH_LONG).show();
                    } else {
                        generateKey();
                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintAuthenticationHandler helper = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                helper = new FingerprintAuthenticationHandler(this);
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }
        */
    }

    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e)

        {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e)

        {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {

            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;

        } catch
                (KeyStoreException | CertificateException | UnrecoverableKeyException
                        | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }

        }, 2000);
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);

            return false;
        }

        return true;
    }

    public void enableTouch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you Enable Touch !!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "Enable Touch Ok:==>");


                check_enabletouch.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    FingerprintManager fingerprintManager = (FingerprintManager)
                            context.getSystemService(Context.FINGERPRINT_SERVICE);

                    checkFingerPrintScanner();


                } else {
                    check_enabletouch.setChecked(false);
                    Toast.makeText(LoginActivity.this, "FINGERPRINT_SENSOR NOT AVAILABLE FOR THIS DEVICE"
                            , Toast.LENGTH_SHORT).show();
                }
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                check_enabletouch.setChecked(false);
            }
        }).show();
    }

    public void enableTouch1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Log on using your " +
                "finger print now");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e(TAG, "Enable Touch Ok:==>");


                check_enabletouch.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    FingerprintManager fingerprintManager = (FingerprintManager)
                            context.getSystemService(Context.FINGERPRINT_SERVICE);

                    checkFingerPrintScanner();

                } else {
                    check_enabletouch.setChecked(false);
                    Toast.makeText(LoginActivity.this, "FINGERPRINT_SENSOR NOT AVAILABLE FOR THIS DEVICE"
                            , Toast.LENGTH_SHORT).show();
                }
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                check_enabletouch.setChecked(false);
            }

        }).show();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute()
        {
            CustomProgressBar.showDialog(LoginActivity.this);
        }

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(Consts.authendicateUrl); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", Username);
                postDataParams.put("password", Password);
                postDataParams.put("resourcename", "Android");
                postDataParams.put("log", "24.0");
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000 /* milliseconds */);
                conn.setConnectTimeout(60000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader
                            (conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        Log.e("line", "line==>" + line.charAt(29));
                        Consts.Error = String.valueOf(line.charAt(29));
                        Log.e("Consts.Error", "Consts.Error==>" + Consts.Error);
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();
                } else {
                    return new String("");
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            userid_edit.setText(Consts.UNAME);
            pwd_edit.setText(Consts.PWD);

            Log.e("result", "result==>" + result);
            CustomProgressBar.hideDialog();

            try
            {



                if(!result.isEmpty())
                {
                    Gson gson = new Gson();
                    LoginDetails responseDetails = gson.fromJson(result, LoginDetails.class);

                    if (responseDetails.status != null) {

                        if (responseDetails.status.CustomErrorCode != null) {
                            if (responseDetails.status.CustomErrorCode.equalsIgnoreCase("0"))
                            {
                                Intent i = new Intent(LoginActivity.this, WebActivity.class);
                                Consts.JSONResponse = result;
                                Log.e("Consts.JSONResponse", "Consts.JSONResponse==>" + Consts.JSONResponse);
                                // LoginActivity.this.finish();
                                startActivity(i);
                            }
                            else if (responseDetails.status.CustomErrorCode.equalsIgnoreCase("100"))
                            {
                                Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                                Consts.Change_PWD = "change_password";
                                Consts.JSONResponse = result;
                                Log.e("Consts.JSONResponse", "Consts.JSONResponse==>" + Consts.JSONResponse);
                                //LoginActivity.this.finish();
                                i.putExtra("URL", "Change");
                                startActivity(i);
                            }
                            else
                            {
                                if (responseDetails.status.Message != null)
                                {
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage(responseDetails.status.Message);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener()
                                            {
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }

                            }

                        }


                    }

                }
            }catch (Exception e)
            {

            }

        }

        public String getPostDataString(JSONObject params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                Object value = params.get(key);
                if (first)
                    first = false;
                else
                    result.append("&");
                Log.e("result==>", "result@@@==>" + result);
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }

    ////////////////////////////////////////////////////////////////////// Forgot_PWD ////////////////////////////////////////////////////////

    public class Forgot_PWD extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            CustomProgressBar.showDialog(LoginActivity.this);
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Consts.forgotPasswordUrl); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("ssn", Consts.ssn_no1);
                postDataParams.put("loanNumber", Consts.loan_no1);
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        Log.e("line", "line==>" + line.charAt(29));
                        Consts.Error_Fpwd = String.valueOf(line.charAt(29));
                        Consts.Count = String.valueOf(line.charAt(180));
                        Log.e(" Count ", " Count==>" + Count);
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String response) {
            Log.e("result", "result==>" + response);


            try {

                Gson gson = new Gson();
                ResponseDetails password = gson.fromJson(response, ResponseDetails.class);


                if (password.status != null) {

                    if (password.status.CustomErrorCode != null) {

                        if (password.status.CustomErrorCode.equalsIgnoreCase("0")) {

                            if (password.data.secQuesstatus.SecurityStatus.equalsIgnoreCase("true") || password.data.secQuesstatus.SecurityStatus.equalsIgnoreCase("0")) {
                                Consts.JSONResponse_Fpwd = response;
                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle("Message");
                                alertDialog.setMessage(password.status.Message);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            } else {
                                Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                                Consts.forgot_pwd_screen = "forgot_password";
                                Consts.JSONResponse_Fpwd = response;
                                //Consts.JSONResponse_Fpwd="Checking session setting";
                                Log.e("JSONResponse_Fpwd", "  Consts.JSONResponse_Fpwd ==>" + Consts.JSONResponse_Fpwd);
                                i.putExtra("URL", response);  // passing the jsonr response to the WebViewActviity
                                // LoginActivity.this.finish();
                                startActivity(i);
                            }

                        } else {


                            Consts.JSONResponse_Fpwd = response;
                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                            alertDialog.setTitle("Message");
                            alertDialog.setMessage(password.status.Message);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }

                    }

                }
            } catch (Exception e)
            {

            }

            CustomProgressBar.hideDialog();



        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                Log.e("result==>", "result@@@==>" + result);

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }

            return result.toString();
        }
    }
    //////////////////////////////////////////////////////////////////////  Forgot_PWD ////////////////////////////////////////////////////////

    public class Forgot_UID extends AsyncTask<String, Void, String> {



        protected void onPreExecute()
        {
            CustomProgressBar.showDialog(LoginActivity.this);
        }

        protected String doInBackground(String... arg0)
        {

            try {

                URL url = new URL(Consts.forgotUserIdUrl); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("ssn", Consts.ssn_no);
                postDataParams.put("loanNumber", Consts.loan_no);
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000 /* milliseconds */);
                conn.setConnectTimeout(60000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter
                        (new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        Log.e("line", "line==>" + line.charAt(29));
                        Consts.Error_F_UID = String.valueOf(line.charAt(29));
                        Log.e("Consts.Error_F_UID", " Consts.Error_F_UID==>" + Consts.Error_F_UID);
                        sb.append(line);
                        break;
                    }

                    in.close();

                    return sb.toString();

                }
                else
                {

                    return new String("");

                }
            } catch (Exception e)

            {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                return new String("Exception: " + e.getMessage());

            }

        }

        @Override
        protected void onPostExecute(String result) {
            CustomProgressBar.hideDialog();

            Log.e("result", "result==>" + result);

            try {

                if (!result.isEmpty())
                {

                    Gson gson = new Gson();
                    ForgetUIDDetails details = gson.fromJson(result, ForgetUIDDetails.class);

                    if (details.status != null)
                    {
                        if (details.status.CustomErrorCode != null)
                        {
                            if (details.status.CustomErrorCode.equalsIgnoreCase("0")) {
                                if (details.status.Message != null) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                    alertDialog.setTitle("Message");
                                    alertDialog.setMessage(details.status.Message);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    dismissForgotUserIdDialog();
                                                }
                                            });
                                    alertDialog.show();
                                }

                            }
                            else
                            {
                                if (details.status.Message != null) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                    alertDialog.setTitle("Message");
                                    alertDialog.setMessage(details.status.Message);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    dismissForgotUserIdDialog();
                                                }
                                            });
                                    alertDialog.show();
                                }

                            }
                        }
                    }


                }
            }
            catch (Exception e)
            {

            }


        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                Log.e("result==>", "result@@@==>" + result);
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }

            return result.toString();
        }
    }

    private void dismissForgotUserIdDialog() {
        if (forgot_uid_dialog != null) {
            forgot_uid_dialog.dismiss();
        }
    }


    public void web_screens() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (InternetStatus.InternetStatus(LoginActivity.this)) {

                    Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                    i.putExtra("URL", "register");
                    // Consts.register = "register";
                    // LoginActivity.this.finish();
                    startActivity(i);

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("Please check Internet Connection !!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                }


            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetStatus.InternetStatus(LoginActivity.this))
                {
                    Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                    //Consts.help = "help";
                    i.putExtra("URL", "help");
                    LoginActivity.this.finish();
                    startActivity(i);
                }
                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("Please check Internet Connection !!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }


            }
        });

        legal_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (InternetStatus.InternetStatus(LoginActivity.this)) {

                    Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                    i.putExtra("URL", "legal");
                    //Consts.legal = "legal";
                    // LoginActivity.this.finish();
                    startActivity(i);

                } else {

                    // Toast.makeText(LoginActivity.this, "Please check Internet Connection !!", Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("Please check Internet Connection !!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                }


            }
        });


    }

////////////////////////////////////////////////////////////////////// FingerprintAuthenticationHandler ////////////////////////////////////////////////////////

    public class FingerprintAuthenticationHandler extends FingerprintManager.AuthenticationCallback {


        private Context context;


        // Constructor
        public FingerprintAuthenticationHandler(Context mContext)
        {
            context = mContext;
        }


        public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            /*
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
            */
        }


        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            //this.update("Fingerprint Authentication error\n" + errString, false);
        }


        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            this.update("Fingerprint Authentication help\n" + helpString, false);
        }


        @Override
        public void onAuthenticationFailed() {
            this.update("Fingerprint Authentication failed.", false);
        }


        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            this.update("Fingerprint Authentication succeeded.", true);
        }


        public void update(String e, Boolean success) {
            if (validationdialog != null) {
                validationdialog.dismiss();
            }

            Toast.makeText(context.getApplicationContext(), e, Toast.LENGTH_SHORT).show();
            if (success) {

                new SendPostRequest().execute();
                //Volley_Login_service();
                Consts.Touch_status = "false";

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (check_enabletouch.isChecked()) {
            Validation();
        }
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void Touch_display()
    {

    }


    public void Volley_service()
    {
        CustomProgressBar.showDialog(LoginActivity.this);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = Consts.forgotPasswordUrl;
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {

                            Gson gson = new Gson();
                            ResponseDetails password = gson.fromJson(response, ResponseDetails.class);

                            if (password.status != null) {

                                if (password.status.CustomErrorCode != null)
                                {

                                    if (password.status.CustomErrorCode.equalsIgnoreCase("0")) {

                                        if (password.data.secQuesCollection!= null && password.data.secQuesCollection.isEmpty() == false)
                                        {
                                            Consts.JSONResponse_Fpwd = response;
                                            Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                                            Consts.forgot_pwd_screen = "forgot_password";
                                            Consts.JSONResponse_Fpwd = response;
                                            //Consts.JSONResponse_Fpwd="Checking session setting";
                                            Log.e("JSONResponse_Fpwd", "  Consts.JSONResponse_Fpwd ==>" + Consts.JSONResponse_Fpwd);
                                            i.putExtra("URL", response);
                                            //LoginActivity.this.finish();
                                            startActivity(i);

                                        }
                                        else
                                        {
                                            Consts.JSONResponse_Fpwd = response;
                                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                            alertDialog.setTitle("Message");
                                            alertDialog.setMessage("A temporary password has been created for yout account and delivered to the email address on file." +
                                                    " If you do not receive the email with your temporary password in the next 30 minutes, please contact customer support to update your email address.");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }

                                    } else {


                                        Consts.JSONResponse_Fpwd = response;
                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                        alertDialog.setTitle("Message");
                                        alertDialog.setMessage(password.status.Message);
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }

                                }

                            }
                        }catch (Exception e)
                        {

                        }

                        CustomProgressBar.hideDialog();
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        CustomProgressBar.hideDialog();
                        // error
                        Log.d("Error.Response", error.toString());

                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("The server is not reachable. The server may be down or your internet settings may be down" +
                                "Please try again later");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ssn", Consts.ssn_no1);
                params.put("loanNumber", Consts.loan_no1);

                return params;
            }
        };

        queue.add(postRequest);
       /* postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void ForgetPasswordService()
    {
        GetPDFStatementInterface forgetservice = ApiClient.getClient().create(GetPDFStatementInterface.class);
        Call<ForgetPasswordDetails> call = forgetservice.Forget(new ForgetPasswordInput(Consts.ssn_no1, Consts.loan_no1));

    }

    public void Volley_Login_service()
    {

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        CustomProgressBar.showDialog(LoginActivity.this);
        String url = Consts.authendicateUrl;
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // response
                        Log.d("Response", response);

                        Gson gson = new Gson();

                        ResponseDetails responseDetails = gson.fromJson(response,ResponseDetails.class);
                        if(responseDetails.status.CustomErrorCode.equals("0"))
                        {
                            Intent i = new Intent(LoginActivity.this, WebActivity.class);
                            Consts.JSONResponse = response;
                            Log.e("Consts.JSONResponse", "Consts.JSONResponse==>" + Consts.JSONResponse);
                            // LoginActivity.this.finish();
                            startActivity(i);
                        }
                        else
                        {

                            Error_resp_txt.setVisibility(View.VISIBLE);
                            Error_resp_txt.setText(responseDetails.status.Message);

                        }

                        CustomProgressBar.hideDialog();

                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // error
                        Log.d("Error.Response", error.toString());
                        CustomProgressBar.hideDialog();

                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("The server is not reachable. The server may be down or your internet settings may be down" +
                                "Please try again later");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();


                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",Username);
                params.put("password", Password);
                params.put("resourcename", "android");
                params.put("log", "25.04");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        queue.add(postRequest);
       /* postRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
    }

}









