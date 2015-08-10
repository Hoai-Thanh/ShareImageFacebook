package com.app.testapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.testapp.R;
import com.app.testapp.encode.Contents;
import com.app.testapp.encode.QRCodeEncoder;
import com.app.testapp.listener.FragmentChangeListener;
import com.app.testapp.utils.DialogUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.Arrays;

/**
 * Created by Thanh Nguyen on 8/10/2015.
 */
public class ShareFragment extends BaseFragment{

    private Activity activity;
    private View view;

    private TextView txtWelcome;
    private EditText edtQRCode;
    private Button btnShareImg;

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private FragmentChangeListener fragmentChangeListener;

    private String userName;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginManager.logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
            shareImage();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        fragmentChangeListener = (FragmentChangeListener) this.activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        userName = bundle.getString("user_name");
        return inflater.inflate(R.layout.test_share_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentChangeListener.setFrom("share");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        initView();
        initEvent();
        fillData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initView(){
        view = getView();
        txtWelcome = (TextView) view.findViewById(R.id.test_shareFragment_txtWelcome);
        edtQRCode = (EditText) view.findViewById(R.id.test_shareFragment_edtMessage);
        btnShareImg = (Button) view.findViewById(R.id.test_shareFragment_btnShareImage);
    }

    private void initEvent() {
        btnShareImg.setOnClickListener(onClickListener);
    }

    private void fillData() {
        txtWelcome.setText(String.format(getString(R.string.test_complete_welcome), userName));
    }

    private void shareImage() {
        String qrText = edtQRCode.getText().toString();
        if (!TextUtils.isEmpty(qrText)) {

            //Encode with a QR Code image
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrText,
                    null,
                    Contents.Type.TEXT,
                    BarcodeFormat.QR_CODE.toString(),
                    setScreenSize());
            try {
                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                getBitmap(bitmap);
                DialogUtils.toastMessage(getActivity(), getString(R.string.test_send_image_success));
            } catch (WriterException e) {

            }
        } else {
            DialogUtils.toastMessage(getActivity(), getString(R.string.test_text_exception));
        }
    }

    private void getBitmap(Bitmap bitmap){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, null);
    }

    private int setScreenSize() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        return smallerDimension;
    }
}

