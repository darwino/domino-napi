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

cmake_minimum_required(VERSION 3.0)

## start setting
SET (this_target DominoNativeApi)
PROJECT(${this_target})

# Establish platform-specific information
# /sdl = <SDLCheck>true</SDLCheck>
# /W3 = warning level 3
# /EHsc = C++ exceptions
# /Gm = Enable minimal rebuild
set (windows_common_flags_msvc "/sdl /W3 /EHsc /Gm")
set (windows_common_flags_msvc_link "/SUBSYSTEM:WINDOWS")
if($ENV{DARWINO_BUILDTYPE} STREQUAL "Win32")
	####################################################################################
	# Windows 32-bit
	####################################################################################
	
	message (STATUS "Using Win32")
	find_package(JNI REQUIRED)
	
	add_definitions("-DWIN32 -D_WINDOWS -D_WINDLL -D_USRDLL -D_X86_=1 -DWIN32 -D_WIN32 -W3 -DHANDLE_IS_32BITS -DNT -DW32 -DW -DW64 /DNT -D_CRT_SECURE_NO_WARNINGS")
	
	if(MSVC)
		# /ZI = debug information format
		set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS_DEBUG} ${windows_common_flags_msvc} /ZI")
		set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} ${windows_common_flags_msvc} /ZI")
		set (CMAKE_SHARED_LINKER_FLAGS "${windows_common_flags_msvc_link}")
		set (notes_lib ${CMAKE_CURRENT_LIST_DIR}/dependencies/lib/notes/mswin32/notes.lib)
	elseif(MINGW)
		# Note: MinGW building currently crashes on DLL use
		set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS_DEBUG} -fpermissive -std=c++11")
		set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -fpermissive -std=c++11")
		set (notes_lib ${CMAKE_CURRENT_LIST_DIR}/dependencies/lib/notes/mswin32/libnnotes.a)
	endif()
	
	# Set static runtime for Domino
	foreach(flag_var
        CMAKE_CXX_FLAGS CMAKE_CXX_FLAGS_DEBUG CMAKE_CXX_FLAGS_RELEASE
        CMAKE_CXX_FLAGS_MINSIZEREL CMAKE_CXX_FLAGS_RELWITHDEBINFO)
		if(${flag_var} MATCHES "/MD")
			string(REGEX REPLACE "/MD" "/MT" ${flag_var} "${${flag_var}}")
		endif(${flag_var} MATCHES "/MD")
	endforeach(flag_var)
	
	set (dll_name domino-win32-x86)
elseif($ENV{DARWINO_BUILDTYPE} STREQUAL "Win64")
	####################################################################################
	# Windows 64-bit
	####################################################################################
	
	message (STATUS "Using Win64")
	find_package(JNI REQUIRED)
	
	add_definitions("-DWIN32 -DNDEBUG -D_WINDOWS -D_WINDLL -D_USRDLL -DDOMINOSQL_EXPORTS -D_AMD64_ -DHANDLE_IS_32BITS -DNT -DW32 -DW -DW64 -DND64 -D_CRT_SECURE_NO_WARNINGS")
	
	if(MSVC)
		# /Zi = debug information format
		set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS_DEBUG} ${windows_common_flags_msvc} /Zi")
		set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} ${windows_common_flags_msvc} /Zi")
		set (CMAKE_SHARED_LINKER_FLAGS "${windows_common_flags_msvc_link}")
		set (notes_lib ${CMAKE_CURRENT_LIST_DIR}/dependencies/lib/notes/mswin64/notes.lib)
	elseif(MINGW)
		# Note: MinGW building currently crashes on DLL use
		set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS_DEBUG} -fpermissive -std=c++11")
		set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -fpermissive -std=c++11")
		set (notes_lib ${CMAKE_CURRENT_LIST_DIR}/dependencies/lib/notes/mswin64/libnnotes.a)
	endif()
	
	# Set static runtime for Domino
	foreach(flag_var
        CMAKE_CXX_FLAGS CMAKE_CXX_FLAGS_DEBUG CMAKE_CXX_FLAGS_RELEASE
        CMAKE_CXX_FLAGS_MINSIZEREL CMAKE_CXX_FLAGS_RELWITHDEBINFO)
		if(${flag_var} MATCHES "/MD")
			string(REGEX REPLACE "/MD" "/MT" ${flag_var} "${${flag_var}}")
		endif(${flag_var} MATCHES "/MD")
	endforeach(flag_var)
	
	set (dll_name domino-win32-x64)
