# AI提示词管理器

一个功能强大的安卓应用，专为管理和快速使用AI提示词而设计。支持悬浮窗显示和自动填充功能。

## 主要功能

### 🎯 核心功能
- **提示词管理**: 添加、编辑、删除和分类管理AI提示词
- **智能搜索**: 支持标题和内容的模糊搜索
- **分类过滤**: 按写作、编程、翻译等分类筛选提示词
- **收藏功能**: 收藏常用提示词以便快速访问
- **使用统计**: 跟踪提示词使用频次

### 🎈 悬浮窗功能
- **全局悬浮球**: 在任何应用上方显示悬浮助手
- **快速访问**: 点击悬浮球即可查看提示词列表
- **拖拽定位**: 自由拖拽悬浮球到任意位置
- **一键复制**: 点击提示词自动复制到剪贴板

### 🤖 自动填充(高级功能)
- **无障碍服务**: 利用无障碍服务实现自动填充
- **智能识别**: 自动识别当前输入框并填入提示词
- **多应用支持**: 支持ChatGPT、Claude等AI聊天应用

## 技术架构

### 🛠 技术栈
- **UI框架**: Jetpack Compose + Material3
- **架构模式**: MVVM + Repository Pattern
- **依赖注入**: Hilt
- **数据库**: Room
- **异步处理**: Kotlin Coroutines + Flow

### 🏗 架构设计
```
├── data/                 # 数据层
│   ├── Prompt.kt        # 数据实体
│   ├── PromptDao.kt     # 数据访问对象
│   └── PromptDatabase.kt # 数据库定义
├── repository/          # 仓库层
│   └── PromptRepository.kt
├── viewmodel/          # 视图模型
│   └── PromptViewModel.kt
├── service/            # 服务层
│   ├── FloatingPromptService.kt    # 悬浮窗服务
│   └── PromptInjectorService.kt    # 无障碍服务
├── ui/                 # UI层
│   ├── MainActivity.kt
│   ├── MainScreen.kt
│   ├── PromptCard.kt
│   └── PromptDialog.kt
└── di/                 # 依赖注入
    └── DatabaseModule.kt
```

## 权限说明

### 必需权限
- `SYSTEM_ALERT_WINDOW`: 悬浮窗权限，用于显示全局悬浮助手

### 可选权限
- `BIND_ACCESSIBILITY_SERVICE`: 无障碍服务权限，用于自动填充功能

## 使用说明

### 1. 安装和设置
1. 安装APK文件
2. 首次启动时会引导申请悬浮窗权限
3. (可选)在设置中开启无障碍服务以启用自动填充

### 2. 管理提示词
1. 在主界面点击"+"按钮添加提示词
2. 设置标题、内容和分类
3. 使用搜索和分类过滤找到所需提示词
4. 长按提示词可进行编辑或删除

### 3. 使用悬浮助手
1. 在主界面启动悬浮服务
2. 悬浮球将出现在屏幕上
3. 点击悬浮球查看提示词列表
4. 点击提示词即可复制或自动填充

## 开发和构建

### 环境要求
- Android Studio Hedgehog | 2023.1.1+
- Kotlin 1.9.10+
- Gradle 8.4+
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)

### 构建步骤
```bash
git clone [repository-url]
cd ai-prompt-manager
./gradlew assembleDebug
```

### 主要依赖
- Jetpack Compose BOM 2023.10.01
- Hilt 2.48
- Room 2.6.1
- Kotlin Coroutines

## 隐私说明

- 所有提示词数据均存储在本地设备
- 不收集、上传任何用户数据
- 无障碍服务仅用于文本填充，不记录用户输入

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。 