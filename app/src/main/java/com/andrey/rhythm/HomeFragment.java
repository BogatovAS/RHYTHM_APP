package com.andrey.rhythm;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andrey.rythm.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewF = inflater.inflate(R.layout.fragment_home, container, false);

        final EditText tk1 = viewF.findViewById(R.id.TK_1);
        final EditText pk1 = viewF.findViewById(R.id.PK_1);
        final EditText tk2 = viewF.findViewById(R.id.TK_2);
        final EditText pk2 = viewF.findViewById(R.id.PK_2);
        final ImageButton calculateButton = viewF.findViewById(R.id.calculate_button);
        final TextView Result = viewF.findViewById(R.id.Result);
        final EditText NM = viewF.findViewById(R.id.needmark);
        final TextView Exam = viewF.findViewById(R.id.Exam);
        final ImageButton clearbutton = viewF.findViewById(R.id.clrbutton);
        final TextView dates = viewF.findViewById(R.id.dates);

        try {
            String resultDates = new GetDatesHttpRequest().execute().get();
            dates.setText(resultDates);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

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
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float tk_1, pk_1, tk_2, pk_2;
                int wishMark;

                tk_1 = getFloatValueOfEditText(tk1);
                pk_1 = getFloatValueOfEditText(pk1);
                tk_2 = getFloatValueOfEditText(tk2);
                pk_2 = getFloatValueOfEditText(pk2);
                wishMark = ((int) getFloatValueOfEditText(NM));

                float rating = calculateRating(tk_1,pk_1,tk_2,pk_2, wishMark);

                if (rating == -1) {
                    return;
                }

                Result.setText("Рейтинг СИ: " + rating);

                float needmark = calculateNeedMark(rating, wishMark);

                Exam.setText(String.format("На экзамене нужно получить оценку %.3g%n", needmark));
            }
        });
        return viewF;
    }

    public float calculateRating(float tk_1, float pk_1, float tk_2, float pk_2, int wishMark){

        if(!isMarksCorrect(tk_1, pk_1, tk_2, pk_2, wishMark))
        {
            return -1;
        }

        float rating = calculateSi(tk_1,pk_1,tk_2,pk_2);

        return rating;
    }

    public boolean isMarksCorrect(float tk_1, float pk_1, float tk_2, float pk_2, int wishMark) {
        if (!(isCorrectValue(tk_1) && isCorrectValue(tk_2) && isCorrectValue(pk_1) && isCorrectValue(pk_2) && isCorrectValue(wishMark))) {
        //    Toast.makeText(getActivity(), "Значения в полях не могут быть больше 5", Toast.LENGTH_LONG).show();
            return false;
        }
        if (tk_1 == 0 && tk_2 == 0 && pk_1 == 0 && pk_2 == 0) {
        //    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public float calculateSi(float tk1, float pk1, float tk2, float pk2){
        float rating = (tk1 + tk2) * 5 + (pk1 + pk2) * 15;
        return rating;
    }

    public float calculateNeedMark(float ratingSi, int wish) {
        float needmark;

        switch (wish){
            case 5:
                needmark = (435 - ratingSi) / 60;
                break;
            case 4:
                needmark = (350 - ratingSi) / 60;
                break;
            case 3:
                needmark = (250 - ratingSi) / 60;
                break;
            default:
                needmark = 0;
        }
        return needmark;
    }

    public float getFloatValueOfEditText(EditText mark){
        float result = 0;
        if (!mark.getText().toString().isEmpty())
            result = Float.valueOf(mark.getText().toString());
        return result;
    }

    public boolean isCorrectValue(float value){
        return value >= 0 && value <= 5;
    }
}

class GetDatesHttpRequest extends AsyncTask<Void, Void, String> {
    private HttpURLConnection conn;

    @Override
    protected String doInBackground(Void...voids) {
        String mainPage = "http://ispu.ru/uch";

        // make sure cookies is turn on
        CookieHandler.setDefault(new CookieManager());
        StringBuilder sb = new StringBuilder();

        try {
            String pageContent = this.GetPageContent(mainPage);
            boolean first = true;
            Document document = Jsoup.parse(pageContent);
            ArrayList<Element> elementsLI = document.getElementsByTag("li");
            for (Element element:elementsLI) {
                String elementText = element.text();
                if(elementText.matches(".*[1-2] [А-Я]{2}.*"))
                {
                    if(elementText.contains("2 ПК") && !first) {
                        sb.append(elementText).append(" Для IV курса (кроме направления 38.03.02 «Менеджмент»)").append("\n");
                    }
                    else{
                        sb.append(elementText).append("\n");
                    }
                    if(elementText.contains("2 ПК")) {
                        first = false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  sb.toString();
    }

    private String GetPageContent(String url) throws Exception {

        URL obj = new URL(url);
        try {
            conn = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // default is GET
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        conn.setUseCaches(false);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        try {
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
