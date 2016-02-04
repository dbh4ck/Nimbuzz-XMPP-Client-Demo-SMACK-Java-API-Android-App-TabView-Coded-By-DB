package com.example.db.nimbuzz_app_tabview_by_db;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Created by DB on 02-02-2016.
 */
public class dbDialog extends Dialog implements View.OnClickListener {
    public dbDialog(Context context) {
        super(context);
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.dbinfo_layout);
        getWindow().setFlags(4, 4);
        setTitle("About DB~@NC");

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
