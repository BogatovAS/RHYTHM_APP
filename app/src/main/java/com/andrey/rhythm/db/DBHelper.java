package com.andrey.rhythm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andrey.rhythm.models.Note;
import com.andrey.rhythm.models.SubjectMarks;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "rhythmDB";
    public static final String SUBJECTS_TABLE = "subjects";
    public static final String NOTES_TABLE = "notes";

    public static final String KEY_ID = "_id";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_MARKS = "marks";
    public static final String KEY_UPDATE_DATE = "update_date";

    public static final String KEY_DATE = "date";
    public static final String KEY_NOTE = "note";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SUBJECTS_TABLE + "("
                + KEY_ID + " integer primary key,"
                + KEY_SUBJECT + " text,"
                + KEY_MARKS + " text,"
                + KEY_UPDATE_DATE + " text"
                + ")");
        db.execSQL("create table " + NOTES_TABLE + "("
                + KEY_ID + " integer primary key,"
                + KEY_SUBJECT + " text,"
                + KEY_DATE + " text,"
                + KEY_NOTE + " text"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + SUBJECTS_TABLE);
        db.execSQL("drop table if exists " + NOTES_TABLE);
        onCreate(db);
    }

    public SubjectMarks getSubject(String subjectName) throws SQLException {
        SubjectMarks mark = new SubjectMarks();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, SUBJECTS_TABLE, new String[]{KEY_ID, KEY_SUBJECT, KEY_MARKS, KEY_UPDATE_DATE},
                KEY_SUBJECT + " = '" + subjectName + "'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            mark.Subject = mCursor.getString(1);
            mark.Marks = mCursor.getString(2).split(";");
            mark.UpdateDate = mCursor.getString(3);
            mCursor.close();
            db.close();
        }
        return mark;
    }

    public boolean isSubjectExist(String subjectName) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, SUBJECTS_TABLE,
                new String[]{KEY_ID, KEY_SUBJECT, KEY_MARKS, KEY_UPDATE_DATE},
                KEY_SUBJECT + " = '" + subjectName + "'", null,
                null, null, null, null);
        if (mCursor != null && mCursor.moveToNext()) {
            mCursor.close();
            db.close();
            return true;
        }
        mCursor.close();
        db.close();
        return false;
    }

    public boolean deleteSubject(String subjectName) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        int result = db.delete(SUBJECTS_TABLE, KEY_SUBJECT + " = '" + subjectName + "'", null);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteSubjectsTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("drop table if exists " + SUBJECTS_TABLE);
    }

    public boolean updateSubject(SubjectMarks subjectMarks) throws SQLException {
        ContentValues values = new ContentValues();

        String marks = "";
        for (String mark: subjectMarks.Marks) {
            marks += mark + ";";
        }

        values.put(KEY_SUBJECT, subjectMarks.Subject);
        values.put(KEY_MARKS, marks);
        values.put(KEY_UPDATE_DATE, subjectMarks.UpdateDate);

        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update(SUBJECTS_TABLE, values, KEY_SUBJECT + " = '" + subjectMarks.Subject + "'", null);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<SubjectMarks> getAllSubjectsRecords() {
        ArrayList<SubjectMarks> marks = new ArrayList<SubjectMarks>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(true, SUBJECTS_TABLE,
                new String[]{KEY_ID, KEY_SUBJECT, KEY_MARKS, KEY_UPDATE_DATE},
                null, null, null, null, null, null);

        while(cursor.moveToNext()){
            SubjectMarks mark = new SubjectMarks();
            mark.Subject = cursor.getString(1);
            mark.Marks = cursor.getString(2).split(";");
            mark.UpdateDate = cursor.getString(3);
            marks.add(mark);
        }
        cursor.close();
        db.close();

        return marks;
    }

    public boolean addSubjectRecord(SubjectMarks subjectMarks) {
        ContentValues values = new ContentValues();

        String marks = "";
        for (String mark: subjectMarks.Marks) {
            marks += mark + ";";
        }

        values.put(KEY_SUBJECT, subjectMarks.Subject);
        values.put(KEY_MARKS, marks);
        values.put(KEY_UPDATE_DATE, subjectMarks.UpdateDate);

        SQLiteDatabase db = this.getWritableDatabase();

        long row = db.insertWithOnConflict(SUBJECTS_TABLE, null, values, SQLiteDatabase.CONFLICT_ABORT);
        db.close();

        return row != -1;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(true, NOTES_TABLE,
                new String[]{KEY_ID, KEY_SUBJECT, KEY_DATE, KEY_NOTE},
                null, null, null, null, null, null);

        while(cursor.moveToNext()){
            Note note = new Note();
            note.Subject = cursor.getString(1);
            note.Date = cursor.getString(2);
            note.Text = cursor.getString(3);
            notes.add(note);
        }
        cursor.close();
        db.close();

        return notes;
    }

    public boolean deleteNote(Note note) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        int result = db.delete(NOTES_TABLE, KEY_DATE + " = '" + note.Date + "'" + "AND " + KEY_NOTE + " = '" + note.Text + "'", null);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public long addNote(Note note) {
        ContentValues values = new ContentValues();

        values.put(KEY_SUBJECT, note.Subject);
        values.put(KEY_DATE, note.Date);
        values.put(KEY_NOTE, note.Text);

        SQLiteDatabase db = this.getWritableDatabase();

        long row = db.insertWithOnConflict(NOTES_TABLE, null, values, SQLiteDatabase.CONFLICT_ABORT);
        db.close();

        return row;
    }


}
