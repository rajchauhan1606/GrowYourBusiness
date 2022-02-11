package com.brandshaastra.https;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.brandshaastra.interfaces.Consts;
import com.brandshaastra.interfaces.Helper;
import com.brandshaastra.jsonparser.JSONParser;
import com.brandshaastra.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * Created by WebKnight Infosystem on 01/01/19.
 */
public class HttpsRequest {

    private String match;
    private Map<String, String> params;
    private Map<String, File> fileparams;
    private String url;
    private Context ctx;
    private JSONObject jObject;

    public HttpsRequest(String match, Map<String, String> params, Context ctx) {
        this.match = match;
        this.params = params;
        this.ctx = ctx;
    }


    public HttpsRequest(String match, String url, Context ctx) {
        this.match = match;
        this.url = url;
        this.ctx = ctx;
    }

    public HttpsRequest(String match, Map<String, String> params, Map<String, File> fileparams, Context ctx) {
        this.match = match;
        this.params = params;
        this.fileparams = fileparams;
        this.ctx = ctx;
    }

    public HttpsRequest(String match, Context ctx) {
        this.match = match;
        this.ctx = ctx;
    }

    public HttpsRequest(String match, JSONObject jObject, Context ctx) {
        this.match = match;
        this.jObject = jObject;
        this.ctx = ctx;
    }

    public void stringPostJson(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL + match)
                .addJSONObjectBody(jObject)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " response body --->" + jObject.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringPost(final String TAG, String controller, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL + controller + match)
                .addBodyParameter(params)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringPos2(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.VIDEO_BASE_URL + match)
                .addBodyParameter(params)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringPosttest(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL_TEST + match)
                .addBodyParameter(params)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void Post(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL + match)
                .addBodyParameter(url)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + url.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringGet(final String TAG, final Helper h) {
        AndroidNetworking.get(Consts.BASE_URL + match)
                .setTag("test")

                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {

                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void imagePost(final String TAG, String controller, final Helper h) {
        AndroidNetworking.upload(Consts.BASE_URL + controller + match)
                .addMultipartFile(fileparams)
                .addMultipartParameter(params)
                .setTag("uploadTest")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);

                        if (jsonParser.RESULT) {

                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void imagePost2(final String TAG, final Helper h) {
        AndroidNetworking.upload(Consts.VIDEO_BASE_URL  + match)
                .addMultipartFile(fileparams)
                .addMultipartParameter(params)
                .setTag("uploadTest")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);

                        if (jsonParser.RESULT) {

                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                        } else {
                            h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

}