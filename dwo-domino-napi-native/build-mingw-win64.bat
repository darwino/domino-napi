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

echo "Configuring for Win64"
rmdir /s /q build-win64
mkdir build-win64 & pushd build-win64
set DARWINO_BUILDTYPE=Win64
cmake -G "MinGW Makefiles" ..
@IF %ERRORLEVEL% GEQ 1 EXIT /B %ERRORLEVEL%
popd

del CMakeCache.txt

echo "Building for Win64"
cmake --build build-win64 --config Debug
@IF %ERRORLEVEL% GEQ 1 EXIT /B %ERRORLEVEL%