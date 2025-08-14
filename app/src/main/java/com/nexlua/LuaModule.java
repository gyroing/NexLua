package com.nexlua;

import com.luajava.value.LuaValue;

public interface LuaModule {
    LuaValue[] run(LuaContext luaContext, LuaValue...args);
}
