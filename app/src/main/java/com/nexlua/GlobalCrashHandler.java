package com.nexlua;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

public class GlobalCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "GlobalCrashHandler";
    private final Context mContext;

    public GlobalCrashHandler(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "FATAL EXCEPTION, launching CrashActivity.", ex);

        // 启动 CrashActivity
        launchCrashActivity(ex);

        // 结束当前进程
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    private void launchCrashActivity(Throwable ex) {
        Intent intent = new Intent(mContext, CrashActivity.class);
        // 关键：必须设置 FLAG_ACTIVITY_NEW_TASK，因为我们是在一个非 Activity 的上下文 (Application) 中启动 Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 将完整的堆栈跟踪信息作为字符串传递给 Activity
        intent.putExtra(CrashActivity.EXTRA_ERROR_STACK_TRACE, getStackTraceAsString(ex));
        mContext.startActivity(intent);
    }

    private String getStackTraceAsString(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
