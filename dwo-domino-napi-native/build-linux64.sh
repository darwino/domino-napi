#!/bin/sh

echo "Configuring for Linux 64-bit"
rm -rf build-linux64
mkdir build-linux64 && cd build-linux64
CC=gcc CPP=g++ DARWINO_BUILDTYPE=linux64 cmake cmake .. || exit 1
cd ..

rm -f CMakeCache.txt

echo "Building for Linux 64-bit"
cmake --build build-linux64 --config Debug || exit 1

cp build-linux64/*.so lib
