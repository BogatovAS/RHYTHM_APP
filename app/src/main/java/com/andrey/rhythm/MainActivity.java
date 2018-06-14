package com.andrey.rhythm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrey.rythm.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText tk1 = findViewById(R.id.TK_1);
        final EditText pk1 = findViewById(R.id.PK_1);
        final EditText tk2 = findViewById(R.id.TK_2);
        final EditText pk2 = findViewById(R.id.PK_2);
        final Button btn1 = findViewById(R.id.button1);
        final TextView Result = findViewById(R.id.Result);
        final EditText NM = findViewById(R.id.needmark);
        final TextView Exam = findViewById(R.id.Exam);
        final Button clearbutton = findViewById(R.id.clrbutton);

        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tk1.setText("");
                pk1.setText("");
                tk2.setText("");
                pk2.setText("");
                NM.setText("");
                Exam.setText("");
                Result.setText("");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float tk_1,pk_1,tk_2,pk_2,needmark=5;
                if (!tk1.getText().toString().isEmpty())
                    tk_1 = Float.valueOf(tk1.getText().toString());
                else tk_1 = 0;
                if (!pk1.getText().toString().isEmpty())
                    pk_1 = Float.valueOf(pk1.getText().toString());
                else pk_1 = 0;
                if (!tk2.getText().toString().isEmpty())
                    tk_2 = Float.valueOf(tk2.getText().toString());
                else tk_2 = 0;
                if (!pk2.getText().toString().isEmpty())
                    pk_2 = Float.valueOf(pk2.getText().toString());
                else pk_2 = 0;

                if (!NM.getText().toString().isEmpty())
                    needmark = Integer.valueOf(NM.getText().toString());
                float rating, need;
                boolean correct = true;

                if (tk_1 == 0 && tk_2 == 0 && pk_1 == 0 && pk_2 == 0) {
                    Toast.makeText(MainActivity.this,"Заполните все поля",Toast.LENGTH_LONG).show();
                    correct = false;
                }
                if (correct) {
                    rating = (tk_1 + tk_2) * 5 + (pk_1 + pk_2) * 15;
                    Result.setText("Рейтинг СИ: " + rating);

                    if (needmark == 5) {
                        need = (435 - rating) / 60;
                    } else if (needmark == 4) {
                        need = (350 - rating) / 60;
                    } else if (needmark == 3) {
                        need = (250 - rating) / 60;
                    } else
                        need = 0;
                    Exam.setText("На экзамене нужно получить оценку   " + need);
                }
            }
        });
    }
}
