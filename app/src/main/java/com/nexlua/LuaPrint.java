package com.nexlua;

import android.content.Context;
import android.widget.Toast;

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
        int top=L.getTop();
        if (top>0) {
            output.append(L.toString(1));
            for (int i = 2; i <= top; i++) {
                output.append("\t");
                output.append(L.toString(i));
            }
            mLuaContext.sendMessage(output.toString());
            output.setLength(0);
        } else {
            mLuaContext.sendMessage("");
        }
        return 0;
    }
}

