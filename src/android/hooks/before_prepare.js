#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');

module.exports = function (context) {
    setGradleProperties();
};

function setGradleProperties() {
    const PLUGIN_NAME = "cordova-plugin-androidx";
    const jvmargs = "org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8";
    const enableAndroidX = "android.useAndroidX=true";
    const setJetifier = "android.enableJetifier=false";
    const style = "kotlin.code.style=official";
    const nonTransitiveRClass = "android.nonTransitiveRClass=true";
    const gradlePropertiesPath = "./platforms/android/gradle.properties";

    let gradleProperties = fs.readFileSync(gradlePropertiesPath);
    if (gradleProperties) {
        let updatedGradleProperties = false;
        gradleProperties = gradleProperties.toString();
        if (!gradleProperties.match(jvmargs)) {
            gradleProperties += "\n" + jvmargs;
            updatedGradleProperties = true;
        }
        if (!gradleProperties.match(enableAndroidX)) {
            gradleProperties += "\n" + enableAndroidX;
            updatedGradleProperties = true;
        }
        if (!gradleProperties.match(setJetifier)) {
            gradleProperties += "\n" + setJetifier;
            updatedGradleProperties = true;
        }
        if (!gradleProperties.match(style)) {
            gradleProperties += "\n" + style;
            updatedGradleProperties = true;
        }
        if (!gradleProperties.match(nonTransitiveRClass)) {
            gradleProperties += "\n" + nonTransitiveRClass;
            updatedGradleProperties = true;
        }
        if (updatedGradleProperties) {
            fs.writeFileSync(gradlePropertiesPath, gradleProperties, 'utf8');
        }
    } else {
//        log("gradle.properties file not found!")
    }

}
