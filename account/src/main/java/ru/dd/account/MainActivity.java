package ru.dd.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText textD, textE;
    SharedPreferences sPref;

    final String SAVED_D = "saved_dollar";
    final String SAVED_E = "saved_euro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // пока фиксированая ориентация
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);

        // загружаем старые значения курсов
        textD = (EditText) findViewById(R.id.tDollar);
        textE = (EditText) findViewById(R.id.tEuro);
        loadText();

        Button bCreateBank = (Button) findViewById(R.id.bCreateBank);
        Button bTransLook = (Button) findViewById(R.id.bTransLook);
        Button bLook = (Button) findViewById(R.id.bLook);

        View.OnClickListener bt = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.bCreateBank:
                        intent = new Intent(MainActivity.this, CreateBank.class); break;
                    case R.id.bTransLook:
                        intent = new Intent(MainActivity.this, TransLook.class); break;
                    case R.id.bLook:
                        intent = new Intent(MainActivity.this, BankLook.class); break;
                }
                startActivity(intent);
            }
        };

        bCreateBank.setOnClickListener(bt);
        bTransLook.setOnClickListener(bt);
        bLook.setOnClickListener(bt);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveText();
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_D, textD.getText().toString());
        ed.putString(SAVED_E, textE.getText().toString());
        ed.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText1 = sPref.getString(SAVED_D, "");
        String savedText2 = sPref.getString(SAVED_E, "");
        textD.setText(savedText1);
        textE.setText(savedText2);
        if (savedText1 != "") CreateTrans.dollar = Float.parseFloat(savedText1); else CreateTrans.dollar = 0;
        if (savedText2 != "") CreateTrans.euro = Float.parseFloat(savedText2); else CreateTrans.euro = 0;
    }
}
