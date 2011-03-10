package com.joshuahou.ciphertxt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CipherTxtMain extends Activity {
    private final String password = "password";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText editText = (EditText) findViewById(R.id.edittext);
        final Button encryptButton = (Button) findViewById(R.id.encryptbutton);
        encryptButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                editText.setText(encrypt(password, editText.getText().toString()));
            }
        });
    }

    private String encrypt(String password, String message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            builder.append((char)(password.charAt(i % password.length()) ^ message.charAt(i)));
        }
        return builder.toString();
    }
}