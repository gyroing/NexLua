package com.luajava.util;

import com.luajava.LuaNatives;

import java.io.File;

/**
 * Android专用的本地库加载器
 * 支持使用标准Android方式加载本地库
 */
public class GlobalLibraryLoader {
    private static volatile Class<? extends LuaNatives> loadedNatives = null;
    private static volatile int nativesLoaded = 0;

    /**
     * 加载本地库（Android专用实现）
     * @param libraryName 库名称（不带前缀/后缀）
     * @return 加载的库文件名
     */
    public static String load(String libraryName) {
        // Android环境直接使用系统加载机制
        System.loadLibrary(libraryName);
        return System.mapLibraryName(libraryName);
    }

    /**
     * 注册已加载的本地库
     * @param natives Lua本地库类
     * @param global 是否全局加载
     */
    public synchronized static void register(Class<? extends LuaNatives> natives, boolean global) {
        if (loadedNatives == null && nativesLoaded == 0) {
            loadedNatives = natives;
            nativesLoaded = global ? 0 : 1;
            return;
        }
        
        if (global) {
            if (loadedNatives == natives && nativesLoaded == 1) {
                nativesLoaded = 0;
                return;
            }
            throw new RuntimeException(
                "Library " + loadedNatives.getName() + " already loaded " +
                "when loading " + natives.getName() + " globally"
            );
        } else {
            if (loadedNatives != null && nativesLoaded == 0 && loadedNatives != natives) {
                throw new RuntimeException(
                    "Global library " + loadedNatives.getName() + " already loaded " +
                    "when loading " + natives.getName()
                );
            }
            loadedNatives = natives;
            nativesLoaded++;
        }
    }
}
