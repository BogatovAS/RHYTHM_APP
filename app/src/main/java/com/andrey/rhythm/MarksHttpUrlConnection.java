package com.andrey.rhythm;

import android.os.AsyncTask;

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

public class MarksHttpUrlConnection extends AsyncTask<String, Void, String> {

    private HttpURLConnection conn;

    private String vowels = "ауеёюяэоиы";

    @Override
    public String doInBackground(String... creds) {
        String loginUrl = "http://ritm.ispu.ru/login";
        String marksPage = "http://ritm.ispu.ru/profile/grades";
        String mainPage = "http://ritm.ispu.ru";

        // make sure cookies is turn on
        CookieHandler.setDefault(new CookieManager());
        String resultMarks = "";

        try {
            List<String> splitCreds = new ArrayList(Arrays.asList(creds[0].split(";")));

            String page = this.GetPageContent(loginUrl);
            String postParams = this.getFormParams(page, splitCreds.get(0), splitCreds.get(1));
            this.sendPost(loginUrl, postParams);

            String UpdatedPageResult = this.GetPageContent(mainPage);
            resultMarks += GetUpdatedDate(UpdatedPageResult) + "\n";

            String result = this.GetPageContent(marksPage);
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

    public String GetMarks1(String html) {
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
            resultString += subject + ":";

            for (String td : tdsText) {
                resultString += td + ";";
            }
            resultString += "\n";
        }
        return resultString;
    }

    public String GetMarks(String html)
    {
        Document doc = Jsoup.parse(html); // 1
        String resultString = "";
        List<Element> trs = doc.getElementsByTag("tr");
        trs.remove(trs.get(0));
        for (Element tr : trs) // 2
        {
            List<String> tdsText = tr.getElementsByTag("td").eachText(); // 3

            String[] words = tdsText.get(0).split(" ");
            tdsText.remove(tdsText.get(0));
            String subject = "";

            for (String word : words) // 4
            {
                if (word.length() >= 4) // 5
                {
                    subject += word.subSequence(0, 3); // 6
                }
                else // 7
                {
                    subject += word; // 8
                }
                subject += " "; // 9
            }
            resultString += subject + ";"; // 10

            for (String td : tdsText) // 11
            {
                resultString += td + ";"; // 12
            }
            resultString += "\n"; // 13
        }
        return resultString; // 14
    }


    private int sendPost(String url, String postParams) throws Exception {

        URL obj = new URL(url);
        conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "keep-alive");

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        return conn.getResponseCode();
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
