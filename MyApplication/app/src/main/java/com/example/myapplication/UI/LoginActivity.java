package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    TextView ErrorTextview;
    EditText LoginEdittext;
    Button ConnexionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ErrorTextview = findViewById(R.id.textviewError);
        LoginEdittext = findViewById(R.id.edittextLogin);
        ConnexionButton = findViewById(R.id.btnConnexion);

        ConnexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code executé lorsque l'utilisateur clique le bouton
                String textSaisi = LoginEdittext.getText().toString();

                if (textSaisi.equals("maxime")) {
                    ErrorTextview.setVisibility(View.GONE);

                    //Ouvre nouvelle page
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userLogin", textSaisi);
                    startActivity(intent);
                }
                else {
                    String errorMsg = getResources().getString(R.string.ErrorWrongLogin);

                    ErrorTextview.setText(errorMsg);
                    ErrorTextview.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
