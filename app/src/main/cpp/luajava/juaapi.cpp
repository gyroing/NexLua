#include "jni.h"
#include "lua.hpp"

#include "jua.h"

/**
 * A custom 'require' function that can load modules from a string.
 * If the second argument is a string, it's treated as the module's code.
 * Otherwise, it calls the original 'require' function.
 */
static int luaJ_require(lua_State *L) {
    if (lua_gettop(L) >= 2 && lua_isstring(L, 2)) {
        const char *name = luaL_checkstring(L, 1);
        size_t sz;
        const char *code = luaL_checklstring(L, 2, &sz);

        lua_getfield(L, LUA_REGISTRYINDEX, "_LOADED");
        lua_getfield(L, -1, name);
        if (!lua_isnil(L, -1)) {
            return 1;
        }
        lua_pop(L, 2);
        if (luaL_loadbuffer(L, code, sz, name) != 0) {
            return luaL_error(L, "error loading module '%s' from string:\n\t%s", name, lua_tostring(L, -1));
        }
        lua_call(L, 0, 1);
        lua_getfield(L, LUA_REGISTRYINDEX, "_LOADED");
        lua_pushvalue(L, -2);
        lua_setfield(L, -2, name);
        lua_pop(L, 1);
        return 1;
    } else {
        lua_pushvalue(L, lua_upvalueindex(1));
        lua_insert(L, 1);
        lua_call(L, lua_gettop(L) - 1, LUA_MULTRET);
        return lua_gettop(L);
    }
}

/**
 * Call this function once when initializing a new Lua state to
 * replace the global 'require' with our custom one.
 */
void luaJ_overloadrequire(lua_State *L) {
    lua_getglobal(L, "require");
    if (lua_isfunction(L, -1)) {
        lua_pushcclosure(L, &luaJ_require, 1);
        lua_setglobal(L, "require");
    } else {
        lua_pop(L, 1);
    }
}

void luaJ_initproxycache(lua_State *L) {
    lua_newtable(L);
    lua_newtable(L);
    lua_pushstring(L, "k");
    lua_setfield(L, -2, "__mode");
    lua_setmetatable(L, -2);
    lua_setfield(L, LUA_REGISTRYINDEX, "__jproxy_cache");
}

int jua_cachedProxy(lua_State *L) {
    luaL_checktype(L, 1, LUA_TFUNCTION);
    lua_getfield(L, LUA_REGISTRYINDEX, "__jproxy_cache");
    lua_pushvalue(L, 1);
    lua_gettable(L, -2);
    if (!lua_isnil(L, -1)) {
        lua_remove(L, -2);
        return 1;
    }
    lua_pop(L, 1);
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    int result = env->CallStaticIntMethod(juaapi_class, juaapi_proxy, (jint) stateIndex);
    if (checkOrError(env, L, result) != 1) {
        return luaL_error(L, "Failed to create java proxy object");
    }
    lua_pushvalue(L, 1);
    lua_pushvalue(L, -2);
    lua_settable(L, -4);
    lua_remove(L, -2);
    return 1;
}

inline int jInvokeObject(lua_State * L, jmethodID methodID,
                         jobject data, const char * name, int params) {
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    jint ret;
    if (name == NULL) {
        ret = env->CallStaticIntMethod(juaapi_class, methodID,
                                       (jint) stateIndex, data, NULL, params);
    } else {
        jstring str = env->NewStringUTF(name);
        ret = env->CallStaticIntMethod(juaapi_class, methodID,
                                       (jint) stateIndex, data, str, params);
        env->DeleteLocalRef(str);
    }
    return checkOrError(env, L, ret);
}

// MODIFIED: This function now gets the object from an upvalue.
inline int jInvoke(lua_State * L, const char * reg, jmethodID methodID) {
    // The object is the first upvalue, the method name is the second.
    jobject * data = (jobject *) luaL_checkudata(L, lua_upvalueindex(1), reg);
    const char * name = lua_tostring(L, lua_upvalueindex(2));
    // With dot notation, all stack elements are parameters.
    return jInvokeObject(L, methodID, *data, name, lua_gettop(L));
}

