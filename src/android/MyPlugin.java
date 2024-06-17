package com.example.myplugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import jp.co.ip_consulting.drivercardocrlibrary.DriverCardOCR;
import androidx.appcompat.app.AppCompatActivity;
import jp.co.ip_consulting.drivercardocrlibrary.DriverCardOCR.RESULT;
import jp.co.ip_consulting.drivercardocrlibrary.DriverCardOCR.SCAN_TYPE;
import android.os.Bundle;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;

public class MyPlugin extends CordovaPlugin {

    public static final int CARDTYPE_MENKYOSYO = 1;
    public static final int CARDTYPE_MYNUMBER = 2;
    public static final int CARDTYPE_ZAIRYU = 3;

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        cordova.setActivityResultCallback(this);
        AppCompatActivity activity = (AppCompatActivity) cordova.getActivity();
        DriverCardOCR.Companion.getShared().doScanCard(activity, new Function3<RESULT, Bundle, SCAN_TYPE, Unit>() {
            @Override
            public Unit invoke(RESULT result, Bundle bundle, SCAN_TYPE scanType) {
		OcrResultInfo info = new OcrResultInfo();
		JSONObject resultJson = new JSONObject();
                if (scanType == SCAN_TYPE.DriverCard) {
                    info.mCardType = CARDTYPE_MENKYOSYO;
                    if (bundle.containsKey("姓名")) {
                        info.mName = bundle.getString("姓名", "");
                    }
                    if (bundle.containsKey("住所")) {
                        info.mAddress = bundle.getString("住所", "");
                    }
                    if (bundle.containsKey("生年月日")) {
                        info.mBirthdate = bundle.getString("生年月日", "").replace("生", "");
                    }
                } else if (scanType == SCAN_TYPE.MyNumberCard) {
                    info.mCardType = CARDTYPE_MYNUMBER;
                    if (bundle.containsKey("氏名")) {
                        info.mName = bundle.getString("氏名", "");
                    }
                    String address = "";
                    if (bundle.containsKey("住所①")) {
                        address += bundle.getString("住所①", "");
                    }
                    if (bundle.containsKey("住所②")) {
                        address += bundle.getString("住所②", "");
                    }
                    info.mAddress = address;
                    if (bundle.containsKey("生年月日")) {
                        info.mBirthdate = bundle.getString("生年月日", "").replace("生", "");
                    }
                    if (bundle.containsKey("性別")) {
                        info.mGender = bundle.getString("性別", "");
                    }
                } else if (scanType == SCAN_TYPE.ZairyuCard) {
                    info.mCardType = CARDTYPE_ZAIRYU;
                    if (bundle.containsKey("氏名")) {
                        info.mName = bundle.getString("氏名", "");
                    }
                    if (bundle.containsKey("住所")) {
                        info.mAddress = bundle.getString("住所", "");
                    }
                    if (bundle.containsKey("生年月日")) {
                        info.mBirthdate = bundle.getString("生年月日", "").replace("生", "");
                    }
                    if (bundle.containsKey("性別")) {
                        info.mGender = bundle.getString("性別", "");
                    }
                } else {
		    try {
                        resultJson.put("errorCode", -1);
                    } catch (JSONException e) {
                    }
                    callbackContext.success(resultJson);
                    return null;
                }
                try {
                    resultJson.put("errorCode", 0);
                    resultJson.put("type", result.mCardType);
                    resultJson.put("name", result.mName);
                    resultJson.put("address", result.mAddress);
                    resultJson.put("birthdate", result.mBirthdate);
                    resultJson.put("gender", result.mGender);
                } catch (JSONException e) {
                }
                callbackContext.success(resultJson);
                return null;
            }
        });
    }

    // MARK: - InnerClass
    public class OcrResultInfo implements Serializable {
        // MARK: - Member
        public int mCardType;
        public String mName;
        public String mAddress;
        public String mBirthdate;
        public String mGender;
    }
}

