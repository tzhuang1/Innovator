# SOLve - Developer Guide

Developer guide for BranchOut! Innovator's mobile application, SOLve.

## Setup

The development environment for SOLve is Android Studio. 

After downloading Android Studio, open the "app" folder (**NOT "innovator"**) to open up the project.

**Again, open "app", not "innovator."**

Gradle will attempt to build on open. You might encounter the issues listed below. After resolving each, click the reload icon at the bottom left to rerun Gradle build.

### Unsupported class file major version 61  
SOLve supports Java 11 (Java 15 also seems to work), however not Java 17. Switch the selected Java installation by following these instructions: [Java Version Changing](https://www.geeksforgeeks.org/how-to-set-java-sdk-path-in-android-studio)

### Task 'wrapper' not found in project ':app'
Add these lines at the end of build.gradle.
```
task wrapper(type: Wrapper){
   gradleVersion = '7.2'
}
```

### Task 'prepareKotlinBuildScriptModel' not found in project ':app'
Add these lines at the end of build.gradle.
```
task prepareKotlinBuildScriptModel {
}
```

### SDK location not found
Find the file local.properties. If it doesn't exist, create it.

Inside, ensure that the uncommented line looks like this, where [YOUR PC USERNAME] is your PC username. This is for Windows. Path might be somewhat different for other OS. 
```
sdk.dir=C\:\\Users\\[YOUR PC USERNAME]\\AppData\\Local\\Android\\Sdk
```

## Development

If you're unfamiliar with Android studio, read this guide on project folder structure: [Guide](https://www.geeksforgeeks.org/android-project-folder-structure/).

The code of interest is in `Innovator/app/src/main`.

`ProgressBarActivity.java` runs on launch, which leads into `MainMenuController.java`. From there, you can trace the code to the page that you're working on. 

## Contributing

Pull requests are welcome. If you're unsure, contact your current Innovator Director or Developer Lead.
