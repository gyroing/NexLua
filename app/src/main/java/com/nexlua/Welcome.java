package com.nexlua;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.luajava.Lua;
import com.luajava.luajit.LuaJit;
import com.luajava.value.LuaFunction;
import com.luajava.value.LuaValue;


public class Welcome extends Activity {

    private boolean isUpdate;

    private LuaApplication app;

    private String luaMdDir;

    private String localDir;

    private long mLastTime;

    private long mOldLastTime;

    private ProgressDialog pd;

    private boolean isVersionChanged;

    private String mVersionName;

    private String mOldVersionName;

    private ArrayList<String> permissions;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(new String(new char[]{'P', 'o', 'w', 'e', 'r', 'e', 'd', ' ', 'b', 'y', ' ', 'C', 'h', 'e', 'e', 's', 'e'}));
        view.setTextColor(0xff888888);
        view.setGravity(Gravity.TOP);
        setContentView(view);
        app = (LuaApplication) getApplication();
        luaMdDir = app.getLuaLibDir();
        localDir = app.getLuaDir();
        if (checkInfo()) {
            if (Build.VERSION.SDK_INT >= 23) {
                Lua lua = null;
                try {
                    lua = new LuaJit();
                    lua.openLibraries();
                    byte[] bytes = LuaUtil.readAsset(Welcome.this, "init.lua");
                    String script = new String(bytes, StandardCharsets.UTF_8);
                    lua.run(script);
                    LuaValue check = lua.get("check_permissions");
                    if (check.type() == Lua.LuaType.BOOLEAN && check.toBoolean()) {
                        new UpdateTask().execute();
                        return;
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                } finally {
                    if (lua != null) try { lua.close(); } catch (Exception ignored) {};
                }
                try {
                    permissions = new ArrayList<>();
                    String[] ps2 = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
                    for (String p : ps2) {
                        try {
                            checkPermission(p);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!permissions.isEmpty()) {
                        String[] ps = new String[permissions.size()];
                        permissions.toArray(ps);
                        requestPermissions(ps,
                                0);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            new UpdateTask().execute();
        } else {
            startActivity();
        }
    }

    private void checkPermission(String permission) {
        if (checkCallingOrSelfPermission(permission)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new UpdateTask().execute();
    }

    public void startActivity() {
        Intent intent = new Intent(Welcome.this, Main.class);
        if (isVersionChanged) {
            intent.putExtra("isVersionChanged", isVersionChanged);
            intent.putExtra("newVersionName", mVersionName);
            intent.putExtra("oldVersionName", mOldVersionName);
        }
        startActivity(intent);
        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out                                                                                                                 );
        finish();
    }

    public boolean checkInfo() {
        try {
            // 获取包信息
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            long lastTime = packageInfo.lastUpdateTime;
            String versionName = packageInfo.versionName;
            // 获取历史包信息
            SharedPreferences info = getSharedPreferences("appInfo", 0);
            String oldVersionName = info.getString("versionName", "");
            // 版本名称不同, 触发版本号更新
            if (!versionName.equals(oldVersionName)) {
                SharedPreferences.Editor edit = info.edit();
                edit.putString("versionName", versionName);
                edit.apply();
                isVersionChanged = true;
                mVersionName = versionName;
                mOldVersionName = oldVersionName;
            }
            // 获取历史更新时间
            long oldLastTime = info.getLong("lastUpdateTime", 0);
            // 如果软件安装的时间与历史更新时间不同, 触发更新
            if (oldLastTime != lastTime) {
                SharedPreferences.Editor edit = info.edit();
                edit.putLong("lastUpdateTime", lastTime);
                edit.apply();
                isUpdate = true;
                mLastTime = lastTime;
                mOldLastTime = oldLastTime;
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return !new File(app.getLuaPath("main.lua")).exists();
    }


    @SuppressLint("StaticFieldLeak")
    private class UpdateTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] p1) {
            onUpdate(mLastTime, mOldLastTime);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            startActivity();
        }

        private void onUpdate(long lastTime, long oldLastTime) {
            Lua lua = null;
            try {
                lua = new LuaJit();
                lua.openLibraries();
                byte[] byteArrays = LuaUtil.readAsset(Welcome.this, "init.lua");
                ByteBuffer buffer=ByteBuffer.wrap(byteArrays);
                lua.load(buffer, "update");
                lua.pCall(0, 0);
                LuaValue value = lua.get("onUpdate");
                if (value.type() == Lua.LuaType.FUNCTION) {
                    value.call(mLastTime, mOldLastTime);
                }
            } catch (Exception ignored) {
            } finally {
                if (lua != null) try { lua.close(); } catch (Exception ignored) {};
            }
            try {
                // LuaUtil.rmDir(new File(localDir),".lua");
                // LuaUtil.rmDir(new File(luaMdDir),".lua");
                unApk("assets", localDir);
                unApk("lua", luaMdDir);
                // unZipAssets("main.alp", extDir);
            } catch (IOException ignored) {
            }
        }

        private void sendMsg(String message) {
            Toast.makeText(Welcome.this, message, Toast.LENGTH_SHORT).show();
        }

        private void unApk(String dir, String extDir) throws IOException {
            int i = dir.length() + 1;
            ZipFile zip = new ZipFile(getApplicationInfo().publicSourceDir);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.indexOf(dir) != 0)
                    continue;
                String path = name.substring(i);
                if (entry.isDirectory()) {
                    File f = new File(extDir + File.separator + path);
                    if (!f.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        f.mkdirs();
                    }
                } else {
                    String fileName = extDir + File.separator + path;
                    File file = new File(fileName);
                    File temp = new File(fileName).getParentFile();
                    if (!temp.exists()) {
                        if (!temp.mkdirs()) {
                            throw new RuntimeException("create file " + temp.getName() + " fail");
                        }
                    }
                    try {
                        if (file.exists() && entry.getSize() == file.length() && LuaUtil.getFileMD5(zip.getInputStream(entry)).equals(LuaUtil.getFileMD5(file)))
                            continue;
                    } catch (NullPointerException ignored) {
                    }
                    FileOutputStream out = new FileOutputStream(extDir + File.separator + path);
                    InputStream in = zip.getInputStream(entry);
                    byte[] buf = new byte[4096];
                    int count;
                    while ((count = in.read(buf)) != -1) {
                        out.write(buf, 0, count);
                    }
                    out.close();
                    in.close();
                }
            }
            zip.close();
        }

    }
}
