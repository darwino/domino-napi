#!/bin/sh
#
# Copyright © 2014-2018 Darwino, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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
