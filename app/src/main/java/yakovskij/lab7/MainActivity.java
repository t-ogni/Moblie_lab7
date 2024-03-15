package yakovskij.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView quantity;
    EditText key;
    EditText value;
    Database db;
    ArrayAdapter <Note> adp;
    int sel = AdapterView.INVALID_POSITION;

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantity = findViewById(R.id.textQuantity);
        key = findViewById(R.id.key);
        value = findViewById(R.id.value);

        db = new Database(this, "my_notes.db", null, 1);
        adp = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1);
        ListView lst = findViewById(R.id.Table);
        lst.setAdapter(adp);
        this.update_adp();
        context = this.getApplicationContext();
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                key.setText(String.valueOf(Objects.requireNonNull(adp.getItem(position)).key));
            }
        });

        quantity.setText("Количество: " + db.getQuantity());
    }

    @SuppressLint("SetTextI18n")
    public void update_adp(){
        adp.clear();
        adp.addAll(db.getAllNotes());
        quantity.setText("Количество: " + db.getQuantity());
    }



    @SuppressLint("SetTextI18n")
    public void onInsertClick(View v) {
        String keyStr = key.getText().toString();
        String valueStr = value.getText().toString();

        if (keyStr.equals("") || valueStr.equals("")) {
            Toast.makeText(this, "Заполните оба поля!", Toast.LENGTH_LONG).show();
            return;
        }


        db.insert(keyStr, valueStr);
        clearFields();
        this.update_adp();
    }

    @SuppressLint("SetTextI18n")
    public void onUpdateClick(View v) {
        String keyStr = key.getText().toString();
        String valueStr = value.getText().toString();

        if (keyStr.equals("") || valueStr.equals("")) {
            Toast.makeText(this, "Заполните оба поля!", Toast.LENGTH_LONG).show();
            return;
        }

        db.update(keyStr, valueStr);
        clearFields();
        this.update_adp();
    }

    public void onSelectClick(View v) {
        String keyStr = key.getText().toString();

        if (keyStr.equals("")) {
            Toast.makeText(this, "Заполните поле Key!", Toast.LENGTH_LONG).show();
            clearFields();
            return;
        }

        String valueStr = db.select(keyStr);

        key.setText(keyStr);
        value.setText(valueStr);
        this.update_adp();
    }

    @SuppressLint("SetTextI18n")
    public void onDeleteClick(View v) {
        String keyStr = key.getText().toString();

        if (keyStr.equals("")) {
            Toast.makeText(this, "Заполните поле Key!", Toast.LENGTH_LONG).show();
            clearFields();
            return;
        }

        db.delete(keyStr);
        clearFields();
        this.update_adp();
    }


    public void onDropAllClick(View v) {
        // TODO: AlertDialog с подтверждением удаления всех

        if (db.getQuantity() == 0) {
            Toast.makeText(this, "Нет записей для удаления!", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.stat_sys_warning)
                .setTitle("Удалить всё?")

                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.dropAll();
                        clearFields();
                        update_adp();
                    }
                })

                .setNegativeButton("Нет", (dialogInterface, i) -> {
                    // Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                })
                .show();
    }


    private void clearFields() {
        key.setText("");
        value.setText("");
    }
}