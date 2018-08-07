/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc. 2014-2018.
 *
 * Notice: The information contained in the source code for these files is the property 
 * of Darwino Inc. which, with its licensors, if any, owns all the intellectual property 
 * rights, including all copyright rights thereto.  Such information may only be used 
 * for debugging, troubleshooting and informational purposes.  All other uses of this information, 
 * including any production or commercial uses, are prohibited. 
 */

package com.darwino.domino.napi.test.wrap;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.design.NSFFileResource;
import com.darwino.domino.napi.wrap.item.NSFCompositeData;
import com.darwino.domino.napi.wrap.item.NSFCompositeDataItem;
import com.ibm.commons.util.io.StreamUtil;

@SuppressWarnings("nls")
public class TestFileResource extends AbstractNoteTest {

	/**
	 * Tests extracting text data from a small text file stored as a File Resource.
	 * 
	 * <p>This tests the lowest-level wrapping: a single NSFCompositeDataItem.</p>
	 */
	@Test
	public void testTextFileResource() throws DominoException {
		NSFNote textFile = designTestbed.getNoteByUNID(AllTests.DESIGN_DOC_FILERES_UNID);
		NSFCompositeDataItem fileData = (NSFCompositeDataItem)textFile.getFirstItem(DominoAPI.ITEM_NAME_FILE_DATA);
		byte[] data = fileData.getFileResourceData();
		assertNotEquals("data should not be null", null, data);
		assertEquals("data should have the expected length", 17, data.length);
		
		String text = new String(data);
		assertEquals("Text should match the expected", "I am a text file.", text);
	}

	/**
	 * Tests extracting data from a multi-item image stored as a File Resource.
	 * 
	 * <p>This tests the middle layer wrapping: the NSFCompositeData multi-item view.</p>
	 */
	@Test
	public void testImageFileResource() throws DominoException, IOException {
		// First, extract the resource in the test plugin
		@SuppressWarnings("resource")
		InputStream is = getClass().getResourceAsStream("/1485101921.png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			StreamUtil.copyStream(is, baos);
		} finally {
			StreamUtil.close(is);
		}
		byte[] expected = baos.toByteArray();
		
		// Now read the note data
		NSFNote note = designTestbed.getNoteByUNID(AllTests.DESIGN_DOC_IMAGE_UNID);
		NSFCompositeData cd = note.getCompositeData(DominoAPI.ITEM_NAME_FILE_DATA);
		byte[] actual = cd.getFileResourceData();
		
		assertEquals("Extracted image data size should match expected", expected.length, actual.length);
		assertTrue("Extracted image data should match expected", Arrays.equals(expected, actual));
	}
	
	/**
	 * Tests extracting data from a multi-item image stored as a File Resource.
	 * 
	 * <p>This tests the top layers of wrapping: the DatabaseDesign and NSFFileResource objects.</p>
	 */
	@Test
	public void testImageFileResourceByName() throws IOException, DominoException {
		// First, extract the resource in the test plugin
		@SuppressWarnings("resource")
		InputStream is = getClass().getResourceAsStream("/1485101921.png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			StreamUtil.copyStream(is, baos);
		} finally {
			StreamUtil.close(is);
		}
		byte[] expected = baos.toByteArray();
		
		// Now read the note data
		NSFFileResource res = designTestbed.getDesign().getFileResource("1485101921.png");
		byte[] actual = res.getFileData();
		
		assertEquals("Extracted image data size should match expected", expected.length, actual.length);
		assertTrue("Extracted image data should match expected", Arrays.equals(expected, actual));
	}
	
	/**
	 * Tests extracting data from a multi-item image stored as a File Resource, finding the resource
	 * by looping through all file resources in the DB.
	 * 
	 * <p>This tests the top layers of wrapping: the DatabaseDesign and NSFFileResource objects.</p>
	 */
	@Test
	public void testImageFileResourceByNameLoop() throws IOException, DominoException {
		// First, extract the resource in the test plugin
		@SuppressWarnings("resource")
		InputStream is = getClass().getResourceAsStream("/1485101921.png");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			StreamUtil.copyStream(is, baos);
		} finally {
			StreamUtil.close(is);
		}
		byte[] expected = baos.toByteArray();
		
		// Now read the note data
		List<NSFFileResource> resources = designTestbed.getDesign().getFileResources();
		NSFFileResource res = null;
		for(NSFFileResource resource : resources) {
			if(resource.getTitle().equals("1485101921.png")) {
				res = resource;
				break;
			}
		}
		assertNotEquals("resource 1485101921.png should be found", null, res);
		if(res != null) {
			byte[] actual = res.getFileData();
			
			assertEquals("Extracted image data size should match expected", expected.length, actual.length);
			assertTrue("Extracted image data should match expected", Arrays.equals(expected, actual));
		}
	}
}
