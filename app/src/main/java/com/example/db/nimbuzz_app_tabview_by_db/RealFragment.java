package com.example.db.nimbuzz_app_tabview_by_db;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

public class RealFragment extends ListFragment {
    private static final String TAG = "RealFragment";
    private List<ChatItem> mChatList = new ArrayList<>();
    private ChatAdapter mChatAdaper;
    private boolean messageSent = false;

    private EditText mMessageInput, mToUsername;
    private Button mSendButton;

    public RealFragment() {
    }


    // Instance is used here to allow adapter updates when an incoming message is received.
    private static RealFragment inst;
    public static RealFragment instance() {
        return inst;
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
        mChatAdaper = new ChatAdapter(getContext(), mChatList);
        getListView().setAdapter(mChatAdaper);

    }

    public void updateChatList(String sender, String message) {
        mChatList.add(new ChatItem(sender, message));
        mChatAdaper.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // View view = inflater.inflate(R.layout.fragment_real_list, container, false);
        View view = inflater.inflate(R.layout.chat_screen_layout, container, false);
        // Set the adapter
        //return view;
       // ArrayAdapter<ChatItem> adapter = new ArrayAdapter<ChatItem>(getActivity(), android.R.layout.simple_list_item_1, mChatList);
       // setListAdapter(adapter);

       // return super.onCreateView(inflater, container, savedInstanceState);

        // The message you want to send
        mMessageInput = (EditText) view.findViewById(R.id.yourMessage);

        // The address of the person you want to send the message to.
        // DISCLAIMER: The address should include the service name as well (i.e. username@server.example.com/Smack)
        mToUsername = (EditText) view.findViewById(R.id.toUsername);

        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // You should be checking for length and valid inputs here
                // In this basic example, we are just sending a message to the receipient.
                sendMessage(mToUsername.getText().toString() + "@nimbuzz.com", mMessageInput.getText().toString());
            }
        });

        createChatListener();
        //new MyChatMessageListener();
        return view;


    }


    private void sendMessage(String address, String chat_message) {

        updateChatList(XMPPLogic.connection.getUser() + ": ", chat_message);
        ChatManager chatManager = ChatManager.getInstanceFor(XMPPLogic.connection);
        final Chat newChat = chatManager.createChat(address);

        final Message message = new Message();
        message.setBody(chat_message);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // We send the message here.
                // You should also check if the username is valid here.
                try {
                    newChat.sendMessage(message);
                    messageSent = true;
                } catch (SmackException.NotConnectedException e) {
                    Log.e(TAG, "Unable to send chat: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(messageSent) {
                    Toast.makeText(getActivity(), "Sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Unable to send", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();


    }


    private MyChatMessageListener mChatMessageListener;
    private void createChatListener() {
        if(XMPPLogic.connection != null) {
            ChatManager chatManager = ChatManager.getInstanceFor(XMPPLogic.connection);
            chatManager.setNormalIncluded(false); // Eliminates a few debug messages
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    if (!createdLocally) {
                        mChatMessageListener = new MyChatMessageListener();
                        chat.addMessageListener(mChatMessageListener);
                        Log.e(TAG, "ChatListener created");
                    }
                }
            });
        }
    }


    public class MyChatMessageListener implements ChatMessageListener {
        protected static final String TAG = "MyChatMessageListener";

        @Override
        public void processMessage(Chat chat, Message message) {
            if (message.getType() == Message.Type.chat && message.getBody() != null) {

                    final String mChatSender = message.getFrom();
                    final String mChatMessage = message.getBody();

                    Log.e(TAG, mChatSender + ": " + mChatMessage);

                    DbMainTabActivity.instance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RealFragment.instance().updateChatList(mChatSender +": ", mChatMessage);
                        }
                    });

            }
        }

    }




    public class ChatAdapter extends ArrayAdapter<ChatItem> {

        /*
        * Viewholder is used for performance improvement. The ListView is populated
        * sped up by caching previous views.
        * */
        private class ViewHolder {
            TextView mChatSender;
            TextView mChatMessage;
        }

        /*
        * We pass a basic List container for the listview adapter. This can be adapted
        * to be a few different types of collections.
        * */
        public ChatAdapter(Context context, List<ChatItem> chatList) {
            super(context, 0, chatList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Retrieve data for this item position
            ChatItem chat = getItem(position);

            ViewHolder viewHolder;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.chat_item_layout, parent, false);
                viewHolder.mChatSender = (TextView) convertView.findViewById(R.id.list_sender);
                viewHolder.mChatMessage = (TextView) convertView.findViewById(R.id.list_message);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mChatSender.setText(chat.getSender());
            viewHolder.mChatMessage.setText(chat.getMessage());

            return convertView;
        }
    }


    public class ChatItem {

        private String sender;
        private String message;

        public ChatItem(String sender, String message) {
            this.sender = sender;
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }

    }


}
