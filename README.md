# Sandvoxer
A 3D voxel-based game made with libGDX in the context of a game jam on itch.io. For now, it is only aimed at desktop usage.

## How to run

You can download and play the binaries at http://timmyotoole.itch.io/sandvoxer. If you are using Windows, you don't need to have Java installed. For Mac or Linux, you will have to install the Java Runtime Environement 1.7 or 1.8 and then run "java -jar sandvoxer-0.1.jar".

## Dependencies
This project is using the libGDX framework (version 1.9.10) which itself depends on LWJGL, GWT (not used in the current scope of the project), Gradle and other platform-specific libraries (like the Android SDK).

## How to build

- Install IntelliJ IDEA (community edition is sufficient) or Android Studio.
- Open build.gradle as a project.

## Potential improvements

- More efficient rendering (merge all voxels into a single mesh)
- Add more levels
- Add moving voxels (cubes that would move)
- Android and WebGL compatibility

## License

This project is under the Apache 2 License (see LICENSE file).
