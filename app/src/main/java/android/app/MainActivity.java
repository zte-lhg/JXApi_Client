package android.app;

import android.app.util.AIDLUtil;
import android.app.util.SerialPortUtil;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

/*
 * 本项目为 晶心安卓单板系统 API 测试参考调用方法。
 * 安装方法 ：adb install -t ./app/build/outputs/apk/debug/JXApiTest.apk
 * 启动方法 ：adb shell am start -n android.app/.MainActivity
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private final String TAG = "JXAPIClient";
    private ListView listView;
    private final String[] data = {"背光"};
    // 端口
    private String port;     // 串口号
    private int baud;        // 波特率
    private int check;       // 校验位
    private int datatable;   // 数据位
    private int stop;        // 停止位

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AIDLUtil.getInstance().bindService(MainActivity.this);

        // 串口初始化
        // 需要延时一下，绑定 api 服务需要时间
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    init_ttyAMA1();
                } catch (Exception e) {
                    Log.i(TAG, "串口使能失败");
                }
            }
        }, 50); // 50ms 延迟

        // 界面初始化
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, data);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        initClick();

        // 打开串口
        openSerial();
    }

    public void openSerial() {
        try {
            getSerialPortSetting();
            SerialPortUtil.openSerialPort(getApplicationContext(), port, baud, check, datatable, stop);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void getSerialPortSetting() {
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    port = "ttyAMA1";
                    break;
                case 1:
                    baud = 115200;
                    break;
                case 2:
                    check = 0;
                    break;
                case 3:
                    datatable = 8;
                    break;
                case 4:
                    stop = 1;
                    break;
            }
        }
    }

    private void init_ttyAMA1() {
        Log.e(TAG, "使能串口ttyAMA1");
        try {
            AIDLUtil.getInstance().ax_EnableUart(false);
        } catch (Exception e) {
            Log.e(TAG, "使能串口失败");
            finish();
        } finally {
            System.out.println("auto service  无论是否发生异常，finally 块中的代码都会执行。");
        }
    }

    private void initClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent_item_backlight = new Intent(MainActivity.this, item_backlight.class);
                        startActivity(intent_item_backlight);
                        Log.e(TAG, "item_backlight start!");
                        break;
                    case 1:
                        Intent intent2 = new Intent();
                        intent2.setClassName(
                                "com.hisilicon.tv.menu",
                                "com.hisilicon.tv.menu.app.TvMenuActivity"
                        );
                        startActivity(intent2);
                        Log.e(TAG, "factoryMenu start!");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Destroy!");
        //解绑服务
        AIDLUtil.getInstance().unbindService(MainActivity.this);
    }
}
