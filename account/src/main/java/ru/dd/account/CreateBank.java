package ru.dd.account;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateBank extends AppCompatActivity {

    EditText tNameCreate;
    BankMaster dbBank;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.create_bank);

        tNameCreate = (EditText) findViewById(R.id.tNameCreate);
        Button bCreateBack = (Button) findViewById(R.id.bCreateBack);

        dbBank = new BankMaster(this);
        str = BankMaster.look (dbBank);

        // обработчик "Создать и вернуться в меню"
        View.OnClickListener b1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // запрет на пустую строку
                if (tNameCreate.getText().toString().equals("")){
                    Toast.makeText(CreateBank.this, "введите название", Toast.LENGTH_SHORT).show();
                }
                // запрет на повтор имени
                else if (BankMaster.nameFind(dbBank, tNameCreate.getText().toString())){
                    Toast.makeText(CreateBank.this, "название занято", Toast.LENGTH_SHORT).show();
                }
                else {
                    BankMaster.insert(dbBank, tNameCreate.getText().toString());
                    Toast.makeText(CreateBank.this, "Успешно", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(CreateBank.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        bCreateBack.setOnClickListener(b1);
    }


}
