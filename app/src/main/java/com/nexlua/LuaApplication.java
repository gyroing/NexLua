package com.nexlua;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.luajava.Lua;
import com.luajava.NotNull;
import com.luajava.value.LuaTableValue;
import com.luajava.value.LuaValue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LuaApplication extends Application implements LuaContext {

    private static LuaApplication mApplication;
    private static final HashMap<String, Object> data = new HashMap<String, Object>();
    protected String luaDir;
    protected String luaExtDir;
    protected String odexDir;
    protected String libDir;
    protected String luaLibDir;
    protected String luaCpath;
    protected String luaLpath;
    private SharedPreferences mSharedPreferences;

    public static LuaApplication getInstance() {
        return mApplication;
    }

    @Override
    public ArrayList<ClassLoader> getClassLoaders() {
        return null;
    }

    @Override
    public String getLuaDir(@NotNull String dir) {
        return new File(getLuaDir(), dir).getAbsolutePath();
    }

    @Override
    public String getLuaDir() {
        return luaDir;
    }

    @Override
    public String getLuaPath() {
        return luaDir;
    }

    @Override
    public String getLuaPath(String path) {
        return new File(getLuaDir(), path).getAbsolutePath();
    }

    @Override
    public String getLuaPath(String dir, String name) {
        return new File(getLuaDir(dir), name).getAbsolutePath();
    }

    @Override
    public String getLuaExtPath(String path) {
        return new File(getLuaExtDir(), path).getAbsolutePath();
    }

    @Override
    public String getLuaExtPath(String dir, String name) {
        return new File(getLuaExtDir(dir), name).getAbsolutePath();
    }


    @Override
    public String getLuaExtDir(String name) {
        File dir = new File(getLuaExtDir(), name);
        if (!dir.exists())
            if (!dir.mkdirs())
                return dir.getAbsolutePath();
        return dir.getAbsolutePath();
    }

    public String getLibDir() {
        return libDir;
    }

    public String getOdexDir() {
        return odexDir;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mSharedPreferences = getSharedPreferences(this);
        // 初始化AndroLua工作目录
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            luaExtDir = sdDir + "/AndroLua";
        } else {
            File[] fs = new File("/storage").listFiles();
            for (File f : fs) {
                String[] ls = f.list();
                if (ls == null)
                    continue;
                if (ls.length > 5)
                    luaExtDir = f.getAbsolutePath() + "/AndroLua";
            }
            if (luaExtDir == null)
                luaExtDir = getDir("AndroLua", Context.MODE_PRIVATE).getAbsolutePath();
        }

        File destDir = new File(luaExtDir);
        if (!destDir.exists())
            destDir.mkdirs();
        // NexLua 文件夹
        luaDir = getFilesDir().getAbsolutePath();
        odexDir = getDir("odex", Context.MODE_PRIVATE).getAbsolutePath();
        libDir = getDir("lib", Context.MODE_PRIVATE).getAbsolutePath();
        luaLibDir = getDir("lua", Context.MODE_PRIVATE).getAbsolutePath();
        luaCpath = getApplicationInfo().nativeLibraryDir + "/lib?.so" + ";" + libDir + "/lib?.so";
        //luaDir = extDir;
        luaLpath = luaLibDir + "/?.lua;" + luaLibDir + "/lua/?.lua;" + luaLibDir + "/?/init.lua;";
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

    @Override
    public void call(String name, Object[] args) {
    }

    @Override
    public void set(String name, Object object) {
        data.put(name, object);
    }

    @Override
    public HashMap<String, Object> getGlobalData() {
        return data;
    }

    @Override
    public Object getSharedData() {
        return mSharedPreferences.getAll();
    }

    @Override
    public Object getSharedData(String key) {
        return mSharedPreferences.getAll().get(key);
    }

    @Override
    public Object getSharedData(String key, Object def) {
        Object ret = mSharedPreferences.getAll().get(key);
        if (ret == null)
            return def;
        return ret;
    }

    @Override
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

    public String getLuaLibDir() {
        return luaLibDir;
    }

    @Override
    public String getLuaExtDir() {
        return luaExtDir;
    }

    @Override
    public void setLuaExtDir(String dir) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            luaExtDir = new File(sdDir, dir).getAbsolutePath();
        } else {
            File[] fs = new File("/storage").listFiles();
            for (File f : fs) {
                String[] ls = f.list();
                if (ls == null)
                    continue;
                if (ls.length > 5)
                    luaExtDir = new File(f, dir).getAbsolutePath();
            }
            if (luaExtDir == null)
                luaExtDir = getDir(dir, Context.MODE_PRIVATE).getAbsolutePath();
        }
    }

    @Override
    public String getLuaLpath() {
        return luaLpath;
    }

    @Override
    public String getLuaCpath() {
        return luaCpath;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Lua getLua() {
        return null;
    }

    @Override
    public Object doFile(String path, Object[] arg) {
        return null;
    }

    @Override
    public void sendMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void sendError(String title, Exception msg) {

    }
}



