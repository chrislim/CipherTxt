package com.joshuahou.ciphertxt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CipherTxtMain extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText editText = (EditText) findViewById(R.id.message);
        final Button encryptButton = (Button) findViewById(R.id.encryptbutton);
        encryptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                editText.setText(encrypt(password, editText.getText().toString()));
            }
        });

        final Button decryptButton = (Button) findViewById(R.id.decryptbutton);
        decryptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                editText.setText(decrypt(password, editText.getText().toString()));
            }
        });
    }

    private boolean validate(String password, String message) {
        if (password.length() < 1) {
            Toast.makeText(CipherTxtMain.this, "Password field must not be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (message.length() < 1) {
            Toast.makeText(CipherTxtMain.this, "Message field must not be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String encrypt(String password, String message) {
        if (!validate(password, message)) {
            return message;
        }
        return String.format("(%s)", xor(password, message));
    }

    private String decrypt(String password, String message) {
        if (!validate(password, message)) {
            return message;
        }
        String encrypted = message.trim();
        if (encrypted.charAt(0) != '(' || encrypted.charAt(encrypted.length() - 1) != ')') {
            Toast.makeText(CipherTxtMain.this, "Improperly formatted message block.", Toast.LENGTH_SHORT).show();
            return message;
        }

        encrypted = encrypted.substring(1, encrypted.length() - 1);
        return xor(password, encrypted);
    }

    private String xor(String password, String message) {
        byte[] messageBytes = message.getBytes();
        byte[] passwordBytes = password.getBytes();
        byte[] encryptedBytes = new byte[messageBytes.length];

        for (int i = 0; i < messageBytes.length; i++) {
            encryptedBytes[i] = (byte) (messageBytes[i] ^ passwordBytes[i % passwordBytes.length]);
        }
        return new String(encryptedBytes);
    }
}