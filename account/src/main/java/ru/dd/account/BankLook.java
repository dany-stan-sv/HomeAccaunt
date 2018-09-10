package ru.dd.account;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BankLook extends AppCompatActivity {

    BankMaster dbBank;
    TextView selection;
    String[] strArr;
    String selectedItem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.bank_look);

        dbBank = new BankMaster(this);
        ListView lvBD = (ListView) findViewById(R.id.lvBD);
        selection = (TextView) findViewById(R.id.tSelect);

        String str = BankMaster.look(dbBank);

        if (!str.equals("")) {
            strArr = str.split("\n");

            // данные из БД в адаптер для ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArr);
            lvBD.setAdapter(adapter);

            // TextView - какой счет выбран
            lvBD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    selectedItem = strArr[position].split(" ")[0];
                    selection.setText(selectedItem);
                }
            });

            Button bDeleteAll = (Button) findViewById(R.id.bDeleteAll);
            Button bDelete = (Button) findViewById(R.id.bDelete);

            // обработчик кнопок удаления
            View.OnClickListener bt = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (v.getId()) {
                        case R.id.bDeleteAll: BankMaster.deleteAll(dbBank); break;
                        case R.id.bDelete: BankMaster.delete(dbBank, selectedItem); break;
                    }
                    String str = BankMaster.look(dbBank);

                    // для обновления данных и ListView
                    Intent intent;
                    intent = new Intent(BankLook.this, BankLook.class);
                    finish();
                    startActivity(intent);
                }
            };
            bDeleteAll.setOnClickListener(bt);
            bDelete.setOnClickListener(bt);


            Button bTransMinus = (Button) findViewById(R.id.bTransMinus);
            Button bTransPlus = (Button) findViewById(R.id.bTransPlus);

            // обработчик кнопок создания транзакций
            View.OnClickListener bt2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectedItem.equals("")) {
                        switch (v.getId()) {
                            case R.id.bTransMinus: CreateTrans.sign = "-"; break;
                            case R.id.bTransPlus: CreateTrans.sign = "+"; break;
                        }
                        CreateTrans.bank = selectedItem;
                        finish();
                        Intent intent = new Intent(BankLook.this, CreateTrans.class);
                        startActivity(intent);
                    }
                }
            };
            bTransMinus.setOnClickListener(bt2);
            bTransPlus.setOnClickListener(bt2);
        }

        else {
            // кнопки не назначены
            selection.setText("Счетов пока нет");
        }
    }
}
