package com.example.db.nimbuzz_app_tabview_by_db;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;


public class ChatroomFragment extends Fragment implements View.OnClickListener{

    MultiUserChatManager mucchatmanager;

    public ChatroomFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatroom, container, false);

        Button roombtn = (Button) v.findViewById(R.id.enterroombtn);
        roombtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enterroombtn:
                roomenter();
                break;
        }
    }

    private void roomenter() {
        EditText roomname = (EditText) getView().findViewById(R.id.editText3);
        String room = roomname.getText().toString();
        mucchatmanager = MultiUserChatManager.getInstanceFor(XMPPLogic.connection);
        MultiUserChat muc2 = mucchatmanager.getMultiUserChat(room + "@" + "conference.nimbuzz.com");

        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(5);
        try {

            muc2.join("googlefiber", "", history, XMPPLogic.connection.getPacketReplyTimeout());
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
