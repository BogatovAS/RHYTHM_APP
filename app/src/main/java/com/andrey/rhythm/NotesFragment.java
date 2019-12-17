package com.andrey.rhythm;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.andrey.rhythm.db.DBHelper;
import com.andrey.rhythm.models.Note;
import com.andrey.rhythm.models.SubjectMarks;
import com.andrey.rythm.R;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    public NotesFragment() {
        // Required empty public constructor
    }

    DBHelper db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.db = new DBHelper(getContext());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_notes, container, false);

        ListView listView = view.findViewById(R.id.notesListView);

        ArrayList<Note> notes = db.getAllNotes();

        final NotesAdapter adapter = new NotesAdapter(getContext(), notes);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note selectedNote = adapter.getItem(i);
                db.deleteNote(selectedNote);
                adapter.remove(selectedNote);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        listView.setAdapter(adapter);

        ImageButton addNoteButton = view.findViewById(R.id.add_note_button);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new CreateNoteFragment()).commit();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
