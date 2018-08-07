# The Darwino Domino NAPI

The [Darwino Domino NAPI](https://github.com/darwino/domino-napi) provides Java access to the Notes/Domino C API via JNI, as well as a set of wrapper classes for easier programming. It is similar to the NAPI included in Domino, but covers more functionality and is not proprietary.

## Usage

To access the bound C API functions directly, use the `com.darwino.domino.napi.DominoAPI` class's `.get()` method, which provides access to each of the C functions. Additionally, the `com.darwino.domino.napi.C` class provides static methods and properties for data-type sizing and memory access.

Beyond the direct accessor methods, there are several packages with encapsulation classes:

- `com.darwino.domino.napi.enums` contains Java-style enums for many of the Notes C enumerations
- `com.darwino.domino.napi.proc` contains abstract classes to implement callbacks in bound functions
- `com.darwino.domino.napi.struct` contains Java representations of Notes C structures, including data getters and setters. The `.cd` package contains composite data record types
- `com.darwino.domino.napi.wrap` contains high-level abstractions generally analagous to the core `lotus.domino` interfaces. The primary starting point is `NSFSession`, which can be instantiated directly or created based on an existing `lotus.domino.Session` object.

### Resource Management

Resource management in the NAPI is handled similarly to the `lotus.domino` API: each struct and wrapper class has `.free()` method to deallocate any associated C-side memory or objects, and each wrapper class maintains a view of its logical "child" wrapper objects and classes. Though all of these classes contain last-ditch calls to `.free()` in their `.finalize()` methods, this should not be relied upon for memory/resource management, as the JVM is inconsistent in handling it.

The direct `C` memory methods (e.g. `C.malloc`) provide no safety or tracking for their memory and so should be used just as they would be in C/C++.

### XPages Applications

To use the library in an XPage application, install the Update Site in Domino and Designer and enable the "com.darwino.domino.napi.xsp" library in the Xsp Properties file. This will add the NAPI classes to your project's classpath.

## Requirements/Supported Platforms

This requires a Notes or Domino runtime on a supported platform:

* Windows 32-bit
* Windows 64-bit
* macOS 64-bit
* Linux 32-bit
* Linux 64-bit

Additionally, the Java classes require Java 6 and IBM Commons, `javax.mail`+`javax.mail.internet`, and the legacy `lotus.domino` API (Notes.jar) on the classpath (enforced in OSGi). The XSP plugin and several API calls require a minimum Notes/Java version of 9.0.

## Building

As a JNI project, building can be complicated. See [Building](Building.md) for more information. In most cases, the native portion can reuse the existing built artifacts in the repository until there is a C++-side change.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for specifics and the [NOTICE](NOTICE.txt) for information about included third-party code.