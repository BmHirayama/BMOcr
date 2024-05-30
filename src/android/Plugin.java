package jp.bluememe.plugin.ocr;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.bluememe.plugin.ocr.plugin.PluginManager;

public class Plugin extends CordovaPlugin {

    // MARK: - Callback
    private CallbackContext mOcrCallbackId = null;

    // MARK: - Member
    private PluginManager mPluginManager = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        Context applicationContext = cordova.getActivity().getApplicationContext();
        if (action.equals("startOCR")) {
            mOcrCallbackId = callbackContext;
            startOcr();
            return true;
        }
        return false;
    }

    // MARK: - Function
    private PluginManager getPluginManager() {
        if (mPluginManager == null) {
            mPluginManager = new PluginManager();
        }
        return mPluginManager;
    }

    private void startOcr() {
        getPluginManager().start(cordova.getActivity(), new PluginManager.OnPluginManagerListener() {
            @Override
            public void onResult(PluginManager sender, int code, PluginManager.OcrResultInfo result) {
                ocrStop();
                JSONObject resultJson = new JSONObject();
                if (code == PluginManager.CODE_SUCCESS) {
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
                } else if (code == PluginManager.CODE_AUTHROIZE) {
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
        getPluginManager().stop();
        mPluginManager = null;
    }
}
