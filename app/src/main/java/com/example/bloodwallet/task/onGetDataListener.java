package com.example.bloodwallet.task;

import com.google.firebase.database.DataSnapshot;

public interface onGetDataListener {
    void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure();
}
