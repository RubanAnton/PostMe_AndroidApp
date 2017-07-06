package com.example.antonruban.postme.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.antonruban.postme.Model.Post;
import com.example.antonruban.postme.Model.User;

/**
 * Created by antonruban on 25.05.17.
 */

public class MyWrapper extends CursorWrapper {

    public MyWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String email = getString(getColumnIndex(MySchema.UserTable.Cols.EMAIL));
        String username = getString(getColumnIndex(MySchema.UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(MySchema.UserTable.Cols.PASSWORD));


        User user = new User(username,password);
        user.setEmail(email);
        return user;
    }

    public Post getPost() {
        String post = getString(getColumnIndex(MySchema.PostTable.Cols.POST));
        String username = getString(getColumnIndex(MySchema.PostTable.Cols.USERNAME));

        Post post_ = new Post(username,post);
        return post_;
    }
}
