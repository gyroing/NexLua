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
        for (int i = 1; i <= L.getTop(); i++) {
            String val = L.toString(i);
            output.append("\t");
            output.append(val);
            output.append("\t");
        }
        Toast.makeText(mLuaContext.getContext(), output.toString().substring(1, output.length() - 1), Toast.LENGTH_SHORT).show();
        output.setLength(0);
        return 0;
    }
}

