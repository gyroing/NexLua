package com.nexlua;


import android.content.Context;
import android.widget.Toast;

import com.luajava.Lua;
import com.luajava.LuaException;
import com.luajava.value.LuaValue;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public interface LuaContext {
    ArrayList<ClassLoader> getClassLoaders();

    Lua getLua();

    File getLuaFile();

    File getLuaDir();

    String getLuaLpath();

    String getLuaCpath();

    Context getContext();

    default void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    default void sendMessage(String message) {
        showToast(message);
    }

    default void sendError(String title, String error) {
        showToast(error);
    }

    default void sendError(Exception e) {
        if (e instanceof LuaException) {
            LuaException luaException = (LuaException) e;
            sendError(luaException.getType(), luaException.getMessage());
        } else {
            sendError(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
