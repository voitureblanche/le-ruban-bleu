package fr.free.gelmir.lerubanbleu.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: DE MENDITTE
 * Date: 25/03/13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public class DetachableResultReceiver extends ResultReceiver {
    private static final String TAG = "DetachableResultReceiver";

    private Receiver mReceiver;

    public DetachableResultReceiver(Handler handler) {
        super(handler);
    }

    public void clearReceiver() {
        mReceiver = null;
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        } else {
            Log.w(TAG, "Dropping result on floor for code " + resultCode + ": "
                    + resultData.toString());
        }
    }
}
