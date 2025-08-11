package com.nexlua;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.luajava.Lua;
import com.luajava.Lua.LuaType;
import com.luajava.luajit.LuaJit;
import com.luajava.value.LuaTableValue;
import com.luajava.value.LuaValue;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LuaApplication extends Application implements LuaContext {

    private static LuaApplication mApplication;
    private static final HashMap<String, Object> data = new HashMap<>();
    private SharedPreferences mSharedPreferences;
    private String luaDir, luaExtDir, odexDir, libDir, luaLibDir, luaCpath, luaLpath, luaPath;
    private Lua L;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mSharedPreferences = getSharedPreferences(this);
        initializeLua();
        initializeLuaEvent();
        luaPath = getLuaPath("app.lua");
        if (new File(luaPath).exists()) {
            L.openLibraries();
            try {
                L.load(ByteBuffer.wrap(LuaUtil.readAll(luaPath)), luaPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOnCreate != null) mOnCreate.call();
    }

    private LuaValue mOnCreate, mOnTerminate, mOnLowMemory, mOnTrimMemory, mOnConfigurationChanged;

    private void initializeLuaEvent() {
        LuaValue val;
        val = L.get("onCreate");
        mOnCreate = (val.type() == LuaType.FUNCTION) ? val : null;
        val = L.get("onTerminate");
        mOnTerminate = (val.type() == LuaType.FUNCTION) ? val : null;
        val = L.get("onLowMemory");
        mOnLowMemory = (val.type() == LuaType.FUNCTION) ? val : null;
        val = L.get("onTrimMemory");
        mOnTrimMemory = (val.type() == LuaType.FUNCTION) ? val : null;
        val = L.get("onConfigurationChanged");
        mOnConfigurationChanged = (val.type() == LuaType.FUNCTION) ? val : null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mOnTerminate != null) mOnTerminate.call();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mOnLowMemory != null) mOnLowMemory.call();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mOnTrimMemory != null) mOnTrimMemory.call(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mOnConfigurationChanged != null) mOnConfigurationChanged.call(newConfig);
    }

    public static LuaApplication getInstance() {
        return mApplication;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        // Android 7 及以上
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Context deContext = context.createDeviceProtectedStorageContext();
            if (deContext != null)
                return PreferenceManager.getDefaultSharedPreferences(deContext);
            else
                return PreferenceManager.getDefaultSharedPreferences(context);
        }
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public HashMap<String, Object> getGlobalData() {
        return data;
    }

    public Object getSharedData() {
        return mSharedPreferences.getAll();
    }

    public Object getSharedData(String key) {
        return mSharedPreferences.getAll().get(key);
    }

    public Object getSharedData(String key, Object def) {
        Object ret = mSharedPreferences.getAll().get(key);
        if (ret == null)
            return def;
        return ret;
    }

    public boolean setSharedData(String key, Object value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        if (value == null)
            edit.remove(key);
        else if (value instanceof String)
            edit.putString(key, value.toString());
        else if (value instanceof Long)
            edit.putLong(key, (Long) value);
        else if (value instanceof Integer)
            edit.putInt(key, (Integer) value);
        else if (value instanceof Float)
            edit.putFloat(key, (Float) value);
        else if (value instanceof Set)
            edit.putStringSet(key, (Set<String>) value);
        else if (value instanceof LuaTableValue) {
            LuaTableValue table = (LuaTableValue) value;
            HashSet<String> stringSet = new HashSet<>();
            for (Map.Entry<LuaValue, LuaValue> entry : table.entrySet()) {
                LuaValue val = entry.getValue();
                if (val != null) {
                    stringSet.add((String) val.toJavaObject());
                }
            }
            edit.putStringSet(key, stringSet);
        } else if (value instanceof Boolean)
            edit.putBoolean(key, (Boolean) value);
        else
            return false;
        edit.apply();
        return true;
    }

    public Object get(String name) {
        return data.get(name);
    }

    // @formatter:off
    public ArrayList<ClassLoader> getClassLoaders() { return null; }
    public String getLuaPath() { return luaPath; }
    public String getLuaPath(String path) { return new File(getLuaDir(), path).getAbsolutePath(); }
    public String getLuaPath(String dir, String name) { return new File(getLuaDir(dir), name).getAbsolutePath(); }
    // @formatter:on
    public void initializeLua() {
        luaDir = getFilesDir().getAbsolutePath();
        odexDir = getDir("odex", Context.MODE_PRIVATE).getAbsolutePath();
        libDir = getDir("lib", Context.MODE_PRIVATE).getAbsolutePath();
        luaLibDir = getDir("lua", Context.MODE_PRIVATE).getAbsolutePath();
        luaCpath = getApplicationInfo().nativeLibraryDir + "/lib?.so" + ";" + libDir + "/lib?.so";
        luaLpath = luaLibDir + "/?.lua;" + luaLibDir + "/lua/?.lua;" + luaLibDir + "/?/init.lua;";
        L = new LuaJit();
        for (String libraryName : new String[]{"package", "string", "table", "math", "io", "os", "debug"})
            L.openLibrary(libraryName);
        // 插入全局 LuaApplication
        L.pushJavaObject(LuaApplication.this);
        L.setGlobal("application");
    }

    // @formatter:off
    public Lua getLua() { return L; }
    public String getLuaDir() { return luaDir; }
    public String getLuaDir(String dir) { return new File(getLuaDir(), dir).getAbsolutePath(); }
    public String getLuaExtDir() { return luaExtDir; }
    public String getLuaExtDir(String dir) { return new File(luaExtDir, dir).getAbsolutePath(); }
    public String getLuaExtPath(String path) { return new File(getLuaExtDir(), path).getAbsolutePath(); }
    public String getLuaExtPath(String dir, String name) { return new File(getLuaExtDir(dir), name).getAbsolutePath(); }
    public String getOdexDir() { return odexDir; }
    public String getLibDir() { return libDir; }
    public String getLuaLibDir() { return luaLibDir; }
    public String getLuaLpath() { return luaLpath; }
    public String getLuaCpath() { return luaCpath; }
    public Context getContext() { return this; }
    public void setLuaExtDir(String dir) { luaExtDir =  dir; }
    public void setLuaDir(String dir) { luaDir = dir; }
    // @formatter:on
}



