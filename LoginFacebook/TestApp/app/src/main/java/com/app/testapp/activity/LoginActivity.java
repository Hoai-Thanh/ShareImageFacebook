package com.app.testapp.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.app.testapp.R;
import com.app.testapp.fragment.LoginFragment;
import com.app.testapp.fragment.ShareFragment;
import com.app.testapp.listener.FragmentChangeListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends Activity implements FragmentChangeListener {

    private LoginFragment loginFragment;
    private ShareFragment shareFragment;
    private String from = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_login_activity);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if(findViewById(R.id.test_loginActivity_frmContainer) != null){

            if (savedInstanceState != null) {
                return;
            }
            loginFragment = new LoginFragment();
            FragmentTransaction transaction = getTransaction();
            transaction.add(R.id.test_loginActivity_frmContainer, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            from = "login";
        }

        // key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        }
    }

    private FragmentTransaction getTransaction(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        return transaction;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(from.equals("login"))
            loginFragment.onActivityResult(requestCode, resultCode, data);
        else if(from.equals("share"))
            shareFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void changeFragment(String args) {
        shareFragment = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_name", args);
        shareFragment.setArguments(bundle);

        FragmentTransaction transaction = getTransaction();
        transaction.replace(R.id.test_loginActivity_frmContainer, shareFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        from = "share";
    }
    @Override
    public void setFrom(String where){
        from = where;
    }
}
