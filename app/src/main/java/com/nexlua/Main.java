package com.nexlua;

import android.content.Intent;
import android.os.Bundle;

public class Main extends LuaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (savedInstanceState==null) {
            if (intent.getData() != null)
                runFunc("onNewIntent", getIntent());
            if (intent.getBooleanExtra("isVersionChanged",false))
                onVersionChanged(getIntent().getStringExtra("newVersionName"),getIntent().getStringExtra("oldVersionName"));
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        runFunc("onNewIntent", intent);
        super.onNewIntent(intent);
    }

    @Override
    public String getLuaDir()
    {
        return getLocalDir();
    }

    @Override
    public String getLuaPath()
    {
        return getLocalDir()+"/main.lua";
    }

    private void onVersionChanged(String newVersionName, String oldVersionName) {
        runFunc("onVersionChanged", newVersionName, oldVersionName);
    }
}
