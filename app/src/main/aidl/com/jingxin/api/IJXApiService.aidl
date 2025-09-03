// IJXApiService.aidl
package com.jingxin.api;

// Declare any non-default types here with import statements

interface IJXApiService {
    // 背光控制
    int getBacklight();
    void setBacklight(int brightness);
    void enableBacklight(boolean enable);

    // 面板翻转
    int getPanelFlipMirror();
    void setPanelFlipMirror(int du);

    // UART 使能
    boolean enableUart(boolean enable);

    // 窗口旋转
    int getWindowRotation();
    int setWindowRotation(int rotation);
}