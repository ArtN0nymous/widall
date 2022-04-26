package com.ramon.widdall3;

import android.app.Application;

import com.bugsee.library.Bugsee;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Para hacer pruebas a la app y generar los reportes de bug
        Bugsee.launch(this, "86cd4712-5cd2-4caa-8c56-750524b43e8a");
    }
}