// MODIFIED: This function now creates a closure with two upvalues: the object and the method name.
inline int jIndex(lua_State * L, const char * reg, jmethodID methodID, lua_CFunction func, bool ret) {
    jobject * data = (jobject *) luaL_checkudata(L, 1, reg);
    const char * name = luaL_checkstring(L, 2);
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    jstring str = env->NewStringUTF(name);
    jint retVal = env->CallStaticIntMethod(juaapi_class, methodID, (jint) stateIndex, *data, str);
    env->DeleteLocalRef(str);
    if (retVal == -1) {
        return checkOrError(env, L, -1);
    }
    if ((retVal & 0x1) != 0 && ret) {
        return 1;
    } else if ((retVal & 0x2) != 0 && ret) {
        // Push the object (self) and the method name as upvalues for the closure.
        lua_pushvalue(L, 1); // upvalue 1: the object
        lua_pushvalue(L, 2); // upvalue 2: the method name
        lua_pushcclosure(L, func, 2);
        return 1;
    } else {
        return 0;
    }
}

inline int jIndex(lua_State * L, const char * reg, jmethodID methodID, lua_CFunction func) {
    return jIndex(L, reg, methodID, func, true);
}

int jarrayInvoke(lua_State * L) {
    return jInvoke(L, JAVA_ARRAY_META_REGISTRY, juaapi_objectinvoke);
}

int jclassInvoke(lua_State * L) {
    return jInvoke(L, JAVA_CLASS_META_REGISTRY, juaapi_classinvoke);
}

int jclassIndex(lua_State * L) {
    return jIndex(L, JAVA_CLASS_META_REGISTRY, juaapi_classindex, &jclassInvoke);
}

int jclassCall(lua_State * L) {
    jobject * data = (jobject *) lua_touserdata(L, 1);
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    return checkOrError(env, L, env->CallStaticIntMethod(juaapi_class, juaapi_classnew,
                                                         (jint) stateIndex, *data, lua_gettop(L) - 1));
}

int jclassNewIndex(lua_State * L) {
    return jIndex(L, JAVA_CLASS_META_REGISTRY, juaapi_classnewindex, NULL, false);
}

int jobjectInvoke(lua_State * L) {
    return jInvoke(L, JAVA_OBJECT_META_REGISTRY, juaapi_objectinvoke);
}

int jobjectCall(lua_State * L) {
    return jInvoke(L, JAVA_OBJECT_META_REGISTRY, juaapi_objectinvoke);
}

static void checkJobject(lua_State * L, int n) {
    if (luaL_testudata(L, n, JAVA_CLASS_META_REGISTRY) != NULL ||
        luaL_testudata(L, n, JAVA_OBJECT_META_REGISTRY) != NULL ||
        luaL_testudata(L, n, JAVA_ARRAY_META_REGISTRY) != NULL) {
        return;
    }
    luaL_error(L, "bad argument #%d to jobjectEquals: %s, %s or %s expected",
               n, JAVA_CLASS_META_REGISTRY, JAVA_OBJECT_META_REGISTRY, JAVA_ARRAY_META_REGISTRY);
}

int jobjectEquals(lua_State * L) {
    checkJobject(L, 1);
    checkJobject(L, 2);
    jobject * obj1 = (jobject *) lua_touserdata(L, 1);
    jobject * obj2 = (jobject *) lua_touserdata(L, 2);
    JNIEnv * env = getJNIEnv(L);
    lua_pushboolean(L, env->IsSameObject(*obj1, *obj2));
    return 1;
}

int jfunctionWrapper(lua_State * L) {
    jobject * data = (jobject *) lua_touserdata(L, lua_upvalueindex(1));
    return jInvokeObject(L, juaapi_objectinvoke, *data, NULL, lua_gettop(L));
}

int jobjectIndex(lua_State * L) {
    return jIndex(L, JAVA_OBJECT_META_REGISTRY, juaapi_objectindex, &jobjectInvoke);
}

int jobjectNewIndex(lua_State * L) {
    return jIndex(L, JAVA_OBJECT_META_REGISTRY, juaapi_objectnewindex, NULL, false);
}

