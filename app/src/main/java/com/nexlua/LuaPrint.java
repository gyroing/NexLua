package com.nexlua;

import com.luajava.Lua;
import com.luajava.JFunction;
import com.luajava.LuaException;
import com.nexlua.LuaContext;

public class LuaPrint implements JFunction {
    private final LuaContext mLuaContext;
    private final StringBuilder output = new StringBuilder();
    public LuaPrint(LuaContext luaContext) {
        mLuaContext = luaContext;
    }
    @Override
    public int __call(Lua L) throws LuaException {
        if (L.getTop() < 2) {
            mLuaContext.sendMsg("");
            return 0;
        }
        for (int i = 2; i <= L.getTop(); i++) {
            String val = L.toString(i);
            output.append("\t");
            output.append(val);
            output.append("\t");
        }
        mLuaContext.sendMsg(output.toString().substring(1, output.length() - 1));
        output.setLength(0);
        return 0;
    }
}

