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