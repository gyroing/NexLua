package com.nexlua;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CrashActivity extends Activity {
    public static final String EXTRA_ERROR_STACK_TRACE = "extra_error_stack_trace";
    private static final int MENU_ID_COPY = 1;
    private static final int MENU_ID_SHARE = 2;
    private String errorStackTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 获取错误信息
        errorStackTrace = getIntent().getStringExtra(EXTRA_ERROR_STACK_TRACE);
        if (TextUtils.isEmpty(errorStackTrace)) {
            errorStackTrace = "No error information provided.";
        }

        // 2. 设置 ActionBar
        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setTitle("程序崩溃啦😱");
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }

        // 3. 创建视图层级
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        TextView errorTextView = new TextView(this);
        errorTextView.setText(errorStackTrace);
        errorTextView.setTextColor(Color.rgb(51, 51, 51));
        errorTextView.setTextSize(12.0f); // 固定字体大小
        errorTextView.setTypeface(Typeface.MONOSPACE);
        errorTextView.setPadding(16, 16, 16, 16);
        errorTextView.setTextIsSelectable(true);

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.addView(errorTextView);

        // --- 关键：使用我们自包含的内部类来解决滚动冲突 ---
        ScrollView verticalScrollView = new TouchCoordinatingScrollView(this);
        verticalScrollView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        verticalScrollView.addView(horizontalScrollView);

        mainLayout.addView(verticalScrollView);
        setContentView(mainLayout);
    }

    // =========================================================================
    // ==               自包含的、用于解决滚动冲突的内部类                     ==
    // =========================================================================
    private static class TouchCoordinatingScrollView extends ScrollView {
        private final int mTouchSlop;
        private float mInitialX;
        private float mInitialY;

        public TouchCoordinatingScrollView(Context context) {
            this(context, null);
        }

        public TouchCoordinatingScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            final int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mInitialX = ev.getX();
                    mInitialY = ev.getY();
                    // 在按下时，从不拦截，让子视图有机会处理
                    super.onInterceptTouchEvent(ev);
                    return false;

                case MotionEvent.ACTION_MOVE:
                    final float x = ev.getX();
                    final float y = ev.getY();
                    final float yDiff = Math.abs(y - mInitialY);
                    final float xDiff = Math.abs(x - mInitialX);

                    // 如果Y轴的移动距离明显大于X轴，就拦截事件进行垂直滚动
                    if (yDiff > mTouchSlop && yDiff > xDiff) {
                        return true;
                    }
                    break;
            }
            // 其他情况遵循默认行为（通常是不拦截，让子视图滚动）
            return super.onInterceptTouchEvent(ev);
        }
    }

    // =========================================================================
    // ==                         菜单相关的代码（无变动）                       ==
    // =========================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem copyItem = menu.add(Menu.NONE, MENU_ID_COPY, 0, "复制");
        copyItem.setIcon(android.R.drawable.ic_menu_edit);
        copyItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem shareItem = menu.add(Menu.NONE, MENU_ID_SHARE, 1, "分享");
        shareItem.setIcon(android.R.drawable.ic_menu_share);
        shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case MENU_ID_COPY:
                copyTextToClipboard();
                return true;
            case MENU_ID_SHARE:
                shareText();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void copyTextToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Crash Log", errorStackTrace);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "错误信息已复制", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareText() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, errorStackTrace);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "分享错误日志"));
    }
}