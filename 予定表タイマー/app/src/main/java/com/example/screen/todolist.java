package com.example.screen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class todolist extends AppCompatActivity {
    private TestOpenHelper helper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        if(helper == null){
            helper = new TestOpenHelper(getApplicationContext());
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        Cursor cursor = db.query(
                "testdb",
                new String[] { "todo", "mini" },
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        final String  todo []= new String[cursor.getCount()];
        final String  todo01 []= new String[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {
            todo[i]=cursor.getString(0)+":"+cursor.getString(1)+"分";
            todo01[i]=cursor.getString(0);
            cursor.moveToNext();
        }

        // 忘れずに！
        cursor.close();


        // itemを表示するTextViewが設定されているlist.xmlを指す
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list);

        // activity_main.xmlのlistViewにListViewをセット
        ListView listView = findViewById(R.id.listview);

        for (String str: todo){
            // ArrayAdapterにitemを追加する
            arrayAdapter.add(str);
        }

        // adapterをListViewにセット
        listView.setAdapter(arrayAdapter);

        // itemがクリックされた時のリスナー
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(todolist.this,
                        String.format(Locale.US,"%sが削除されました",todo[position]),
                        Toast.LENGTH_LONG).show();
                arrayAdapter.remove(todo[position]);
                db.delete("testdb","todo=?",new String[]{todo01[position]} );
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }




}
