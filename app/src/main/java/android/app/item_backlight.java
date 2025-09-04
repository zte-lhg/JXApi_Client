package android.app;

import android.annotation.SuppressLint;
import android.app.util.AIDLUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class item_backlight extends Activity implements View.OnClickListener {
    private static final String TAG = "item_backlight";
    private SeekBar seekbar_backlight;
    private TextView tv_brightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_backlight);
        this.seekbar_backlight = findViewById(R.id.seekbar_backlight);

        Button btn_openBacklight = findViewById(R.id.btn_openBacklight);
        Button btn_closeBacklight = findViewById(R.id.btn_closeBacklight);
        btn_openBacklight.setOnClickListener(this);
        btn_closeBacklight.setOnClickListener(this);

        tv_brightness = findViewById(R.id.tv_progress);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 延迟执行的操作
                try {
                   int progress = AIDLUtil.getInstance().ax_getBacklight();
                    Log.i(TAG, "progress="+progress);
                    seekbar_backlight.setProgress(progress);
                    tv_brightness.setText(String.valueOf(progress));

                } catch (Exception e) {
                    Log.i(TAG, "获取背光失败");
                }
            }
        }, 400); // 延迟

        seekbar_backlight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.seekbar_backlight) {
                    try {
                        Log.i(TAG, "设置背光s");
                        AIDLUtil.getInstance().ax_setBacklight(progress * 2);

                        Log.i(TAG, "pic.setBacklight()=" + progress);
                        TextView textView = tv_brightness;
                        textView.setText("" + progress);
                    } catch (Exception e) {
                        Log.i(TAG, "获取背光失败");
                    } finally {
                        System.out.println("无论是否发生异常，finally 块中的代码都会执行。");
                    }
                    return;
                }
                return;
            }
        });
    }
    
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_closeBacklight:
                try {
                    Log.i(TAG, "关闭背光");
                    AIDLUtil.getInstance().ax_EnableBacklight(false);
                } catch (Exception e) {
                    Log.i(TAG, "AIDLUtil 关闭背光失败");
                }
                break;
            case  R.id.btn_openBacklight:
                try {
                    Log.i(TAG, "打开背光");
                    AIDLUtil.getInstance().ax_EnableBacklight(true);
                } catch (Exception e) {
                    Log.i(TAG, "AIDLUtil 打开背光失败");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Destroy!");
        // 解绑服务
        AIDLUtil.getInstance().unbindService(item_backlight.this);
    }
}