package cordova.plugin.adudevice;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;


///NEW
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbConstants;
import android.os.Bundle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.hardware.usb.*;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class AduDevice extends CordovaPlugin {

    // logging tag
    private final String TAG = "ADUDEVICE";//AduDevice.class.getSimpleName()
    private static final String ACTION_USB_PERMISSION = "cordova.plugin.adudevice.USB_PERMISSION";

    // Vendor and product IDs from: http://www.ontrak.net/Nodll.htm
    private int VENDOR_ID_ADU = 0x0a07;

    PendingIntent mPermissionIntent;

    private UsbManager mManager;
    private UsbDeviceConnection mDeviceConnection;
    private UsbDevice mAduDevice;
    private UsbEndpoint mEpIn;
    private UsbEndpoint mEpOut;
    private byte[] mWriteBuffer;
    private byte[] mReadBuffer;

    private Activity activity;

    // actions definitions
    private static final String ACTION_REQUEST_PERMISSION = "requestPermission";
    private static final String ACTION = "coolMethod";
	private static final String ACTION_READ = "aduRead";
	private static final String ACTION_WRITE = "aduWrite";
	//private static final String ACTION_CLOSE = "closeAduDevice";
    //private static final String ACTION_READ_CALLBACK = "registerReadCallback";
    

    private boolean sleepOnPause;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        else if (ACTION_REQUEST_PERMISSION.equals(action)) {
			//JSONObject opts = arg_object.has("opts")? arg_object.getJSONObject("opts") : new JSONObject();
			requestPermission(callbackContext);
			return true;
		}
        // write to the serial port
		else if (ACTION_WRITE.equals(action)) {
			String data = args.getString(0);
			aduWrite(data, callbackContext);
			return true;
        }
        // read on the serial port
		else if (ACTION_READ.equals(action)) {
			aduRead(callbackContext);
			return true;
		}
        return false;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("usb receiver data received");
            String action = intent.getAction();
            Log.d(TAG, action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                        && device != null) {

                    Log.d(TAG, "permission granted device " + device);
                    try {
                        //openAduDevice(device);
                    } catch(RuntimeException e) {
                        Log.d(TAG, e.getMessage());
                    }
                } else {
                    Log.d(TAG, "permission denied for device " + device);
                }
            }
        }
    };

    private void coolMethod(final String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

	/**
	 * Request permission the the user for the app to use the USB/serial port
	 * @param callbackContext the cordova {@link CallbackContext}
	 */
	private void requestPermission(final CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			public void run() {
                Log.d(TAG, "Adu Device - requestPermission ENTER");
				// get UsbManager from Android
                mManager = (UsbManager) cordova.getActivity().getSystemService(Context.USB_SERVICE);
                Log.d(TAG, "Initialized USB Manager");
                activity = cordova.getActivity();
                Log.d(TAG, "Has activity handle - requestPermission EXIT");
                callbackContext.success("requestPermission SUCCESS");
            }
        });
    }

    /**
	 * Write on the serial port
	 * @param data the {@link String} representation of the data to be written on the port
	 * @param callbackContext the cordova {@link CallbackContext}
	 */
    private void aduWrite(final String data, final CallbackContext callbackContext) {
        if (data != null && data.length() > 0) {
            callbackContext.success(data);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    
        /**
	 * Write on the serial port
	 * @param data the {@link String} representation of the data to be written on the port
	 * @param callbackContext the cordova {@link CallbackContext}
	 */
    private void aduRead(final CallbackContext callbackContext) {

            callbackContext.success("Read you are weird");

	}
}
