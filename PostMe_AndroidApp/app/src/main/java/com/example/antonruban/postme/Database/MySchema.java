package com.example.antonruban.postme.Database;

/**
 * Created by antonruban on 25.05.17.
 */

public class MySchema {
    public static final int VERSION = 2;

    public static final String DATABASE_NAME = "ruban.db";

    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String EMAIL = "email";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";

        }
    }

    public static final class PostTable {
        public static final String NAME = "posts";

        public static final class Cols {
            public static final String POST = "post";
            public static final String USERNAME = "username";

        }
    }
}