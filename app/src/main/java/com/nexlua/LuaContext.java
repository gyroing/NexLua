package com.nexlua;


import android.content.Context;
import com.luajava.Lua;
import java.util.ArrayList;

public interface LuaContext {
    // NexLua Environment
    ArrayList<ClassLoader> getClassLoaders();
    Lua getLua();
    String getLuaDir();
    String getLuaDir(String dir);
    String getLuaExtDir();
    String getLuaExtDir(String dir);
    String getLuaExtPath(String path);
    String getLuaExtPath(String dir, String name);
    String getOdexDir();
    String getLibDir();
    String getLuaLibDir();
    String getLuaLpath();
    String getLuaCpath();
    Context getContext();
    void setLuaExtDir(String dir);
    void setLuaDir(String dir);
    void sendMessage(String message);
    void sendError(String title, String error);
}
