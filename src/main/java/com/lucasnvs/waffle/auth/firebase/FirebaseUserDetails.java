package com.lucasnvs.waffle.auth.firebase;

/**
 * Container for Firebase user details.
 */
public class FirebaseUserDetails {

    private final String uid;
    private final String email;
    private final String name;
    private final boolean isAdmin;

    public FirebaseUserDetails(String uid, String email, String name, boolean isAdmin) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

