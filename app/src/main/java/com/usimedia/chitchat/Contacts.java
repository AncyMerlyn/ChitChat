package com.usimedia.chitchat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.usimedia.chitchat.adapter.ChatContactListAdapter;
import com.usimedia.chitchat.model.ChatContacts;
import com.usimedia.chitchat.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contacts extends AppCompatActivity {
    private static  final String CONTACTS_SERVICE_URL="http://192.168.2.175:8000/contact";
    private  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    int numberOfContact;
    Set<String> contactNumbers;

    private List<ChatContacts> getContacts(List<String> phoneNumbers) throws JSONException, IOException
     {
        JSONArray jsonNumbers = new JSONArray(phoneNumbers);
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("numbers", jsonNumbers);

        String rawResult = post(CONTACTS_SERVICE_URL, jsonRequest.toString());
        Log.d("Debug", rawResult);
        JSONObject jsonResult = new JSONObject(rawResult);

        JSONArray jsonContacts = jsonResult.getJSONArray("contacts");

        final List<ChatContacts> contactList = new ArrayList<>();
        ChatContacts currentContact= new ChatContacts();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != jsonContacts){
            for (int i=0; i<jsonContacts.length();i++){
                currentContact.setName(jsonContacts.getJSONObject(i).getString("name"));
                currentContact.setStatus(jsonContacts.getJSONObject(i).getString("status"));
                try {Log.d("date",jsonContacts.getJSONObject(i).getString("lastseen"));
                    currentContact.setLastSeen(dateformat.parse(jsonContacts.getJSONObject(i).getString("lastseen")));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                contactList.add(currentContact);
            }
        }

        return  contactList;
    }

    private String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();

        return response.body().string();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

      //  ListView contactsListView = (ListView) findViewById(R.id.contact_activity_contacts);
        ContentResolver cr = getContentResolver();

        final Uri provider_uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] elementsRequired = new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = cr.query(provider_uri, elementsRequired, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" DESC ");

        numberOfContact = cursor.getCount();
       // List<String> contactNames = new ArrayList<>();
        contactNumbers = new HashSet<>();


        String contactName;
        String number;
        while (cursor.moveToNext()) {
            //contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          //  contactNumbers.add(contactName);
            if(number != null){
                number =number.replaceAll("//s+","");
            }
            contactNumbers.add(number);


        }

        final List<String> distinctNumbers = new ArrayList<>(contactNumbers);

        BrowseContacts browse = new BrowseContacts();

        String[] numberArray = new String[distinctNumbers.size()];
        browse.execute(distinctNumbers.toArray(numberArray));

   /*   final  ArrayAdapter<String> contactListAdapter = new ArrayAdapter<>(
                Contacts.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
               distinctNumbers
        );

        contactsListView.setAdapter(contactListAdapter); */

    }

    private class BrowseContacts extends AsyncTask<String,Void,List<ChatContacts>>  {
        protected List<ChatContacts> doInBackground(String...distinctNumbers) {
           List<String> result = new ArrayList<>();
            try {
                List<String> phoneNumbers = Arrays.asList(distinctNumbers);
               return(getContacts(phoneNumbers));

            } catch (JSONException e) {
                Log.d("Contacts", "JSON parser exception");
                e.printStackTrace();
                return Collections.emptyList();
            } catch (IOException e) {
                Log.d("Contacts", "NETWORK exception");
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        protected void onPostExecute(List<ChatContacts> result) {
            super.onPostExecute(result);
            setListView(result);
        }
    }

    public void setListView(final List<ChatContacts> result){
        ChatContacts[] chatArray = new ChatContacts[result.size()];

        final ArrayAdapter<ChatContacts> contactListAdapter = new ChatContactListAdapter(
                Contacts.this,
                result.toArray(chatArray)
        );
     /*   final  ArrayAdapter<String> contactListAdapter = new ArrayAdapter<String>(
                Contacts.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                result
        );*/



        ListView contactsListView = (ListView) findViewById(R.id.contact_activity_contacts);
        contactsListView.setAdapter(contactListAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toContactProfileActivity = new Intent(Contacts.this,ChatProfile.class);
                toContactProfileActivity.putExtra("chat_contact",result.get(position) );
                startActivity(toContactProfileActivity);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Contact", "Number of contacts = "+numberOfContact);
        Toast.makeText(Contacts.this, "Number of contacts = " + numberOfContact, Toast.LENGTH_LONG).show();
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
