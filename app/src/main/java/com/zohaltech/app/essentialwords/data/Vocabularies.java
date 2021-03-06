package com.zohaltech.app.essentialwords.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zohaltech.app.essentialwords.classes.MyRuntimeException;
import com.zohaltech.app.essentialwords.entities.Vocabulary;

import java.util.ArrayList;

public class Vocabularies
{
    public static final String TableName = "Vocabularies";
    public static final String Id = "Id";
    public static final String Lesson = "Lesson";
    public static final String Pronunciation = "Pronunciation";
    public static final String Vocabulary = "Vocabulary";
    public static final String EnglishDef = "EnglishDef";
    public static final String PersianDef = "PersianDef";
    public static final String Learned = "Learned";
    public static final String Bookmarked = "Bookmarked";

    static final String CreateTable = "CREATE TABLE " + TableName + " ( " +
            Id + " INTEGER PRIMARY KEY NOT NULL, " +
            Lesson + " INTEGER, " +
            Vocabulary + " VARCHAR(250), " +
            Pronunciation + " VARCHAR(1024), " +
            EnglishDef + " VARCHAR(1024)," +
            PersianDef + " VARCHAR(1024), " +
            Learned + " Boolean DEFAULT (0), " +
            Bookmarked + " Boolean DEFAULT (0) );";

    static final String DropTable = "Drop Table If Exists " + TableName;

    public static ArrayList<Vocabulary> select(String whereClause, String[] selectionArgs, String limitClause)
    {
        ArrayList<Vocabulary> vocabularies = new ArrayList<>();
        DataAccess da = new DataAccess();
        SQLiteDatabase db = da.getReadableDB();
        Cursor cursor = null;

        try
        {
            String query = "Select * From " + TableName + " " + whereClause + " " + limitClause;
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.moveToFirst())
            {
                do
                {
                    Vocabulary vocabulary = new Vocabulary(cursor.getInt(cursor.getColumnIndex(Id)),
                            cursor.getInt(cursor.getColumnIndex(Lesson)),
                            cursor.getString(cursor.getColumnIndex(Vocabulary)),
                            cursor.getString(cursor.getColumnIndex(Pronunciation)),
                            cursor.getString(cursor.getColumnIndex(EnglishDef)),
                            cursor.getString(cursor.getColumnIndex(PersianDef)).replace('|', '\n'),
                            cursor.getInt(cursor.getColumnIndex(Learned)) == 1,
                            cursor.getInt(cursor.getColumnIndex(Bookmarked)) == 1);


                    vocabularies.add(vocabulary);
                } while (cursor.moveToNext());
            }
        }
        catch (MyRuntimeException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }
        return vocabularies;
    }

    public static ArrayList<Vocabulary> select()
    {
        return select("", null, "");
    }

    public static ArrayList<Vocabulary> selectSiblings(long vocabularyId)
    {
        Vocabulary vocabulary = select(vocabularyId);
        assert vocabulary != null;

        return select("Where " + Lesson + " = ? ", new String[]{"" + vocabulary.getLesson()}, "");
    }

    public static ArrayList<Vocabulary> selectBookmarks()
    {
        return select("Where " + Bookmarked + " = ? ", new String[]{"1"}, "");
    }

    public static Vocabulary select(long vocabularyId)
    {
        ArrayList<Vocabulary> vocabularies = select("Where " + Id + " = ? ", new String[]{String.valueOf(vocabularyId)}, "");
        if (vocabularies.size() == 1)
        {
            return vocabularies.get(0);
        }
        else
        {
            return null;
        }
    }

    public static Vocabulary next(long vocabularyId)
    {
        ArrayList<Vocabulary> selectedVocabulary = select("Where " + Id + " > ? ", new String[]{String.valueOf(vocabularyId)}, " Limit 1");
        if (selectedVocabulary.size() == 0)
        {
            return null;
        }
        else
        {
            return selectedVocabulary.get(0);
        }
    }

    public static ArrayList<Vocabulary> selectByLesson(long lesson)
    {
        String whereClause = " Where " + Lesson + " = " + lesson;
        return select(whereClause, null, "");
    }

    public static ArrayList<Vocabulary> search(String searchText)
    {
        String query = "SELECT DISTINCT v.* FROM " + TableName + " v\n" +
                "INNER JOIN " + Examples.TableName + " e\n" +
                "ON v.Id=e." + Examples.VocabularyId + "\n" +
                "LEFT JOIN " + Notes.TableName + " n\n" +
                "ON v.Id=n." + Notes.VocabularyId + "\n" +
                "WHERE v." + Vocabulary + " LIKE '%" + searchText + "%'\n" +
                "OR v." + EnglishDef + "  LIKE '%" + searchText + "%'\n" +
                "OR v." + PersianDef + "  LIKE '%" + searchText + "%'\n" +
                "OR e." + Examples.English + " LIKE '%" + searchText + "%'\n" +
                "OR n." + Notes.Description + " LIKE '%" + searchText + "%'";

        ArrayList<Vocabulary> vocabularies = new ArrayList<>();
        DataAccess da = new DataAccess();
        SQLiteDatabase db = da.getReadableDB();
        Cursor cursor = null;

        try
        {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst())
            {
                do
                {
                    Vocabulary vocabulary = new Vocabulary(cursor.getInt(cursor.getColumnIndex(Id)),
                            cursor.getInt(cursor.getColumnIndex(Lesson)),
                            cursor.getString(cursor.getColumnIndex(Vocabulary)),
                            cursor.getString(cursor.getColumnIndex(Pronunciation)),
                            cursor.getString(cursor.getColumnIndex(EnglishDef)),
                            cursor.getString(cursor.getColumnIndex(PersianDef)),
                            cursor.getInt(cursor.getColumnIndex(Learned)) == 1,
                            cursor.getInt(cursor.getColumnIndex(Bookmarked)) == 1);

                    vocabularies.add(vocabulary);
                } while (cursor.moveToNext());
            }
        }
        catch (MyRuntimeException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }
        return vocabularies;
    }

    public static long insert(Vocabulary vocabulary)
    {
        DataAccess da = new DataAccess();
        return da.insert(TableName, getContentValues(vocabulary));
    }

    public static long update(Vocabulary vocabulary)
    {
        ContentValues values = new ContentValues();
        values.put(Id, vocabulary.getId());
        values.put(Learned, vocabulary.getLearned());
        values.put(Bookmarked, vocabulary.getBookmarked());
        DataAccess da = new DataAccess();
        return da.update(TableName, values, Id + " = ? ", new String[]{String.valueOf(vocabulary.getId())});
    }

    public static ContentValues getContentValues(Vocabulary vocabulary)
    {
        ContentValues values = new ContentValues();
        values.put(Id, vocabulary.getId());
        values.put(Lesson, vocabulary.getLesson());
        values.put(Vocabulary, vocabulary.getVocabulary());
        values.put(EnglishDef, vocabulary.getVocabEnglishDef());
        values.put(PersianDef, vocabulary.getVocabPersianDef());
        values.put(Learned, vocabulary.getLearned());
        values.put(Bookmarked, vocabulary.getBookmarked());

        return values;
    }

    public static void resetLearnedVocabularies()
    {
        ContentValues values = new ContentValues();
        values.put(Learned, 0);
        DataAccess db = new DataAccess();

        db.update(TableName, values, null, null);
    }
}


