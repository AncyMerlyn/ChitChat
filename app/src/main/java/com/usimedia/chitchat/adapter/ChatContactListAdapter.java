package com.usimedia.chitchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usimedia.chitchat.R;
import com.usimedia.chitchat.model.ChatContacts;

import java.text.SimpleDateFormat;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContactListAdapter extends ArrayAdapter<ChatContacts> {

    private static final int CHAT_CONTACT_LAYOUT_ID = R.layout.chat_contact_list_item;
    private ChatContacts[] contacts;
    private Activity context;
    private static final SimpleDateFormat UI_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    public ChatContactListAdapter(Activity activity,ChatContacts[] objects) {
        super(activity, CHAT_CONTACT_LAYOUT_ID, objects);
        contacts=objects;
        context=activity;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //initializing view
        View chatcontactView;
        if (convertView ==null) {
            chatcontactView = context.getLayoutInflater().inflate(CHAT_CONTACT_LAYOUT_ID, parent, false);
        }
        else
        {
            chatcontactView=convertView;
        }
        ChatContacts currentContact = contacts[position];
        TextView nameTextView = (TextView) chatcontactView.findViewById(R.id.chat_contact_list_name);
        nameTextView.setText(currentContact.getName());
        TextView statusTextView = (TextView) chatcontactView.findViewById(R.id.chat_contact_list_status);
        statusTextView.setText(currentContact.getStatus());
        TextView lastSeenTextView = (TextView) chatcontactView.findViewById(R.id.chat_contact_list_lastSeen);
        lastSeenTextView.setText(UI_DATE_FORMAT.format(currentContact.getLastSeen()));
        return chatcontactView;
    }
}
