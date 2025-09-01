package android.app.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.app.sdk_api_service.IAidlInterface;

import androidx.annotation.NonNull;


public class AIDLUtil {
    private static AIDLUtil aidlUtil;
    private IAidlInterface iService;

    private AIDLUtil() {
    }

    public static AIDLUtil getInstance() {
        if (null == aidlUtil) {
            synchronized (AIDLUtil.class) {
                if (null == aidlUtil) {
                    aidlUtil = new AIDLUtil();
                }
            }
        }
        return aidlUtil;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("LEO", "绑定成功");
            iService = IAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService = null;
        }
    };

    public void bindService(Context context) {
        Intent intent = new Intent("com.app.sdk_api_service");
       intent.setPackage("com.app.sdk_api_service");
       // Intent intent = new Intent("com.leo.aidl");
       // intent.setPackage("com.leo.aidl");

        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (null == iService) {
            return;
        }
        Log.i("LEO", "取消绑定");
        context.unbindService(connection);
        iService = null;
    }

    /*
    public String request(@NonNull String func, @NonNull String params) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return null;
        }
        Log.i("LEO", "发起AIDL请求");
        try {
            String data = iService.getData(func, params);
            return data;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


     */

    public Integer ax_getBacklight() {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return null;
        }
        try {
            int data = iService.ax_getBacklight();
            return data;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void ax_setBacklight(int data) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return ;
        }
        try {
           iService.ax_setBacklight(data);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void ax_EnableUart(Boolean flag) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return ;
        }
        try {
            iService.ax_EnableUart(flag);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void ax_EnableBacklight(Boolean flag) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return ;
        }
        try {
            iService.ax_EnableBacklight(flag);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
/*
    public int ax_getCurSourceId(int var1) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return 0;
        }
        try {
            int id=iService.ax_getCurSourceId(var1);
            return  id;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int[] ax_getAvailSourceList() {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return null;
        }
        try {
            int sourcelist[]=iService.ax_getAvailSourceList();
            return  sourcelist;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new int[0];
    }
    public String ax_getSourceHolder() {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return null;
        }
        Log.i("LEO", "发起AIDL请求");
        try {
            String holder=iService.ax_getSourceHolder();
            return  holder;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void ax_setSourceHolder(String holder) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return ;
        }
        try {
           iService.ax_setSourceHolder(holder);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public int ax_setBootSource(int  var) {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return 0 ;
        }
        try {
           int bootsource= iService.ax_setBootSource(var);
            return bootsource;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int ax_getBootSource() {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return 0;
        }
        try {
           int bootsource= iService.ax_getBootSource();
            return bootsource;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int ax_getSelectSourceId() {
        if (null == iService) {
            Log.i("LEO", "iService为null");
            return 0 ;
        }
        try {
             int selectsource =iService.ax_getSelectSourceId();
            return selectsource;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0 ;
    }
  */









}
