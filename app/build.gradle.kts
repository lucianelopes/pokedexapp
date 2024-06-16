plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.pokedexapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokedexapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("junit:junit:4.13.2")
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // ConstraintLayout (para o layout)
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Room components
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    // Room Coroutines support
    implementation ("androidx.room:room-ktx:2.6.1")

    // Optional - Test Helpers
    testImplementation ("androidx.room:room-testing:2.6.1")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

    // Imagem
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")

    // Unit Testing
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    // Instrumented Testing
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")

    // ViewModel and LiveData Testing (verifique a versão correta)
    testImplementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    testImplementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
}