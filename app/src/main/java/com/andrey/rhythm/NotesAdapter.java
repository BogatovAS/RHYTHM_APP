package com.andrey.rhythm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andrey.rhythm.models.Note;
import com.andrey.rythm.R;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {

    Context context;
    ArrayList<Note> data;
    private static LayoutInflater inflater = null;

    public NotesAdapter(Context context, ArrayList<Note> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Note getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(Note note){
        this.data.remove(note);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.note, null);
        }
        ((TextView)view.findViewById(R.id.note_subject)).setText(data.get(position).Subject);
        ((TextView)view.findViewById(R.id.note_text)).setText(data.get(position).Text);
        ((TextView)view.findViewById(R.id.note_creation_time)).setText(data.get(position).Date);
        return view;
    }
}
