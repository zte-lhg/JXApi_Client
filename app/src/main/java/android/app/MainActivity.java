package android.app;

import android.annotation.SuppressLint;
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

//import com.app.sdk_api_service.IAidlInterface;
//import com.hisilicon.android.tvapi.HitvManager;

import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;

/*
 * 本项目为 晶心安卓单板系统 API 测试参考调用方法。
 * 安装方法 ：adb install -t ./app/build/outputs/apk/debug/JXApiTest.apk
 * 启动方法 ：adb shell am start -n android.app/.MainActivity
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private String TAG = "Sdk_Client";
    private ListView listView;
    private String[] data = {"背光"};
    // 端口
    private String port;//串口号
    private int baud;//波特率
    private int check;//校验位
    private int databyte;//数据位
    private int stop;//停止位

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定服务
        AIDLUtil.getInstance().bindService(MainActivity.this);


        //串口初始化
        //需要延时一下，绑定api服务需要时间
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 延迟执行的操作
                try {
                    //初始化使能串口ttyAMA1
                    init_ttyAMA1();
                } catch (Exception e) {
                    Log.i(TAG, "串口使能失败");
                    e.printStackTrace();
                }
            }
        }, 50); // 50ms 延迟

        //界面初始化
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, data);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        initClick();

        //打开串口
        openSerial();
    }

    public void openSerial() {
        try {
            getSerialPortSetting();
            SerialPortUtil.openSerialPort(getApplicationContext(), port, baud, check, databyte, stop);
        } catch (Exception e) {
            e.printStackTrace();
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
                    databyte = 8;
                    break;
                case 4:
                    stop = 1;
                    break;
            }
        }
    }

    private void init_ttyAMA1() {
        //false 为使能ttyAMA1
        Log.e(TAG, "使能串口ttyAMA1");
        try {
            AIDLUtil.getInstance().ax_EnableUart(false);
        } catch (Exception e) {
            Log.e(TAG, "使能串口失败");
            e.printStackTrace();
            finish();//结束app
        } finally {
            System.out.println("autoservice  无论是否发生异常，finally 块中的代码都会执行。");
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
//                    case 1:
//                        //am start -n com.hisilicon.tv.menu/com.hisilicon.tv.menu.app.TvMenuActivity
//                        Intent intent2 = new Intent();
//                        intent2.setClassName("com.hisilicon.tv.menu", "com.hisilicon.tv.menu.app.TvMenuActivity");
//                        startActivity(intent2);
//                        Log.e(TAG, "FactoryMenu start!");
//                        break;
                    default:
                        break;
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    void use1() {

        //1,通过firefly_service 服务在系统中进行数据交互
        // 参考\frameworks\base\core\java\android\app\FirlyFlyManager.java文件
        //使用例子
        // mFireflyManage = (FireflyManager) getSystemService("firefly_service");
        //关机
        // mFireflyManage.shutdown(false);
        //重启
        //mFireflyManage.reboot(false,null);
        //休眠
        // mFireflyManage.sleep();
        //亮度 不可用
        //mFireflyManage.setBrightness(66);
        //getAndroidDisplay
        //mFireflyManage.getAndroidDisplay()
        //等等

    }

    void use2() {
        //2,参考 com.app.sdk_api_service.IAidlInterface 中的函数使用
        //注意必须把生成的apk放到源码中编译才可以调用海思api
        // initView();
        //绑定服务
        //  AIDLUtil.getInstance().bindService(MainActivity.this);
        //使用例子
        //参考按键功能使用

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.btn_getAndroidDisplay:
//                //getAndroidDisplay
//
//                String androiddisplay = mFireflyManage.getAndroidDisplay();
//                if (!TextUtils.isEmpty(androiddisplay)) {
//                    mResultTv.append("getAndroidDisplay" + "\n");
//                    mResultTv.append(androiddisplay);
//                    mResultTv.append("\n");
//                }
//
//                break;

//            case R.id.btn_sendTest:
//                String request = AIDLUtil.getInstance().request("char", "");
//                if (!TextUtils.isEmpty(request)) {
//                    mResultTv.append("收到返回的测试数据" + "\n");
//                    mResultTv.append(request);
//                    mResultTv.append("\n");
//                }
//                break;
//            case R.id.btn_getbacklight:
//                int backlight = AIDLUtil.getInstance().ax_getBacklight();
//                mResultTv.append("收到返回的背光亮度" + "\n");
//                mResultTv.append(String.valueOf(backlight));
//                mResultTv.append("\n");
//                break;
//            case R.id.btn_setbacklight:
//                AIDLUtil.getInstance().ax_setBacklight(10);
//                mResultTv.append("设置背光为10" + "\n");
//                mResultTv.append("\n");
//                break;

//            case R.id.btn_getCurSourceID:
//                int id = AIDLUtil.getInstance().ax_getCurSourceId(0);
//                mResultTv.append("收到返回的SourceID" + "\n");
//                mResultTv.append(String.valueOf(id));
//                mResultTv.append("\n");
//                break;

//            case R.id.btn_setBootSourceID:
//                //  int sourcelist[]= AIDLUtil.getInstance().ax_getAvailSourceList();
//                //   for(int i=0;i<sourcelist.length;i++) Log.i(TAG, "sourcelist: "+sourcelist[i]);
//
//                //                String holder=AIDLUtil.getInstance().ax_getSourceHolder();
//                //                mResultTv.append("收到返回的holder"+"\n");
//                //                mResultTv.append(holder);
//                //                mResultTv.append("\n");
//
//                //设置输入源
//                AIDLUtil.getInstance().ax_setBootSource(9);//参考下面被注释掉的输入源
//                int bootsource = AIDLUtil.getInstance().ax_getBootSource();
//                mResultTv.append("收到返回的bootsource" + "\n");
//                mResultTv.append(String.valueOf(bootsource));
//                mResultTv.append("\n");
//
//                //切换输入源
//                Intent intent = new Intent();
//                intent.setAction("com.hisilicon.action.PLAY_ATV");
//                intent.putExtra("SourceName", 9);
//                intent.setClassName("com.hisilicon.tvui", "com.hisilicon.tvui.MainActivity");
//                startActivity(intent);
//
//
//                break;


//            case R.id.btn_getSelectSource:
//                //获取输入源
//                int selectID=AIDLUtil.getInstance().ax_getSelectSourceId();
//                mResultTv.append("收到返回的selectID" + "\n");
//                mResultTv.append(String.valueOf(selectID));
//                mResultTv.append("\n");
//                break;

//            case  R.id.btn_factory:
//                //am start -n com.hisilicon.tv.menu/com.hisilicon.tv.menu.app.TvMenuActivity
//
//                Intent intent2 = new Intent();
//                intent2.setClassName("com.hisilicon.tv.menu", "com.hisilicon.tv.menu.app.TvMenuActivity");
//                startActivity(intent2);
//
//
//
//                break;

        }
    }

//    /*
//        public static final int SOURCE_ATV = 0;
//        public static final int SOURCE_DVBC = 1;
//        public static final int SOURCE_DTMB = 2;
//        public static final int SOURCE_CVBS1 = 3;
//        public static final int SOURCE_CVBS2 = 4;
//        public static final int SOURCE_CVBS3 = 5;
//        public static final int SOURCE_VGA = 6;
//        public static final int SOURCE_YPBPR1 = 7;
//        public static final int SOURCE_YPBPR2 = 8;
//        public static final int SOURCE_HDMI1 = 9;
//        public static final int SOURCE_HDMI2 = 10;
//        public static final int SOURCE_HDMI3 = 11;
//        public static final int SOURCE_HDMI4 = 12;
//        public static final int SOURCE_MEDIA = 13;
//        public static final int SOURCE_MEDIA2 = 14;
//        public static final int SOURCE_SCART1 = 15;
//        public static final int SOURCE_SCART2 = 16;
//        public static final int SOURCE_AUTO = 17;
//        public static final int SOURCE_DVBT = 18;
//        public static final int SOURCE_ATSC = 19;
//        public static final int SOURCE_DVBS = 20;
//        public static final int SOURCE_ISDBT = 21;
//        public static final int SOURCE_BUTT = 22;
//    */


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Destroy!");
        //解绑服务
        AIDLUtil.getInstance().unbindService(MainActivity.this);
    }
}
