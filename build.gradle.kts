// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    extra["compose_version"] = "1.3.0"
    extra["kotlin_version"] = "1.5.31"
    // ...
}