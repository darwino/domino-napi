#!/bin/sh

echo "Configuring for Eclipse"
rm -rf build-eclipse
mkdir build-eclipse && cd build-eclipse
CC=gcc CPP=g++ DARWINO_BUILDTYPE=macosx64 /usr/local/bin/cmake .. -G "Eclipse CDT4 - Unix Makefiles" -DCMAKE_ECLIPSE_VERSION=4.6 || exit 1
cd ..

echo "Configuring for Xcode"
rm -rf build-xcode
mkdir build-xcode && cd build-xcode
CC=gcc CPP=g++ DARWINO_BUILDTYPE=macosx64 /usr/local/bin/cmake .. -G Xcode || exit 1
cd ..

echo "Configuring for Mac OS X 64-bit"
rm -rf build-macosx64
mkdir build-macosx64 && cd build-macosx64
CC=gcc CPP=g++ DARWINO_BUILDTYPE=macosx64 /usr/local/bin/cmake cmake .. || exit 1
cd ..

rm -f CMakeCache.txt

echo "Building for Mac OS X 64-bit"
/usr/local/bin/cmake --build build-macosx64 --config Debug || exit 1

cp build-macosx64/*.dylib lib
