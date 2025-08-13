package com.nexlua;

import android.Manifest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class LuaConfig {
    public static final String APP_NAME = "NexLua"; // 软件名称
    public static final String PACKAGE_NAME = "com.nexlua"; // 软件包名
    public static final String APP_VERSION = "1.0.0"; // 版本名
    public static final int APP_CODE = 1; // 版本号
    public static final int APP_SDK = 21; // 最低 SDK 版本
    public static final int APP_THEME = R.style.AppTheme;
    public static final boolean DEBUG_MODE = false; // 是否是调试模式
    // 打包时配置的权限
    public static final boolean REQUEST_PERMISSIONS = true; // 在开始时请求权限
    public static final String[] USER_PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    // 在 App 启动时申请的权限
    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public static final String[] ONLY_DECOMPRESS = new String[]{
            // 指定解压的文件
            // "res/gradle.tar.xz"
    };
    public static final String[] SKIP_DECOMPRESS = new String[]{
            // 跳过解压的文件
            // "res/gradle.tar.xz"
    };
    // Lua 入口文件
    public static final String LUA_ENTRY = "main.lua";
    // 抽离到 Dex 的 Lua 的映射表
    public static final Map<String, Class<?>> LUA_DEX_MAP;

    static {
        Map<String, Class<?>> map = new HashMap<>();
        // map.put("main2.lua", com.nexlua.Main2.class);
        LUA_DEX_MAP = Collections.unmodifiableMap(map);
    }
}
