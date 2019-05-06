# Gradle Play publishing plugin

Gradle plugin for modifying google play app listings.

## Usage

### Apply plugin

#### Gradle 4.8 or newer
    plugins {
        id "solar.blaz.play-publisher" version "0.4.3"
    }
    
#### Gradle 2.0 or older
    buildscript {
        repositories {
            jcentral()
        }
        dependencies {
            classpath 'solar.blaz.gradle:play-publisher:0.4.3'
        }
    }

    apply plugin: 'solar.blaz.play-publisher'

### Configuration - Kotlin
    playPublisher {
        artifacts {
            create("<artifact_name>") {
                appId = "<app id>" // Application id of apps to publish
                clientSecretJson = file("<play_publish.json>") // path to play publish file
                action = "completed" // One of "completed", "inProgress" or "draft"
                userFraction = 0.05 // if action is inProgress
                track = "production" // one of "production", "beta", "alpha", "internal"
                listingDir = file(".listings/") // Dirictory to listings information
            }
        }
        
    }

### Publish artifact

`./gradlew playUpload<artifact_name>`

### Multi project configuration

TBD

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
