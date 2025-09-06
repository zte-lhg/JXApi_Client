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
   返回值：int 类型，面板翻转状态值
          0: MIRROR_FLIP_OFF;
          1: MIRROR_FLIP_MIRROR;
          2: MIRROR_FLIP_FLIP;
          3: MIRROR_FLIP_MIRROR_FLIP;

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
   参数：du - 翻转模式值
        0: MIRROR_FLIP_OFF;
        1: MIRROR_FLIP_MIRROR;
        2: MIRROR_FLIP_FLIP;
        3: MIRROR_FLIP_MIRROR_FLIP;

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
   返回值：int 类型，旋转角度 0、90、180、270 等）
          VO_ROTATION_0         = 0;
          VO_ROTATION_90        = 1;
          VO_ROTATION_180       = 2;
          VO_ROTATION_270       = 3;

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
   参数：rotation - 目标旋转角度（0、90、180、270 ）同上
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
   方法签名：boolean openPortStr(String strPort)(其他相关接口操作方式类似)
   功能描述：打开 gpio 口
   参数：strPort - gpio 口的名称
   返回值：gpio 口打开状态

```java
   import jxapi.SysControl;

   SysControl sysControl = SysControl.GetInstance();
   sysControl.getGpioValue(port);
   sysControl.setGpioValue(port, value);
   sysControl.getGpioValue(port, direction);
   上面接口直接使用 gpio 的端口号设置 gpio 值，比如 gpio126
   
   sysControl.setGpioValue(String port, int value);
   sysControl.getGpioValue(String port, String direction);
   sysControl.setGpioDirection(String port, String direction);
   sysControl.getGpioDirection(String port);
   上面接口使用 gpio 的字符串设置 gpio 值，接口内部会做自动转换 比如 GPIO7_B5，内部会自动做转换
```

9. 系统设备 ID 获取
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

10. 获取系统属性
   方法签名：String getSystemProperty(String property, String defaultValue)
   功能描述：打开 gpio 口
   参数：1. property - 属性名称
        2. defaultValue --- 默认属性名
   返回值：返回系统属性名称

```java
   import jxapi.SysControl;

    SysControl sysControl = SysControl.GetInstance();
    Log.i(TAG, "system device is " + sysControl.getSystemProperty("persist.prop.screenorientation", "landscape");
```
11. 获取系统 DeviceID
    方法签名：String getDeviceID()
    功能描述：获取系统设备的 deviceID
    参数：无
    返回值：返回系统单板芯片唯一 serialID 串口，每个芯片唯一

```java
   import jxapi.SysControl;

    SysControl sysControl = SysControl.GetInstance();
    Log.i(TAG, "system deviceID is " + sysControl.getDeviceID();

```
## 自测
目前除了 gpio 需要提供对应的 gpio 编号外，其他接口都已经测试，测试日志如下：

```json
09-06 19:53:58.795  7450  7450 I JxApiTest: system device id is 0000000000000000
09-06 19:53:58.796  7450  7450 I JxApiTest: system sdk version is 28
09-06 19:53:58.803  7450  7450 I JxApiTest: system sdk firmware version is 
09-06 19:53:58.809  7450  7450 I JxApiTest: system sdk getDisplayOrientation is landscape
09-06 19:53:58.810  7450  7450 I JxApiTest: system sdk getSystemMaxBrightness is 255
09-06 19:53:58.868  7450  7450 I JxApiTest: 绑定成功
09-06 19:53:58.870  7450  7450 E JxApiTest: 使能串口ttyAMA1
09-06 19:54:07.401  7450  7450 E JxApiTest: item_backlight start!
09-06 19:54:08.275  7450  7450 I JxApiTest: progress=100
09-06 19:54:08.282  7450  7450 I JxApiTest: 设置背光s
09-06 19:54:08.297  7450  7450 I JxApiTest: pic.setBacklight()=100
09-06 19:54:14.045  7450  7450 I JxApiTest: 依次测试各个接口
09-06 19:54:14.053  7450  7450 I JxApiTest: disableBacklight 
09-06 19:54:14.060  7450  7450 I JxApiTest: enableBacklight 
09-06 19:54:14.067  7450  7450 I JxApiTest: current backlight is 100
09-06 19:54:14.076  7450  7450 I JxApiTest: current system getPanelFlipMirror is 0
09-06 19:54:14.099  7450  7450 I JxApiTest: current system setPanelFlipMirror 0
09-06 19:54:14.106  7450  7450 I JxApiTest: current system getWindowRotation is 0
09-06 19:54:14.113  7450  7450 I JxApiTest: current system setWindowRotation 0
09-06 19:54:14.113  7450  7450 I JxApiTest: all case test pass!!
```
