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
package com.darwino.domino.napi.wrap;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.enums.CmdArgID;
import com.darwino.domino.napi.enums.CmdId;
import com.darwino.domino.napi.enums.UAT;
import com.darwino.domino.napi.struct.HTMLAPIReference;
import com.darwino.domino.napi.struct.HTMLAPI_URLArg;
import com.darwino.domino.napi.struct.HTMLAPI_URLTargetComponent;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.io.NSFTempFileInputStream;
import com.darwino.domino.napi.wrap.item.NSFFileItem;
import com.ibm.commons.Platform;
import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * <p>This object represents the process of converting a composite data or MIME item from
 * an NSF note (usually via {@link NSFNote}). It provides access to the HTML processor's
 * configuration options (via {@link #options(String...)}) as well as triggers for handling
 * of inline images and attachments.</p>
 * 
 * @author Jesse Gallagher
 *
 */
public class HTMLExporter {
	
	private static final String LOG_GROUP = "NAPI.HTMLExporter"; //$NON-NLS-1$
	private static final LogMgr log = Log.load(LOG_GROUP);
	static {
		log.setLogLevel(LogMgr.LOG_WARN_LEVEL);
	}
	public static void setLogLevel(int logLevel) {
		log.setLogLevel(logLevel);
	}
	
	/**
	 * When the converter comes across a rich text link, it will call an attached
	 * InlineAttachmentHandler to convert the URL of the HTML anchor.
	 */
	public static interface InlineAttachmentHandler {
		/**
		 * @return the value to use for the href of the generated anchor element
		 */
		public String handle(String databaseName, String unid, String fileName);
	}
	/**
	 * When the converter comes across an embedded image, it will call an attached
	 * InlineImageHandler to handle the image data and return an anchor href.
	 */
	public static interface InlineImageHandler {
		// TODO change the data type?
		/**
		 * @return the value to use for the src of the generated image element
		 */
		public String handle(String imageName, String format, ByteBuffer data);
	}
	
	public static interface DoclinkHandler {
		public String handle(String replicaId, String viewId, String documentId, String serverHint);
	}

	private NSFNote note;
	private String itemName;
	// These are mostly XPage-alike defaults
	private String[] htmlOptions = {
			"AutoClass=2", //$NON-NLS-1$
			"RowAtATimeTableAlt=2", //$NON-NLS-1$
			"SectionAlt=1", //$NON-NLS-1$
			"XMLCompatibleHTML=1", //$NON-NLS-1$
			"AttachmentLink=1", //$NON-NLS-1$
			"TableStyle=1", //$NON-NLS-1$
			"FontConversion=1", //$NON-NLS-1$
			"LinkHandling=1", //$NON-NLS-1$
			"ListFidelity=1", //$NON-NLS-1$
			"ParagraphIndent=2" //$NON-NLS-1$
	};
	private InlineAttachmentHandler inlineAttachmentHandler;
	private InlineImageHandler inlineImageHandler;
	private DoclinkHandler doclinkHandler;
	
	public HTMLExporter(NSFNote note, String itemName) {
		this.note = note;
		this.itemName = itemName;
	}
	
	/**
	 * Sets the HTMLOptions for the exporter. See <code>HTMLSetHTMLOptions</code> in the Notes C
	 * API reference for a description. For example:
	 * 
	 * <pre>String html = note.createHTMLExporter("Body")
     *  .options(
     *      "AutoClass=2",
     *      "RowAtATimeTableAlt=2",
     *      "SectionAlt=1",
     *      "XMLCompatibleHTML=1"
     *  )
     *  .export();</pre>
     *  
     * @param htmlOptions The HTML exporter configuration strings
     * @return The HTMLExporter itself
	 */
	public HTMLExporter options(String... htmlOptions) {
		this.htmlOptions = new String[htmlOptions.length];
		System.arraycopy(htmlOptions, 0, this.htmlOptions, 0, htmlOptions.length);
		return this;
	}
	
	/**
	 * Sets the {@link InlineAttachmentHandler} to be triggered when processing encounters
	 * an inline file attachment in the rich text.
	 * 
	 * @param inlineAttachmentHandler The handler to set, or <code>null</code> to unset an existing
	 * 		handler
     * @return The HTMLExporter itself
	 */
	public HTMLExporter inlineAttachmentHandler(InlineAttachmentHandler inlineAttachmentHandler) {
		this.inlineAttachmentHandler = inlineAttachmentHandler;
		return this;
	}
	
	/**
	 * Sets the {@link InlineImageHandler} to be triggered when processing encounters
	 * an inline image in the rich text.
	 * 
	 * @param inlineImageHandler The handler to set, or <code>null</code> to unset an existing
	 * 		handler
     * @return The HTMLExporter itself
	 */
	public HTMLExporter inlineImageHandler(InlineImageHandler inlineImageHandler) {
		this.inlineImageHandler = inlineImageHandler;
		return this;
	}
	
	/**
	 * Sets the {@link DoclinkHandler} to be triggered when processing encounters
	 * @param doclinkHandler the handler to set, or <code>null</null> to unset an existing
	 * 		handler
	 * @return the HTMLExporter itself
	 */
	public HTMLExporter doclinkHandler(DoclinkHandler doclinkHandler) {
		this.doclinkHandler = doclinkHandler;
		return this;
	}
	
	/**
	 * Process the item to convert it to HTML with the specified options and handlers.
	 * 
	 * @return The generated HTML
	 * @throws DominoException if one of the underlying API functions throws an exception
	 */
	public String export() throws DominoException {
		DominoAPI api = note.getAPI();
		
		if(log.isTraceDebugEnabled()) {
			log.traceDebug("Creating converter"); //$NON-NLS-1$
		}
		
		long hHTML = api.HTMLCreateConverter();
		try {
			if(log.isTraceDebugEnabled()) {
				log.traceDebug("created converter; setting options"); //$NON-NLS-1$
			}
			if(htmlOptions != null && htmlOptions.length > 0) {
				api.HTMLSetHTMLOptions(hHTML, htmlOptions);
			}
			
			if(log.isTraceDebugEnabled()) {
				log.traceDebug("calling HTMLConvertItem with handle {0}, DB handle {1}, note handle {2}, item name {3}", hHTML, note.getParent().getHandle(), note.getHandle(), itemName); //$NON-NLS-1$
			}
			
			api.HTMLConvertItem(hHTML, note.getParent().getHandle(), note.getHandle(), itemName);
			
			if(log.isTraceDebugEnabled()) {
				log.traceDebug("HTMLConvertItem success"); //$NON-NLS-1$
			}
			
			// Find the references and convert them to Darwino URLs
			long numRefsPtr = C.malloc(C.sizeOfDWORD);
			long numRefs = 0;
			try {
				api.HTMLGetProperty(hHTML, DominoAPI.HTMLAPI_PROP_NUMREFS, numRefsPtr);
				numRefs = C.getDWORD(numRefsPtr, 0);
			} finally {
				C.free(numRefsPtr);
			}
			if(log.isTraceDebugEnabled()) {
				log.traceDebug("HTML API returned {0} refs", numRefs); //$NON-NLS-1$
			}
			for(int i = 0; i < numRefs; i++) {
				// Fetch the reference's text
				long hRef = api.HTMLGetReference(hHTML, i);
				if(log.isTraceDebugEnabled()) {
					log.traceDebug("Fetched reference {0}", hRef); //$NON-NLS-1$
				}
				long refPtr = api.HTMLLockAndFixupReference(hRef);
				try {
					if(log.isTraceDebugEnabled()) {
						log.traceDebug("Locked reference {0}", refPtr); //$NON-NLS-1$
					}
					
					// refPtr is now pointing to an HTMLAPIReference structure
					HTMLAPIReference ref = new HTMLAPIReference(refPtr);
					
					String resultText = null;
					
					switch(ref.getRefType()) {
					case HREF: {
						if(log.isTraceDebugEnabled()) {
							log.traceDebug("Ref is HTMLAPI_REF_HREF"); //$NON-NLS-1$
						}
						CmdId commandId = ref.getCommandId();
						if(commandId != null) {
							switch(ref.getCommandId()) {
							case Redirect:
								// Doclinks come through as redirect
								if(this.doclinkHandler != null) {
									String replicaId = null;
									String viewId = null;
									String documentId = null;
									String serverHint = null;
									
									HTMLAPI_URLTargetComponent[] targets = ref.getTargets();
									
									for(HTMLAPI_URLTargetComponent target : targets) {
										UAT addressableType = target.getAddressableType();
										if(addressableType != null) {
											switch(addressableType) {
											case Database:
												replicaId = target.getValueDBID().toHexString();
												break;
											case View:
												viewId = target.getValueUNID().getUNID();
												break;
											case Document:
												documentId = target.getValueUNID().getUNID();
												break;
											default:
												break;
											}
										}
									}
									
									HTMLAPI_URLArg[] args = ref.getArgs();
									for(HTMLAPI_URLArg arg : args) {
										CmdArgID argId = arg.getId();
										if(argId != null) {
											switch(argId) {
											case Name:
												serverHint = arg.getValueString();
												break;
											default:
												break;
											}
										}
									}
									
									resultText = this.doclinkHandler.handle(replicaId, viewId, documentId, serverHint);
								}
								
								
								break;
							default:
								if(inlineAttachmentHandler != null) {
									// From this, we want to find the database name, UNID, and file name
									String databaseName = null;
									UNIVERSALNOTEID unid = null;
									String fileName = null;
									
									HTMLAPI_URLTargetComponent[] targets = ref.getTargets();
									
									for(HTMLAPI_URLTargetComponent target : targets) {
										UAT addressableType = target.getAddressableType();
										if(addressableType != null) {
											switch(addressableType) {
											case Database:
												databaseName = target.getValueName();
												break;
											case Document:
												unid = target.getValueUNID();
												break;
											case Filename:
											case ActualFilename:
												fileName = target.getValueName();
											default:
												break;
											}
										}
									}
									
									if(databaseName != null && unid != null && fileName != null) {
										resultText = inlineAttachmentHandler.handle(databaseName, unid.getUNID(), fileName);
									}
								}
								break;
							}
						}
						
						break;
					}
					case IMG: {
						if(log.isTraceDebugEnabled()) {
							log.traceDebug("Ref is HTMLAPI_REF_IMG"); //$NON-NLS-1$
						}
						
						// Only worry about OpenElement for now
						if(ref.getCommandId() != CmdId.OpenElement) {
							break;
						}
						
						if(inlineImageHandler != null) {
							// There are two potential cases for images here: an inline image, identified by an offset in the CD block,
							// and a file attachment ref, identified by a DB, UNID, and file name.
							// The former will have targets in the format:
							//    - Database
							//    - Note
							//    - Field
							//    - FieldOffset
							// The latter will be in the format:
							//    - Database
							//    - Note
							//    - Filename
							HTMLAPI_URLTargetComponent[] targets = ref.getTargets();
							HTMLAPI_URLArg[] args = ref.getArgs();
							
							// Each potential path should generate its binary data and call the image handler
							if(isEmbeddedImageRef(targets)) {
								// In this case, use the offset information to extract the image data from the CD buffer
								
								HTMLAPI_URLTargetComponent offsetTarget = targets[3];
								
								// Look for the format
								String format = null;
								for(HTMLAPI_URLArg arg : args) {
									if(arg.getId() == CmdArgID.FieldElemFormat) {
										format = arg.getValueString();
									}
								}
								
								String elemOffset = offsetTarget.getValueName();
								if(log.isTraceDebugEnabled()) {
									log.traceDebug("elemOffset is {0}", elemOffset); //$NON-NLS-1$
								}
								// This should contain a string in the form of "$elemNumber.$offset"
								// elemNumber is base 10, offset is base 16
								String[] parts = elemOffset.split("\\."); //$NON-NLS-1$
								int itemIndex = 0;
								int offset = 0;
								try {
									itemIndex = Integer.parseInt(parts[0], 10);
									offset = Integer.parseInt(parts[1], 16);
								} catch(NumberFormatException e) {
									throw new DominoException(e, "Encountered NumberFormatException when parsing elemOffset {0} from parts {1}, args {2}, note {3}", elemOffset, Arrays.asList(targets), Arrays.asList(args), note); //$NON-NLS-1$
								}
								
								long innerHTML = api.HTMLCreateConverter();
								try {
									api.HTMLConvertElement(innerHTML, note.getParent().getHandle(), note.getHandle(), itemName, itemIndex, offset);
									
									// Make sure the converted element is binary before continuing
									long bBinary = C.malloc(C.sizeOfBOOL);
									try {
										api.HTMLGetProperty(innerHTML, DominoAPI.HTMLAPI_PROP_BINARYDATA, bBinary);
										if(C.getBOOL(bBinary, 0)) {
											ByteBuffer data = this.readHTMLBytes(innerHTML);
											resultText = inlineImageHandler.handle(elemOffset, format, data);
										} else {
											if(log.isWarnEnabled()) {
												log.warn("Found an inline image in item '{0}' in note {1} that is not binary data", itemName, note); //$NON-NLS-1$
											}
										}
									} finally {
										C.free(bBinary);
									}
								} finally {
									api.HTMLDestroyConverter(innerHTML);
								}
							} else if(isAttachmentImageRef(targets)) {
								// In this case, find the file attachment matching the name
								String fileName = targets[2].getValueName();
								
								NSFItem fileItem = note.getFirstItem(DominoAPI.ITEM_NAME_ATTACHMENT);
								int breaker = 0;
								while(fileItem != null) {
									if(fileItem instanceof NSFFileItem) {
										NSFFileItem file = (NSFFileItem)fileItem;
										if(StringUtil.equalsIgnoreCase(fileName, file.getFileName())) {
											// Then we have the file!
											
											InputStream is = file.getInputStream();
											
											// Use some inner knowledge of the wrapper class. This is because
											// an actual file size may differ from the stated file size
											// from the FILEOBJECT in the item if the file comes from a Mac.
											// We'll only have the data fork here, so get the size of that
											int size = 0;
											if(is instanceof NSFTempFileInputStream) {
												size = (int)((NSFTempFileInputStream)is).getFileSize();
											} else {
												size = is.available();
											}
											
											ByteBuffer data = ByteBuffer.allocate(size);
											try {
												byte[] buffer = new byte[8192];
												int readChunk;
												while ((readChunk = is.read(buffer)) > 0) {
													data.put(buffer, 0, readChunk);
												}
											} finally {
												StreamUtil.close(is);
											}
											
											String format = null;
											if(fileName.contains(".")) { //$NON-NLS-1$
												format = fileName.substring(fileName.lastIndexOf('.')+1);
											}
											resultText = inlineImageHandler.handle(fileName, format, data);
											
											file.free();
											break;
										}
									}
									
									NSFItem tempItem = fileItem;
									fileItem = note.getNextItem(fileItem, DominoAPI.ITEM_NAME_ATTACHMENT);
									tempItem.free();
									
									if(breaker++ > 1000) {
										throw new RuntimeException("Probable infinite loop detected");
									}
								}
								
							} else {
								if(log.isWarnEnabled()) {
									log.warn("Ignoring IMG ref with targets {0}", Arrays.asList(targets)); //$NON-NLS-1$
								}
							}
						}
						
						break;
					}
					default:
						if(log.isInfoEnabled()) {
							log.info("HTML Conversion: unhandled HTML reference type {0}", ref.getRefType()); //$NON-NLS-1$
						}
					}
					
					if(log.isTraceDebugEnabled()) {
						log.traceDebug("ref result text is {0}", resultText); //$NON-NLS-1$
					}
					
					if(resultText != null) {
						api.HTMLSetReferenceText(hHTML, i, resultText);
					}
					
				} finally {
					api.OSMemoryUnlock(hRef);
					api.OSMemoryFree(hRef);
				}
			}
			
			return readHTML(hHTML);
		} catch(DominoException e) {
			if(e.getStatus() == DominoAPI.ERR_HTMLAPI_GENERATING_HTML) {
				// This shows up with invalid images and similar - ignore, sadly
				if(log.isErrorEnabled()) {
					log.error(e, "Encountered HTMLAPI error converting note {0}, item '{1}'", note, itemName); //$NON-NLS-1$
				}
			} else {
				if(log.isErrorEnabled()) {
					log.error(e, "Unhandled error converting HTML in note {0}, item '{1}'", note, itemName); //$NON-NLS-1$
				}
			}
			throw e;
		} catch(IOException ioe) {
			if(log.isErrorEnabled()) {
				log.error(ioe, "Unhandled error converting HTML in note {0}, item '{1}'", note, itemName); //$NON-NLS-1$
			}
			throw new DominoException(ioe);
		} finally {
			api.HTMLDestroyConverter(hHTML);
		}
	}

	private String readHTML(long hHTML) throws DominoException {
		DominoAPI api = note.getAPI();
		
		// To get the buffer content, find the text length, create a buffer, and fetch
		long textLengthPtr = C.malloc(C.sizeOfDWORD);
		long textLength;
		try {
			api.HTMLGetProperty(hHTML, DominoAPI.HTMLAPI_PROP_TEXTLENGTH, textLengthPtr);
			textLength = C.getDWORD(textLengthPtr, 0);
		} finally {
			C.free(textLengthPtr);
		}
		
		// The result size may be higher than Integer.MAX_VALUE, to read in blocks
		long readBytes = 0;
		StringBuilder result = new StringBuilder();
		IntRef textRead = new IntRef();
		int blockSize = textLength < Integer.MAX_VALUE ? (int)textLength : Integer.MAX_VALUE;
		// Initialize the text buffer with the initial read value, which will be the highest we need
		long textPtr = C.malloc(C.sizeOfChar * blockSize);
		try {
		
			int breaker = 0;
			while(readBytes < textLength) {
				textRead.set(blockSize);
				api.HTMLGetText(hHTML, (int)readBytes, textRead, textPtr);
				long textReadBytes = textRead.get() & 0xFFFFFFFFFFFFFFFFL;
				readBytes += textReadBytes;
				
				// Convert the text from "OS native" (DOS, apparently) to Unicode
				// TODO see if this can be done in one pass with NLS_translate (the last param is unclear)
				if(textReadBytes > 0) {
					long lmbcsPtr = C.malloc((int)(textReadBytes * 1.5));
					long unicodePtr = 0;
					try {
						int resultSize = (int)(textReadBytes * 1.5);
						api.OSTranslate(DominoAPI.OS_TRANSLATE_OSNATIVE_TO_LMBCS, textPtr, (short)textReadBytes, lmbcsPtr, (short)resultSize);
						unicodePtr = C.malloc((int)(textReadBytes * 1.5));
						api.OSTranslate(DominoAPI.OS_TRANSLATE_LMBCS_TO_UTF8, lmbcsPtr, (short)resultSize, unicodePtr, (short)resultSize);
						
						// TODO can this be done without copying the data in memory?
						byte[] textData = new byte[resultSize];
						C.readByteArray(textData, 0, unicodePtr, 0, resultSize);
						int nullIndex = 0;
						for(nullIndex = 0; nullIndex <= textData.length; nullIndex++) {
							if(textData[nullIndex] == 0) {
								break;
							}
						}
						result.append(new String(textData, 0, nullIndex, DominoNativeUtils.UTF_8));
					} finally {
						C.free(lmbcsPtr);
						if(unicodePtr != 0) {
							C.free(unicodePtr);
						}
					}
				} else {
					// Then we're in a situation where HTMLGetText returned zero bytes when we requested some.
					// This occurs in very old documents - log it and move on.
					if(log.isInfoEnabled()) {
						log.info("HTMLExporter: encountered API-truncated data (expected {0}, truncated to {1}) in readHTML for item '{2}' in note {3}", textLength, readBytes, itemName, note);
					}
					break;
				}

				if(breaker++ >= 10) {
					Platform.getInstance().log(new RuntimeException("HTMLExporter: encountered probable infinite loop (readBytes=" + readBytes + ", textLength=" + textLength + ", blockSize=" + blockSize + ") in readHTML for note " + note)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					break;
				}
				
				// Set the next block size to be the next chunk we need
				long remaining = textLength - readBytes;
				blockSize = remaining < Integer.MAX_VALUE ? (int)remaining : Integer.MAX_VALUE;
			}
		} finally {
			C.free(textPtr);
		}
		
		return result.toString().replace("\r\n", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// TODO merge with the above method, ideally with an InputStream to read the data smoothly
	private ByteBuffer readHTMLBytes(long hHTML) throws DominoException {
		DominoAPI api = note.getAPI();
		
		// To get the buffer content, find the text length, create a buffer, and fetch
		long textLengthPtr = C.malloc(C.sizeOfDWORD);
		long textLength;
		try {
			api.HTMLGetProperty(hHTML, DominoAPI.HTMLAPI_PROP_TEXTLENGTH, textLengthPtr);
			textLength = C.getDWORD(textLengthPtr, 0);
		} finally {
			C.free(textLengthPtr);
		}
		
		// The result size may be higher than Integer.MAX_VALUE, to read in blocks
		long readBytes = 0;
//		ByteBlockBuffer result = new ByteBlockBuffer();
		ByteBuffer result = ByteBuffer.allocate((int)textLength);
		IntRef textRead = new IntRef();
		int blockSize = textLength < Integer.MAX_VALUE ? (int)textLength : Integer.MAX_VALUE;
		// Initialize the text buffer with the initial read value, which will be the highest we need
		long textPtr = C.malloc(C.sizeOfChar * blockSize);
		try {
			int breaker = 0;
			while(readBytes < textLength) {
				textRead.set(blockSize);
				api.HTMLGetText(hHTML, (int)readBytes, textRead, textPtr);
				readBytes += textRead.get() & 0xFFFFFFFFFFFFFFFFL;
				
				// TODO can this be done without copying the data in memory?
				byte[] textData = new byte[(int)textLength];
				C.readByteArray(textData, 0, textPtr, 0, (int)textLength);
				// TODO what's the encoding here? Presumably, it's whatever's in the server config, which is... awkward
				// Is it worth trying to look that up and convert to Unicode if it's otherwise, or just make it a server-config
				// prereq?
				result.put(textData);
//				result.addBytes(textData);
				
				// Set the next block size to be the next chunk we need
				long remaining = textLength - readBytes;
				blockSize = remaining < Integer.MAX_VALUE ? (int)remaining : Integer.MAX_VALUE;
				
				
				if(breaker >= 100) {
					Platform.getInstance().log(new RuntimeException("HTMLExporter: encountered probable infinite loop in readHTMLBytes for note " + note)); //$NON-NLS-1$
					break;
				}
			}
		} finally {
			C.free(textPtr);
		}
		
		result.position(0);
		return result;
	}
	
	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/
	private boolean isEmbeddedImageRef(HTMLAPI_URLTargetComponent[] targets) {
		//	  - Database
		//    - Note
		//    - Field
		//    - FieldOffset
		if(targets != null && targets.length == 4) {
			if(
				targets[0].getAddressableType() == UAT.Database
				&& targets[1].getAddressableType() == UAT.Document
				&& targets[2].getAddressableType() == UAT.Field
				&& targets[3].getAddressableType() == UAT.FieldOffset) {
				return true;
			}
		}
		return false;
	}
	private boolean isAttachmentImageRef(HTMLAPI_URLTargetComponent[] targets) {
		//    - Database
		//    - Note
		//    - Filename
		if(targets != null && targets.length == 3) {
			if(
				targets[0].getAddressableType() == UAT.Database
				&& targets[1].getAddressableType() == UAT.Document
				&& targets[2].getAddressableType() == UAT.Filename) {
				return true;
			}
		}
		return false;
	}
}
