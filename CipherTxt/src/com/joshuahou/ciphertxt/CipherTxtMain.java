package com.joshuahou.ciphertxt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CipherTxtMain extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText editText = (EditText) findViewById(R.id.message);
        final Button encryptButton = (Button) findViewById(R.id.encryptbutton);
        encryptButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                editText.setText(encrypt(password, editText.getText().toString()));
            }
        });
    }

    private static String encrypt(String password, String message) {
        byte[] messageBytes = message.getBytes();
        byte[] passwordBytes = password.getBytes();
        byte[] encryptedBytes = new byte[messageBytes.length];

        for (int i = 0; i < messageBytes.length; i++) {
            encryptedBytes[i] = (byte) (messageBytes[i] ^ passwordBytes[i % passwordBytes.length]);
        }
        return new String(encryptedBytes);
    }
}