@REM
@REM Copyright © 2014-2018 Darwino, Inc.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM     http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

echo "Configuring for Win32"
rmdir /s /q build-win32
mkdir build-win32 & pushd build-win32
set DARWINO_BUILDTYPE=Win32
cmake -G "Visual Studio 12 2013" ..
@IF %ERRORLEVEL% GEQ 1 EXIT /B %ERRORLEVEL%
popd

del CMakeCache.txt

echo "Building for Win32"
cmake --build build-win32 --config Debug
@IF %ERRORLEVEL% GEQ 1 EXIT /B %ERRORLEVEL%

copy build-win32\Debug\*.dll lib