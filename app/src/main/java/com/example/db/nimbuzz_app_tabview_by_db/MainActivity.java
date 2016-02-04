package com.example.db.nimbuzz_app_tabview_by_db;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private Boolean exit = false;
    private static final String TAG = "MainActivity";
    public ConnectXmpp mService;
    private View view;
    private boolean mBounded;
    private final IBinder mBinder = new ServiceBinder(this.mService);



    public class ServiceBinder<T> extends Binder {

        private final WeakReference<T> mService;

        public ServiceBinder(final T service) {
            mService = new WeakReference<T>(service);
        }

        public T getService() {
            return mService.get();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Click Handler for Login Button
    public void onClickLoginBtn(View view)
    {
        try {
            EditText userId = (EditText) findViewById(R.id.txtUser);
            EditText userPwd = (EditText) findViewById(R.id.txtPwd);
            String userName = userId.getText().toString();
            String passWord = userPwd.getText().toString();
            Intent intent = new Intent(getBaseContext(),ConnectXmpp.class);
            intent.putExtra("user",userName);
            intent.putExtra("pwd",passWord);
            startService(intent);

            Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
            //mService.connectConnection(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed()
    {
        //   super.onBackPressed();
        if(exit){
            finish();
        }
        else{
            Toast.makeText(this, "Press Back Again To Exit" , Toast.LENGTH_SHORT).show();
            exit = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent dbdito = new Intent(Intent.ACTION_MAIN);
                    dbdito.addCategory(Intent.CATEGORY_HOME);
                    dbdito.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dbdito);
                }
            }, 1000);
        }
    }



}
