<h1 align="center">XXPermissions-ktx</h1>
<p align="center">
  <a href="http://www.apache.org/licenses/LICENSE-2.0">
    <img src="https://img.shields.io/hexpm/l/plug?style=flat-square">
  </a>
  <a href="https://jitpack.io/#Zhao-YinGang/XXPermissions-ktx">
    <img src="https://jitpack.io/v/Zhao-YinGang/XXPermissions-ktx.svg?style=flat-square">
  </a>
  <a href="https://github.com/RichardLitt/standard-readme">
    <img src="https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square">
  </a>
</p>

<p align="center">
  <a href="/README.md">English</a> •
  <a href="/docs/README-zh-cn.md">简体中文 (Simplified Chinese)</a>
</p>

**Android 动态权限请求库**

- [XXPermissions-ktx](https://github.com/Zhao-YinGang/XXPermissions-ktx)
  是 [XXPermissions](https://github.com/getActivity/XXPermissions) 的 Kotlin 扩展库，使用 Kotlin DSL
  轻松地申请权限。
- 提供 Java 兼容的链式调用，在 Java 中也可以轻松使用。

## 内容列表

- [集成步骤](#集成步骤)
- [使用说明](#使用说明)
- [示例](#示例)
- [相关仓库](#相关仓库)
- [维护者](#维护者)
- [如何贡献](#如何贡献)
- [使用许可](#使用许可)

## 集成步骤

### 添加 [JitPack](https://jitpack.io/) 远程仓库

* 如果你的 Gradle 版本小于 `7.0`，在项目根目录下的 `build.gradle.kts` 或 `build.gradle` 中加入远程仓库配置：

```kotlin
// build.gradle.kts
allprojects {
    repositories {
        maven("https://jitpack.io")
    }
}
```

```groovy
// build.gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

* 如果你的 Gradle 版本是 `7.0` 及以上，在项目根目录下的 `settings.gradle.kts` 或 `settings.gradle`
  中加入远程仓库配置：

```kotlin
// build.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
    }
}
```

```groovy
// build.gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### 引入远程依赖

* 配置完远程仓库后，在模块目录下的 `build.gradle.kts` 或 `build.gradle` 文件中加入远程依赖：

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.Zhao-YinGang:XXPermissions-ktx:V1.0.0")
}
```

```groovy
// build.gradle
dependencies {
    implementation 'com.github.Zhao-YinGang:XXPermissions-ktx:V1.0.0'
}
```

### AndroidX 迁移配置

XXPermissions-ktx 只考虑兼容 AndroidX，没有兼容 support。  
由于 [XXPermissions](https://github.com/getActivity/XXPermissions) 兼容了 support
库，请在项目根目录下的 `gradle.properties` 中加入 AndroidX 迁移配置

```
# 表示将第三方库迁移到 AndroidX
android.enableJetifier = true
```

## 使用说明

* Kotlin 基本用法示例

```kotlin
xxPermissions {
    // 申请位置权限
    permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
    // 权限申请结果
    onResult { allGranted, grantedList, deniedList ->
        toast(
            "allGranted: " + allGranted +
                "\ngrantedList: " + grantedList +
                "\ndeniedList: " + deniedList
        )
    }
}
```

* Kotlin 完整用法示例

```kotlin
xxPermissions {
    xxPermissions {
        // 申请BLE权限
        permissions(Permission.BLUETOOTH_CONNECT)
        // 申请相机权限
        permissions(Permission.CAMERA)
        // 申请位置权限
        permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
        // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
        onShouldShowRationale { shouldShowRationaleList, onUserResult ->
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this@requestDemo)
                .setTitle("权限申请")
                // 根据 rationalePermissions 进行适当的提示
                .setMessage(
                    "应用需要以下权限：" +
                        permissionsNames(shouldShowRationaleList) +
                        "如果拒绝授予这些权限，则应用部分功能将受限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("去申请") { dialog, _ ->
                    dialog.dismiss()
                    // 用户同意，通过此回调通知框架开始权限申请
                    onUserResult.onResult(true)
                }
                .setOnCancelListener {
                    // 用户不同意，通过此回调通知框架
                    onUserResult.onResult(false)
                }
                .show()
        }
        onDoNotAskAgain { doNotAskAgainList, onUserResult ->
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this@requestDemo)
                .setTitle("权限被永久拒绝")
                // 根据被拒绝的权限列表进行适当的提示
                .setMessage(
                    "以下权限被永久拒绝：" +
                        permissionsNames(doNotAskAgainList) +
                        "请进入设置界面手动授予权限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("去设置") { dialog, _ ->
                    dialog.dismiss()
                    // 用户同意，通过此回调通知框架进入应用设置界面
                    onUserResult.onResult(false)
                }
                .setOnCancelListener {
                    // 用户不同意，通过此回调通知框架
                    onUserResult.onResult(false)

                }
                .show()
        }
        // 权限申请结果
        onResult { allGranted, grantedList, deniedList ->
            toast(
                "allGranted: " + allGranted +
                    "\ngrantedList: " + grantedList +
                    "\ndeniedList: " + deniedList
            )
        }
    }
}
```

* Java 完整用法示例

```java
public class RequestDemo4j {
    public static void run(Activity activity) {
        XXPermissionsExt.with(activity)
            // 申请位置权限
            .permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            // 申请相机权限
            .permissions(Permission.CAMERA)
            // 申请BLE权限
            .permissions(Permission.BLUETOOTH_CONNECT)
            // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
            .onShouldShowRationale((shouldShowRationaleList, onUserResult) -> {
                // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                new AlertDialog.Builder(activity)
                    .setTitle("权限申请")
                    // 根据 rationalePermissions 进行适当的提示
                    .setMessage(
                        "应用需要以下权限：" +
                            RequestDemoKt.permissionsNames(shouldShowRationaleList) +
                            "如果拒绝授予这些权限，则应用部分功能将受限"
                    )
                    .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("去申请", (dialog, which) -> {
                        dialog.dismiss();
                        // 用户同意，通过此回调通知框架
                        onUserResult.onResult(true);
                    })
                    .setOnCancelListener(dialog -> {
                        // 用户不同意，通过此回调通知框架
                        onUserResult.onResult(false);
                    })
                    .show();
            })
            .onDoNotAskAgain((doNotAskAgainList, onUserResult) -> {
                // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                new AlertDialog.Builder(activity)
                    .setTitle("权限被永久拒绝")
                    // 根据被拒绝的权限列表进行适当的提示
                    .setMessage(
                        "以下权限被永久拒绝：" +
                            RequestDemoKt.permissionsNames(doNotAskAgainList) +
                            "请进入设置界面手动授予权限"
                    )
                    .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("去设置", (dialog, which) -> {
                        dialog.dismiss();
                        // 用户同意，通过此回调通知框架
                        onUserResult.onResult(true);
                    })
                    .setOnCancelListener(dialog -> {
                        // 用户不同意，通过此回调通知框架
                        onUserResult.onResult(false);
                    })
                    .show();
            })
            .onResult((allGranted, grantedList, deniedList) -> ToastHelper.toast(activity,
                "allGranted: " + allGranted +
                    "\ngrantedList: " + grantedList +
                    "\ndeniedList: " + deniedList
            ))
            .request();
    }
}
```

## 示例

想了解如何使用 XXPermissions-ktx，请参考 [demo](../app)。

## 相关仓库

- [XXPermissions](https://github.com/getActivity/XXPermissions) - Android 权限请求框架，已适配
  Android 13

## 维护者

[@Zhao-YinGang](https://github.com/Zhao-YinGang)。

## 如何贡献

非常欢迎你的加入！[提一个 Issue](https://github.com/Zhao-YinGang/XXPermissions-ktx/issues/new) 或者提交一个
Pull Request。

本仓库遵循 [Contributor Covenant](http://contributor-covenant.org/version/1/3/0/) 行为规范。

## 使用许可

[Apache-2.0](../LICENSE) © Zhao-YinGang

