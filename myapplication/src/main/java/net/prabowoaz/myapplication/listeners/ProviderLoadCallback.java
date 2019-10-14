package net.prabowoaz.myapplication.listeners;

import android.database.Cursor;

public interface ProviderLoadCallback {
    void postExecute(Cursor movies);
}