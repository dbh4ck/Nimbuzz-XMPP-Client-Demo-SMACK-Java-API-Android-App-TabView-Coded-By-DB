package com.example.db.nimbuzz_app_tabview_by_db;

import android.content.Context;
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
import android.widget.ListAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import java.util.ArrayList;
import java.util.List;

public class conferenceFragment extends ListFragment implements View.OnClickListener{
    private static final String TAG = "conferenceFragment";

    private EditText RoomName, RoomMsg;
    private Button EnterRoomBtn, SendMsgRoomBtn;

    MultiUserChat muc2;
    MultiUserChatManager mucchatmanager;

    private List<ChatRoomItem> mChatRoomList = new ArrayList<>();
    private ChatRoomAdapter mChatRoomAdaper;

    public conferenceFragment() {
    }

    // Instance is used here to allow adapter updates when an incoming message is received.
    private static conferenceFragment inst;
    public static conferenceFragment instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
        mChatRoomAdaper = new ChatRoomAdapter(getContext(), mChatRoomList);
        getListView().setAdapter(mChatRoomAdaper);

    }

    public void updateChatList(String room_sender, String room_message) {
        mChatRoomList.add(new ChatRoomItem(room_sender, room_message));
        mChatRoomAdaper.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.conference_screen_layout, container, false);

        // The message you want to send

        RoomName = (EditText) view.findViewById(R.id.roomname);

        RoomMsg = (EditText) view.findViewById(R.id.yourRoomMessage);

        EnterRoomBtn = (Button) view.findViewById(R.id.enterconfroombtn);
        EnterRoomBtn.setOnClickListener(this);

        SendMsgRoomBtn = (Button) view.findViewById(R.id.sendRoomMsgButton);
        SendMsgRoomBtn.setOnClickListener(this);
        //createChatRoomListener();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enterconfroombtn:
                roomenter();
                break;
            case R.id.sendRoomMsgButton:
                try {
                    sendMsgRoom();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void sendMsgRoom() throws SmackException.NotConnectedException {
        if(XMPPLogic.connection != null && muc2.isJoined()) {

            Message message = new Message();
            message.setType(Message.Type.groupchat);
            message.setBody(RoomMsg.getText().toString());
            message.setTo(RoomName.getText().toString() + "@conference.nimbuzz.com");
            muc2.sendMessage(message);
            updateChatList(XMPPLogic.connection.getUser() + ": ", RoomMsg.getText().toString());
        }


    }


    private void roomenter() {

        mucchatmanager = MultiUserChatManager.getInstanceFor(XMPPLogic.connection);
        muc2 = mucchatmanager.getMultiUserChat(RoomName.getText().toString() + "@conference.nimbuzz.com");
        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(5);

        try {

            muc2.join("googlefiber", "", history, XMPPLogic.connection.getPacketReplyTimeout());

            if(XMPPLogic.connection != null && muc2.isJoined()){

                MessageTypeFilter filter = (MessageTypeFilter) MessageTypeFilter.GROUPCHAT;
                XMPPLogic.connection.addPacketListener(new PacketListener() {
                    @Override
                    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                        Message message = (Message) packet;
                        if(message.getBody() != null)
                        {
                            final String mChatRoomSender = message.getFrom();
                            final String mChatRoomMessage = message.getBody();

                            Log.e(TAG, mChatRoomSender + ": " + mChatRoomMessage);

                            DbMainTabActivity.instance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    conferenceFragment.instance().updateChatList(mChatRoomSender + ": ", mChatRoomMessage);
                                }
                            });

                        }
                    }
                },filter);
            }


        }

        catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }





    private class ChatRoomAdapter extends ArrayAdapter<ChatRoomItem>  implements ListAdapter {

        public ChatRoomAdapter(Context context, List<ChatRoomItem> mChatRoomList) {
            super(context, 0, mChatRoomList);
        }

        private class ViewHolder {
            TextView mChatRoomSender;
            TextView mChatRoomMessage;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Retrieve data for this item position
            ChatRoomItem chat = getItem(position);


            ViewHolder viewHolder;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.conference_item_layout, parent, false);
                viewHolder.mChatRoomSender = (TextView) convertView.findViewById(R.id.room_sender);
                viewHolder.mChatRoomMessage = (TextView) convertView.findViewById(R.id.room_message);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mChatRoomSender.setText(chat.getSender());
            viewHolder.mChatRoomMessage.setText(chat.getMessage());

            return convertView;
        }

    }

    private class ChatRoomItem {
        private String room_sender;
        private String room_message;

        public ChatRoomItem(String room_sender, String room_message) {
            this.room_sender = room_sender;
            this.room_message = room_message;
        }

        public String getSender() {
            return room_sender;
        }

        public String getMessage() {
            return room_message;
        }
    }

}