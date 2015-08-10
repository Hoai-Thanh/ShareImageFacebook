package com.app.testapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.testapp.R;
import com.app.testapp.listener.FragmentChangeListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONObject;
import java.util.Arrays;

/**
 * Created by Thanh Nguyen on 8/10/2015.
 */
public class LoginFragment extends BaseFragment {

    private Activity activity;
    private View view;

    private Button btnLogin;

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private FragmentChangeListener fragmentChangeListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initFacebook();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        fragmentChangeListener = (FragmentChangeListener) this.activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_login_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentChangeListener.setFrom("login");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        view = getView();
        btnLogin = (Button) view.findViewById(R.id.test_loginFragment_btnLoginFacebook);
    }

    private void initEvent() {
        btnLogin.setOnClickListener(onClickListener);
    }

    private void initFacebook(){
        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        //login with permission for getting user profile.
        loginManager.logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String userName = object.optString("name");
                                    fragmentChangeListener.changeFragment(userName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

                /* make the API call */
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/friends",
                        null, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }
}
