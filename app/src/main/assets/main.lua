-- Lua 跑分脚本
-- 专注于比较不同实现方式的效率，而不仅仅是系统速度。
-- (兼容 LuaJIT, Lua 5.1-5.3)
local TIMES = 1000
local DEBUG = true -- 启用则输出报错信息
local MERGE = false -- 启用则将所有输出合并

-- 兼容函数
local load = load or loadstring
local luajava = luajava or luakt or java

local function getAppName()
  if luajava then
    local packageManager = activity.getPackageManager()
    local appInfo = packageManager.getApplicationInfo(activity.getPackageName(), packageManager.GET_META_DATA)
    return packageManager.getApplicationLabel(appInfo)
  else
    return _VERSION
  end
end

local output = ""
local _print = print
if MERGE then
  print = function(...)
    local args = {...}
    if #args == 0 then
      output = output .. "\n\n"
    else
      local msg = "\n" .. args[1]
      for i = 2, #args do
        msg = msg .. "\n" .. args[i]
      end
      output = output .. msg
    end
  end
end

printAll = function()
  if MERGE then
    _print(output)
  end
end

local welcomeMessage = "Hello, I'm " .. getAppName()
print(welcomeMessage)
print(welcomeMessage)

-- 性能测试函数
local function handle_error(e)
  if DEBUG then
    return "跑分失败: " .. tostring(e)
  else
    return "0 分"
  end
end

local total = 0
local function test(name, func, times)
  local score = 0
  local message = nil
  times = times or TIMES
  xpcall(function()
    collectgarbage("collect")
    local startTime = os.clock()
    -- if not func() then return end
    func()
    duration = os.clock() - startTime
    score = duration > 0 and (times / duration) or 0
    message = tostring(score) .. " 分"
  end, function(e)
    message = handle_error(e)
  end)
  print("项目: " .. name .. ", " .. tostring(message))
  total = total + score
  return score
end

local function test_chunk(name, code)
  local chunk, err = load(code)
  if chunk then
    test(name ,chunk)
  else
    print("项目: " .. name .. ", " .. handle_error(e))
  end
end

-- Lua for 循环性能测试
local function test_for()
  test("for 循环", function()
    local sum = 0
    for i = 1, TIMES do
      sum = sum + i
    end
  end)
  test("创建数组", function()
    local t = {}
    for i = 1, TIMES do
      table.insert(t, i)
    end
  end)
  test("for 循环嵌套", function()
    local sum = 0
    for i = 1, TIMES do
      for j = 1, TIMES do
        sum = sum + i * j
      end
    end
  end, TIMES*TIMES)
  local kv_table = {}
  test("for 创建 k-v 数组", function()
    for i = 1, TIMES do
      for j = 1, TIMES do
        local key = tostring(i) .. "_" .. tostring(j)
        kv_table[key] = j
      end
    end
  end, TIMES*TIMES)
  test("for pairs", function()
    local sum = 0
    for i, v in pairs(kv_table) do
      sum = sum + v
    end
  end, TIMES*TIMES)
end

-- Lua while 性能测试
local function test_while()
  test("while 循环", function()
    local i = 1
    while i <= TIMES do
      i = i + 1
    end
  end)
end

-- Lua repeat 循环性能测试
local function test_repeat()
  test("repeat 循环", function()
    local i = 1
    repeat
      i = i + 1
    until i > TIMES
  end)
end

-- Lua goto 性能测试
local function test_goto()
  test_chunk("goto 循环", [[
    local n = 1000
    local i = 1
    ::loop::
    if i <= n then
      i = i + 1
      goto loop
    end
    return true
  ]])
end




-- ===================================================================
-- 2. Lua 自带库使用性能对比
-- ===================================================================

local function test_luajava()
  local luajava = luajava or java or require("luajava")
  if not luajava then
    print("未找到 luajava 库，跳过测试")
    return
  end
  print("正在测试 luajava 库")
  local ArrayList = nil
  local Runnable = nil
  test("luajava.bindClass", function()
    for i = 1, TIMES do
      ArrayList = luajava.bindClass("java.util.ArrayList")
      Runnable = luajava.bindClass("java.lang.Runnable")
    end
  end)
  local array = nil
  test("luajava.new", function()
    for i = 1, TIMES do
      array = luajava.new(ArrayList)
    end
  end)
  test("luajava.newInstance", function()
    for i = 1, TIMES do
      array = luajava.newInstance("java.util.ArrayList")
    end
  end)
  test("luajava 方法调用", function()
    for i = 1, TIMES do
      array.add(i)
    end
  end)
  test("luajava.createProxy", function()
    local runnable = nil
    for i = 1, TIMES do
      runnable = luajava.createProxy("java.lang.Runnable", { run = function() end })
      runnable.run()
    end
  end)
end

test_for()
test_while()
test_repeat()
test_goto()
test_luajava()
print("总分: " .. tostring(total))
printAll()
