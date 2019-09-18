package com.andrey.rhythm;


import android.annotation.TargetApi;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.andrey.rythm.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Marks extends Fragment {


    public Marks() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_marks, container, false);

        view.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                EditText login = view.findViewById(R.id.Login);
                EditText password = view.findViewById(R.id.Password);

                boolean areCorrectCreds = GetAllInfo(view, login.getText().toString() + ";" + password.getText().toString());

                if (areCorrectCreds) {
                    CreateFile(login.getText().toString(), password.getText().toString());
                    view.findViewById(R.id.loginForm).setVisibility(View.GONE);
                    view.findViewById(R.id.table).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.Updated).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.DeleteCreds).setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.DeleteCreds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "РИТМ");
                File file = new File(directory.getPath() + File.separator + "creds.txt");
                if (file.exists()) {
                    file.delete();
                    directory.delete();
                    Toast.makeText(getActivity(), "Файл с Вашими данными удален", Toast.LENGTH_LONG).show();
                    view.findViewById(R.id.loginForm).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.table).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.Updated).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.DeleteCreds).setVisibility(View.INVISIBLE);
                }
            }
        });

        File file = new File(getContext().getFilesDir().getAbsolutePath() + File.separator + "РИТМ" + File.separator + "creds.txt");

        if (!file.exists()) {
            view.findViewById(R.id.loginForm).setVisibility(View.VISIBLE);
            view.findViewById(R.id.table).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.Updated).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.DeleteCreds).setVisibility(View.INVISIBLE);
        } else {
            view.findViewById(R.id.loginForm).setVisibility(View.GONE);
            String inputData = ReadFromFile();
            GetAllInfo(view, inputData);
        }
        return view;
    }

    public String ReadFromFile() {
        String dataFromFile = "";
        File file = new File(getContext().getFilesDir().getAbsolutePath() + File.separator + "РИТМ" + File.separator + "creds.txt");
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ((receiveString = bufferedReader.readLine()) != null) {
                    dataFromFile += receiveString;
                }
                inputStreamReader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataFromFile;
    }

    public void CreateFile(String login, String password) {
        File directory = new File(getContext().getFilesDir().getAbsolutePath() + File.separator + "РИТМ");
        File file = new File(directory.getPath() + File.separator + "creds.txt");
        if (!file.exists()) {
            try {
                directory.mkdir();
                file.createNewFile();
                OutputStream fo = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fo);
                outputStreamWriter.write(login + ";");
                outputStreamWriter.write(password);
                outputStreamWriter.close();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "Файл с Вашими данными создан", Toast.LENGTH_LONG).show();
        }
    }

    public void CacheMarks(String marks)
    {
        File directory = new File(getContext().getFilesDir().getAbsolutePath() + File.separator + "РИТМ");
        File file = new File(directory.getPath() + File.separator + "marks.txt");
        try {
            directory.mkdir();
            file.createNewFile();
            OutputStream fo = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fo);
            outputStreamWriter.write(marks);
            outputStreamWriter.close();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Файл с Вашими оценками создан", Toast.LENGTH_LONG).show();
    }

    public String ReadCachedMarks()
    {
        String dataFromFile = "";
        File directory = new File(getContext().getFilesDir().getAbsolutePath() + File.separator + "РИТМ");
        File file = new File(directory.getPath() + File.separator + "marks.txt");
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ((receiveString = bufferedReader.readLine()) != null) {
                    dataFromFile += receiveString + "\n";
                }
                inputStreamReader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataFromFile;
    }

    public boolean GetAllInfo(View view, String creds) {

        HttpUrlConnectionExample http = new HttpUrlConnectionExample();

        String marks = this.ReadCachedMarks();

        if(marks.isEmpty()) {
            try {
                marks = http.execute(creds).get();
                this.CacheMarks(marks);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        List<String> splitMarks = new ArrayList<>(Arrays.asList(marks.split("\n")));

        String currentDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        if(!splitMarks.get(0).equals(currentDay))
        {
            try {
                marks = http.execute(creds).get();
                this.CacheMarks(marks);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        TextView updated = view.findViewById(R.id.Updated);

        updated.setText("Обновлено: " + splitMarks.get(0));

        splitMarks.remove(0);

        if (splitMarks.isEmpty()) {
            return false;
        }

        TableLayout table = view.findViewById(R.id.table);
        TableRow titleRow = table.findViewById(R.id.TitleView);
        table.removeAllViewsInLayout();
        table.addView(titleRow);

        for (String mark : splitMarks) {
            String[] subject = mark.split(";");

            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(9);
            tr.setPadding(0, 10, 0, 10);

            for (String s : subject) {
                TextView tv = new TextView(getContext());
                tv.setText(s);
                tv.setTextSize(10);
                tv.setGravity(Gravity.CENTER);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                tv.setLayoutParams(params);
                tv.setTextColor(Color.WHITE);
                tr.addView(tv);
            }
            table.addView(tr);
        }
        return true;
    }

    class HttpUrlConnectionExample extends AsyncTask<String, Void, String> {

        private List<String> cookies;
        private HttpURLConnection conn;

        private String vowels = "ауеёюяэоиы";

        @Override
        protected String doInBackground(String... creds) {
            String loginUrl = "http://ritm.ispu.ru/login";
            String marksPage = "http://ritm.ispu.ru/profile/grades";
            String mainPage = "http://ritm.ispu.ru";

            final HttpUrlConnectionExample http = new HttpUrlConnectionExample();

            // make sure cookies is turn on
            CookieHandler.setDefault(new CookieManager());

            List<String> splitCreds = new ArrayList(Arrays.asList(creds[0].split(";")));

            String resultMarks = "";
            try {
                String page = http.GetPageContent(loginUrl);
                String postParams = http.getFormParams(page, splitCreds.get(0), splitCreds.get(1));
                http.sendPost(loginUrl, postParams);

                String UpdatedPageResult = http.GetPageContent(mainPage);
                resultMarks += GetUpdatedDate(UpdatedPageResult) + "\n";

                String result = http.GetPageContent(marksPage);
                resultMarks += GetMarks(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMarks;
        }


        private String GetUpdatedDate(String html) {
            Document doc = Jsoup.parse(html);
            String resultString = "";
            List<Element> faculties = doc.getElementsByClass("span4");

            for (Element faculty : faculties) {
                if (faculty.getElementsByTag("span").first().text().equals("ИВТФ")) {
                    resultString = faculty.getElementsByTag("p").text();
                    break;
                }
            }
            return resultString;
        }

        private String GetMarks(String html) {
            Document doc = Jsoup.parse(html);
            String resultString = "";
            List<Element> trs = doc.getElementsByTag("tr");
            trs.remove(trs.get(0));
            for (Element tr : trs) {
                List<String> tdsText = tr.getElementsByTag("td").eachText();

                String[] words = tdsText.get(0).split(" ");
                tdsText.remove(tdsText.get(0));
                String subject = "";

                for (String word : words) {
                    if (word.length() >= 4) {
                        if (vowels.indexOf(word.charAt(2)) == -1) {
                            subject += word.subSequence(0, 3);
                        } else {
                            for (int i = 3; i < word.length(); i++) {
                                if (vowels.indexOf(word.charAt(i)) == -1) {
                                    subject += word.subSequence(0, i + 1);
                                    break;
                                }
                            }
                        }
                    } else {
                        subject += word;
                    }
                    subject += " ";
                }
                resultString += subject + ";";

                for (String td : tdsText) {
                    resultString += td + ";";
                }
                resultString += "\n";
            }
            return resultString;
        }

        private void sendPost(String url, String postParams) throws Exception {

            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");

            // Send post request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            conn.getResponseCode();
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

        private String getFormParams(String html, String username, String password)
                throws UnsupportedEncodingException {

            System.out.println("Extracting form's data...");

            Document doc = Jsoup.parse(html);

            Element loginform = doc.getElementById("login-form");
            Elements inputElements = loginform.getElementsByTag("input");
            List<String> paramList = new ArrayList<String>();
            for (Element inputElement : inputElements) {
                String key = inputElement.attr("name");
                String value = inputElement.attr("value");

                if (key.equals("LoginForm[username]"))
                    value = username;
                else if (key.equals("LoginForm[password]"))
                    value = password;
                paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            }

            // build parameters list
            StringBuilder result = new StringBuilder();
            for (String param : paramList) {
                if (result.length() == 0) {
                    result.append(param);
                } else {
                    result.append("&" + param);
                }
            }
            return result.toString();
        }
    }

}