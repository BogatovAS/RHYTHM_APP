package com.andrey.rhythm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.andrey.rhythm.db.DBHelper;
import com.andrey.rhythm.models.Note;
import com.andrey.rythm.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteFragment extends Fragment {

    DBHelper db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.db = new DBHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_note, container, false);

        ImageButton createNoteButton = view.findViewById(R.id.create_note_button);

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                String noteSubject = ((EditText)view.findViewById(R.id.create_note_subject)).getText().toString();
                String noteText = ((EditText)view.findViewById(R.id.create_note_text)).getText().toString();
                String creationDate = new SimpleDateFormat("dd MMMM YYYY").format(new Date());

                Note newNote = new Note();
                newNote.Subject = noteSubject;
                newNote.Date = creationDate;
                newNote.Text = noteText;

                db.addNote(newNote);

                Toast.makeText(getActivity(), "Заметка успешно создана", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotesFragment()).commit();
            }
        });

        return view;
    }
}
