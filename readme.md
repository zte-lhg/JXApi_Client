# JxApi.jar 接口调用文档
## 接口概述
JxApi 是基于 hi3751 晶心开发单板提供设备硬件控制与系统参数配置能力，支持背光调节、面板翻转、及窗口旋转、GPIO 控制等核心功能
目前为 V1.0, 提供的基本功能接口如下：

## 服务绑定方法
使用接口前，需要先绑定 JXApiService 服务，具体可以参考 AidlUtils.java 部分代码实现

```java
    // 导入 service 包名
    import com.jingxin.api.IJXApiService;

    // 声明AIDL接口变量
    private IJXApiService iService;

    
    // 服务连接对象
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
    
    // 绑定服务的代码
    Intent intent = new Intent("com.jingxin.api");
    intent.setPackage("com.jingxin.api");
    context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
```

## 接口列表及调用说明
1. 获取当前背光值
   方法签名：int getBacklight() throws RemoteException
   功能描述：获取设备当前的背光亮度值
   返回值：  int 类型，当前背光亮度值（具体范围取决于硬件实现）

```java
    try {
        int brightness = iService.getBacklight();
        Log.d(TAG, "当前背光值: " + brightness);
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```

2. 设置背光值
   方法签名：void setBacklight(int brightness) throws RemoteException
   功能描述：设置设备的背光亮度
   参数：brightness - 目标背光亮度值（具体范围取决于硬件实现）
```java
    try {
        iService.setBacklight(80); // 设置背光值为80
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```

3. 背光使能控制
   方法签名：void enableBacklight(boolean enable) throws RemoteException
   功能描述：开启或关闭设备背光
   参数：enable - true 开启背光，false 关闭背光
```java
    try {
        iService.enableBacklight(true); // 开启背光
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```

4. 获取面板翻转状态
   方法签名：int getPanelFlipMirror() throws RemoteException
   功能描述：获取当前面板的翻转状态
   返回值：int 类型，面板翻转状态值（具体含义取决于硬件实现）

```java
    try {
        int status = iService.getPanelFlipMirror();
        Log.d(TAG, "面板翻转状态: " + status);
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```

5. 设置面板翻转
   方法签名：void setPanelFlipMirror(int du) throws RemoteException
   功能描述：设置面板的翻转模式
   参数：du - 翻转模式值（具体含义取决于硬件实现）
```java
    try {
        iService.setPanelFlipMirror(1); // 设置特定的面板翻转模式
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```

6. 获取窗口旋转角度
   方法签名：int getWindowRotation() throws RemoteException
   功能描述：获取当前窗口的旋转角度
   返回值：int 类型，旋转角度（通常为 0、90、180、270 等）

```java
    try {
        int rotation = iService.getWindowRotation();
        Log.d(TAG, "当前窗口旋转角度: " + rotation + "°");
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```

7. 设置窗口旋转角度
   方法签名：int setWindowRotation(int rotation) throws RemoteException
   功能描述：设置窗口的旋转角度
   参数：rotation - 目标旋转角度（通常为 0、90、180、270 等）
   返回值：int 类型，实际设置的旋转角度（可能与请求值不同）
```java
    try {
        int result = iService.setWindowRotation(90); // 设置窗口旋转90度
        Log.d(TAG, "实际设置的旋转角度: " + result + "°");
    } catch (RemoteException e) {
        e.printStackTrace();
    }
```
8. GPIO 端口的控制
   方法签名：boolean openPortStr(String strPort)
   功能描述：打开 gpio 口
   参数：strPort - gpio 口的名称
   返回值：gpio 口打开状态

```java
   import jxapi.SysControl;

   SysControl sysControl = SysControl.GetInstance();
   SysControl sysControl = SysControl.GetInstance();
   sysControl.getGpioValue(port);
   sysControl.setGpioValue(port, value);
   sysControl.getGpioValue(port, direction);
   sysControl.setGpioValue(port, value);
   
```


9. 系统设备ID 获取
   方法签名：boolean openPortStr(String strPort)
   功能描述：打开 gpio 口
   参数：strPort - gpio 口的名称
   返回值：gpio 口打开状态

```java
   import jxapi.SysControl;

    SysControl sysControl = SysControl.GetInstance();
    Log.i(TAG, "system device is " + sysControl.getDeviceID());
    Log.i(TAG, "system sdk version is " + sysControl.getSDKVersion());
    
```
