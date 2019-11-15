package com.example.starsonicerr;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User> {

    private Activity context;
    //list of users
    List<User> Users;

    public UserList(Activity context, List<User> Users) {
        super(context, R.layout.layout_user_list, Users);
        this.context = context;
        this.Users = Users;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);
        //initialize
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textviewdate = (TextView) listViewItem.findViewById(R.id.textviewdate);
        TextView textviewcategory = (TextView) listViewItem.findViewById(R.id.textviewcategory);

        //getting user at position
        User User = Users.get(position);
        //set user name
        textViewName.setText(User.getUsername());
        //set user date
        textviewdate.setText(User.getUserdate());
        //set user category
        textviewcategory.setText(User.getUsercategory());

        return listViewItem;
    }
}