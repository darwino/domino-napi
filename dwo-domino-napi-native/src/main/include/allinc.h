/**
 * Copyright © 2014-2018 Darwino, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef _ALLINC_H_
#define _ALLINC_H_

// Set the JNI FULLDEBUG capability
#ifdef _DEBUG // as set by the MS compiler
	#define	FULLDEBUG	1
#endif

// Notes API headers

#include <assert.h>

// Standard C and C++ library
#include <limits.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <inttypes.h>

// Platform-specific tweaks
#ifdef MAC
	// JNI headers from SUN
	#include <jni.h>
	#define stricmp strcasecmp
	#define _stricmp strcasecmp
	#define __int64 long long
	#define _ultoa_s ultoa
	#define _ltoa_s ltoa
	#define sprintf_s sprintf
	#define FALSE 0
#elif LINUX
	#include <jni.h>
	#define stricmp strcasecmp
	#define _stricmp strcasecmp
	#define __int64 long long
	#define _ultoa_s ultoa
	#define _ltoa_s ltoa
	#define sprintf_s sprintf
	#define FALSE 0
#else
	#include <jni.h>
#endif

// Notes headers

#include    <global.h>
//#include    <oslocal.h>
#include    <osmem.h>
#include    <osmisc.h>
#include    <osfile.h>
#include    <ostime.h>
#include    <osenv.h>
#include    <misc.h>
#include    <miscerr.h>
#include    <intl.h>
#include    <kfm.h>
#include    <nsfdb.h>
#include    <nsfdata.h>
#include    <nsfnote.h>
#include    <nsfsearc.h>
#include    <ods.h>
#include    <odstypes.h>
#include    <editods.h>
#include    <nif.h>
#include    <nifcoll.h>
#include    <viewfmt.h>
#include    <editods.h>
#include    <editdflt.h>
#include    <fontid.h>
#include    <stdnames.h>
#include	<xml.h>
#include	<idtable.h>
#include    <dsapi.h>
#include    <nls.h>
#include    <colorid.h>
#include	<easycd.h>
#include	<textlist.h>
#include	<nsferr.h>
#include    <acl.h>
//#include	<ptrhndl.h>
//#include	<javaconv.h>
#include	<htmlapi.h>
//#include	<ces.h>
#include	<misc.h>
#include	<mime.h>
#include	<mimedir.h>
#include	<mimeods.h>
#include	<nsfmime.h>
#include	<ods.h>
#include	<odserr.h>
#include	<oserr.h>
#include	<nsfobjec.h>
#include	<foldman.h>
#include	<idvault.h>
#include	<ns.h>

#include	"jnihelper.h"
#include	"noteshelper.h"
#include	"notesproc.h"
#include	"notesext.h"

#define	STRCPY	strcpy_s
#define	STRICMP	_stricmp

// Used by the STRUCT files
#define OFFSET(STRUCT,MEMBER) ((jint)(size_t)((char *)&((STRUCT*)0)->MEMBER - (char *)(STRUCT*)0 ))

#endif
