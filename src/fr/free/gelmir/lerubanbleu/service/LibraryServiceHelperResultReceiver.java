package fr.free.gelmir.lerubanbleu.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 24/03/13
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
public LibraryServiceHelperResultReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public LibraryServiceHelperResultReceiver(Handler handler) {
            super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver
    {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
