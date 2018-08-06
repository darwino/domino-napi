#!/bin/sh

echo "Configuring for Linux 32-bit"
rm -rf build-linux32
mkdir build-linux32 && cd build-linux32
CC=gcc CPP=g++ DARWINO_BUILDTYPE=linux32 cmake cmake .. || exit 1
cd ..

rm -f CMakeCache.txt

echo "Building for Linux 32-bit"
cmake --build build-linux32 --config Debug || exit 1

cp build-linux32/*.so lib
