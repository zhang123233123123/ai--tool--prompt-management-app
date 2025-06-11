#!/bin/bash
echo "开始构建AI提示词管理器..."
echo "检查环境..."

# 检查是否有Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo "警告: ANDROID_HOME 环境变量未设置"
    echo "请确保已安装Android SDK并设置ANDROID_HOME环境变量"
fi

echo "清理项目..."
./gradlew clean

echo "构建Debug版本..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "构建成功！"
    echo "APK文件位置: app/build/outputs/apk/debug/app-debug.apk"
    
    # 创建发布目录
    mkdir -p release
    cp app/build/outputs/apk/debug/app-debug.apk release/ai-prompt-manager-debug.apk
    echo "APK已复制到: release/ai-prompt-manager-debug.apk"
else
    echo "构建失败，请检查错误信息"
    exit 1
fi

echo "构建完成！" 