elseif($ENV{DARWINO_BUILDTYPE} STREQUAL "macosx64")
	####################################################################################
	# Mac OS X 64-bit
	####################################################################################
	
	message (STATUS "Using Mac OS X 64-bit")

	set(JAVA_INCLUDE_PATH $ENV{JAVA_HOME}/include $ENV{JAVA_HOME}/include/darwin)
	
	set(CMAKE_MACOSX_RPATH 1)
	
	#set (notes_lib ${CMAKE_CURRENT_LIST_DIR}/dependencies/lib/notes/macosx64/notes0.o)
	set (notes_lib "$ENV{Notes_ExecDirectory}/libnotes.dylib")
	
	add_definitions(-DMAC -DMAC_OSX -DMAC_CARBON -D__CF_USE_FRAMEWORK_INCLUDES__ -DLARGE64_FILES -DHANDLE_IS_32BITS -DTARGET_API_MAC_CARBON -DTARGET_API_MAC_OS8=0   -DPRODUCTION_VERSION  -DOVERRIDEDEBUG -DND64)
	
	set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DGCC3 -DGCC4 -DGCC_LBLB_NOT_SUPPORTED    -mmacosx-version-min=10.5 -dynamic  -O3 -fpascal-strings -Wmissing-declarations -Wimplicit-function-declaration -Wno-deprecated-declarations -Wreturn-type -std=c++11")
	set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -DGCC3 -DGCC4 -DGCC_LBLB_NOT_SUPPORTED    -mmacosx-version-min=10.5 -dynamic -fpascal-strings -Wmissing-declarations -Wimplicit-function-declaration -Wno-deprecated-declarations -Wreturn-type -std=c++11")
	set (CMAKE_SHARED_LINKER_FLAGS "-felide-constructors")
	
	set (dll_name domino-macosx-x64)
elseif($ENV{DARWINO_BUILDTYPE} STREQUAL "linux64")
	####################################################################################
	# Linux 64-bit
	####################################################################################
	
	message (STATUS "Using Linux 64-bit")
	find_package(JNI REQUIRED)
	
	set (notes_lib $ENV{Notes_ExecDirectory}/libnotes.so)
	#set (notes_lib "")
	
	add_definitions("-DGCC3 -DGCC4 -fno-strict-aliasing -DGCC_LBLB_NOT_SUPPORTED -Wformat -Wall -Wcast-align  -DUNIX -DLINUX -DLINUX86 -DND64 -DW32 -DLINUX64 -DW -DLINUX86_64 -DDTRACE -DPTHREAD_KERNEL -D_REENTRANT -DUSE_THREADSAFE_INTERFACES -D_POSIX_THREAD_SAFE_FUNCTIONS  -DHANDLE_IS_32BITS -DHAS_IOCP -DHAS_BOOL -DHAS_DLOPEN -DUSE_PTHREAD_INTERFACES -DLARGE64_FILES -D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE -DNDUNIX64 -DLONGIS64BIT   -DPRODUCTION_VERSION -DOVERRIDEDEBUG -fPIC")
	
	set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -m64 -std=c++11 -static-libgcc")
	set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -m64 -std=c++11 -static-libgcc")
	set (CMAKE_SHARED_LINKER_FLAGS "-lnotes -lpthread -lm -lnsl -lresolv -dl -Wl,--wrap=memcpy")
	
	set (dll_name domino-linux-x64)
