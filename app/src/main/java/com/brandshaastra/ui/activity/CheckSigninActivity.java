package com.brandshaastra.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.brandshaastra.DTO.BusinessDataDto;
import com.brandshaastra.DTO.UserDTO;
import com.brandshaastra.R;
import com.brandshaastra.SplashScreenActivity;
import com.brandshaastra.api.apiClient;
import com.brandshaastra.api.apiRest;
import com.brandshaastra.databinding.ActivityCheckSigninBinding;
import com.brandshaastra.https.HttpsRequest;
import com.brandshaastra.interfaces.Consts;
import com.brandshaastra.interfaces.Helper;
import com.brandshaastra.network.NetworkManager;
import com.brandshaastra.preferences.SharedPrefrence;
import com.brandshaastra.utils.ProjectUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class CheckSigninActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private String TAG = CheckSigninActivity.class.getSimpleName();
    ActivityCheckSigninBinding binding;
    ProgressDialog progressDialog;
    private SharedPreferences firebase;
    //    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    String email, name;
    UserDTO userDTO;
    private SharedPrefrence prefrence;
    private HashMap<String, String> parms = new HashMap<>();
    //    List<BusinessDataDto> businessdataList;
    List<BusinessDataDto> businessdataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CheckSigninActivity.this, R.layout.activity_check_signin);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        mContext = CheckSigninActivity.this;
        progressDialog = new ProgressDialog(CheckSigninActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        prefrence = SharedPrefrence.getInstance(CheckSigninActivity.this);

        userDTO = prefrence.getParentUser(Consts.USER_DTO);
//        businessDataDto = prefrence.getBusinessData(Consts.BUSINESSDATA_DTO);

//        callbackManager = CallbackManager.Factory.create();

        MaterialButton btn_login_google = findViewById(R.id.btn_login_google);
        mAuth = FirebaseAuth.getInstance();

        btn_login_google.setOnClickListener(view -> {
            if (isNetworkAvailable()) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(CheckSigninActivity.this, gso);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 112);
            } else {
                Toast.makeText(CheckSigninActivity.this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
            }
        });


        /*findViewById(R.id.CBsignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckSigninActivity.this, SigninActivity.class));
            }
        });*/
        setUiAction();
    }

    public void setUiAction() {
        binding.CBsignIn.setOnClickListener(this);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
//                        Consts.LOGIN_TYPE.equals("google");
                        prefrence.setBooleanValue(Consts.GOOGLE_REGISTERED, true);
                        FirebaseUser user = mAuth.getCurrentUser();
                        email = user.getEmail();
                        name = user.getDisplayName();
                        register();
//                        login();

                        if (prefrence.getBooleanValue(Consts.IS_REGISTERED)) {
//                            Log.e("USER_ID", "" + userDTO.getUser_id());

                            check_state();
//                            register();

                        }
//                        else {
//
//                            register();
//
//                        }


                    } else {
                        Toast.makeText(CheckSigninActivity.this, "Failed to Sign IN", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 112) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                if (resultCode != 0) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    firebaseAuthWithGoogle(task.getResult().getIdToken());
                } else {
                    Log.e("google","1");
                    Toast.makeText(CheckSigninActivity.this, "Error while login with google", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("google","2");

                Toast.makeText(CheckSigninActivity.this, "Error while login with google", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void clickForSubmit() {

        if (!ProjectUtils.isEmailValid(binding.CETemailadd.getText().toString().trim())) {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_email));

        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    public void check_state() {

        Log.e("USER_ID_trscker", "1");


        parms.put(Consts.USER_ID, userDTO.getUser_id());
        Log.e("USER_ID_trscker", " 2 " + "userid-params" + parms.toString());

        new HttpsRequest(Consts.CHECK_APPROVE, parms, CheckSigninActivity.this).stringPost("TAG", Consts.USER_CONTROLLER, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                try {

                    Log.e("BUS_DATA",""+response.toString());
                    Type getpetDTO = new TypeToken<List<BusinessDataDto>>() {
                    }.getType();
                    businessdataList = (ArrayList<BusinessDataDto>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
//                    businessdataList = new Gson().fromJson(response.getJSONObject("data").toString(), getpetDTO);
//                    Log.e("USERDTO", "" + userDTO.getName());
//                    prefrence.setParentUser(userDTO, Consts.NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                flag = true;
                if (flag) {
                    if (businessdataList.size() != 0) {
                        prefrence.setBusinessData(businessdataList.get(0), Consts.BUSINESSDATA_DTO);
                    }
                    Intent in = new Intent(CheckSigninActivity.this, HomeScreenActivity.class);
                    startActivity(in);
                    finish();
//                    Log.e("fgxg", response.toString());
//                    Log.e("dfcsk", "backResponse: " + response.toString());
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                } else {
                    Intent in = new Intent(CheckSigninActivity.this, BusinessDocumentActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }
            }
        });
    }


    public void login() {
        Log.e("EMAIL", "" + ProjectUtils.getEditTextValue(binding.CETemailadd));

        progressDialog.show();
//        Toast.makeText(getApplicationContext(), Consts.LOGIN_TYPE, Toast.LENGTH_SHORT).show();
        Retrofit retrofit = apiClient.getClient(Consts.BASE_URL + Consts.USER_CONTROLLER);
        apiRest api = retrofit.create(apiRest.class);

        Call<ResponseBody> callone = api.checksignin(ProjectUtils.getEditTextValue(binding.CETemailadd), firebase.getString(Consts.DEVICE_TOKEN, ""));


        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                //       Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("Checksignin_response", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                          /*  Intent in = new Intent(mContext, SigninActivity.class);
                            in.putExtra(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(binding.CETemailadd));
                            startActivity(in);*/


                            sendotp();


                        } else if (sstatus == 2) {
                            Toast.makeText(CheckSigninActivity.this, message,
                                    LENGTH_LONG).show();
                        } else {
//                            if (Consts.LOGIN_TYPE.equals("google")) {
//
//                                register();
//
//                            }
//                            else {

                            sendotp();

//                            Intent in = new Intent(mContext, OtpActivity.class);
//                            in.putExtra(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(binding.CETemailadd));
//                            startActivity(in);
//
//                            overridePendingTransition(R.anim.anim_slide_in_left,
//                                    R.anim.anim_slide_out_left);
//                            }
                        }


                    } else {
                        Toast.makeText(CheckSigninActivity.this, "Try again. Server is not responding",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(CheckSigninActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();


            }
        });
    }

    public void register() {

        progressDialog.show();
        new HttpsRequest(Consts.REGISTER_API_GOOGLE, getparm(), mContext).stringPost(TAG, Consts.USER_CONTROLLER, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  ProjectUtils.showToast(mContext, msg);
                progressDialog.dismiss();
                if (flag) {
                    try {

                        Log.e(TAG + "register123_response", response.toString());


                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);

                        Log.e("USERDTO", "" + userDTO.getUser_id());
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        String is_v = userDTO.getIs_verified();
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                        if (is_v.equalsIgnoreCase("0")) {
                            Intent in = new Intent(mContext, BusinessDocumentActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        } else {
                            if (businessdataList.size() != 0) {
                                prefrence.setBusinessData(businessdataList.get(0), Consts.BUSINESSDATA_DTO);
                            }
                            Intent in = new Intent(CheckSigninActivity.this, HomeScreenActivity.class);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);

//                    Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();

//                    overridePendingTransition(R.anim.anim_slide_in_left,
//                            R.anim.anim_slide_out_left);
                    try {
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Toast.makeText(getApplicationContext(), "he", Toast.LENGTH_SHORT).show();


                    String is_v = userDTO.getIs_verified();
                    prefrence.setBooleanValue(Consts.IS_REGISTERED, true);

                    if (is_v.equalsIgnoreCase("0")) {
                        Intent in = new Intent(mContext, BusinessDocumentActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } else {
                        if (businessdataList.size() != 0) {
                            prefrence.setBusinessData(businessdataList.get(0), Consts.BUSINESSDATA_DTO);
                        }

                        Intent in = new Intent(CheckSigninActivity.this, HomeScreenActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);

                    }

//                    check_state();


                }


            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, name);
        parms.put(Consts.EMAIL, email);
        //  parms.put(Consts.PASSWORD, pasword);
//        parms.put(Consts.REFERRAL_CODE, "");
//        parms.put(Consts.MOBILE, mobile);
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        Log.e(TAG + "register123", parms.toString());
        return parms;
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //       Glob.BUBBLE_VALUE = "0";
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void sendotp() {
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient(Consts.BASE_URL + Consts.USER_CONTROLLER);
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.sendOtp2(ProjectUtils.getEditTextValue(binding.CETemailadd));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e(TAG + " Login_response", s);

                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {

                            try {

                                String otp = object.getString("otp");

                                Intent intent = new Intent(CheckSigninActivity.this, OtpActivity.class);
                                intent.putExtra("signin_flag", true);
                                intent.putExtra(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(binding.CETemailadd));
                                intent.putExtra(Consts.OTP, otp);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(CheckSigninActivity.this, message,
                                    LENGTH_LONG).show();
                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CheckSigninActivity.this, "Try Again Later ",
                                LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CheckSigninActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();


            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.CBsignIn:
                clickForSubmit();
                break;
        }
    }
}