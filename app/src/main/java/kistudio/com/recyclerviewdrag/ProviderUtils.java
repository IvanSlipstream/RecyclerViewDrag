package kistudio.com.recyclerviewdrag;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;

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
                new String[] { contactIdKey, contactNameKey}, null, null, null
        );
        if (c == null){
            return;
        }
        if (c.getCount()!=0){
            while (c.moveToNext()){
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
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? AND "+
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
}
