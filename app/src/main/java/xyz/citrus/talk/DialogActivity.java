package xyz.citrus.talk;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    FileInputStream fis;
    FileOutputStream fos;
    ArrayList<String> noteItems = new ArrayList<>();
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ListView myListView = (ListView) findViewById(R.id.dialogListView);
        final EditText myEditText = (EditText) findViewById(R.id.dialogEditText);
        Button button = (Button) findViewById(R.id.buttonSend);
        try {
            fis = openFileInput("content.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<String> noteItemsFromFile = (ArrayList<String>)ois.readObject();
            for(int i=0;i<noteItemsFromFile.size();i++)
                noteItems.add(noteItemsFromFile.get(i));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteItems);
        myListView.setAdapter(aa);
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                aa.remove(selectedItem);
                aa.notifyDataSetChanged();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteItems.add(myEditText.getText().toString());
                aa.notifyDataSetChanged();
                saveToFile();
                myEditText.setText("");
            }
        });
    }

    public void saveToFile() {
        try {
            fos = openFileOutput("content.dat", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(noteItems);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
