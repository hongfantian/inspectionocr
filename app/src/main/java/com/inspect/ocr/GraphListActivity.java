package com.inspect.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import jp.dki.labtestvaluesgraph.Data;

public class GraphListActivity extends AppCompatActivity {

    private static final String TAG = GraphListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_list );

        ListView listView = findViewById(R.id.list_view);

/*
        final EditText editText = findViewById(R.id.edit);
*/
        Data.setItems();
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, R.layout.one_column_list_item, Data.itemNames);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "click position:" + position + "(" + Data.itemNames[position] + ")");

                Intent intent = new Intent(getApplication(), GraphActivity.class);
                intent.putExtra("item", Data.itemNames[position]);
//                intent.putExtra("userId", editText.getText().toString());
                Integer userId = Profile.Companion.getUserId();
                intent.putExtra("userId", userId.toString() );
                startActivity(intent);
            }
        });
    }
}
