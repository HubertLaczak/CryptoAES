package com.example.dell.aescrypto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText inputText, inputPassword;
    TextView outputText;
    Button encBtn, decBtn;
    String AES = "AES";

    String outputString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPassword = findViewById(R.id.password);
        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        encBtn = findViewById(R.id.encBtn);
        decBtn = findViewById(R.id.decBtn);

        encBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = encrypt(inputText.getText().toString(), inputPassword.getText().toString());
                    outputText.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = decrypt(outputString, inputPassword.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outputText.setText(outputString);
            }
        });
    }

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedVal = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedVal);
        String decryptedValue = new String(decValue);
        return decryptedValue;

    }

    private String encrypt(String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES); //c reprezentuje klasę szyfru
        c.init(Cipher.ENCRYPT_MODE, key); //inicjalizuje szyfr z danym kluczem, w trybie zmiany wiadomości na szyfr
        byte[] encVal = c.doFinal(Data.getBytes()); //kończy operację szyfrowania
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT); //zamiana na String bez przesunięć/paddingu, DEFAULT
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256"); //tworzenie instancji SkrótuWiadomości z algorytmemSHA-256bit
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length); //przekazuje do instancji digest tablicze bytes
        byte[] key = digest.digest(); //generuje klucz wiadomości w byte, oblicza skrót
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES"); //tworzy klucz z tablicy key zgodny z algorytmem AES
        return secretKeySpec;
    }
}
