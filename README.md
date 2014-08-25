# Gradle Play publishing plugin

Gradle plugin for modifying google play app listings.

## Usage

### Apply plugin

#### Gradle 2.1 or newer
    plugins {
        id "com.github.blazsolar.play-publisher" version "<version>"
    }
    
#### Gradle 2.0 or older
    buildscript {
        repositories {
            jcentral()
        }
        dependencies {
            classpath 'com.github.blazsolar.gradle:play-publisher:<version>'
        }
    }

    apply plugin: 'com.github.blazsolar.play-publisher'

### Configuration
    playPublisher {
        applicationName "<company><app_name>/<version>"
        packageName "<app_package_name>"
        serviceAccountEmail "<service_account_email>"
        keyP12 "<service_account_key>"
    }

### Add upload app task
    task uploadApk(type: com.github.blazsolar.gradle.tasks.UploadApkTask) {
        apkPath "<path to apk file>"
        track TRACK_ALPHA | TRACK_BETA | TRACK_ROLLOUT | TRACK_PRODUCTION
        userFraction 0.05 | 0.1 | 0.2 | 0.5 // only if track is set to TRACK_ROLLOUT
        listings = [
                (Locale.US.toString()): "What's new for us_EN language"
        ]
    }

## License
    
    The MIT License (MIT)
    
    Copyright (c) 2014 Blaž Šolar
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.