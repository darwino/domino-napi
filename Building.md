# Building the NAPI

There are two components to the NAPI, each with their own environment requirements to build.

## Native Code 

The C++ code is in the `dwo-domino-napi-native` folder. Binary versions of the libraries are kept in the repo, and so it is not necessary to rebuild this unless there is a change on the C++ side. By default, the prebuilt files will be installed by a `mvn install`.

To do a build for each platform, there are several external dependencies to configure:

* The header files from the "include" folder of the [IBM C API Toolkit for Notes/Domino 9.0.1](https://www.ibm.com/developerworks/develop/collaboration/index.html), placed in the "dependencies/notes" directory in that project
* CMake, installed from https://cmake.org or via your OS's package manager
* A compatible C/C++ compiler toolchain:
    * macOS: XCode
    * Linux: gcc/g++
    * Windows: MSVC 15+
* Java JDK 6+ and `JAVA_HOME` set
* A bitness-compatible Notes or Domino runtime (e.g. the Notes client for 32-bit Windows or a 64-bit Domino server for 64-bit Windows) and the `notes-program` Maven property set in your settings.xml to the filesystem path to the program root

## Java Code

The Java projects are within the `eclipse` folder and have a few dependencies:

* The native project's binary libraries must be available in an active Maven repository
* A p2 site for a version of Domino 9.0.1 or above and pointed to in URL format in the `notes-platform` Maven property in your settings.xml. The [IBM Domino Update Site for Build Management](https://openntf.org/main.nsf/project.xsp?r=project/IBM%20Domino%20Update%20Site%20for%20Build%20Management) works for this purpose.