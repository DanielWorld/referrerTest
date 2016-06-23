package danielworld.referrertest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    final String ReferrerUrl = "market://details?id=com.incombine.app.bapul&referrer=";
    final String Format = "UTF-8";

    EditText editText;
    TextView referrer, url;
    Button button;
    RadioButton radioButton, radioButton2, radioButton3, radioButton4;

    boolean isChanging = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        referrer = (TextView) findViewById(R.id.textView);
        url = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);

        radioButton.setChecked(true);
        radioButton3.setChecked(true);

        url.setText(ReferrerUrl);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioButton3.isChecked()) {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ReferrerUrl + referrer.getText().toString()));
                    startActivity(marketIntent);
                }
                else {

                    Intent i = new Intent("com.android.vending.INSTALL_REFERRER");
                    i.setPackage("com.incombine.app.bapul");
                    //referrer is a composition of the parameter of the campaing
                    i.putExtra("referrer", referrer.getText().toString());
                    sendBroadcast(i);
                }

                // Daniel (2016-06-08 18:12:09): Hide keyboard!
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (editText != null)
                    inputManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isChanging = true;
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                isChanging = false;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isChanging) {
                            referrer.setText(convertURL(editable.toString()));
                        }
                    }
                }, 1000);
            }
        });

        radioButton.setOnCheckedChangeListener(this);
        radioButton2.setOnCheckedChangeListener(this);
        radioButton3.setOnCheckedChangeListener(this);
        radioButton4.setOnCheckedChangeListener(this);

    }

    private String convertURL(String referrer) {
        if (radioButton.isChecked()) {

            try {
                return URLEncoder.encode(referrer, Format);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return referrer;
    }

    private String encodeURL(String decodedURL) {
        try {
            return URLEncoder.encode(decodedURL, Format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedURL;
    }

    private String decodeURL(String encodedURL) {
        try {
            return URLDecoder.decode(encodedURL, Format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedURL;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        synchronized (this) {
            if (b) {
                switch (compoundButton.getId()) {
                    case R.id.radioButton:
                        referrer.setText(encodeURL(referrer.getText().toString()));
                        break;
                    case R.id.radioButton2:
                        referrer.setText(decodeURL(referrer.getText().toString()));
                        break;
                    case R.id.radioButton3:
                        button.setText("구글 플레이 이동");
                        break;
                    case R.id.radioButton4:
                        button.setText("Install referrer 세팅");
                        break;
                }
            }
        }
    }
}
