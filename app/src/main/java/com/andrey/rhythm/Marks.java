package com.andrey.rhythm;


import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import com.andrey.rhythm.db.DBHelper;
import com.andrey.rhythm.models.SubjectMarks;
import com.andrey.rythm.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

    DBHelper dbSQL;

    public Marks() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_marks, container, false);

        dbSQL = new DBHelper(getContext());

        view.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                EditText login = view.findViewById(R.id.Login);
                EditText password = view.findViewById(R.id.Password);

                boolean areCorrectCreds = false;
                try {
                    areCorrectCreds = GetAllInfo(view, login.getText().toString() + ";" + password.getText().toString());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

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
                File directory = new File(getContext().getFilesDir().getAbsolutePath() + File.separator + "РИТМ");
                File file = new File(directory.getPath() + File.separator + "creds.txt");
                if (file.exists()) {
                    file.delete();
                    directory.delete();
                    Toast.makeText(getActivity(), "Файл с Вашими данными удален", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.loginForm).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.table).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.Updated).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.DeleteCreds).setVisibility(View.INVISIBLE);
                }

                dbSQL.deleteSubjectsTable();
                Toast.makeText(getActivity(), "Файл с Вашими оценками удален", Toast.LENGTH_LONG).show();
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
            try {
                GetAllInfo(view, inputData);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
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

    public ArrayList<SubjectMarks> GetMarks(String marksString, DBHelper dbSQL) {
        ArrayList<SubjectMarks> marks = new ArrayList<>();
        List<String> splitMarks = new ArrayList<>(Arrays.asList(marksString.split("\n")));

        for(int i=1; i<splitMarks.size();i++)
        {
            String[] subjectSplit = splitMarks.get(i).split(":");
            SubjectMarks subject = new SubjectMarks();
            subject.Subject = subjectSplit[0];
            subject.Marks = subjectSplit[1].split(";");
            subject.UpdateDate = splitMarks.get(0);
            boolean isAdded = dbSQL.addSubjectRecord(subject);

            if(isAdded) {
                marks.add(subject);
            }
        }
        return marks;
    }

    public boolean GetAllInfo(View view, String credentials) throws ExecutionException, InterruptedException {
        MarksHttpUrlConnection http = new MarksHttpUrlConnection();
        ArrayList<SubjectMarks> marks = dbSQL.getAllSubjectsRecords();

        if(marks.isEmpty()) {
            String marksString = http.execute(credentials).get();
            marks = GetMarks(marksString, this.dbSQL);
        }

        String currentDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        if(!marks.get(0).UpdateDate.equals(currentDay)){
            marks.clear();
            String marksString = http.execute(credentials).get();
            marks = GetMarks(marksString, this.dbSQL);
        }

        TextView updated = view.findViewById(R.id.Updated);

        updated.setText("Обновлено: " + marks.get(0).UpdateDate);

        if (marks.isEmpty()) {
            return false;
        }

        TableLayout table = view.findViewById(R.id.table);
        TableRow titleRow = table.findViewById(R.id.TitleView);
        table.removeAllViewsInLayout();
        table.addView(titleRow);

        for (SubjectMarks subject : marks) {
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(9);
            tr.setPadding(0, 10, 0, 10);

            TextView tv = new TextView(getContext());
            tv.setText(subject.Subject);
            tv.setTextSize(10);
            tv.setGravity(Gravity.CENTER);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            tv.setLayoutParams(params);
            tv.setTextColor(Color.WHITE);
            tr.addView(tv);

            for (String markString : subject.Marks) {
                tv = new TextView(getContext());
                tv.setText(markString);
                tv.setTextSize(10);
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(params);
                tv.setTextColor(Color.WHITE);
                tr.addView(tv);
            }
            table.addView(tr);
        }
        return true;
    }
}

