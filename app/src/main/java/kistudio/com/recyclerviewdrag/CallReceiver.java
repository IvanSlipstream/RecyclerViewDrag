package kistudio.com.recyclerviewdrag;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telecom.Call;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    public CallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String stateType = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d("test", stateType);
        if (stateType.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    new String[]{CallLog.Calls._ID}, null, null, CallLog.Calls.DATE + " asc");
            if (c != null) {
                Log.d("test", String.valueOf(c.getCount()));
                c.moveToFirst();
                Log.d("test", c.getString(c.getColumnIndex(CallLog.Calls._ID)));
                context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                        CallLog.Calls._ID+"=?", new String[]{c.getString(c.getColumnIndex(CallLog.Calls._ID))});
                c.close();
            }
        }
    }
}
