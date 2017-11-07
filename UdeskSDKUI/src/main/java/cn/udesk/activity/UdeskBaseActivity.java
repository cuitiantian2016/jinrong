package cn.udesk.activity;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by AYM on 2017/1/18.
 */

public class UdeskBaseActivity extends Activity {

    @Override
    public void finish() {
        InputMethodManager imm = (InputMethodManager) getSystemService
                (Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0); //强制隐藏键盘
        super.finish();
    }
}
