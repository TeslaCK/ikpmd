package com.example.starsonicerr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    //initialize
    public static EditText editTextName;
    EditText editTextDay;
    EditText editTextdate;
    static EditText editTextCategory;

    Button scan_btn;
    Button buttonAddUser;
    ListView listViewUsers;

    public static int countusers = 0;



    //a list to store all the User from firebase database
    List<User> Users;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         // method for find ids of views
        findViews();

        // to maintian click listner of views
        initListner();


        scan_btn = (Button)findViewById(R.id.btn_scan);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            }
        });

        Button graphButton = (Button) findViewById(R.id.graph_button);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("graph", "graph button zetten");
                startActivity(new Intent(MainActivity.this, PieChartActivity.class));
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }



    private void findViews() {
        //getRefrance for user table
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextdate = (EditText) findViewById(R.id.editTextdate);
        editTextDay = (EditText) findViewById(R.id.editTextDay);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);

        SimpleDateFormat dateF = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        SimpleDateFormat dateDay = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        String day = dateDay.format(Calendar.getInstance().getTime());

        editTextDay.setText(day);
        editTextdate.setText(date);


        //list for store objects of user
        Users = new ArrayList<>();
    }
    private void initListner() {
        //adding an onclicklistener to button
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addUser()
                //the method is defined below
                //this method is actually performing the write operation
                addUser();
            }
        });

        // list item click listener
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User User = Users.get(i);
                CallUpdateAndDeleteDialog(User.getUserid(), User.getUsername(),User.getUserdate(), User.getUserday(), User.getUsercategory());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous User list
                Users.clear();

             //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    User User = postSnapshot.getValue(User.class);
                    //adding User to the list
                    Users.add(User);
                }
                //creating Userlist adapter
                UserList UserAdapter = new UserList(MainActivity.this, Users);
                //attaching adapter to the listview
                listViewUsers.setAdapter(UserAdapter);

                countusers = (int) dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CallUpdateAndDeleteDialog(final String userid, String username, final String date, String day, String category) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.updateTextname);
        final EditText updateTextdate = (EditText) dialogView.findViewById(R.id.updateTextdate);
        final EditText updateTextday = (EditText)  dialogView.findViewById(R.id.updateTextday);
        final EditText updateTextcategory = (EditText) dialogView.findViewById(R.id.updateTextcategory);
        updateTextname.setText(username);
        updateTextdate.setText(date);
        updateTextday.setText(day);
        updateTextcategory.setText(category);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);
        //username for set dialog title
        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();

       // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextname.getText().toString().trim();
                String date = updateTextdate.getText().toString().trim();
                String day = updateTextday.getText().toString().trim();
                String category = updateTextcategory.getText().toString().trim();
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(date)) {
                        if (!TextUtils.isEmpty(category)) {
                            //Method for update data
                            updateUser(userid, name, date, day, category);
                            b.dismiss();
                        }
                    }
                }

            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(userid);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(String id, String name, String date, String day, String category) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        User User = new User(id, name, date, day, category);
        //update  User  to firebase
        UpdateReference.setValue(User);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
        return true;
    }


    private void addUser() {


        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String date = editTextdate.getText().toString().trim();
        String day = editTextDay.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();


        //checking if the value is provided or not Here, you can Add More Validation as you required

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(date)) {
                if (!TextUtils.isEmpty(category)) {

                    //it will create a unique id and we will use it as the Primary Key for our User
                    String id = databaseReference.push().getKey();
                    //creating an User Object
                    User User = new User(id, name, day, date, category);
                    //Saving the User
                    databaseReference.child(id).setValue(User);

                    editTextName.setText("");
                    editTextDay.setText("");
                    editTextCategory.setText("");
                    editTextdate.setText("");
                    Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Please enter a category", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please enter a date", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}