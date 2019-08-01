package com.thaliees.broadcastreceive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Objects;

public class SmsReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        SmsMessage[] msg;
        if(bundle != null) {
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msg = new SmsMessage[Objects.requireNonNull(pdus).length];

                for (int i = 0; i < msg.length; i++) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String textBody = msg[i].getMessageBody();
                    String textFrom = msg[i].getOriginatingAddress();
                    SendCode(textFrom, textBody, context);
                }
            }
            catch (Exception x){
            }
        }
    }

    public void SendCode(String from, String text, Context context) {
        Intent send = new Intent("SMS_RECEIVED");
        Bundle result = getResultExtras(true);
        result.putString("From", from);
        result.putString("Message", text);
        setResultCode(Activity.RESULT_OK);
        setResultExtras(result);
        context.sendOrderedBroadcast(send, null, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, null, Activity.RESULT_OK, null, result);
    }
}
