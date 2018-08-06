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