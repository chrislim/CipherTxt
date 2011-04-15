package com.joshuahou.ciphertxt;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CipherTxtMain extends Activity {
	
    private float mPosX;
    private float mPosY;
    
    private float mLastTouchX;
    private float mLastTouchY;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText editText = (EditText) findViewById(R.id.message);
        editText.setOnTouchListener(new View.OnTouchListener() {
        	private static final int INVALID_POINTER_ID = -1;

        	// The ‘active pointer’ is the one currently moving our object.
        	private int mActivePointerId = INVALID_POINTER_ID;

        	// Existing code ...

        	@Override
        	public boolean onTouch(View v, MotionEvent ev) {
        	    final int action = ev.getAction();
        	    switch (action & MotionEvent.ACTION_MASK) {
        	    case MotionEvent.ACTION_DOWN: {
        	        final float x = ev.getX();
        	        final float y = ev.getY();
        	        
        	        mLastTouchX = x;
        	        mLastTouchY = y;

        	        // Save the ID of this pointer
        	        mActivePointerId = ev.getPointerId(0);
        	        break;
        	    }
        	        
        	    case MotionEvent.ACTION_MOVE: {
        	        // Find the index of the active pointer and fetch its position
        	        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
        	        final float x = ev.getX(pointerIndex);
        	        final float y = ev.getY(pointerIndex);
        	        
        	        final float dx = x - mLastTouchX;
        	        final float dy = y - mLastTouchY;
        	        
        	        mPosX += dx;
        	        mPosY += dy;
        	        
        	        if(dx > 0){
        	        	String password = ((EditText) findViewById(R.id.password)).getText().toString();
                        editText.setText(encrypt(password, editText.getText().toString()));
        	        } else if(dx < 0){
        	        	String password = ((EditText) findViewById(R.id.password)).getText().toString();
                        editText.setText(decrypt(password, editText.getText().toString()));
        	        }
        	        
        	        mLastTouchX = x;
        	        mLastTouchY = y;
        	        
        	        break;
        	    }
        	        
        	    case MotionEvent.ACTION_UP: {
        	        mActivePointerId = INVALID_POINTER_ID;
        	        break;
        	    }
        	        
        	    case MotionEvent.ACTION_CANCEL: {
        	        mActivePointerId = INVALID_POINTER_ID;
        	        break;
        	    }
        	    
        	    case MotionEvent.ACTION_POINTER_UP: {
        	        // Extract the index of the pointer that left the touch sensor
        	        final int pointerIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) 
        	                >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        	        final int pointerId = ev.getPointerId(pointerIndex);
        	        if (pointerId == mActivePointerId) {
        	            // This was our active pointer going up. Choose a new
        	            // active pointer and adjust accordingly.
        	            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
        	            mLastTouchX = ev.getX(newPointerIndex);
        	            mLastTouchY = ev.getY(newPointerIndex);
        	            mActivePointerId = ev.getPointerId(newPointerIndex);
        	        }
        	        break;
        	    }
        	    }
        	    
        	    return true;
        	}
		});
        
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

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final Button copyButton = (Button) findViewById(R.id.copybutton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clipboardManager.setText(editText.getText());
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