int jarrayLength(lua_State * L) {
    jobject * data = (jobject *) luaL_checkudata(L, 1, JAVA_ARRAY_META_REGISTRY);
    JNIEnv * env = getJNIEnv(L);
    int len = (int) env->CallStaticIntMethod(juaapi_class, juaapi_arraylen, *data);
    lua_pushinteger(L, len);
    return 1;
}

inline int jarrayJIndex(lua_State * L, jmethodID func, bool ret) {
    jobject * data = (jobject *) luaL_checkudata(L, 1, JAVA_ARRAY_META_REGISTRY);
    int i = (int) luaL_checknumber(L, 2);
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    int retVal = checkOrError(env, L,
                              env->CallStaticIntMethod(juaapi_class, func, (jint) stateIndex, *data, i));
    return ret ? retVal : 0;
}

int jarrayIndex(lua_State * L) {
    if (lua_isnumber(L, 2)) {
        return jarrayJIndex(L, juaapi_arrayindex, true);
    }
    if (lua_isstring(L, 2)) {
        return jIndex(L, JAVA_ARRAY_META_REGISTRY, juaapi_objectindex, &jarrayInvoke);
    }
    return luaL_error(L, "bad argument #2 to __index (expecting number or string)");
}

int jarrayNewIndex(lua_State * L) {
    return jarrayJIndex(L, juaapi_arraynewindex, false);
}

// c = jobject('methodName', 'signature') --> returns a closure
inline int jSigCall(lua_State * L, lua_CFunction func) {
    int top = lua_gettop(L);
    if (top == 3) {
        lua_pushcclosure(L, func, 3);
        return 1;
    } else if (top == 2) {
        lua_pushcclosure(L, func, 2);
        return 1;
    } else {
        return 0;
    }
}

// c = jobject('methodName', 'signature') --> returns a closure
// c(param1, param2) --> method call
inline int jSigInvoke(lua_State * L, const char * reg, jmethodID methodID) {
    jobject * data = (jobject *) luaL_checkudata(L, lua_upvalueindex(1), reg);
    const char * name = luaL_checkstring(L, lua_upvalueindex(2));
    const char * signature = luaL_optstring(L, lua_upvalueindex(3), NULL);

    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);

    jstring nameS = env->NewStringUTF(name);
    jstring signatureS = signature == NULL ? NULL : env->NewStringUTF(signature);
    int ret = env->CallStaticIntMethod(juaapi_class, methodID,
                                       (jint) stateIndex, *data, nameS, signatureS, lua_gettop(L));
    if (signature != NULL) {
        env->DeleteLocalRef(signatureS);
    }
    env->DeleteLocalRef(nameS);
    return checkOrError(env, L, ret);
}

int jclassSigInvoke(lua_State * L) {
    return jSigInvoke(L, JAVA_CLASS_META_REGISTRY, juaapi_classsiginvoke);
}

int jobjectSigInvoke(lua_State * L) {
    return jSigInvoke(L, JAVA_OBJECT_META_REGISTRY, juaapi_objsiginvoke);
}

int jclassSigCall(lua_State * L) {
    return jSigCall(L, &jclassSigInvoke);
}

int jobjectSigCall(lua_State * L) {
    return jSigCall(L, &jobjectSigInvoke);
}

// Calls juaapi_load and loads with an ExternalLoader
int jmoduleLoad(lua_State * L) {
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    const char * name = luaL_checkstring(L, 1);
    jstring moduleName = env->NewStringUTF(name);
    int ret = env->CallStaticIntMethod(juaapi_class, juaapi_load,
                                       (jint) stateIndex, moduleName);
    env->DeleteLocalRef(moduleName);
    return checkOrError(env, L, ret);
}

// Calls juaapi_loadmodule and loads a Java static method
int jloadModule(lua_State * L) {
    JNIEnv * env = getJNIEnv(L);
    int stateIndex = getStateIndex(L);
    const char * name = luaL_checkstring(L, 1);
    jstring moduleName = env->NewStringUTF(name);
    env->CallStaticIntMethod(juaapi_class, juaapi_loadmodule,
                             (jint) stateIndex, moduleName);
    env->DeleteLocalRef(moduleName);
    return checkOrError(env, L, 1);
}