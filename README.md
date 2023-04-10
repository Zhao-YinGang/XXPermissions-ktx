# XXPermissions-ktx

[![](https://img.shields.io/hexpm/l/plug)](http://www.apache.org/licenses/LICENSE-2.0)
[![](https://jitpack.io/v/Zhao-YinGang/XXPermissions-ktx.svg)](https://jitpack.io/#Zhao-YinGang/XXPermissions-ktx)

# 权限请求框架

本框架是 [XXPermissions](https://github.com/getActivity/XXPermissions) 的 Kotlin 扩展库，让你在 Kotlin 中更轻松的申请权限。  
本框架完全兼容 Java，在 Java 中也可以轻松使用。
* 项目地址：[Github XXPermissions-ktx](https://github.com/Zhao-YinGang/XXPermissions-ktx)

## 集成步骤

* 如果你的项目 Gradle 配置是在 `7.0` 以下，需要在 `build.gradle` 文件中加入

```groovy
allprojects {
    repositories {
        // JitPack 远程仓库：https://jitpack.io
        maven { url 'https://jitpack.io' }
    }
}
```

* 如果你的 Gradle 配置是 `7.0` 及以上，则需要在 `settings.gradle.kts` 或 `settings.gradle` 文件中加入
```kotlin
dependencyResolutionManagement {
    repositories {
        // JitPack 远程仓库：https://jitpack.io
        maven("https://jitpack.io")
    }
}
```

```groovy
dependencyResolutionManagement {
    repositories {
        // JitPack 远程仓库：https://jitpack.io
        maven { url 'https://jitpack.io' }
    }
}
```

* 配置完远程仓库后，在项目 app 模块下的 `build.gradle.kts` 或 `build.gradle` 文件中加入远程依赖

```kotlin
dependencies {
    // 权限请求框架：https://github.com/getActivity/XXPermissions
    implementation("com.github.getActivity:XXPermissions:16.8")

    // XXPermissions-ktx https://github.com/Zhao-YinGang/XXPermissions-ktx
    implementation("com.github.Zhao-YinGang:XXPermissions-ktx:V1.0.0-bate01")
}
```

```groovy
dependencies {
    // 权限请求框架：https://github.com/getActivity/XXPermissions
    implementation 'com.github.getActivity:XXPermissions:16.8'

    // XXPermissions-ktx https://github.com/Zhao-YinGang/XXPermissions-ktx
    implementation 'com.github.Zhao-YinGang:XXPermissions-ktx:V1.0.0-bate01'
}
```

[XXPermissions-ktx](https://github.com/Zhao-YinGang/XXPermissions-ktx) 只考虑兼容 AndroidX，没有兼容 support。 但是 [XXPermissions](https://github.com/getActivity/XXPermissions) 是 support 库编写的，请在项目 gradle.properties 文件中加入 AndroidX 迁移配置
```
# 表示将第三方库迁移到 AndroidX
android.enableJetifier = true
```
## 用法示例
* Kotlin 基本用法示例
```kotlin
xxPermissions {
    // 申请位置权限
    permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
    // 授予了权限
    onGranted { granted ->
        if (granted.isAllGranted) {  // 用户同意了所有权限
            toast("所有权限获取成功")
        } else { // 用户同意了部分权限
            toast("获取到了以下权限：${permissionsNames(granted.grantedList)}")
        }
    }
    // 拒绝了权限
    onDenied { denied ->
        toast("以下权限被拒绝：${permissionsNames(denied.deniedList)}")
    }
}
```

* Kotlin 完整用法示例

```kotlin
xxPermissions {
    // 申请BLE权限
    permissions(Permission.BLUETOOTH_CONNECT)
    // 申请相机权限
    permissions(Permission.CAMERA)
    // 申请位置权限
    permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
    // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
    onShowRationale { rationale ->
        // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
        AlertDialog.Builder(this@requestDemo)
            .setTitle("权限申请")
            // 根据 rationalePermissions 进行适当的提示
            .setMessage(
                "应用需要以下权限：" +
                    permissionsNames(rationale.rationaleList) +
                    "如果拒绝授予这些权限，则应用部分功能将受限"
            )
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("去申请") { dialog, _ ->
                dialog.dismiss()
                // 用户同意，通过此回调通知框架开始权限申请
                rationale.onConsent()
            }
            .show()
    } 
    // 授予了权限
    onGranted { granted ->
        if (granted.isAllGranted) {  // 用户同意了所有权限
            toast("所有权限获取成功")
        } else { // 用户同意了部分权限
            toast("获取到了以下权限：${permissionsNames(granted.grantedList)}")
        }
    }
    // 拒绝了权限
    onDenied { denied ->
        if (denied.hasDoNotAskAgain) {
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this@requestDemo)
                .setTitle("权限被永久拒绝")
                // 根据被拒绝的权限列表进行适当的提示
                .setMessage(
                    "以下权限被永久拒绝：" +
                        permissionsNames(denied.doNotAskAgainList) +
                        "请进入设置界面手动授予权限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("去设置") { dialog, _ ->
                    dialog.dismiss()
                    // 用户同意，通过此回调通知框架进入应用设置界面
                    denied.onConsent()
                }
                .show()
        } else {
            toast("以下权限被拒绝：${permissionsNames(denied.deniedList)}")
            requestDemo()
        }
    }
}
```

* Java 完整用法示例

```java
public class RequestDemo4j {
    public static void run(Activity activity) {
        XXPermissions4j.with(activity)
            // 申请BLE权限
            .permissions(Permission.BLUETOOTH_CONNECT)
            // 申请相机权限
            .permissions(Permission.CAMERA)
            // 申请位置权限
            .permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
            .onShowRationale(rationale -> {
                // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                new AlertDialog.Builder(activity)
                    .setTitle("权限申请")
                    // 根据 rationalePermissions 进行适当的提示
                    .setMessage(
                        "应用需要以下权限：" +
                            RequestDemoKt.permissionsNames(rationale.getRationaleList()) +
                            "如果拒绝授予这些权限，则应用部分功能将受限"
                    )
                    .setNeutralButton("取消", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("去申请", (dialog, which) -> {
                        dialog.dismiss();
                        // 用户同意，通过此回调通知框架开始权限申请
                        rationale.getOnConsent().invoke();
                    })
                    .show();
            })
            // 授予了权限
            .onGranted(granted -> {
                if (granted.isAllGranted()) {  // 用户同意了所有权限
                    ToastHelper.toast(activity, "所有权限获取成功");
                } else { // 用户同意了部分权限
                    ToastHelper.toast(activity, "获取到了以下权限：" + RequestDemoKt.permissionsNames(granted.getGrantedList()));
                }
            })
            // 拒绝了权限
            .onDenied(denied -> {
                if (denied.getHasDoNotAskAgain()) {
                    // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                    new AlertDialog.Builder(activity)
                        .setTitle("权限被永久拒绝")
                        // 根据被拒绝的权限列表进行适当的提示
                        .setMessage(
                            "以下权限被永久拒绝：" +
                                RequestDemoKt.permissionsNames(denied.getDoNotAskAgainList()) +
                                "请进入设置界面手动授予权限"
                        )
                        .setNeutralButton("取消", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("去申请", (dialog, which) -> {
                            dialog.dismiss();
                            // 用户同意，通过此回调通知框架进入应用设置界面
                            denied.getOnConsent().invoke();
                        })
                        .show();
                } else {
                    ToastHelper.toast(activity, "以下权限被拒绝：" + RequestDemoKt.permissionsNames(denied.getDeniedList()));
                    run(activity);
                }
            })
            .request();
    }
}
```


* 更多关于 XXPermissions 的用法，请参考 [XXPermissions](https://github.com/getActivity/XXPermissions)
