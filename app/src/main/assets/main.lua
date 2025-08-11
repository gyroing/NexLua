local Toast = java.import("android.widget.Toast")
local Button = java.import("android.widget.Button")
local btn = Button(activity)

btn.setText("Click me")
activity.setContentView(btn)
btn.setOnClickListener(function()
 Toast.makeText(activity, "Hello World!", Toast.LENGTH_SHORT).show()
end)