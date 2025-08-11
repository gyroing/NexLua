#include <com_luajava_luajit_LuaJitNatives.h>

//@line:99

#include "luacustomamalg.h"

#include "lua.hpp"
#include "jni.h"

#include "jua.h"

#include "luacomp.h"

#include "juaapi.h"
#include "jualib.h"
#include "juaamalg.h"

#include "luacustom.h"

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_reopenGlobal
        (JNIEnv *env, jobject object, jstring obj_file, char *file) {

//@line:147

    return (jint) reopenAsGlobal((const char *) file);

}

extern "C" {
JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_reopenGlobal(JNIEnv *env, jobject object, jstring obj_file) {
    char *file = (char *) env->GetStringUTFChars(obj_file, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_reopenGlobal(env, object,
                                                                                      obj_file,
                                                                                      file);

    env->ReleaseStringUTFChars(obj_file, file);

    return JNI_returnValue;
}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_initBindings(JNIEnv *env, jclass clazz) {


//@line:151

    return (jint) initLuaJitBindings(env);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_getRegistryIndex(JNIEnv *env, jobject object) {


//@line:158

    return LUA_REGISTRYINDEX;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1checkstack(JNIEnv *env, jobject object, jlong ptr,
                                                      jint extra) {


//@line:185

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_checkstack((lua_State *) L, (int) extra);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1close(JNIEnv *env, jobject object, jlong ptr) {


//@line:218

    lua_State *L = (lua_State *) ptr;

    lua_close((lua_State *) L);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1concat(JNIEnv *env, jobject object, jlong ptr, jint n) {


//@line:249

    lua_State *L = (lua_State *) ptr;

    lua_concat((lua_State *) L, (int) n);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1createtable(JNIEnv *env, jobject object, jlong ptr,
                                                       jint narr, jint nrec) {


//@line:280

    lua_State *L = (lua_State *) ptr;

    lua_createtable((lua_State *) L, (int) narr, (int) nrec);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1equal(JNIEnv *env, jobject object, jlong ptr,
                                                 jint index1, jint index2) {


//@line:312

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_equal((lua_State *) L, (int) index1, (int) index2);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1error(JNIEnv *env, jobject object, jlong ptr) {


//@line:343

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_error((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1gc(JNIEnv *env, jobject object, jlong ptr, jint what,
                                              jint data) {


//@line:431

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_gc((lua_State *) L, (int) what, (int) data);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1getfenv(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index) {


//@line:458

    lua_State *L = (lua_State *) ptr;

    lua_getfenv((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1getfield(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index, jstring obj_k) {
    char *k = (char *) env->GetStringUTFChars(obj_k, 0);


//@line:487

    lua_State *L = (lua_State *) ptr;

    lua_getfield((lua_State *) L, (int) index, (const char *) k);

    env->ReleaseStringUTFChars(obj_k, k);

}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1getfield(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index, jstring obj_k) {
    char *k = (char *) env->GetStringUTFChars(obj_k, 0);


//@line:516

    lua_State *L = (lua_State *) ptr;

    lua_getfield((lua_State *) L, (int) index, (const char *) k);

    env->ReleaseStringUTFChars(obj_k, k);

}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1getglobal(JNIEnv *env, jobject object, jlong ptr,
                                                     jstring obj_name) {
    char *name = (char *) env->GetStringUTFChars(obj_name, 0);


//@line:546

    lua_State *L = (lua_State *) ptr;

    lua_getglobal((lua_State *) L, (const char *) name);

    env->ReleaseStringUTFChars(obj_name, name);

}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1getglobal(JNIEnv *env, jobject object, jlong ptr,
                                                      jstring obj_name) {
    char *name = (char *) env->GetStringUTFChars(obj_name, 0);


//@line:576

    lua_State *L = (lua_State *) ptr;

    lua_getglobal((lua_State *) L, (const char *) name);

    env->ReleaseStringUTFChars(obj_name, name);

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1getmetatable(JNIEnv *env, jobject object, jlong ptr,
                                                        jint index) {


//@line:606

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_getmetatable((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1gettable(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:641

    lua_State *L = (lua_State *) ptr;

    lua_gettable((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1gettable(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:675

    lua_State *L = (lua_State *) ptr;

    lua_gettable((lua_State *) L, (int) index);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1gettop(JNIEnv *env, jobject object, jlong ptr) {


//@line:703

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_gettop((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1insert(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:732

    lua_State *L = (lua_State *) ptr;

    lua_insert((lua_State *) L, (int) index);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isboolean(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:759

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isboolean((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1iscfunction(JNIEnv *env, jobject object, jlong ptr,
                                                       jint index) {


//@line:787

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_iscfunction((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isfunction(JNIEnv *env, jobject object, jlong ptr,
                                                      jint index) {


//@line:815

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isfunction((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1islightuserdata(JNIEnv *env, jobject object, jlong ptr,
                                                           jint index) {


//@line:843

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_islightuserdata((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isnil(JNIEnv *env, jobject object, jlong ptr,
                                                 jint index) {


//@line:871

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isnil((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isnone(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:900

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isnone((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isnoneornil(JNIEnv *env, jobject object, jlong ptr,
                                                       jint index) {


//@line:930

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isnoneornil((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isnumber(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:959

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isnumber((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isstring(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:988

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isstring((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1istable(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index) {


//@line:1016

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_istable((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isthread(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:1044

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isthread((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1isuserdata(JNIEnv *env, jobject object, jlong ptr,
                                                      jint index) {


//@line:1072

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_isuserdata((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1lessthan(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index1, jint index2) {


//@line:1105

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_lessthan((lua_State *) L, (int) index1, (int) index2);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1newtable(JNIEnv *env, jobject object, jlong ptr) {


//@line:1131

    lua_State *L = (lua_State *) ptr;

    lua_newtable((lua_State *) L);


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1newthread(JNIEnv *env, jobject object, jlong ptr) {


//@line:1166

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) lua_newthread((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1newuserdata(JNIEnv *env, jobject object, jlong ptr,
                                                       jlong size) {


//@line:1211

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) lua_newuserdata((lua_State *) L, (size_t) size);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1next(JNIEnv *env, jobject object, jlong ptr,
                                                jint index) {


//@line:1268

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_next((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1objlen(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:1300

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) lua_objlen((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pcall(JNIEnv *env, jobject object, jlong ptr, jint nargs,
                                                 jint nresults, jint errfunc) {


//@line:1388

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_pcall((lua_State *) L, (int) nargs, (int) nresults,
                                                (int) errfunc);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pop(JNIEnv *env, jobject object, jlong ptr, jint n) {


//@line:1414

    lua_State *L = (lua_State *) ptr;

    lua_pop((lua_State *) L, (int) n);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushboolean(JNIEnv *env, jobject object, jlong ptr,
                                                       jint b) {


//@line:1439

    lua_State *L = (lua_State *) ptr;

    lua_pushboolean((lua_State *) L, (int) b);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushinteger(JNIEnv *env, jobject object, jlong ptr,
                                                       jlong n) {


//@line:1464

    lua_State *L = (lua_State *) ptr;
    // What we want to achieve here is:
    // Pushing any Java number (long or double) always results in an approximated number on the stack,
    // unless the number is a Java long integer and the Lua version supports 64-bit integer,
    // when we just push an 64-bit integer instead.
    // The two cases either produce an approximated number or the exact integer value.

    // The following code ensures that no truncation can happen,
    // and the pushed number is either approximated or precise.

    // If the compiler is smart enough, it will optimize
    // the following code into a branch-less single push.
    if (sizeof(lua_Integer) == 4) {
        lua_pushnumber((lua_State *) L, (lua_Number) n);
    } else {
        lua_pushinteger((lua_State *) L, (lua_Integer) n);
    }


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushlightuserdata(JNIEnv *env, jobject object, jlong ptr,
                                                             jlong p) {


//@line:1513

    lua_State *L = (lua_State *) ptr;

    lua_pushlightuserdata((lua_State *) L, (void *) p);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushnil(JNIEnv *env, jobject object, jlong ptr) {


//@line:1537

    lua_State *L = (lua_State *) ptr;

    lua_pushnil((lua_State *) L);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushnumber(JNIEnv *env, jobject object, jlong ptr,
                                                      jdouble n) {


//@line:1562

    lua_State *L = (lua_State *) ptr;

    lua_pushnumber((lua_State *) L, (lua_Number) n);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushstring(JNIEnv *env, jobject object, jlong ptr,
                                                      jstring obj_s) {
    char *s = (char *) env->GetStringUTFChars(obj_s, 0);


//@line:1593

    lua_State *L = (lua_State *) ptr;

    lua_pushstring((lua_State *) L, (const char *) s);

    env->ReleaseStringUTFChars(obj_s, s);

}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1pushstring(JNIEnv *env, jobject object, jlong ptr,
                                                       jstring obj_s) {
    char *s = (char *) env->GetStringUTFChars(obj_s, 0);


//@line:1624

    lua_State *L = (lua_State *) ptr;

    lua_pushstring((lua_State *) L, (const char *) s);

    env->ReleaseStringUTFChars(obj_s, s);

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushthread(JNIEnv *env, jobject object, jlong ptr) {


//@line:1650

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_pushthread((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1pushvalue(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:1677

    lua_State *L = (lua_State *) ptr;

    lua_pushvalue((lua_State *) L, (int) index);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1rawequal(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index1, jint index2) {


//@line:1708

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_rawequal((lua_State *) L, (int) index1, (int) index2);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1rawget(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:1735

    lua_State *L = (lua_State *) ptr;

    lua_rawget((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1rawget(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index) {


//@line:1761

    lua_State *L = (lua_State *) ptr;

    lua_rawget((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1rawgeti(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index, jint n) {


//@line:1790

    lua_State *L = (lua_State *) ptr;

    lua_rawgeti((lua_State *) L, (int) index, (int) n);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1rawgeti(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index, jint n) {


//@line:1819

    lua_State *L = (lua_State *) ptr;

    lua_rawgeti((lua_State *) L, (int) index, (int) n);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1rawset(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:1845

    lua_State *L = (lua_State *) ptr;

    lua_rawset((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1rawseti(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index, jint n) {


//@line:1879

    lua_State *L = (lua_State *) ptr;

    lua_rawseti((lua_State *) L, (int) index, (int) n);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1remove(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:1907

    lua_State *L = (lua_State *) ptr;

    lua_remove((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1replace(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index) {


//@line:1934

    lua_State *L = (lua_State *) ptr;

    lua_replace((lua_State *) L, (int) index);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1resume(JNIEnv *env, jobject object, jlong ptr,
                                                  jint narg) {


//@line:1983

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_resume((lua_State *) L, (int) narg);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1setfenv(JNIEnv *env, jobject object, jlong ptr,
                                                   jint index) {


//@line:2015

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_setfenv((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1setfield(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index, jstring obj_k) {
    char *k = (char *) env->GetStringUTFChars(obj_k, 0);


//@line:2050

    lua_State *L = (lua_State *) ptr;

    lua_setfield((lua_State *) L, (int) index, (const char *) k);

    env->ReleaseStringUTFChars(obj_k, k);

}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1setglobal(JNIEnv *env, jobject object, jlong ptr,
                                                     jstring obj_name) {
    char *name = (char *) env->GetStringUTFChars(obj_name, 0);


//@line:2081

    lua_State *L = (lua_State *) ptr;

    lua_setglobal((lua_State *) L, (const char *) name);

    env->ReleaseStringUTFChars(obj_name, name);

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1setmetatable(JNIEnv *env, jobject object, jlong ptr,
                                                        jint index) {


//@line:2109

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_setmetatable((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1setmetatable(JNIEnv *env, jobject object, jlong ptr,
                                                         jint index) {


//@line:2137

    lua_State *L = (lua_State *) ptr;

    lua_setmetatable((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1settable(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:2171

    lua_State *L = (lua_State *) ptr;

    lua_settable((lua_State *) L, (int) index);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1settop(JNIEnv *env, jobject object, jlong ptr,
                                                  jint index) {


//@line:2200

    lua_State *L = (lua_State *) ptr;

    lua_settop((lua_State *) L, (int) index);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1status(JNIEnv *env, jobject object, jlong ptr) {


//@line:2231

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_status((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1toboolean(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:2266

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_toboolean((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1tointeger(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:2302

    lua_State *L = (lua_State *) ptr;
    // See lua_pushinteger for comments.
    if (sizeof(lua_Integer) == 4) {
        return (jlong) lua_tonumber(L, index);
    } else {
        return (jlong) lua_tointeger(L, index);
    }


}

JNIEXPORT jdouble JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1tonumber(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:2336

    lua_State *L = (lua_State *) ptr;

    jdouble returnValueReceiver = (jdouble) lua_tonumber((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1topointer(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:2372

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) lua_topointer((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jstring JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1tostring(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:2399

    lua_State *L = (lua_State *) ptr;

    const char *returnValueReceiver = (const char *) lua_tostring((lua_State *) L, (int) index);
    return env->NewStringUTF(returnValueReceiver);


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1tothread(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index) {


//@line:2429

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) lua_tothread((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1touserdata(JNIEnv *env, jobject object, jlong ptr,
                                                      jint index) {


//@line:2460

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) lua_touserdata((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1type(JNIEnv *env, jobject object, jlong ptr,
                                                jint index) {


//@line:2501

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_type((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jstring JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1typename(JNIEnv *env, jobject object, jlong ptr,
                                                    jint tp) {


//@line:2529

    lua_State *L = (lua_State *) ptr;

    const char *returnValueReceiver = (const char *) lua_typename((lua_State *) L, (int) tp);
    return env->NewStringUTF(returnValueReceiver);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1xmove(JNIEnv *env, jobject object, jlong from, jlong to,
                                                 jint n) {


//@line:2561

    lua_xmove((lua_State *) from, (lua_State *) to, (int) n);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1yield(JNIEnv *env, jobject object, jlong ptr,
                                                 jint nresults) {


//@line:2602

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_yield((lua_State *) L, (int) nresults);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1gethookcount(JNIEnv *env, jobject object, jlong ptr) {


//@line:2628

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_gethookcount((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1gethookmask(JNIEnv *env, jobject object, jlong ptr) {


//@line:2654

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) lua_gethookmask((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT jstring JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1getupvalue(JNIEnv *env, jobject object, jlong ptr,
                                                      jint funcindex, jint n) {


//@line:2699

    lua_State *L = (lua_State *) ptr;

    const char *returnValueReceiver = (const char *) lua_getupvalue((lua_State *) L,
                                                                    (int) funcindex, (int) n);
    return env->NewStringUTF(returnValueReceiver);


}

JNIEXPORT jstring JNICALL
Java_com_luajava_luajit_LuaJitNatives_lua_1setupvalue(JNIEnv *env, jobject object, jlong ptr,
                                                      jint funcindex, jint n) {


//@line:2737

    lua_State *L = (lua_State *) ptr;

    const char *returnValueReceiver = (const char *) lua_setupvalue((lua_State *) L,
                                                                    (int) funcindex, (int) n);
    return env->NewStringUTF(returnValueReceiver);


}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1callmeta
        (JNIEnv *env, jobject object, jlong ptr, jint obj, jstring obj_e, char *e) {

//@line:2775

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_callmeta((lua_State *) L, (int) obj, (const char *) e);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1callmeta(JNIEnv *env, jobject object, jlong ptr,
                                                     jint obj, jstring obj_e) {
    char *e = (char *) env->GetStringUTFChars(obj_e, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1callmeta(env, object,
                                                                                        ptr, obj,
                                                                                        obj_e, e);

    env->ReleaseStringUTFChars(obj_e, e);

    return JNI_returnValue;
}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1dostring
        (JNIEnv *env, jobject object, jlong ptr, jstring obj_str, char *str) {

//@line:2812

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_dostring((lua_State *) L, (const char *) str);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1dostring(JNIEnv *env, jobject object, jlong ptr,
                                                     jstring obj_str) {
    char *str = (char *) env->GetStringUTFChars(obj_str, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1dostring(env, object,
                                                                                        ptr,
                                                                                        obj_str,
                                                                                        str);

    env->ReleaseStringUTFChars(obj_str, str);

    return JNI_returnValue;
}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1getmetafield
        (JNIEnv *env, jobject object, jlong ptr, jint obj, jstring obj_e, char *e) {

//@line:2844

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_getmetafield((lua_State *) L, (int) obj,
                                                        (const char *) e);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1getmetafield(JNIEnv *env, jobject object, jlong ptr,
                                                         jint obj, jstring obj_e) {
    char *e = (char *) env->GetStringUTFChars(obj_e, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1getmetafield(env,
                                                                                            object,
                                                                                            ptr,
                                                                                            obj,
                                                                                            obj_e,
                                                                                            e);

    env->ReleaseStringUTFChars(obj_e, e);

    return JNI_returnValue;
}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1getmetatable(JNIEnv *env, jobject object, jlong ptr,
                                                         jstring obj_tname) {
    char *tname = (char *) env->GetStringUTFChars(obj_tname, 0);


//@line:2871

    lua_State *L = (lua_State *) ptr;

    luaL_getmetatable((lua_State *) L, (const char *) tname);

    env->ReleaseStringUTFChars(obj_tname, tname);

}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1getmetatable(JNIEnv *env, jobject object, jlong ptr,
                                                         jstring obj_tname) {
    char *tname = (char *) env->GetStringUTFChars(obj_tname, 0);


//@line:2897

    lua_State *L = (lua_State *) ptr;

    luaL_getmetatable((lua_State *) L, (const char *) tname);

    env->ReleaseStringUTFChars(obj_tname, tname);

}

static inline jstring wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1gsub
        (JNIEnv *env, jobject object, jlong ptr, jstring obj_s, jstring obj_p, jstring obj_r,
         char *s, char *p, char *r) {

//@line:2931

    lua_State *L = (lua_State *) ptr;

    const char *returnValueReceiver = (const char *) luaL_gsub((lua_State *) L, (const char *) s,
                                                               (const char *) p, (const char *) r);
    return env->NewStringUTF(returnValueReceiver);

}

JNIEXPORT jstring JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1gsub(JNIEnv *env, jobject object, jlong ptr,
                                                 jstring obj_s, jstring obj_p, jstring obj_r) {
    char *s = (char *) env->GetStringUTFChars(obj_s, 0);
    char *p = (char *) env->GetStringUTFChars(obj_p, 0);
    char *r = (char *) env->GetStringUTFChars(obj_r, 0);

    jstring JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1gsub(env, object,
                                                                                       ptr, obj_s,
                                                                                       obj_p, obj_r,
                                                                                       s, p, r);

    env->ReleaseStringUTFChars(obj_s, s);
    env->ReleaseStringUTFChars(obj_p, p);
    env->ReleaseStringUTFChars(obj_r, r);

    return JNI_returnValue;
}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1loadstring
        (JNIEnv *env, jobject object, jlong ptr, jstring obj_s, char *s) {

//@line:2969

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_loadstring((lua_State *) L, (const char *) s);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1loadstring(JNIEnv *env, jobject object, jlong ptr,
                                                       jstring obj_s) {
    char *s = (char *) env->GetStringUTFChars(obj_s, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1loadstring(env,
                                                                                          object,
                                                                                          ptr,
                                                                                          obj_s, s);

    env->ReleaseStringUTFChars(obj_s, s);

    return JNI_returnValue;
}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1newmetatable
        (JNIEnv *env, jobject object, jlong ptr, jstring obj_tname, char *tname) {

//@line:3006

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_newmetatable((lua_State *) L, (const char *) tname);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1newmetatable(JNIEnv *env, jobject object, jlong ptr,
                                                         jstring obj_tname) {
    char *tname = (char *) env->GetStringUTFChars(obj_tname, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1newmetatable(env,
                                                                                            object,
                                                                                            ptr,
                                                                                            obj_tname,
                                                                                            tname);

    env->ReleaseStringUTFChars(obj_tname, tname);

    return JNI_returnValue;
}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1newstate(JNIEnv *env, jobject object, jint lid) {


//@line:3042

    lua_State *L = luaL_newstate();
    luaJavaSetup(L, env, lid);
    return (jlong) L;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1openlibs(JNIEnv *env, jobject object, jlong ptr) {


//@line:3066

    lua_State *L = (lua_State *) ptr;

    luaL_openlibs((lua_State *) L);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1ref(JNIEnv *env, jobject object, jlong ptr, jint t) {


//@line:3110

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_ref((lua_State *) L, (int) t);
    return returnValueReceiver;


}

JNIEXPORT jstring JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1typename(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:3137

    lua_State *L = (lua_State *) ptr;

    const char *returnValueReceiver = (const char *) luaL_typename((lua_State *) L, (int) index);
    return env->NewStringUTF(returnValueReceiver);


}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1typerror
        (JNIEnv *env, jobject object, jlong ptr, jint narg, jstring obj_tname, char *tname) {

//@line:3175

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaL_typerror((lua_State *) L, (int) narg,
                                                    (const char *) tname);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1typerror(JNIEnv *env, jobject object, jlong ptr,
                                                     jint narg, jstring obj_tname) {
    char *tname = (char *) env->GetStringUTFChars(obj_tname, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaL_1typerror(env, object,
                                                                                        ptr, narg,
                                                                                        obj_tname,
                                                                                        tname);

    env->ReleaseStringUTFChars(obj_tname, tname);

    return JNI_returnValue;
}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1unref(JNIEnv *env, jobject object, jlong ptr, jint t,
                                                  jint ref) {


//@line:3211

    lua_State *L = (lua_State *) ptr;

    luaL_unref((lua_State *) L, (int) t, (int) ref);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaL_1where(JNIEnv *env, jobject object, jlong ptr,
                                                  jint lvl) {


//@line:3252

    lua_State *L = (lua_State *) ptr;

    luaL_where((lua_State *) L, (int) lvl);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1openlib(JNIEnv *env, jobject object, jlong ptr,
                                                    jstring obj_lib) {
    char *lib = (char *) env->GetStringUTFChars(obj_lib, 0);


//@line:3269

    lua_State *L = (lua_State *) ptr;

    luaJ_openlib((lua_State *) L, (const char *) lib);

    env->ReleaseStringUTFChars(obj_lib, lib);

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1compare(JNIEnv *env, jobject object, jlong ptr,
                                                    jint index1, jint index2, jint op) {


//@line:3289

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_compare((lua_State *) L, (int) index1, (int) index2,
                                                   (int) op);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1len(JNIEnv *env, jobject object, jlong ptr,
                                                jint index) {


//@line:3308

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_len((lua_State *) L, (int) index);
    return returnValueReceiver;


}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaJ_1loadbuffer
        (JNIEnv *env, jobject object, jlong ptr, jobject obj_buffer, jint size, jstring obj_name,
         unsigned char *buffer, char *name) {

//@line:3329

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_loadbuffer((lua_State *) L, (unsigned char *) buffer,
                                                      (int) size, (const char *) name);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1loadbuffer(JNIEnv *env, jobject object, jlong ptr,
                                                       jobject obj_buffer, jint size,
                                                       jstring obj_name) {
    unsigned char *buffer = (unsigned char *) (obj_buffer ? env->GetDirectBufferAddress(obj_buffer)
                                                          : 0);
    char *name = (char *) env->GetStringUTFChars(obj_name, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaJ_1loadbuffer(env,
                                                                                          object,
                                                                                          ptr,
                                                                                          obj_buffer,
                                                                                          size,
                                                                                          obj_name,
                                                                                          buffer,
                                                                                          name);

    env->ReleaseStringUTFChars(obj_name, name);

    return JNI_returnValue;
}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaJ_1dobuffer
        (JNIEnv *env, jobject object, jlong ptr, jobject obj_buffer, jint size, jstring obj_name,
         unsigned char *buffer, char *name) {

//@line:3350

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_dobuffer((lua_State *) L, (unsigned char *) buffer,
                                                    (int) size, (const char *) name);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1dobuffer(JNIEnv *env, jobject object, jlong ptr,
                                                     jobject obj_buffer, jint size,
                                                     jstring obj_name) {
    unsigned char *buffer = (unsigned char *) (obj_buffer ? env->GetDirectBufferAddress(obj_buffer)
                                                          : 0);
    char *name = (char *) env->GetStringUTFChars(obj_name, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaJ_1dobuffer(env, object,
                                                                                        ptr,
                                                                                        obj_buffer,
                                                                                        size,
                                                                                        obj_name,
                                                                                        buffer,
                                                                                        name);

    env->ReleaseStringUTFChars(obj_name, name);

    return JNI_returnValue;
}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1resume(JNIEnv *env, jobject object, jlong ptr,
                                                   jint nargs) {


//@line:3369

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_resume((lua_State *) L, (int) nargs);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1pushobject(JNIEnv *env, jobject object, jlong ptr,
                                                       jobject obj) {


//@line:3387

    lua_State *L = (lua_State *) ptr;

    luaJ_pushobject((JNIEnv *) env, (lua_State *) L, (jobject) obj);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1pushclass(JNIEnv *env, jobject object, jlong ptr,
                                                      jobject clazz) {


//@line:3404

    lua_State *L = (lua_State *) ptr;

    luaJ_pushclass((JNIEnv *) env, (lua_State *) L, (jobject) clazz);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1pusharray(JNIEnv *env, jobject object, jlong ptr,
                                                      jobject array) {


//@line:3421

    lua_State *L = (lua_State *) ptr;

    luaJ_pusharray((JNIEnv *) env, (lua_State *) L, (jobject) array);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1pushfunction(JNIEnv *env, jobject object, jlong ptr,
                                                         jobject func) {


//@line:3438

    lua_State *L = (lua_State *) ptr;

    luaJ_pushfunction((JNIEnv *) env, (lua_State *) L, (jobject) func);


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1isobject(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:3456

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_isobject((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jobject JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1toobject(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:3475

    lua_State *L = (lua_State *) ptr;

    jobject returnValueReceiver = (jobject) luaJ_toobject((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jlong JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1newthread(JNIEnv *env, jobject object, jlong ptr,
                                                      jint lid) {


//@line:3494

    lua_State *L = (lua_State *) ptr;

    jlong returnValueReceiver = (jlong) luaJ_newthread((lua_State *) L, (int) lid);
    return returnValueReceiver;


}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1initloader(JNIEnv *env, jobject object, jlong ptr) {


//@line:3512

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_initloader((lua_State *) L);
    return returnValueReceiver;


}

static inline jint wrapped_Java_com_luajava_luajit_LuaJitNatives_luaJ_1invokespecial
        (JNIEnv *env, jobject object, jlong ptr, jclass clazz, jstring obj_method, jstring obj_sig,
         jobject obj, jstring obj_params, char *method, char *sig, char *params) {

//@line:3536

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_invokespecial((JNIEnv *) env, (lua_State *) L,
                                                         (jclass) clazz, (const char *) method,
                                                         (const char *) sig, (jobject) obj,
                                                         (const char *) params);
    return returnValueReceiver;

}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1invokespecial(JNIEnv *env, jobject object, jlong ptr,
                                                          jclass clazz, jstring obj_method,
                                                          jstring obj_sig, jobject obj,
                                                          jstring obj_params) {
    char *method = (char *) env->GetStringUTFChars(obj_method, 0);
    char *sig = (char *) env->GetStringUTFChars(obj_sig, 0);
    char *params = (char *) env->GetStringUTFChars(obj_params, 0);

    jint JNI_returnValue = wrapped_Java_com_luajava_luajit_LuaJitNatives_luaJ_1invokespecial(env,
                                                                                             object,
                                                                                             ptr,
                                                                                             clazz,
                                                                                             obj_method,
                                                                                             obj_sig,
                                                                                             obj,
                                                                                             obj_params,
                                                                                             method,
                                                                                             sig,
                                                                                             params);

    env->ReleaseStringUTFChars(obj_method, method);
    env->ReleaseStringUTFChars(obj_sig, sig);
    env->ReleaseStringUTFChars(obj_params, params);

    return JNI_returnValue;
}

JNIEXPORT jint JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1isinteger(JNIEnv *env, jobject object, jlong ptr,
                                                      jint index) {


//@line:3555

    lua_State *L = (lua_State *) ptr;

    jint returnValueReceiver = (jint) luaJ_isinteger((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1removestateindex(JNIEnv *env, jobject object,
                                                             jlong ptr) {


//@line:3572

    lua_State *L = (lua_State *) ptr;

    luaJ_removestateindex((lua_State *) L);


}

JNIEXPORT void JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1gc(JNIEnv *env, jobject object, jlong ptr) {


//@line:3588

    lua_State *L = (lua_State *) ptr;

    luaJ_gc((lua_State *) L);


}

JNIEXPORT jobject JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1dumptobuffer(JNIEnv *env, jobject object, jlong ptr) {


//@line:3605

    lua_State *L = (lua_State *) ptr;

    jobject returnValueReceiver = (jobject) luaJ_dumptobuffer((lua_State *) L);
    return returnValueReceiver;


}

JNIEXPORT jobject JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1tobuffer(JNIEnv *env, jobject object, jlong ptr,
                                                     jint index) {


//@line:3624

    lua_State *L = (lua_State *) ptr;

    jobject returnValueReceiver = (jobject) luaJ_tobuffer((lua_State *) L, (int) index);
    return returnValueReceiver;


}

JNIEXPORT jobject JNICALL
Java_com_luajava_luajit_LuaJitNatives_luaJ_1todirectbuffer(JNIEnv *env, jobject object, jlong ptr,
                                                           jint index) {
//@line:3643
    lua_State *L = (lua_State *) ptr;
    jobject returnValueReceiver = (jobject) luaJ_todirectbuffer((lua_State *) L, (int) index);
    return returnValueReceiver;
}

}