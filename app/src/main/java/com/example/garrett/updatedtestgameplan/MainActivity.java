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
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private List<String> arrayListStrings;
    private List<Tasks> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;
    Button buttonAdd;
    EditText etToDO;
    int userId;
    DBHelper helper = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = (int) getIntent().getExtras().get("userId");
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        etToDO = (EditText) findViewById(R.id.etToDo);
        arrayListToDo = helper.getAllTasksByUser(userId);
        arrayListStrings = new ArrayList<String>();
        for (Tasks task : arrayListToDo) {
            arrayListStrings.add(task.getTask());
        }
        arrayAdapterToDo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListStrings);
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
    }
//
//    public class TaskAdapter extends ArrayAdapter<Tasks> {
//        public TaskAdapter(Context context, ArrayList<Tasks> tasks) {
//            super(context,0,tasks);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Tasks task = getItem(position);
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main,parent,false);
//            }
//            ListView lvTask = (ListView) convertView.findViewById(R.id.listViewToDo);
//            buttonAdd.setText(task.task);
//
//            return convertView;
//        }
//    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.listViewToDo) {
            return;
        }

        menu.setHeaderTitle("What would you like to do?");


        for (String option : new String[]{"Delete Task", "Go back"}) {
            menu.add(option);
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked CONTEXTMENU PART 1 ");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedItem = info.position;

        if (item.getTitle().equals("Delete Task")) {
            arrayListStrings.remove(selectedItem);
            Tasks taskdel = arrayListToDo.get(selectedItem);
            helper.deleteTask(taskdel.getId());
            arrayListToDo.remove(selectedItem);
            arrayAdapterToDo.notifyDataSetChanged();
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked CONTEXTMENU PART 2 ");

        return true;
    }

    public void onBackPressed() {
        try {
            Log.i("ON BACK PRESSED", "The on back Pressed event has occurred!");

            PrintWriter pw = new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));
            for (String ToDo : arrayListStrings) {
                pw.println((ToDo));
            }

            pw.close();
        } catch (Exception e) {
            Log.i("ON BACK PRESSED", e.getMessage());
        }

    }

    public void buttonAddClick(View v) {
        EditText editTextTODo = (EditText) findViewById(R.id.etToDo);
        String STRtask = etToDO.getText().toString();
        Tasks t = new Tasks();

        if (!STRtask.isEmpty()) {
            long id;
            //insert tasks into tasks table
            t.setTask(STRtask);

            Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked Main Activity PART 1 ");
            id = helper.insertTask(t);
            t.setId((int) id);
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked Main Activity PART 2 ");

            //intersection method is called
            helper.createIntersection(userId,id);

        } else {
            return;
        }

        if (!STRtask.isEmpty()) {
            arrayAdapterToDo.add(STRtask);
            arrayListToDo.add(t);
            editTextTODo.setText("");
        } else {
            return;
        }

    }
}
