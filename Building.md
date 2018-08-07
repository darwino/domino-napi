# Building the NAPI

There are two components to the NAPI, each with their own environment requirements to build.

## Native Code 

The C++ code is in the `dwo-domino-napi-project` and has a few dependencies that must be configured separately:

* The header files from the "include" folder of the [IBM C API Toolkit for Notes/Domino 9.0.1](https://www.ibm.com/developerworks/develop/collaboration/index.html), placed in the "dependencies/notes" directory in that project