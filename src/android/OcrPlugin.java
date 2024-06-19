package jp.bluememe.plugin.ocr;

import android.content.Context;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import jp.bluememe.plugin.ocr.plugin.OcrPluginManager;

public class OcrPlugin extends CordovaPlugin {

    // MARK: - Define
    private static final String[] dPermission = { Manifest.permission.CAMERA };

    // MARK: - Callback
    private CallbackContext mOcrCallbackId = null;

    // MARK: - Member
    private OcrPluginManager mOcrPluginManager = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        Context applicationContext = cordova.getActivity().getApplicationContext();
        if (action.equals("startOCR")) {
            Log.i("DEBUG_LOG", "A00");
            mOcrCallbackId = callbackContext;
            start();
            return true;
        }
        return false;
    }

    // MARK: - Function
    private OcrPluginManager getOcrPluginManager() {
        if (mOcrPluginManager == null) {
            mOcrPluginManager = new OcrPluginManager();
        }
        return mOcrPluginManager;
    }

    private void start() {
        AppCompatActivity activity = (AppCompatActivity)cordova.getActivity();
        if (isPermissionGranted(activity)) {
            Log.i("DEBUG_LOG", "C00");
            startOcr();
            return;
        }
        Log.i("DEBUG_LOG", "C01");
        ActivityResultLauncher<String[]> requestPermission = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Log.i("DEBUG_LOG", "C02");
            if (!result.values().contains(false)) {
                startOcr();
                return;
            }
            Log.i("DEBUG_LOG", "C03");
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("警告");
            builder.setMessage("カメラアクセスの許可が必要です");
            builder.setCancelable(false);
            builder.setNegativeButton("設定へ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null
                    ));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });
            builder.create().show();

            JSONObject resultJson = new JSONObject();
            try {
                resultJson.put("errorCode", -1);
            } catch (JSONException e) {
            }
            mOcrCallbackId.error(resultJson);
        });
        requestPermission.launch(dPermission);
    }

    private void startOcr() {
        Log.i("DEBUG_LOG", "A01");
        getOcrPluginManager().start(cordova.getActivity(), new OcrPluginManager.OnOcrPluginManagerListener() {
            @Override
            public void onResult(OcrPluginManager sender, int code, OcrPluginManager.OcrResultInfo result) {
                Log.i("DEBUG_LOG", "A02");
                ocrStop();
                JSONObject resultJson = new JSONObject();
                if (code == OcrPluginManager.CODE_SUCCESS) {
                    if (result == null) {
                        try {
                            resultJson.put("errorCode", -1);
                        } catch (JSONException e) {
                        }
                        mOcrCallbackId.error(resultJson);
                    } else {
                        try {
                            resultJson.put("errorCode", 0);
                            resultJson.put("type", result.mCardType);
                            resultJson.put("name", result.mName);
                            resultJson.put("address", result.mAddress);
                            resultJson.put("birthdate", result.mBirthdate);
                            resultJson.put("gender", result.mGender);
                        } catch (JSONException e) {
                        }
                        mOcrCallbackId.error(resultJson);
                    }
                } else if (code == OcrPluginManager.CODE_AUTHROIZE) {
                    try {
                        resultJson.put("errorCode", -2);
                    } catch (JSONException e) {
                    }
                    mOcrCallbackId.error(resultJson);
                } else {
                    try {
                        resultJson.put("errorCode", -1);
                    } catch (JSONException e) {
                    }
                    mOcrCallbackId.error(resultJson);
                }
            }
        });
    }

    private void ocrStop() {
        getOcrPluginManager().stop();
        mOcrPluginManager = null;
    }

    private static boolean isPermissionGranted(AppCompatActivity activity) {
        for (String permission: dPermission) {
            int checkPermission = ContextCompat.checkSelfPermission(activity, permission);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
