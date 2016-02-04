package com.example.db.nimbuzz_app_tabview_by_db;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.ArrayList;
import java.util.Collection;


public class ContactsFragment extends ListFragment{

    Presence presence;
    public ContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    String[] FriendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(XMPPLogic.connection.isConnected() && XMPPLogic.connection.isAuthenticated()){

            notifyRosterChanged();
            Roster roster = Roster.getInstanceFor(XMPPLogic.connection);
            final ArrayList<String> userlist = new ArrayList<String>();
            for (RosterEntry entry : roster.getEntries() ) {
                userlist.add(entry.getUser());
            }
            String[] mylist = new String[userlist.size()];
            for (int i = 0; i < userlist.size(); i++) {
                mylist[i] = userlist.get(i);
            }
            FriendList = mylist;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, FriendList);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private void notifyRosterChanged() {
        Roster roster = Roster.getInstanceFor(XMPPLogic.connection);
        roster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> addresses) {

            }

            @Override
            public void entriesUpdated(Collection<String> addresses) {

            }

            @Override
            public void entriesDeleted(Collection<String> addresses) {

            }

            @Override
            public void presenceChanged(Presence presence) {
                //  DB~@NC WAS ONLINE
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        //  Toast.makeText(getActivity(), "Item:" + position, Toast.LENGTH_SHORT).show();
        //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.fragment_container, new ChatsFragment());
        //transaction.addToBackStack(null);
        //transaction.commit();
    }
}
