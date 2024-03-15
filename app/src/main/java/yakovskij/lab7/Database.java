package yakovskij.lab7;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // String sql = "CREATE TABLE notes (id INT not null unique primary key, key_col TEXT, value_col TEXT);";
        String sql = "CREATE TABLE notes (key_col TEXT, value_col TEXT);";
        db.execSQL(sql);
    }



    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<Note>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT key_col, value_col FROM notes;";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                Note note = new Note();
                note.key = cur.getString(0);
                note.value = cur.getString(1);
                noteList.add(note);
            } while (cur.moveToNext());

        }
        cur.close();
        return noteList;
    }

    public void insert(String key, String value) {

        SQLiteDatabase db = getWritableDatabase();

        if (checkAlreadyExistingKey(key)) {
            createToastMessage("Такая запись уже есть в БД!");
        } else {
            String sql = "INSERT INTO notes VALUES ('" + key + "', '" + value + "');";
            db.execSQL(sql);

            createToastMessage("Успешное добавление записи в БД");
        }

    }

    public String select(String key) {
        if (checkAlreadyExistingKey(key)) {
            String sql = "SELECT value_col FROM notes WHERE key_col = '" + key + "';";
            SQLiteDatabase db = getReadableDatabase();

            @SuppressLint("Recycle")
            Cursor cur = db.rawQuery(sql, null);

            if (cur.moveToFirst()) {
                return cur.getString(0);
            }
        } else {
            createToastMessage("Key not found");
        }

        return "";
    }


    public void update(String key, String value) {
        if (!checkAlreadyExistingKey(key)) {
            createToastMessage("Key not found");
            return;
        }

        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE notes SET key_col='"+key+"',value_col='"+value+"' where key_col='"+key+"';";

        db.execSQL(sql);
        createToastMessage("Успешное обновление Value");
    }


    public void delete(String key) {
        if (!checkAlreadyExistingKey(key)) {
            createToastMessage("Key not found");
            return ;
        }

        SQLiteDatabase db = getWritableDatabase();
        String sql = "Delete from notes where key_col='"+key+"';";
        // Т.е. key_col = "переданный_key"
        db.execSQL(sql);
        createToastMessage(String.format("Успешное удаление \"%s\"", key));
    }


    public boolean checkAlreadyExistingKey(String key) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT value_col FROM notes WHERE key_col = '" + key + "';";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    public void dropAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM notes";
        db.execSQL(sql);
        createToastMessage("Успешное удаление всех записей!");
    }


    public static void createToastMessage(String message) {
        Toast.makeText(MainActivity.context, message, Toast.LENGTH_SHORT).show();
    }


    public int getQuantity() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM notes", null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}