package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jingxin.api.IJXApiService;

public class AidlUtils {
    private IJXApiService iService;
    private static final String TAG = "JxApiTest";

    private AidlUtils() {
    }

    private static final class AidlUtilHolder {
        static final AidlUtils aidlUtil = new AidlUtils();
    }

    public static AidlUtils getInstance() {
        return AidlUtilHolder.aidlUtil;
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "绑定成功");
            iService = IJXApiService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iService = null;
        }
    };

    public void bindService(Context context) {
        Log.i(TAG, "bindService on MainActivity");
        Intent intent = new Intent("com.jingxin.api");
        intent.setPackage("com.jingxin.api");
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (null == iService) {
            return;
        }
        Log.i(TAG, "取消绑定");
        context.unbindService(connection);
        iService = null;
    }

    public Integer getBacklight() {
        if (null == iService) {
            Log.i(TAG, "iService 为 null");
            return null;
        }
        try {
            return iService.getBacklight();
        } catch (RemoteException e) {
            Log.e(TAG, "iService getBacklight exception");
        }
        return null;
    }
    public void setBacklight(int data) {
        if (null == iService) {
            Log.i(TAG, "iService 为 null");
            return ;
        }
        try {
           iService.setBacklight(data);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setBacklight exception");
        }
    }

    public void enableUart(Boolean flag) {
        if (null == iService) {
            Log.i(TAG, "iService 为 null");
            return ;
        }
        try {
            iService.enableUart(flag);
        } catch (RemoteException e) {
            Log.e(TAG, "iService enableUart exception");
        }
    }

    public void enableBacklight(Boolean flag) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return ;
        }
        try {
            iService.enableBacklight(flag);
        } catch (RemoteException e) {
            Log.e(TAG, "iService enableBacklight exception");
        }
    }

    public Integer getPanelFlipMirror() {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return null;
        }
        try {
             return iService.getPanelFlipMirror();
        } catch (RemoteException e) {
            Log.e(TAG, "iService getPanelFlipMirror exception");
        }
        return null;
    }

    public void setPanelFlipMirror(int du) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return ;
        }
        try {
            iService.setPanelFlipMirror(du);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setPanelFlipMirror exception");
        }
    }

    public Integer getWindowRotation() {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return null;
        }
        try {
            return iService.getWindowRotation();
        } catch (RemoteException e) {
            Log.e(TAG, "iService getWindowRotation exception");
        }
        return null;
    }

    public void setWindowRotation(int rotation) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return ;
        }
        try {
            iService.setWindowRotation(rotation);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setWindowRotation exception");
        }
    }

    public void setGpioValue(int port, int value) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return ;
        }
        try {
            iService.setGpioValue(port, value);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setGpioOutput exception");
        }
    }

    public Integer getGpioValue(int port) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return null;
        }
        try {
            return iService.getGpioValue(port);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setWindowRotation exception");
        }
        return null;
    }

    public void setGpioDirection(int port, int direction) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return ;
        }
        try {
            iService.setGpioDirection(port, direction);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setWindowRotation exception");
        }
    }

    public Integer getGpioRotation(int port) {
        if (null == iService) {
            Log.i(TAG, "iService为null");
            return null;
        }
        try {
            return iService.getGpioDirection(port);
        } catch (RemoteException e) {
            Log.e(TAG, "iService setWindowRotation exception");
        }
        return null;
    }
}