elseif($ENV{DARWINO_BUILDTYPE} STREQUAL "linux32")
	####################################################################################
	# Linux 32-bit
	####################################################################################
	
	message (STATUS "Using Linux 32-bit")
	find_package(JNI REQUIRED)
	
	set (notes_lib $ENV{Notes_ExecDirectory}/libnotes.so)
	#set (notes_lib "")
	
	add_definitions("-DGCC3 -DGCC4 -fno-strict-aliasing -DGCC_LBLB_NOT_SUPPORTED -Wformat -Wall -Wcast-align  -DUNIX -DLINUX -DLINUX86 -DW32 -DW -DDTRACE -DPTHREAD_KERNEL -D_REENTRANT -DUSE_THREADSAFE_INTERFACES -D_POSIX_THREAD_SAFE_FUNCTIONS  -DHANDLE_IS_32BITS -DHAS_IOCP -DHAS_BOOL -DHAS_DLOPEN -DUSE_PTHREAD_INTERFACES -DLARGE64_FILES -D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE -DPRODUCTION_VERSION -DOVERRIDEDEBUG -fPIC")
	
	set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -m32 -std=c++11 -static-libgcc")
	set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -m32 -std=c++11 -static-libgcc")
	set (CMAKE_SHARED_LINKER_FLAGS "-lnotes -lpthread -lm -lnsl -lresolv -dl")
	
	set (dll_name domino-linux-x86)
else()
	message (STATUS "Could not determine build type from DARWINO_BUILDTYPE env")
endif()

## section: include directory
INCLUDE_DIRECTORIES(
	${JAVA_INCLUDE_PATH}
	dependencies/inc/jre
	dependencies/inc/notes
	src/main/include
  )

## section: source files
SET (${this_target}_SOURCE_FILES
    src/main/c++/C.cpp
	src/main/c++/CRefs.cpp
	src/main/c++/JNIHelper.cpp
	src/main/c++/jni/global.cpp
	src/main/c++/jni/acl.cpp
	src/main/c++/jni/foldman.cpp
	src/main/c++/jni/htmlapi.cpp
	src/main/c++/jni/idtable.cpp
	src/main/c++/jni/idvault.cpp
	src/main/c++/jni/kfm.cpp
	src/main/c++/jni/log.cpp
	src/main/c++/jni/mime.cpp
	src/main/c++/jni/mimedir.cpp
	src/main/c++/jni/misc.cpp
	src/main/c++/jni/nif.cpp
	src/main/c++/jni/nls.cpp
	src/main/c++/jni/ns.cpp
	src/main/c++/jni/nsfdb.cpp
	src/main/c++/jni/nsfmime.cpp
	src/main/c++/jni/nsfnote.cpp
	src/main/c++/jni/nsfobjec.cpp
	src/main/c++/jni/nsfsearc.cpp
	src/main/c++/jni/ods.cpp
	src/main/c++/jni/osenv.cpp
	src/main/c++/jni/osfile.cpp
	src/main/c++/jni/osmem.cpp
	src/main/c++/jni/osmisc.cpp
	src/main/c++/jni/ostime.cpp
	src/main/c++/MainDLL.cpp
	src/main/c++/NotesHelpers.cpp
	src/main/c++/PROC.cpp
	src/main/c++/STRUCT.cpp
    )

## section: header files
SET(${this_target}_HEADER_FILES
    "src/main/include/allinc.h"
	"src/main/include/jnihelper.h"
	"src/main/include/notesext.h"
	"src/main/include/noteshelper.h"
	"src/main/include/notesproc.h"
    )

SET_SOURCE_FILES_PROPERTIES(${this_target}_HEADER_FILES
                            PROPERTIES HEADER_FILE_ONLY TRUE)
LIST(APPEND ${this_target}_SOURCE_FILES ${${this_target}_HEADER_FILES})

## section: add target

ADD_LIBRARY(${dll_name} SHARED ${${this_target}_SOURCE_FILES} )

## section: set link libraries
TARGET_LINK_LIBRARIES( ${dll_name} ${notes_lib} )
