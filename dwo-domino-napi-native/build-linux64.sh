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


echo "Configuring for Linux 64-bit"
rm -rf build-linux64
mkdir build-linux64 && cd build-linux64
CC=gcc CPP=g++ DARWINO_BUILDTYPE=linux64 cmake cmake .. || exit 1
cd ..

rm -f CMakeCache.txt

echo "Building for Linux 64-bit"
cmake --build build-linux64 --config Debug || exit 1

cp build-linux64/*.so lib
