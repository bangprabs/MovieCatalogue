package net.prabowoaz.moviecatalogue.widget.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import net.prabowoaz.moviecatalogue.widget.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}