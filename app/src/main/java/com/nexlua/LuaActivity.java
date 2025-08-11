package com.nexlua;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.luajava.JFunction;
import com.luajava.Lua;
import com.luajava.Lua.LuaType;
import com.luajava.LuaException;
import com.luajava.LuaException.LuaError;
import com.luajava.luajit.LuaJit;
import com.luajava.value.LuaValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class LuaActivity extends Activity implements LuaBroadcastReceiver.OnReceiveListener, LuaContext {
    private LuaDexLoader mLuaDexLoader;
    private int mWidth, mHeight;
    private LuaValue mOnKeyDown, mOnKeyUp, mOnKeyLongPress, mOnKeyShortcut, mOnTouchEvent, mOnAccessibilityEvent;
    private LuaValue mOnCreateOptionsMenu, mOnCreateContextMenu, mOnOptionsItemSelected, mOnMenuItemSelected, mOnContextItemSelected;
    private LuaValue mOnActivityResult, onRequestPermissionsResult;
    private LuaValue mOnConfigurationChanged, mOnCreate, mOnError, mOnReceive;
    private LuaBroadcastReceiver mReceiver;
    private LuaResources mResources;
    private String luaDir, luaExtDir, odexDir, libDir, luaLibDir, luaCpath, luaLpath;
    // Toast
    private Toast toast;
    private final StringBuilder toastBuilder = new StringBuilder();
    private long toastTime;
    private final Lua L = new LuaJit();
    private LuaApplication app;
    private Menu optionsMenu;

    public HashMap<String, String> getLibrarys() {
        return mLuaDexLoader.getLibrarys();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 设置主题
        setTheme(R.style.AppTheme);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(null);
        // 获取屏幕大小
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = outMetrics.widthPixels;
        mHeight = outMetrics.heightPixels;
        // 定义文件夹
        app = (LuaApplication) getApplication();
        luaDir = app.getLuaDir();
        luaExtDir = app.getLuaExtDir();
        odexDir = app.getOdexDir();
        libDir = app.getLibDir();
        luaLibDir = app.getLuaLibDir();
        luaCpath = app.getLuaCpath();
        luaLpath = app.getLuaLpath();
        initializeLua();
        mLuaDexLoader = new LuaDexLoader(this);
        mLuaDexLoader.loadLibs();
        setTitle(LuaConfig.APP_NAME);
        setTheme(LuaConfig.APP_THEME);
        // 执行 Lua
        loadLua();
        // 绑定事件
        // onCreate
        mOnCreate = getNullableValue("onCreate");
        // onKeyEvent
        mOnKeyShortcut = getNullableValue("onKeyShortcut");
        mOnKeyDown = getNullableValue("onKeyDown");
        mOnKeyUp = getNullableValue("onKeyUp");
        mOnKeyLongPress = getNullableValue("onKeyLongPress");
        // onTouchEvent
        mOnTouchEvent = getNullableValue("onTouchEvent");
        // onAccessibilityEvent
        mOnAccessibilityEvent = getNullableValue("onAccessibilityEvent");
        // onCreateOptionsMenu
        mOnCreateOptionsMenu = getNullableValue("onCreateOptionsMenu");
        // mOnCreateContextMenu
        mOnCreateContextMenu = getNullableValue("onCreateContextMenu");
        // onOptionsItemSelected
        mOnOptionsItemSelected = getNullableValue("onOptionsItemSelected");
        // onMenuItemSelected
        mOnMenuItemSelected = getNullableValue("onMenuItemSelected");
        // onContextItemSelected
        mOnContextItemSelected = getNullableValue("onContextItemSelected");
        // onActivityResult
        mOnActivityResult = getNullableValue("onActivityResult");
        // onRequestPermissionsResult
        onRequestPermissionsResult = getNullableValue("onRequestPermissionsResult");
        // onConfigurationChanged
        mOnConfigurationChanged = getNullableValue("onConfigurationChanged");
        // onReceive
        mOnReceive = getNullableValue("onReceive");
        // mOnError
        mOnError = getNullableValue("onError");
    }

    public void loadLua() {
        try {
            doAsset("main.lua", null);
        } catch (Exception e) {
            sendError(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    private LuaValue getNullableValue(String name) {
        LuaValue luaValue = L.get(name);
        return luaValue.type() != LuaType.NIL ? luaValue : null;
    }

    private boolean isViewInflated = false;
    private LinearLayout consoleLayout;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        isViewInflated = true;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        isViewInflated = true;
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        isViewInflated = true;
    }

    public void setConsoleLayout() {
        if (consoleLayout == null) {
            // 获取主题颜色
            TypedArray array = getTheme().obtainStyledAttributes(new int[]{
                    android.R.attr.colorBackground,
                    android.R.attr.textColorPrimary,
                    android.R.attr.textColorHighlightInverse,
            });
            int backgroundColor = array.getColor(0, 0xFF00FF);
            int textColor = array.getColor(1, 0xFF00FF);
            array.recycle();
            // 初始化控件
            consoleLayout = new LinearLayout(this);
            listView = new ListView(this);
            listView.setFastScrollEnabled(true);
            listView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setClipboardText(adapter.getItem(i));
                    showToast("复制成功");
                    return true;
                }
            });
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setTextColor(textColor);
                    return view;
                }
            };
            listView.setAdapter(adapter);
            consoleLayout.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            consoleLayout.setBackgroundColor(backgroundColor);
        }
        setContentView(consoleLayout);
        isViewInflated = false;
    }

    private boolean onLuaEvent(LuaValue event, Object... args) {
        if (event != null) {
            LuaValue[] ret = event.call(args);
            return ret != null && ret.length > 0 && ret[0].toBoolean();
        }
        return false;
    }

    public DexClassLoader loadApp(String path) throws LuaException {
        return mLuaDexLoader.loadApp(path);
    }

    public DexClassLoader loadDex(String path) throws LuaException {
        return mLuaDexLoader.loadDex(path);
    }

    public void loadResources(String path) {
        mLuaDexLoader.loadResources(path);
    }

    @Override
    public AssetManager getAssets() {
        if (mLuaDexLoader != null && mLuaDexLoader.getAssets() != null)
            return mLuaDexLoader.getAssets();
        return super.getAssets();
    }

    public LuaResources getLuaResources() {
        Resources superRes = (mLuaDexLoader != null && mLuaDexLoader.getResources() != null) ? mLuaDexLoader.getResources() : super.getResources();
        mResources = new LuaResources(getAssets(), superRes.getDisplayMetrics(), superRes.getConfiguration());
        mResources.setSuperResources(superRes);
        return mResources;
    }

    @Override
    public Resources getResources() {
        if (mLuaDexLoader != null && mLuaDexLoader.getResources() != null)
            return mLuaDexLoader.getResources();
        if (mResources != null)
            return mResources;
        return super.getResources();
    }

    public Object loadLib(String name) throws LuaException {
        int i = name.indexOf(".");
        String fn = name;
        if (i > 0)
            fn = name.substring(0, i);
        File f = new File(libDir + "/lib" + fn + ".so");
        if (!f.exists()) {
            f = new File(luaDir + "/lib" + fn + ".so");
            if (!f.exists())
                throw new LuaException(LuaError.FILE, "can not find lib " + name);
            LuaUtil.copyFile(luaDir + "/lib" + fn + ".so", libDir + "/lib" + fn + ".so");
        }
        LuaValue require = L.get("require");
        return require.call(name);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public Intent registerReceiver(LuaBroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public Intent registerReceiver(LuaBroadcastReceiver.OnReceiveListener ltr, IntentFilter filter) {
        LuaBroadcastReceiver receiver = new LuaBroadcastReceiver(ltr);
        return super.registerReceiver(receiver, filter);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public Intent registerReceiver(IntentFilter filter) {
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
        mReceiver = new LuaBroadcastReceiver(this);
        return super.registerReceiver(mReceiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mOnReceive != null) mOnReceive.call(context, intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        isViewInflated = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        runFunc("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        runFunc("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        runFunc("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        runFunc("onStop");
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) unregisterReceiver(mReceiver);
        runFunc("onDestroy");
        super.onDestroy();
        System.gc();
        L.gc();
        L.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mOnActivityResult != null) mOnActivityResult.call(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (onRequestPermissionsResult != null)
            onRequestPermissionsResult.call(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return onLuaEvent(mOnKeyShortcut, keyCode, event) | super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return onLuaEvent(mOnKeyDown, keyCode, event) | super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return onLuaEvent(mOnKeyUp, keyCode, event) | super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return onLuaEvent(mOnKeyLongPress, keyCode, event) | super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return onLuaEvent(mOnTouchEvent, event) | super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return onLuaEvent(mOnCreateOptionsMenu, menu) | super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (item.hasSubMenu())
                ? super.onOptionsItemSelected(item)
                : onLuaEvent(mOnOptionsItemSelected, item);
    }

    public Menu getOptionsMenu() {
        return optionsMenu;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (!isViewInflated && mOnOptionsItemSelected == null && item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return (item.hasSubMenu())
                ? super.onMenuItemSelected(featureId, item)
                : onLuaEvent(mOnMenuItemSelected, featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        onLuaEvent(mOnCreateContextMenu, menu, view, info);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return onLuaEvent(mOnContextItemSelected, item) | super.onContextItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics outMetrics = new DisplayMetrics();
        mWidth = outMetrics.widthPixels;
        mHeight = outMetrics.heightPixels;
        if (mOnConfigurationChanged != null) mOnConfigurationChanged.call(newConfig);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void newActivity(String path) throws FileNotFoundException {
        newActivity(1, path, new Object[0]);
    }

    public void newActivity(String path, Object[] arg) throws FileNotFoundException {
        newActivity(1, path, arg);
    }

    public void newActivity(int req, String path) throws FileNotFoundException {
        newActivity(req, path, new Object[0]);
    }

    public void newActivity(int req, String path, Object[] arg) throws FileNotFoundException {
        Intent intent = new Intent(this, LuaActivity.class);
        startActivityForResult(intent, req);
    }

    public void newActivity(String path, int in, int out) throws FileNotFoundException {
        newActivity(1, path, in, out, new Object[0]);
    }

    public void newActivity(String path, int in, int out, Object[] arg) throws FileNotFoundException {
        newActivity(1, path, in, out, arg);
    }

    public void newActivity(int req, String path, int in, int out) throws FileNotFoundException {
        newActivity(req, path, in, out, new Object[0]);
    }

    public void newActivity(int req, String path, int in, int out, Object[] arg) throws FileNotFoundException {
        Intent intent = new Intent(this, LuaActivity.class);
        startActivityForResult(intent, req);
        overridePendingTransition(in, out);
    }

    @SuppressLint("ObsoleteSdkInt")
    public void finish(boolean finishTask) {
        if (finishTask && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = getIntent();
            if (intent != null && (intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_DOCUMENT) != 0)
                finishAndRemoveTask();
        }
        super.finish();
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription tDesc = null;
            tDesc = new ActivityManager.TaskDescription(title.toString());
            setTaskDescription(tDesc);
        }
    }

    // 运行lua脚本
    public Object doFile(String filePath) throws IOException {
        return doFile(filePath, new Object[0]);
    }

    public Object doFile(String filePath, Object[] args) throws IOException {
        String path = (filePath.charAt(0) != '/')
                ? luaDir + "/" + filePath
                : filePath;
        L.run(ByteBuffer.wrap(LuaUtil.readAll(filePath)), filePath);
        return L.toObject(-1);
    }

    public Object doAsset(String name, Object[] args) throws IOException {
        L.run(new String(LuaUtil.readAsset(this, name)));
        return L.toObject(-1);
    }

    //运行lua函数
    public Object runFunc(String funcName, Object... args) {
        synchronized (L) {
            // 清空 Lua 栈
            L.setTop(0);
            L.getGlobal(funcName);
            if (!L.isFunction(-1)) return null;
            // 压 traceback 作为错误处理
            L.getGlobal("debug");
            L.getField(-1, "traceback");
            L.remove(-2);
            L.insert(-2);
            // 压入参数
            for (Object arg : args) {
                L.push(arg, Lua.Conversion.SEMI);
            }
            L.pCall(args.length, 1);
            return L.toObject(-1);
        }
    }

    // 显示toast
    @SuppressLint("ShowToast")
    public void showToast(String text) {
        long now = System.currentTimeMillis();
        if (toast == null || now - toastTime > 1000) {
            toastBuilder.setLength(0);
            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toastBuilder.append(text);
            toast.show();
        } else {
            toastBuilder.append("\n");
            toastBuilder.append(text);
            toast.setText(toastBuilder.toString());
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toastTime = now;
    }

    public static void setClipboardText(String text) {
        LuaApplication.setClipboardText(text);
    }

    public static void setClipboardText(String label, String text) {
        LuaApplication.setClipboardText(label, text);
    }

    public static void getClipboardText() {
        LuaApplication.getClipboardText();
    }

    @Override
    public void sendMessage(String message) {
        if (!isViewInflated) {
            setConsoleLayout();
            ActionBar actionBar = getActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(message);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void sendError(String title, String message) {
        boolean ret = onLuaEvent(mOnError, message);
        if (!ret) {
            setTitle(title);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(message);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
    // @formatter:off
    public ArrayList<ClassLoader> getClassLoaders() { return mLuaDexLoader.getClassLoaders(); }
    public String getLuaPath() { /* return luaPath */ return null; }
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
        L.openLibraries();
        for (String libraryName : new String[]{"package", "string", "table", "math", "io", "os", "debug"})
            L.openLibrary(libraryName);
        L.pushJavaObject(this);
        L.setGlobal("activity");
        L.getGlobal("activity");
        L.setGlobal("this");
        // LuaPrint
        LuaPrint print = new LuaPrint(this);
        L.push(print);
        L.setGlobal("print");
        // 插入全局 LuaApplication
        L.pushJavaObject(app);
        L.setGlobal("application");
        // 插入 luaLpath
        L.getGlobal("package");
        L.push(luaLpath);
        L.setField(-2, "path");
        L.push(luaCpath);
        L.setField(-2, "cpath");
        L.pop(1);
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
