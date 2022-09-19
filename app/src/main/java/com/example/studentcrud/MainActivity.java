package com.example.studentcrud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Student> students;
    SQLiteDatabaseHandler db;
    Button btnSubmit;
    PopupWindow pwindo;
    Activity activity;
    ListView listView;
    CustomStudentList customStudentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity=this;
        db= new SQLiteDatabaseHandler(this);
        listView = (ListView) findViewById(R.id.lsContact);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopUp();
            }
        });
        Log.d("MainActivity: ", "Before reading mainactivity");
        students = (ArrayList) db.getAllStudent();

        for (Student student : students) {
            String log = "Id: " + student.getId() + " ,Name: " + student.getName() + " ,Email: " + student.getEmail() + " ,Tel: " + student.getTel();
            // Writing Countries to log
            Log.d("Name: ", log);
        }

        CustomStudentList customStudentList = new CustomStudentList(this, students, db);
        listView.setAdapter(customStudentList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "You Selected " + students.get(position).getName() + " as Name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPopUp() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.edit_popup,
                (ViewGroup) activity.findViewById(R.id.popup_element));
        pwindo = new PopupWindow(layout, 600, 670, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        final EditText nameEdit = (EditText) layout.findViewById(R.id.editTextName);
        final EditText emailEdit = (EditText) layout.findViewById(R.id.editTextEmail);
        final EditText telEdit = (EditText) layout.findViewById(R.id.editTextTel);

        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String tel = telEdit.getText().toString();
                Student student = new Student(name, email, tel);
                db.addStudent(student);
                if(customStudentList==null)
                {
                    customStudentList = new CustomStudentList(activity, students, db);
                    listView.setAdapter(customStudentList);
                }
                customStudentList.students = (ArrayList) db.getAllStudent();
                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                for (Student student1 : students) {
                    String log = "Id: " + student1.getId() + " ,Name: " + student.getName() + " ,Email: " + student.getEmail() + " ,Tel: " + student.getTel();
                    // Writing Countries to log
                    Log.d("Name: ", log);
                }
                pwindo.dismiss();
            }
        });
    }
}