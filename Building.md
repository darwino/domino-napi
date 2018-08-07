# Building the NAPI

There are two components to the NAPI, each with their own environment requirements to build.

## Native Code 

The C++ code is in the `dwo-domino-napi-native` folder and has a few dependencies that must be configured separately:

* The header files from the "include" folder of the [IBM C API Toolkit for Notes/Domino 9.0.1](https://www.ibm.com/developerworks/develop/collaboration/index.html), placed in the "dependencies/notes" directory in that project
* CMake, installed from https://cmake.org or via your OS's package manager
* A compatible C/C++ compiler toolchain:
    * macOS: XCode
    * Linux: gcc/g++
    * Windows: MSVC 15+
* Java JDK 6+ and `JAVA_HOME` set

## Java Code

The Java projects are within the `eclipse` folder and have a few dependencies:

* The native project's binary libraries must be available in an active Maven repository
* A p2 site for a version of Domino 9.0.1 or above and pointed to in URL format in the `notes-platform` Maven property in your settings.xml. The [IBM Domino Update Site for Build Management](https://openntf.org/main.nsf/project.xsp?r=project/IBM%20Domino%20Update%20Site%20for%20Build%20Management) works for this purpose.