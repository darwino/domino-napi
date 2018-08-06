/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc 2014-2015.
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.     
 */

#include "allinc.h"

#include <string>
#include <sstream>

/////////////////////////////////////////////////////////////////////
// Global variables
/////////////////////////////////////////////////////////////////////

JavaVM* JNIUtils::globalVM = 0;

jboolean JNIUtils::FLAG_ENABLE_TRACE	= false;
jboolean JNIUtils::FLAG_TRACE_FUNCTION	= false;

TraceFunction::TraceFunction(const char* fctName,
						const char* fmt,
						StringParameter p1,
						StringParameter p2,
						StringParameter p3,
						StringParameter p4,
						StringParameter p5,
						StringParameter p6,
						StringParameter p7,
						StringParameter p8,
						StringParameter p9,
						StringParameter p10 ) {
	this->fctName = fctName;
	if(JNIUtils::FLAG_TRACE_FUNCTION) {
		if(fmt) {
			char b[1024];
			FormatStr(b,sizeof(b),fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
			jprintln("Entering Function {0}, {1}",fctName,b);
		} else {
		jprintln("Entering Function {0}",fctName);
	}
}
}

TraceFunction::~TraceFunction() {
	if(JNIUtils::FLAG_TRACE_FUNCTION) {
		jprintln("Exiting Function {0}",fctName);
	}
}

void jprintln(JNIEnv* env, const char* fmt, 
						StringParameter p1,
						StringParameter p2,
						StringParameter p3,
						StringParameter p4,
						StringParameter p5,
						StringParameter p6,
						StringParameter p7,
						StringParameter p8,
						StringParameter p9,
						StringParameter p10 ) {
	char c[1024]; FormatStr(c,sizeof(c),fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
	JNIUtils::println(env,env->NewStringUTF(c));
}
void jprintln(const char* fmt, 
						StringParameter p1,
						StringParameter p2,
						StringParameter p3,
						StringParameter p4,
						StringParameter p5,
						StringParameter p6,
						StringParameter p7,
						StringParameter p8,
						StringParameter p9,
						StringParameter p10 ) {
	JNIEnv* env;
    if (JNI_OK != JNIUtils::globalVM->GetEnv((void **)&env, JNI_VERSION_1_2)) {
        return;
	}
    jprintln(env,fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
}

#ifdef LINUX64
	// Request an older version of memcpy from glibc (GLIBC_2.2.5) for compatibility
	__asm__(".symver memcpy,memcpy@GLIBC_2.2.5");
	void *__wrap_memcpy(void *dest, const void *src, size_t n)
	{
	    return memcpy(dest, src, n);
	}
#endif

/////////////////////////////////////////////////////////////////////
// JNIUtils
/////////////////////////////////////////////////////////////////////

jclass		JNIUtils::jniClass = 0;
jmethodID	JNIUtils::m_createException = 0;
jmethodID	JNIUtils::m_createFormulaException = 0;
jmethodID	JNIUtils::m_createExceptionWithoutStackTrace = 0;
jmethodID	JNIUtils::m_flush = 0;
jmethodID	JNIUtils::m_print = 0;
jmethodID	JNIUtils::m_println = 0;
jmethodID	JNIUtils::m_addObjectToList = 0;
jmethodID	JNIUtils::m_addIntToList = 0;
jmethodID	JNIUtils::m_addLongToList = 0;

bool JNIUtils::init(JavaVM* globalVM, JNIEnv* env) {
	JNIUtils::globalVM = globalVM;

	// Load the class
    if(!JNIFindClass(env,jniClass,"com/darwino/domino/napi/JNIUtils")) return false;

    //    public static Object createException(int cause, String msg)
	if(!JNIGetStaticMethodID( env, m_createException, jniClass, "createException", "(ILjava/lang/String;)Ljava/lang/Object;")) return false;
	//    public static Object createFormulaException(int cause, String msg, int errorLine, int errorColumn, int errorOffset, int errorLength)
	if (!JNIGetStaticMethodID(env, m_createFormulaException, jniClass, "createFormulaException", "(ILjava/lang/String;ILjava/lang/String;IIII)Ljava/lang/Object;")) return false;
	// public static Object createExceptionWithoutStackTrace(int cause, String msg)
	if (!JNIGetStaticMethodID(env, m_createExceptionWithoutStackTrace, jniClass, "createExceptionWithoutStackTrace", "(ILjava/lang/String;)Ljava/lang/Object;")) return false;
    //    public static void flush()
	if(!JNIGetStaticMethodID( env, m_flush, jniClass, "_flush", "()V")) return false;
    //    public static void print(String s)
	if(!JNIGetStaticMethodID( env, m_print, jniClass, "_print", "(Ljava/lang/String;)V")) return false;
    //    public static void println(String s)
	if(!JNIGetStaticMethodID( env, m_println, jniClass, "_println", "(Ljava/lang/String;)V")) return false;
    //    public static void addObjectToList(List list, Object v)
	if(!JNIGetStaticMethodID( env, m_addObjectToList, jniClass, "_addObjectToList", "(Ljava/util/List;Ljava/lang/Object;)V")) return false;
    //    public static void addIntToList(List list, int v)
	if(!JNIGetStaticMethodID( env, m_addIntToList, jniClass, "_addIntToList", "(Ljava/util/List;I)V")) return false;
    //    public static void addLongToList(List list, long v)
	if(!JNIGetStaticMethodID( env, m_addLongToList, jniClass, "_addLongToList", "(Ljava/util/List;J)V")) return false;

	return true;
}

JNIEnv* JNIUtils::getJNIEnv() 
{
	JNIEnv* env;
	if (JNI_OK != globalVM->GetEnv((void **)&env, JNI_VERSION)) {
		return NULL;
	}
	return env;
}


void JNIUtils::throwex(JNIEnv *env)
{
	throwex(env,(const char*)NULL);
}

void JNIUtils::throwex(JNIEnv *env, const char *str,	
						StringParameter p1,
						StringParameter p2,
						StringParameter p3,
						StringParameter p4,
						StringParameter p5,
						StringParameter p6,
						StringParameter p7,
						StringParameter p8,
						StringParameter p9,
						StringParameter p10)

{
	// Format the string and throw it...
	jstring js = FormatJString(env,str,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
	throwex(env,js);
}

void JNIUtils::throwex(JNIEnv *env, jstring js) {
    // Clear the pending exception status
    env->ExceptionDescribe();
    env->ExceptionClear();

	// Format the string and throw it...
	jthrowable ex = (jthrowable)env->CallStaticObjectMethod(jniClass,m_createException,js);
	env->Throw(ex);
}


/////////////////////////////////////////////////////////////////////
// Static helpers
/////////////////////////////////////////////////////////////////////


bool JNIFindClass(JNIEnv* env, jclass& c, const char* className)
{
    c = env->FindClass(className);
    if (!c) return false;
    c = (jclass)env->NewGlobalRef(c);
	return true;
}

bool JNIGetStaticMethodID(JNIEnv* env, jmethodID& m, jclass c, const char* methodName, const char* sig)
{
    m = env->GetStaticMethodID( c, methodName, sig );	
    return m!=NULL;
}

bool JNIGetMethodID(JNIEnv* env, jmethodID& m, jclass c, const char* methodName, const char* sig)
{
    m = env->GetMethodID( c, methodName, sig );	
    return m!=NULL;
}

bool JNIGetStaticFieldID(JNIEnv* env, jfieldID& f, jclass c, const char* fieldName, const char* sig) 
{
	f = env->GetStaticFieldID( c, fieldName, sig);	
    return f!=NULL;
}

bool JNIGetFieldID(JNIEnv* env, jfieldID& f, jclass c, const char* fieldName, const char* sig) 
{
	f = env->GetFieldID( c, fieldName, sig);	
    return f!=NULL;
}


/* Returns number of 16-bit blocks in UTF-16 string, not including null. */
jsize jstrlen(const jchar *str)
{
    const jchar *s;
    for (s = str; *s; s++);
    return (jsize)(s - str);
}


/////////////////////////////////////////////////////////////////////
// StringBuilder
/////////////////////////////////////////////////////////////////////

StringBuilder::StringBuilder() {
	this->count = 0;
	this->bufferSize = sizeof(buffer)-1;
	this->str = buffer;
}

StringBuilder::~StringBuilder() {
	if(str!=buffer) {
		free(str);
	}
}

void StringBuilder::ensure(size_t capacity) {
	if(count+capacity>bufferSize) {
		size_t	 newl = count+capacity+256;
		char* news = (char*)MemAlloc(newl+1);
		memcpy(news,str,count);
		if(str!=buffer) {
			free(str);
		}
		str = news;
		bufferSize = newl;
	}
}

BYTE* StringBuilder::getBytes() {
	return (BYTE*)str;
}

const char* StringBuilder::toString() {
	str[count] = 0;
	return str;
}

const char* StringBuilder::copy() {
	char* s = (char*)MemAlloc(count+1);
	memcpy(s,str,count);
	s[count]=0;
	return s;
}

void StringBuilder::append(char c) {
	ensure(1);
	str[count++] = c;
}

void StringBuilder::append(const char* s) {
	if(s) {
		size_t len = strlen(s);
		append(s,len);
	}
}

void StringBuilder::append(const char* s, size_t length) {
	ensure(length);
	memcpy(str+count,s,length);
	count += length;
}

jbyteArray StringBuilder::toByteArray(JNIEnv* env) {
	jbyteArray jb=env->NewByteArray((jsize)length());
    env->SetByteArrayRegion(jb, 0, (jsize)length(), (jbyte*)getBytes());
	return jb;
}


/////////////////////////////////////////////////////////////////////
// StringBuilder UTF16
/////////////////////////////////////////////////////////////////////

StringBuilder16::StringBuilder16() {
	this->count = 0;
	this->bufferSize = sizeof(buffer)/sizeof(jchar)-1;
	this->str = buffer;
}

StringBuilder16::~StringBuilder16() {
	if(str!=buffer) {
		free(str);
	}
}

void StringBuilder16::ensure(size_t capacity) {
	if(count+(jsize)capacity>bufferSize) {
		// We always alocate an extra char so we can add a trailing \0
		jsize  newl = count+(jsize)capacity+256;
		jchar* news = (jchar*)MemAlloc((newl+1)*sizeof(jchar));
		memcpy(news,str,count*sizeof(jchar));
		if(str!=buffer) {
			free(str);
		}
		str = news;
		bufferSize = newl;
	}
}

const jchar* StringBuilder16::toString() {
	str[count] = 0;
	return str;
}

const jchar* StringBuilder16::copy() {
	jchar* s = (jchar*)MemAlloc((count+1)*sizeof(jchar));
	memcpy(s,str,count*sizeof(jchar));
	s[count]=0;
	return s;
}

void StringBuilder16::append(jchar c) {
	ensure(1);
	str[count++] = c;
}

void StringBuilder16::append(const jchar* s) {
	if(s) {
		append(s,jstrlen(s));
	}
}

void StringBuilder16::append(const jchar* s, size_t length) {
	if(s && length) {
		ensure(length);
		memcpy(str+count,s,length*sizeof(jchar));
		count += (jsize)length;
	}
}

void StringBuilder16::appendChar(char c) {
	append((jchar)c);
}

void StringBuilder16::appendChar(const char* c) {
	if(c) {
		for( ;*c; c++) {
			append((jchar)*c);
		}
	}
}

void StringBuilder16::appendChar(const char* c, int length) {
	if(c) {
		for(int i=0; i<length; i++) {
			append((jchar)(c[i]));
		}
	}
}


/////////////////////////////////////////////////////////////////////
// String parser
/////////////////////////////////////////////////////////////////////

#define TRACE_PARSER 0

StringParser::StringParser(const char* str) {
	this->str = str;
	this->length = strlen(str);
	this->ptr = 0;
}

bool StringParser::eof() {
	while(ptr<length && str[ptr]==' ') {
		ptr++;
	}
	return ptr==length;
}

bool StringParser::isString() {
	if(!eof()) {
		return str[ptr]=='"';
	}
	return false;
}

bool StringParser::isObject() {
	if(!eof()) {
		return str[ptr]=='{';
	}
	return false;
}

jint StringParser::readInt() {
	if(!eof()) {
		bool minus = false;
		if(str[ptr]=='-') {
			minus = true;
			ptr++;
		}
		jint v = 0;
		while(ptr<length) {
			char c=str[ptr];
			if(c>='0' && c<='9') {
				v = v*10 + (c-'0');
				ptr++;
			} else {
				break;
			}
		}
		if(TRACE_PARSER) {
			jprintln("READ INT: {0}",minus?-v:v);
		}
		return minus?-v:v;
	}
	return 0;
}

const char* StringParser::readString() {
	if(!eof()) {
		if(str[ptr]!='\"') {
			if(TRACE_PARSER) {
				jprintln("READ STRING: NULL/2");
			}
			return NULL;
		}
		ptr++;
		size_t start = ptr;
		while(ptr<length) {
			if(str[ptr]=='\"') {
				size_t len = ptr-start;
				char* s = (char*)MemAlloc(len+1);
				memcpy(s,str+start,len);
				s[len] = 0;
				ptr++;
				if(TRACE_PARSER) {
					jprintln("READ STRING: '{0}'",s);
				}
				return s;
			}
			// TODO: decode?
			ptr++;
		}
	}
	if(TRACE_PARSER) {
		jprintln("READ STRING: NULL/1");
	}
	return NULL;
}

// TODO: optimize that!
jstring StringParser::readJString() {
	if(!eof()) {
		if(str[ptr]!='\"') {
			if(TRACE_PARSER) {
				jprintln("READ STRING: NULL/2");
			}
			return NULL;
		}
		ptr++;
		size_t start = ptr;
		while(ptr<length) {
			if(str[ptr]=='\"') {
				size_t len = ptr-start;
				char* s = (char*)MemAlloc(len+1);
				memcpy(s,str+start,len);
				s[len] = 0;
				ptr++;
				if(TRACE_PARSER) {
					jprintln("READ STRING: '{0}'",s);
				}
				jstring js = JNIUtils::getJNIEnv()->NewStringUTF(s);
				MemFree(s);
				return js;
			}
			// TODO: decode?
			ptr++;
		}
	}
	if(TRACE_PARSER) {
		jprintln("READ STRING: NULL/1");
	}
	return NULL;
}


bool StringParser::readSeparator(char sep) {
	if(!eof()) {
		if(str[ptr]==sep) {
			ptr++;
			return true;
		}
	}
	return false;
}

bool StringParser::startObject() {
	if(!eof()) {
		if(str[ptr]=='{') {
			ptr++;
			return true;
		}
	}
	return false;
}

bool StringParser::endObject() {
	if(!eof()) {
		if(str[ptr]=='}') {
			ptr++;
			return true;
		}
	}
	return false;
}



////////////////////////////////////////////////////////////////////////////////////////////////////////
// String formatting
////////////////////////////////////////////////////////////////////////////////////////////////////////

const char* FormatStr( char* dest, int destSize, const char* fmt, StringParameter p1, StringParameter p2, StringParameter p3, StringParameter p4, StringParameter p5, StringParameter p6, StringParameter p7, StringParameter p8, StringParameter p9, StringParameter p10 ) {
    const char* destsav = dest;

    // Scan each character
	if(fmt) {
		// Scan each character
		for( unsigned len=destSize-1; *fmt && len; fmt++ ) {
			if( fmt[0]== '{' && fmt[1]!=0 && fmt[2] == '}') {
				// Find the param
				const char* param = NULL;
				switch( fmt[1] ) {
					case '0':   param = p1;     break;
					case '1':   param = p2;     break;
					case '2':   param = p3;     break;
					case '3':   param = p4;     break;
					case '4':   param = p5;     break;
					case '5':   param = p6;     break;
					case '6':   param = p7;     break;
					case '7':   param = p8;     break;
					case '8':   param = p9;     break;
					case '9':   param = p10;    break;
				}

				// And add it, converted
				if( param ) {
					while( *param && len ) { 
						*dest++ = *param++; len--; 
					}
				} else {
					*dest++='{'; 
					*dest++=fmt[1];
					*dest++='}'; 
				}
				fmt+=2;
			} else {
				// Simply add the character
				*dest++ = *fmt; len--;
			}
		}
	}

    // return a "C" string
    *dest = 0; return destsav;
}

jstring FormatJString(JNIEnv* env, const char* fmt, 
						StringParameter p1,
						StringParameter p2,
						StringParameter p3,
						StringParameter p4,
						StringParameter p5,
						StringParameter p6,
						StringParameter p7,
						StringParameter p8,
						StringParameter p9,
						StringParameter p10 ) {
	char c[1024]; FormatStr(c,sizeof(c),fmt,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
	return env->NewStringUTF(c);
}



////////////////////////////////////////////////////////////////////////////////////////////////////////
// Notes Error handling
////////////////////////////////////////////////////////////////////////////////////////////////////////

// Converters
StringParameter::StringParameter( const StringParameter& from )
    : text(from.text) {
}

StringParameter::StringParameter( const char* text )
    : text(text) {
}

StringParameter::StringParameter( const char* text, int length ) {
	this->text = "";
	if(text && length) {
		size_t len = min(MAXBUFFER,length-1);
		memcpy(buffer,text,len);
		buffer[len] = 0;
	}
}

StringParameter::StringParameter( MemString& str )
: text(str) {
}

StringParameter::StringParameter( jstring js ) {
	initJString(js);
}

StringParameter::StringParameter( const jchar* text ) {
	// Not the most efficient, but enough for debugging purposes
	JNIEnv* jniEnv = JNIUtils::getJNIEnv();
	jstring js = jniEnv->NewString(text,jstrlen(text));
	initJString(js);
}

StringParameter::StringParameter( const jchar* text, int length ) {
	// Not the most efficient, but enough for debugging purposes
	JNIEnv* jniEnv = JNIUtils::getJNIEnv();
	if(length<0) length = jstrlen(text);
	jstring js = jniEnv->NewString(text,length);
	initJString(js);
}

StringParameter::StringParameter( char ch ) {
	buffer[0] = ch;
	buffer[1] = 0;
	text = buffer;
}

StringParameter::StringParameter( unsigned char value ) {
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}

StringParameter::StringParameter( short value ) {             
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}

StringParameter::StringParameter( unsigned short value ) {
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}


StringParameter::StringParameter( int value ) {             
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}

StringParameter::StringParameter( unsigned int value ) {
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}

StringParameter::StringParameter( long value ) {
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}

StringParameter::StringParameter( unsigned long value ) {
	std::stringstream stream;
	stream << value;

	text = stream.str().c_str();
}
StringParameter::StringParameter(__int64 value ) {
	sprintf_s( buffer, "%" PRId64, value );
    text = buffer;
}

StringParameter::StringParameter( double value ) {
	sprintf_s( buffer, "%f", value );
    text = buffer;
}

StringParameter::StringParameter( bool value ) {
    text = value ? "true" : "false";
}

StringParameter::StringParameter( void* value ) {
    sprintf_s( buffer, "%p", value );
    text = buffer;
}

void StringParameter::initJString(jstring js) {
	this->text = "";
	if(js) {
		JNIEnv* jniEnv = JNIUtils::getJNIEnv();
		jsize jcsize = jniEnv->GetStringLength(js);
		if(jcsize>0) {
			jsize len = (jsize)min(MAXBUFFER/3,jcsize);
			memset(buffer,0,MAXBUFFER);
			jniEnv->GetStringUTFRegion( js, 0, len, buffer );
			text = buffer;
		}
	}
}
