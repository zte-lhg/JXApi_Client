
cmake_minimum_required(VERSION 3.4.1)
#project(NDK)

add_library(
        SerialPort
        SHARED
        src/main/jni/SerialPort.c
)

find_library(
        log-Lib
        log
)

target_link_libraries(
        SerialPort
        ${log-Lib}
)
