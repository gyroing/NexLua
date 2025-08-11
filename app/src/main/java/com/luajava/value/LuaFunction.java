package com.luajava.value;

import com.luajava.Lua;

/**
 * Interface for functions implemented in Java.
 */
public interface LuaFunction {
    /**
     * Implements the function body
     *
     * <p>
     * Unlike {@link com.luajava.JFunction#__call(Lua)}, before actually calling this function,
     * the library converts all the arguments to {@link LuaValue LuaValues} and pops them off the stack.
     * </p>
     *
     * @param L    the Lua state
     * @param args the arguments
     * @return the return values (nullable)
     */
    LuaValue[] call(Lua L, LuaValue[] args);
}
