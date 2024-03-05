plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    //hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    //proto
    id("com.google.protobuf") version "0.9.4"

    // mapsplatform
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1"
}

android {

    namespace = "com.example.weathers"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weathers"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += "room.incremental" to "true"
            }
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("/Volumes/BackupA/Work/AndroidStudioProjects/Weathers/key/ssm.key")
            storePassword = ""
            keyAlias = ""
            keyPassword = ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
    }

    testOptions {
        unitTests {
//            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //추가 라이브러리
    // test core
    // To use the androidx.test.core APIs
    androidTestImplementation("androidx.test:core:1.5.0")
    // Kotlin extensions for androidx.test.core
    androidTestImplementation("androidx.test:core-ktx:1.5.0")

    //activity-ktx
    implementation("androidx.activity:activity-ktx:1.8.2")

    //reflection
    implementation(kotlin("reflect"))

    // interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //gson
    implementation("com.google.code.gson:gson:2.10.1")

    //hilt
    val hilt_version = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:$hilt_version")

    //room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
//    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // Navi
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Proto datastore
    implementation("androidx.datastore:datastore:1.0.0")

    // Protobuf
    var protobufVersion = "3.19.4"
    implementation("com.google.protobuf:protobuf-kotlin:3.19.4")

    // Location
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    // Maps SDK for Android KTX Library
    implementation("com.google.maps.android:maps-ktx:3.0.0")
    // Maps SDK for Android Utility Library KTX Library
    implementation("com.google.maps.android:maps-utils-ktx:3.0.0")
    // Lifecycle Runtime KTX Library
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.plugins{
                create("kotlin")
                create("java")
            }
        }
    }
}