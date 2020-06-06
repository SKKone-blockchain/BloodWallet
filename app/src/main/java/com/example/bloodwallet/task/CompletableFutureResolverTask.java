package com.example.bloodwallet.task;


import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import java8.util.concurrent.CompletableFuture;

public class CompletableFutureResolverTask<T> extends AsyncTask<CompletableFuture<T>, Void, T> {

    private WithProgressView activity;
    private SimpleCallback<T> callback;

    public CompletableFutureResolverTask(WithProgressView activity, SimpleCallback<T> callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    protected T doInBackground(CompletableFuture<T>... completableFutures) {
        try {
            return completableFutures[0].get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProgress();
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        activity.hideProgress();
        callback.run(t);
    }
}
