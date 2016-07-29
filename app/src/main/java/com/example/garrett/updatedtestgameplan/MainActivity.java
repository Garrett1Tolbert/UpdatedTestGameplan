package com.example.garrett.updatedtestgameplan;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;
    Button buttonAdd;
    EditText etToDO;
    DBHelper helper = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        etToDO = (EditText) findViewById(R.id.etToDo);
        arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListToDo);
        ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
        listViewToDo.setAdapter(arrayAdapterToDo);

        registerForContextMenu(listViewToDo);

        try {
            Log.i("ON CREATE", "The on create has occurred");

            Scanner scanner = new Scanner(openFileInput("ToDo.txt"));

            while (scanner.hasNextLine()) {
                String toDo = scanner.nextLine();
                arrayAdapterToDo.add(toDo);
            }
            scanner.close();
        } catch (Exception e) {
            Log.i("ON CREATE", e.getMessage());
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "This is a test 1");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.listViewToDo) {
            return;
        }

        menu.setHeaderTitle("What would you like to do?");


        for (String option : new String[]{"Delete Task", "Go back"}) {
            menu.add(option);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedItem = info.position;

        if (item.getTitle().equals("Delete Task")) {
            arrayListToDo.remove(selectedItem);
            arrayAdapterToDo.notifyDataSetChanged();
        }
        return true;
    }

    public void onBackPressed() {
        try {
            Log.i("ON BACK PRESSED", "The on back Pressed event has occurred!");

            PrintWriter pw = new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));
            for (String ToDo : arrayListToDo) {
                pw.println((ToDo));
            }

            pw.close();
        } catch (Exception e) {
            Log.i("ON BACK PRESSED", e.getMessage());
        }

        Logger.getLogger(getClass().getName()).log(Level.WARNING, "This is a test 2");
    }

    public void buttonAddClick(View v) {
        EditText editTextTODo = (EditText) findViewById(R.id.etToDo);
        String ToDo = editTextTODo.getText().toString().trim();
        String STRtask = etToDO.getText().toString();

        if (!STRtask.isEmpty()) {
            //insert tasks into tasks table
            Tasks t = new Tasks();
            t.setTask(STRtask);

            Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS IS A TEST3");

            helper.insertTask(t);

            Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS IS A TEST4");

        } else {
            return;
        }

        if (!ToDo.isEmpty()) {
            arrayAdapterToDo.add(ToDo);
            editTextTODo.setText("");
        } else {
            return;
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "This is a test 5");

    }
}