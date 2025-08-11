local Toast = luajava.bindClass("android.widget.Toast")
local Button = luajava.bindClass("android.widget.Button")
local btn = Button(activity)

btn.setText("Click me")
activity.setContentView(btn)
btn.setOnClickListener(function()
 Toast.makeText(activity, "Hello World!", Toast.LENGTH_SHORT).show()
end)