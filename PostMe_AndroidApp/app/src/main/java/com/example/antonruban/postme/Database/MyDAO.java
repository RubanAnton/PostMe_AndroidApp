package com.example.antonruban.postme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.antonruban.postme.Model.Post;
import com.example.antonruban.postme.Model.User;

import java.util.LinkedList;
import java.util.List;


public class MyDAO {
    private static MyDAO DAO;
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private final List<User> mUsersList;
    private final List<Post> mPostList;

    private MyDAO(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MyOpenHelper(context).getWritableDatabase();
        mUsersList = new LinkedList<>();
        mPostList = new LinkedList<>();

    }

    public static MyDAO get(Context context) {
        if (DAO == null) {
            DAO = new MyDAO(context);
        }
        return DAO;
    }

    public void addUser(User user) {
        mDatabase.insert(
                MySchema.UserTable.NAME,
                null,
                getUserContentValues(user)
        );
    }

    private static ContentValues getUserContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(MySchema.UserTable.Cols.EMAIL, user.getEmail());
        values.put(MySchema.UserTable.Cols.USERNAME, user.getUsername());
        values.put(MySchema.UserTable.Cols.PASSWORD, user.getPassword());
        return values;
    }

    private MyWrapper queryUser(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                MySchema.UserTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return new MyWrapper(cursor);
    }

    public User getUser(String username) { //get user by username
        MyWrapper wrapper = queryUser(
                MySchema.UserTable.Cols.USERNAME + "=?",
                new String[]{username}
        );
        User user = null;
        if (wrapper.getCount() != 0) {
            wrapper.moveToFirst();
            user = wrapper.getUser();
        }
        wrapper.close();

        return user;
    }


    /////////
    public void addPost(Post post) {
        mDatabase.insert(
                MySchema.PostTable.NAME,
                null,
                getPostContentValues(post)
        );
    }

    private static ContentValues getPostContentValues(Post post) {
        ContentValues values = new ContentValues();
        values.put(MySchema.PostTable.Cols.POST, post.getPost_());
        values.put(MySchema.PostTable.Cols.USERNAME, post.getUsername_());
        return values;
    }

    private MyWrapper queryPost(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                MySchema.PostTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return new MyWrapper(cursor);
    }


    public List<Post> getPosts() {
        mPostList.clear();
        MyWrapper wrapper = queryPost(null, null);
        try {
            wrapper.moveToFirst();
            while (wrapper.isAfterLast() == false) {
                Post post = wrapper.getPost();
                mPostList.add(post);
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }
        return mPostList;
    }

}
