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

**Library for Android runtime permissions**

- [XXPermissions-ktx](https://github.com/Zhao-YinGang/XXPermissions-ktx) is the Kotlin extension library for [XXPermissions](https://github.com/getActivity/XXPermissions), Easily request permissions using Kotlin DSL.
- Provide Java compatible chain calling, which can also be easily used in Java.

## Table of Contents

- [Integration steps](#integration-steps)
- [Usage](#usage)
- [Example](#example)
- [Related Efforts](#related-efforts)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## Integration steps

### Add [JitPack](https://jitpack.io/) package repository

* If your Gradle version is below `7.0`, add JitPack in your root `build.gradle.kts` or `build.gradle` at the end of repositories:

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

* If your Gradle version is `7.0` or higher，add JitPack in your root `settings.gradle.kts` or `settings.gradle` at the end of repositories:
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
### Add the dependency
* Add the dependency in your module `build.gradle.kts` or `build.gradle`：

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.Zhao-YinGang:XXPermissions-ktx:V1.6.0")
}
```

```groovy
// build.gradle
dependencies {
    implementation 'com.github.Zhao-YinGang:XXPermissions-ktx:V1.6.0'
}
```

### Migrate to AndroidX
XXPermissions-ktx is for AndroidX. Add the AndroidX migration configuration in your `gradle.properties`:
```
# Migrate third-party libraries to Android X
android.enableJetifier = true
```

## Usage
* Kotlin Basic usage
```kotlin
xxPermissions {
    // add location permissions
    permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
    // request result
    onResult { allGranted, grantedList, deniedList ->
        toast(
            "allGranted: " + allGranted +
                "\ngrantedList: " + grantedList +
                "\ndeniedList: " + deniedList
        )
    }
}
```

* Kotlin Complete Usage

```kotlin
xxPermissions {
    // add bluetooth connect permission
    permissions(Permission.BLUETOOTH_CONNECT)
    // add camera permission
    permissions(Permission.CAMERA)
    // add location permissions
    permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
    // If you should show rationale before requesting permissions, this callback will be called
    onShouldShowRationale { shouldShowRationaleList, onUserResult ->
        // The dialog here is just an example and does not use DialogFragment to handle the dialog lifecycle
        AlertDialog.Builder(this@requestDemo)
            .setTitle("Request Permissions")
            // Provide appropriate prompts based on rationale parameter
            .setMessage(
                "The application requires the following permissions: " +
                    permissionsNames(shouldShowRationaleList) +
                    "If these permissions are denied, some functions will be restricted."
            )
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                // User clicked positive button, call onConsent callback to requesting permissions
                onUserResult.onResult(true)
            }
            .setOnCancelListener {
                onUserResult.onResult(false)
            }
            .show()
    }
    // permissions denied permanently
    onDoNotAskAgain { doNotAskAgainList, onUserResult ->
        // The dialog here is just an example and does not use DialogFragment to handle the dialog lifecycle
        AlertDialog.Builder(this@requestDemo)
            .setTitle("Permissions permanently denied")
            // Provide appropriate prompts based on denied parameter
            .setMessage(
                "The following permissions have been permanently denied: " +
                    permissionsNames(denied.doNotAskAgainList) +
                    "Please enter the settings screen to grant permissions."
            )
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Enter Settings") { dialog, _ ->
                dialog.dismiss()
                // User clicked positive button, call onConsent callback to entering settings screen
                onUserResult.onResult(true)
            }
            .setOnCancelListener {
                onUserResult.onResult(false)
            }
            .show()
    }
    // request result
    onResult { allGranted, grantedList, deniedList ->
        toast(
            "allGranted: " + allGranted +
                "\ngrantedList: " + grantedList +
                "\ndeniedList: " + deniedList
        )
    }
}
```

* Java Complete Usage

```java
public class RequestDemo4j {
    public static void run(Activity activity) {
        XXPermissions4j.with(activity)
            // add bluetooth connect permission
            .permissions(Permission.BLUETOOTH_CONNECT)
            // add camera permission
            .permissions(Permission.CAMERA)
            // add location permissions
            .permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            // If you should show rationale before requesting permissions, this callback will be called
            .onShouldShowRationale((shouldShowRationaleList, onUserResult) -> {
                // The dialog here is just an example and does not use DialogFragment to handle the dialog lifecycle
                new AlertDialog.Builder(activity)
                    .setTitle("Request Permissions")
                    // Provide appropriate prompts based on rationale parameter
                    .setMessage(
                        "The application requires the following permissions: " +
                            RequestDemoKt.permissionsNames(rationale.getRationaleList()) +
                            "If these permissions are denied, some functions will be restricted."
                    )
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        // User clicked positive button, call onConsent callback to requesting permissions
                        onUserResult.onResult(true);
                    })
                    .setOnCancelListener(dialog -> onUserResult.onResult(false))
                    .show();
            })
            .onDoNotAskAgain((doNotAskAgainList, onUserResult) -> {
                // The dialog here is just an example and does not use DialogFragment to handle the dialog lifecycle
                new AlertDialog.Builder(activity)
                    .setTitle("Permissions permanently denied")
                    // Provide appropriate prompts based on denied parameter
                    .setMessage(
                        "The following permissions have been permanently denied: " +
                            RequestDemoKt.permissionsNames(denied.getDoNotAskAgainList()) +
                            "Please enter the settings screen to grant permissions."
                    )
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        // User clicked positive button, call onConsent callback to entering settings screen
                        onUserResult.onResult(true);
                    })
                    .setOnCancelListener(dialog -> onUserResult.onResult(false))
                    .show();
            })
            // request result
            .onResult((allGranted, grantedList, deniedList) -> ToastHelper.toast(activity,
                "allGranted: " + allGranted +
                    "\ngrantedList: " + grantedList +
                    "\ndeniedList: " + deniedList
            ))
            // request permissions
            .request();
    }
}
```

## Example
To learn how to use XXPermissions-ktx, please refer to [demo](app)。

## Related Efforts
- [XXPermissions](https://github.com/getActivity/XXPermissions) - Android permission request framework, adapted to Android 13

## Maintainers

[@Zhao-YinGang](https://github.com/Zhao-YinGang)。

## Contributing
Feel free to dive in! [Open an issue](https://github.com/RichardLitt/standard-readme/issues/new) or submit PRs.

XXPermissions-ktx follows the [Contributor Covenant](http://contributor-covenant.org/version/1/3/0/) Code of Conduct.

## License

[Apache-2.0](LICENSE) © Zhao-YinGang

