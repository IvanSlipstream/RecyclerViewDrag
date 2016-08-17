package kistudio.com.recyclerviewdrag;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * Created by Slipstream on 12.08.2016.
 */
public class ProviderUtils {

    private static String contactIdKey = ContactsContract.Contacts._ID;
    private static String contactNameKey = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME;

    public static void fillContacts(Context context, ArrayList<RecyclerObject> contacts) {

        Cursor c = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{contactIdKey, contactNameKey}, null, null, null
        );
        if (c == null) {
            return;
        }
        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                RecyclerObject localRo = new RecyclerObject();
                localRo.setId(c.getInt(c.getColumnIndex(contactIdKey)));
                localRo.setName(c.getString(c.getColumnIndex(contactNameKey)));
                localRo.setPhone(getPhone(context, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, localRo.getId()));
                contacts.add(localRo);
            }
        }
        c.close();
    }

    private static String getPhone(Context context, int type, int id) {
        String number = "";
        Cursor c = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + "= ?",
                new String[]{String.valueOf(id), String.valueOf(type)},
                null
        );
        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            int index = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            number = c.getString(index);
            c.close();
        }
        return number;
    }

    public static void getCallHistory(Context context, ArrayList<RecyclerObject> historyList) {
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
        Cursor c = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null, null
        );
        if (c != null) {
            while (c.moveToNext()){
                RecyclerObject ro = new RecyclerObject();
                String name = c.getString(c.getColumnIndex(CallLog.Calls.DATE));
                int type = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                switch (type){
                    case CallLog.Calls.OUTGOING_TYPE:
                        name+=" outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        name+=" incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        name+=" missed";
                        break;
                }
                ro.setPhone(c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)));
                ro.setName(name);
                historyList.add(ro);
            }
            c.close();
        }
    }
}
