package com.nexlua;

import com.luajava.value.LuaValue;

public class Main2 implements LuaModule {
    public LuaValue[] run(LuaContext luaContext, LuaValue... args) {
        LuaActivity activity = (LuaActivity) luaContext;
        activity.doString("print('Hello from Java')" +
                "function main(code)\n" +
                "    if code ~= nil then\n" +
                "        load(code)()\n" +
                "    end\n" +
                "end", getClass().getSimpleName());
        return new LuaValue[0];
    }
}