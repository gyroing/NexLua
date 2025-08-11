local Button = java.import('android.widget.Button')
local btn = Button(activity)
btn:setText("Hello, Android, I'm LuaJit")
activity:setContentView(btn)