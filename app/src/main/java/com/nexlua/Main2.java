package com.nexlua;

import com.luajava.Lua;
import com.luajava.value.LuaFunction;
import com.luajava.value.LuaValue;

public class Main2 implements LuaFunction {
    @Override
    public LuaValue[] call(Lua L, LuaValue[] args) {
        L.load("function main(code)\n" +
                "    if code ~= nil then\n" +
                "        load(code)()\n" +
                "    end\n" +
                "end");
        return new LuaValue[0];
    }
}