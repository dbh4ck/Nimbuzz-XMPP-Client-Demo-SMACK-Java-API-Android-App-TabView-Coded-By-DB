package com.example.db.nimbuzz_app_tabview_by_db;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;


public class ProfileFragment extends Fragment implements View.OnClickListener{
    private ChatManager chatmanager;
    private Chat newChat;
    ImageView avatar;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_profile, container, false);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button sndmsgbtn = (Button) v.findViewById(R.id.sndmsgbutton);
        sndmsgbtn.setOnClickListener(this);

        Button logoutbtn = (Button) v.findViewById(R.id.logoutbutton);
        logoutbtn.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sndmsgbutton:
                sndmsg();
                break;
            case R.id.logoutbutton:
                logout();
                break;
        }

    }

    private void logout() {
        // Toast.makeText(getContext(), "Logout" , Toast.LENGTH_SHORT).show();
        XMPPLogic.connection.disconnect();
        if (!XMPPLogic.connection.isConnected()) {
            //finish();
            //  Return Back To MainActivity
            Intent dbint2 = new Intent(getActivity(), MainActivity.class);
            dbint2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(dbint2);
        }
    }

    private void sndmsg() {
        // Toast.makeText(getContext(), "sent" , Toast.LENGTH_SHORT).show();
        if(XMPPLogic.connection.isConnected())
        {
            chatmanager = ChatManager.getInstanceFor(XMPPLogic.connection);
            newChat = chatmanager.createChat("db~@nimbuzz.com");
            try {

                EditText urmsg = (EditText) getView().findViewById(R.id.editText2);
                String msg = urmsg.getText().toString();

                newChat.sendMessage(msg);
                Toast.makeText(getActivity(), "Your Message Has Been Sent!" , Toast.LENGTH_SHORT).show();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }

    }
}
