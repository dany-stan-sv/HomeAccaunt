package ru.dd.account;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTrans extends AppCompatActivity {

    public static String sign, bank;
    public static float euro, dollar;

    boolean flagMCheckBox1 = true;
    boolean flagMCheckBox2 = false;
    boolean flagMCheckBox3 = false;

    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;

    BankMaster dbBank;
    TransMaster dbTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.create_trans);

        dbBank = new BankMaster(this);
        dbTrans = new TransMaster(this);

        // текущая информация по транзакции
        TextView tInfo = (TextView) findViewById(R.id.tInfo);
        if (sign == "-") tInfo.setText("перевести с " + bank);
        if (sign == "+") tInfo.setText("пополнить " + bank);

        // рублей
        mCheckBox1 = (CheckBox) findViewById(R.id.checkBox1);
        // долларов
        mCheckBox2 = (CheckBox) findViewById(R.id.checkBox2);
        // евро
        mCheckBox3 = (CheckBox) findViewById(R.id.checkBox3);

        mCheckBox2.setText("Долларов (" + Float.toString(dollar) + " р.)");
        mCheckBox3.setText("Евро (" + Float.toString(euro) + " р.)");

        // выбор валюты, выбрать можно только один чекбокс, рубли по дефолту
        mCheckBox1.setChecked(flagMCheckBox1);
        View.OnClickListener check = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.checkBox1:
                        flagMCheckBox1 = true;
                        flagMCheckBox2 = false;
                        flagMCheckBox3 = false;
                        break;
                    case R.id.checkBox2:
                        flagMCheckBox1 = false;
                        flagMCheckBox2 = true;
                        flagMCheckBox3 = false;
                        break;
                    case R.id.checkBox3:
                        flagMCheckBox1 = false;
                        flagMCheckBox2 = false;
                        flagMCheckBox3 = true;
                        break;
                }
                mCheckBox1.setChecked(flagMCheckBox1);
                mCheckBox2.setChecked(flagMCheckBox2);
                mCheckBox3.setChecked(flagMCheckBox3);
            }
        };
        mCheckBox1.setOnClickListener(check);
        mCheckBox2.setOnClickListener(check);
        mCheckBox3.setOnClickListener(check);

        // обработчик кнопки создания транзакции
        Button buttonOk = (Button) findViewById(R.id.buttonOK);
        View.OnClickListener ok = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ввод суммы транзакции
                EditText tSum = (EditText) findViewById(R.id.tSum);
                if (!tSum.getText().toString().equals("")) {
                    float sum = Float.parseFloat(tSum.getText().toString());

                    // переводим в рубли, если выбрана другая валюта
                    float sumOld = sum;
                    String currency = "\u20BD"; // символ рубля
                    if (flagMCheckBox2) {
                        sum = sum * dollar;
                        currency = "$";
                    }
                    if (flagMCheckBox3) {
                        sum = sum * euro;
                        currency = "€";
                    }

                    // ввод комментария (не обязателен)
                    EditText tComment = (EditText) findViewById(R.id.tComment);

                    if (sign == "-") sum = sum * (-1);

                    if (BankMaster.trans(dbBank, bank, sum)) {
                        // добавляем запись в таблицу транзакций
                        TransMaster.insert(dbTrans, bank, sign, sumOld, currency, tComment.getText().toString());

                        Toast.makeText(CreateTrans.this, "Успешно", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(CreateTrans.this, BankLook.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(CreateTrans.this, "Недостаточно средств", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(CreateTrans.this, "Введите сумму", Toast.LENGTH_SHORT).show();
            }
        };
        buttonOk.setOnClickListener(ok);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
