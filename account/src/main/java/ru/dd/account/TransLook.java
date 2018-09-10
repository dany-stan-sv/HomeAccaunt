package ru.dd.account;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class TransLook extends AppCompatActivity {

    TransMaster dbTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.trans_look);

        ListView lvBD = (ListView) findViewById(R.id.lvTransStory);

        dbTrans = new TransMaster(this);
        String str = TransMaster.look(dbTrans);
        String[] strArr = str.split("\n ");

        // данные из БД в адаптер для ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArr);
        lvBD.setAdapter(adapter);

        Button bClear = (Button) findViewById(R.id.bClear);
        // обработчик кнопок удаления
        View.OnClickListener bt = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransMaster.clear(dbTrans);

                Intent intent;
                intent = new Intent(TransLook.this, TransLook.class);
                finish();
                startActivity(intent);
            }
        };
        bClear.setOnClickListener(bt);
    }
}
