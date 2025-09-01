package android.app.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUtil {

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static boolean flag = false;
    public static int[] values;
    public static String character;

    private static String TAG = "SerialPortUtil";

    /**
     * 打开串口的方法
     */
    public static void openSerialPort(Context context, String port, int baudRate, int parity, int dataBits, int stopBits) throws IOException {
        Log.i(TAG, "打开串口");
        File file = new File("/dev/" + port);
        serialPort = new SerialPort(file, baudRate, parity, dataBits, stopBits);
        //获取打开的串口0中的输入输出流，以便于串口数据的收发
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
        flag = true;
        // receiveSerialPort(context);
    }

    /**
     * 关闭串口的方法
     * 关闭串口中的输入输出流
     * 关闭串口
     */
    public static void closeSerialPort() {
        Log.i("test", "关闭串口");
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            flag = false;
            serialPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送十六进制串口数据的方法
     *
     * @param data 要发送的数据
     */
    public static void sendHexSerialPort(int[] data) {
        Log.d("test", "发送串口数据");
        try {
            //将16进制的int类型的数组转换为byte数组
            byte[] buf = AryChangeManager.hexToByte(data);
            //  if(buf[0]==(byte)0xfe)  MainActivity.refreshReceive("串口发送数据正确");
            //将数据写入串口
            outputStream.write(buf);
            outputStream.flush();
            for(int i=0;i<data.length;i++)
                Log.i(TAG, String.valueOf(data[i]));

            Log.i("test", "串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test", "串口数据发送失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送串口数据的方法
     *
     * @param data 要发送的数据
     *             这里没有开线程，
     */
    public static void sendSerialPort(byte[] data) {
        // Log.i("test", "发送串口数据");
        try {
            //if(data[0]==(byte)0xfe) MainActivity.refreshReceive("串口发送数据正确");
            outputStream.write(data);
            outputStream.flush();
            Log.i(TAG, "串口数据发送成功:");

            String out=AryChangeManager.bytesToHexString(data);
            Log.i(TAG, out);
            // for(int i=0;i<data.length;i++)
            //    Log.i(TAG, String.valueOf(data[i]));

        } catch (IOException e) {
            e.printStackTrace();
            Log.i( TAG, "串口数据发送失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * bytes转换成十六进制字符串
     * @param  b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    public static String byteArrayToHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


    /**
     * 发送文本串口数据的方法
     *
     * @param data 要发送的数据
     */
    public static void sendTextSerialPort(String data) {
        Log.i("test", "发送串口数据");
        try {
            //将16进制的int类型的数组转换为byte数组
//            byte[] buf = AryChangeManager.hexToByte(data);
            //将数据写入串口
            outputStream.write(data.getBytes());
            outputStream.flush();
            Log.i("test", "串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test", "串口数据发送失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receiveSerialPort(final Context context) {
        Log.i(TAG, "接收串口数据 ");
        final Handler handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                StringBuilder buffer = new StringBuilder();
                for (int i : values) {
                    buffer.append(AryChangeManager.dexToHex(i));
                    buffer.append(" ");
                }

                Log.i(TAG, "buffer: " + buffer);

                switch (msg.what) {
                    case 10:
                        // mainActivity. refreshReceive("调节MIC音效声场");
                        break;
                    case 11:
                        break;
                    case 12:
                        break;
                    case 13:
                        break;

                    case 20:
                        break;
                    case 21:
                        break;
                    case 22:
                        break;
                    case 23:
                        break;

                    case 30:


                        //  mMICSettingActivity.handleUartMessage(30);
                        break;

                    case 31:
                        break;


                    default:
                        break;
                }
            }
        };
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (flag) {       //while (!isInterrupted()) {
                    try {
                        if (inputStream == null) {
                            return;
                        }
                        byte[] readData = new byte[1024 * 4];
                        if (inputStream.available() > 0 && flag) {
                            // handler.sendEmptyMessage(101);
                            SystemClock.sleep(200);
                            int size = inputStream.read(readData);

                            if ((readData[0] == (byte) 0x7E) && (readData[size - 1] == (byte) 0x7F) && size > 0) {

//                                Intent bootServiceIntent = new Intent(context.getApplicationContext(), MainActivity.class);
//                                bootServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.getApplicationContext().startActivity(bootServiceIntent);
                                //接收数据
                                values = new int[size];

                                for (int i = 0; i < values.length; i++) {
                                    values[i] |= (readData[i] & 0x000000ff);


                                    StringBuilder buffer = new StringBuilder();
                                    for (int j : values) {
                                        buffer.append(AryChangeManager.dexToHex(j));
                                        buffer.append(" ");
                                    }
                                    Log.i(TAG, "buffer: " + buffer);


                                    switch (readData[1]) {
                                        case 0x01:
                                            if ((readData[2] == 0)) handler.sendEmptyMessage(10);
                                            else if ((readData[2] == 1))
                                                handler.sendEmptyMessage(11);
                                            else if ((readData[2] == 2))
                                                handler.sendEmptyMessage(12);
                                            else if ((readData[2] == 3))
                                                handler.sendEmptyMessage(13);
                                            else if ((readData[2] == 4))
                                                handler.sendEmptyMessage(14);
                                            break;
                                        case 0x02:
                                            if ((readData[2] == 0)) handler.sendEmptyMessage(20);
                                            else if ((readData[2] == 1))
                                                handler.sendEmptyMessage(21);
                                            else if ((readData[2] == 2))
                                                handler.sendEmptyMessage(22);
                                            else if ((readData[2] == 3))
                                                handler.sendEmptyMessage(23);
                                            else if ((readData[2] == 4))
                                                handler.sendEmptyMessage(24);
                                            break;
                                        case 0x03:
                                            if ((readData[2] == 0)) handler.sendEmptyMessage(30);
                                            else if ((readData[2] == 1))
                                                handler.sendEmptyMessage(31);
                                            break;

                                        default:
                                            break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }


    public static void receiveSerialPort2(Handler handler) {
        Log.i(TAG, "接收串口数据 ");

        Message msg = new Message();
        Bundle bundle = new Bundle();
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (flag) {       //while (!isInterrupted()) {
                    try {
                        if (inputStream == null) {
                            return;
                        }
                        byte[] readData = new byte[1024 * 4];
                        if (inputStream.available() > 0 && flag) {
                            // handler.sendEmptyMessage(101);
                            SystemClock.sleep(200);
                            int size = inputStream.read(readData);

                            if ((readData[0] == (byte) 0x7E) && (readData[size - 1] == (byte) 0x7F) && size > 0) {

//                                Intent bootServiceIntent = new Intent(context.getApplicationContext(), MainActivity.class);
//                                bootServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.getApplicationContext().startActivity(bootServiceIntent);
                                //接收数据
                                values = new int[size];

                                for (int i = 0; i < values.length; i++) {
                                    values[i] |= (readData[i] & 0x000000ff);
                                }

                                StringBuilder buffer = new StringBuilder();
                                for (int j : values) {
                                    buffer.append(AryChangeManager.dexToHex(j));
                                    buffer.append(" ");
                                }
                                Log.i(TAG, "buffer: " + buffer);
                                switch (readData[1]) {
                                    case 0x01:
                                        if ((readData[2] == 0)) handler.sendEmptyMessage(10);
                                        else if ((readData[2] == 1)) handler.sendEmptyMessage(11);
                                        else if ((readData[2] == 2)) handler.sendEmptyMessage(12);
                                        else if ((readData[2] == 3)) handler.sendEmptyMessage(13);
                                        else if ((readData[2] == 4)) handler.sendEmptyMessage(14);
                                        break;
                                    case 0x02:
                                        if ((readData[2] == 0)) handler.sendEmptyMessage(20);
                                        else if ((readData[2] == 1)) handler.sendEmptyMessage(21);
                                        else if ((readData[2] == 2)) handler.sendEmptyMessage(22);
                                        else if ((readData[2] == 3)) handler.sendEmptyMessage(23);
                                        else if ((readData[2] == 4)) handler.sendEmptyMessage(24);
                                        break;
                                    case 0x03:
                                        if ((readData[2] == 0))
                                            handler.sendEmptyMessage(30);
                                        else if ((readData[2] == 1))
                                            handler.sendEmptyMessage(31);
                                        break;
                                    case 0x04:
                                        if ((readData[2] == 0))
                                            handler.sendEmptyMessage(40);
                                        else if ((readData[2] == 1))
                                            handler.sendEmptyMessage(41);
                                        break;
                                    case 0x05:
                                        if ((readData[2] == 0))
                                            handler.sendEmptyMessage(50);
                                        else if ((readData[2] == 1))
                                            handler.sendEmptyMessage(51);
                                        break;
                                    case 0x06:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 60;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 60;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x07:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 70;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 70;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x08:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 80;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 80;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x09:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 90;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 90;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x0a:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 100;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 100;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x0b:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 110;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 110;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x0c:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 120;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 120;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    case 0x0d:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 130;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 130;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    //触摸反馈
                                    case 0x0f:
                                        if (handler.obtainMessage(msg.what) != null) {
                                            Message msg = handler.obtainMessage();
                                            msg.what = 150;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            // Log.i(TAG, "value: "+String.valueOf(readData[2]));
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        } else {
                                            msg.what = 150;
                                            bundle.putString("value", String.valueOf(readData[2]));  //往Bundle中存放数据
                                            msg.setData(bundle);//mes利用Bundle传递数据
                                            handler.sendMessage(msg);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }
}
