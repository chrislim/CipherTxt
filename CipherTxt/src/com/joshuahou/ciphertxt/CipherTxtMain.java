package com.joshuahou.ciphertxt;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CipherTxtMain extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void decryptButtonClick(View button) {
        EditText editText = (EditText) findViewById(R.id.message);
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        editText.setText(decrypt(password, editText.getText().toString()));
    }

    public void encryptButtonClick(View button) {
        EditText editText = (EditText) findViewById(R.id.message);
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        editText.setText(encrypt(password, editText.getText().toString()));
    }

    public void copyButtonClick(View button) {
        EditText editText = (EditText) findViewById(R.id.message);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setText(editText.getText());
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
        String encryptedMessage = Base64.encodeToString(xor(message.getBytes(), password.getBytes()), Base64.NO_WRAP);
        return String.format("%s", encryptedMessage);
    }

    private String decrypt(String password, String message) {
        if (!validate(password, message)) {
            return message;
        }
        String encrypted = message.trim();

        try {
            byte[] decoded = Base64.decode(encrypted, Base64.NO_WRAP);
            return new String(xor(decoded, password.getBytes()));
        } catch (IllegalArgumentException e) {
            Toast.makeText(CipherTxtMain.this, "Improperly formatted message block.", Toast.LENGTH_SHORT).show();
            return message;
        }
    }

    private byte[] xor(byte[] message, byte[] password) {
        byte[] out = new byte[message.length];

        for (int i = 0; i < message.length; i++) {
            out[i] = (byte) (message[i] ^ password[i % password.length]);
        }
        return out;
    }
}