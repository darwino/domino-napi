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
package com.darwino.domino.napi.test.wrap;

import static org.junit.Assert.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.darwino.domino.napi.enums.LDDELIM;
import com.darwino.domino.napi.enums.LDELIM;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.wrap.design.NSFForm;
import com.darwino.domino.napi.wrap.design.NSFFormField;
import com.darwino.domino.napi.wrap.design.NSFFormField.DisplayType;

/**
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
@SuppressWarnings("nls")
public class FormsTest extends AbstractNoteTest {
	@Test
	public void readForm1() throws Exception {
		NSFForm form1 = designTestbed.getDesign().getForm("Form 1");
		assertNotEquals("form1 should not be null", null, form1);
		
		List<NSFFormField> fields = form1.getFields(false);
		assertNotEquals("fields should not be null", null, fields);
		assertEquals("fields should have 16 elements", 16, fields.size());
		
		{
			NSFFormField textField = fields.get(0);
			assertEquals("textField should be 'TextField'", "TextField", textField.getName());
			assertEquals("textField should be type text", ValueType.TEXT, textField.getType());
			assertEquals("textField default value formula should match", "\"textField default value\"", textField.getDefaultValueFormula().getFormulaText(false));
			assertEquals("textField input translation formula should match", "\"textField input translation\"", textField.getInputTranslationFormula().getFormulaText(false));
			assertEquals("textField input validation formula should match", "\"textField input validation\"", textField.getInputValidationFormula().getFormulaText(false));
			assertEquals("textField hint should match", "This is the text field ı", textField.getHintText());
			assertEquals("textField type should match", DisplayType.TEXT, textField.getDisplayType());
			assertFalse("should not be multi", textField.isAllowMultiple());
		}
		{
			NSFFormField multiTextField = fields.get(1);
			assertEquals("multiTextField should be 'MultiTextField'", "MultiTextField", multiTextField.getName());
			assertEquals("multiTextField should be type text list", ValueType.TEXT_LIST, multiTextField.getType());
			Set<LDELIM> listDelim = EnumSet.of(LDELIM.NEWLINE, LDELIM.BLANKLINE);
			assertEquals("mutliTextField should be delimited with NEWLINE and BLANKLINE", listDelim, multiTextField.getListDelim());
			assertEquals("multiTextField should be displayed delimited with NEWLINE", LDDELIM.NEWLINE, multiTextField.getListDisplayDelim());
			assertEquals("multiTextField type should match", DisplayType.TEXT, multiTextField.getDisplayType());
			assertTrue("should be multi", multiTextField.isAllowMultiple());
		}
		{
			NSFFormField dateField = fields.get(2);
			assertEquals("dateField should be 'DateField'", "DateField", dateField.getName());
			assertEquals("dateField should be type time", ValueType.TIME, dateField.getType());
			assertEquals("dateField type should match", DisplayType.DATETIME, dateField.getDisplayType());
			assertFalse("should not be multi", dateField.isAllowMultiple());
			
			// TODO test font info
		}
		{
			NSFFormField multiDateField = fields.get(3);
			assertEquals("multiDateField should be 'MultiDateField'", "MultiDateField", multiDateField.getName());
			assertEquals("multiDateField should be type time range", ValueType.TIME_RANGE, multiDateField.getType());
			Set<LDELIM> listDelim = EnumSet.of(LDELIM.COMMA);
			assertEquals("multiDateField should be delimited with COMMA", listDelim, multiDateField.getListDelim());
			assertEquals("multiDateField should be displayed delimited with COMMA", LDDELIM.COMMA, multiDateField.getListDisplayDelim());
			assertEquals("multiDateField type should match", DisplayType.DATETIME, multiDateField.getDisplayType());
			assertTrue("should be multi", multiDateField.isAllowMultiple());
		}
		{
			NSFFormField richTextField = fields.get(4);
			assertEquals("richTextField should be 'RichTextField'", "RichTextField", richTextField.getName());
			assertEquals("richTextField should be type composite", ValueType.COMPOSITE, richTextField.getType());
			assertEquals("richTextField type should match", DisplayType.RICH_TEXT, richTextField.getDisplayType());
			assertFalse("should not be multi", richTextField.isAllowMultiple());
		}
		{
			NSFFormField numberField = fields.get(5);
			assertEquals("numberField should be 'NumberField'", "NumberField", numberField.getName());
			assertEquals("numberField should be type number", ValueType.NUMBER, numberField.getType());
			assertEquals("numberField type should match", DisplayType.NUMBER, numberField.getDisplayType());
			assertFalse("should not be multi", numberField.isAllowMultiple());
		}
		{
			NSFFormField checkboxField = fields.get(6);
			assertEquals("checkboxField should be 'CheckboxField'", "CheckboxField", checkboxField.getName());
			assertEquals("checkboxField should be type text", ValueType.TEXT_LIST, checkboxField.getType());
			assertEquals("checkboxField type should match", DisplayType.CHECKBOX, checkboxField.getDisplayType());
			assertTrue("should be multi", checkboxField.isAllowMultiple());
			
			String[] values = checkboxField.getValues();
			assertArrayEquals("checkboxField values should match", new String[] { "One", "Two", "Three|3" }, values);
		}
		{
			NSFFormField checkboxFormulaField = fields.get(7);
			assertEquals("checkboxFormulaField should be 'CheckboxFormulaField'", "CheckboxFormulaField", checkboxFormulaField.getName());
			assertEquals("checkboxFormulaField should be type text", ValueType.TEXT_LIST, checkboxFormulaField.getType());
			assertEquals("checkboxFormulaField type should match", DisplayType.CHECKBOX, checkboxFormulaField.getDisplayType());
			assertTrue("should be multi", checkboxFormulaField.isAllowMultiple());
			
			String valuesFormula = checkboxFormulaField.getValuesFormula().getFormulaText(false);
			assertEquals("checkboxFormulaField formula should match", "\"hey there\"", valuesFormula);
		}
		{
			NSFFormField radioField = fields.get(8);
			assertEquals("radioField should be 'RadioField'", "RadioField", radioField.getName());
			assertEquals("radioField should be type text", ValueType.TEXT, radioField.getType());
			assertEquals("radioField type should match", DisplayType.RADIO_BUTTON, radioField.getDisplayType());
			assertFalse("should not be multi", radioField.isAllowMultiple());
			
			String[] values = radioField.getValues();
			assertArrayEquals("radioField values should match", new String[] { "One", "Two", "Three|3" }, values);
		}
		{
			NSFFormField listboxField = fields.get(9);
			assertEquals("listboxField should be 'ListboxField'", "ListboxField", listboxField.getName());
			assertEquals("listboxField should be type text", ValueType.TEXT, listboxField.getType());
			assertEquals("listboxField type should match", DisplayType.LISTBOX, listboxField.getDisplayType());
			assertFalse("should not be multi", listboxField.isAllowMultiple());
			
			String[] values = listboxField.getValues();
			assertArrayEquals("listboxField values should match", new String[] { "One", "Two", "Three|3" }, values);
		}
		{
			NSFFormField comboboxField = fields.get(10);
			assertEquals("comboboxField should be 'ComboboxField'", "ComboboxField", comboboxField.getName());
			assertEquals("comboboxField should be type text", ValueType.TEXT, comboboxField.getType());
			assertEquals("comboboxField type should match", DisplayType.COMBOBOX, comboboxField.getDisplayType());
			assertFalse("should not be multi", comboboxField.isAllowMultiple());
			
			String[] values = comboboxField.getValues();
			assertArrayEquals("comboboxField values should match", new String[] { "One", "Two", "Three|3" }, values);
		}
		{
			NSFFormField emptyListboxField = fields.get(11);
			assertEquals("emptyListboxField should be 'EmptyListboxField'", "EmptyListboxField", emptyListboxField.getName());
			assertEquals("emptyListboxField should be type text", ValueType.TEXT, emptyListboxField.getType());
			assertEquals("emptyListboxField type should match", DisplayType.LISTBOX, emptyListboxField.getDisplayType());
			assertFalse("should not be multi", emptyListboxField.isAllowMultiple());
			
			String[] values = emptyListboxField.getValues();
			assertArrayEquals("emptyListboxField values should match", new String[] {  }, values);
		}
		{
			NSFFormField dialogListField = fields.get(12);
			assertEquals("dialogListField should be 'DialogListField'", "DialogListField", dialogListField.getName());
			assertEquals("dialogListField should be type text", ValueType.TEXT, dialogListField.getType());
			assertEquals("dialogListField type should match", DisplayType.DIALOG_LIST, dialogListField.getDisplayType());
			assertFalse("should not be multi", dialogListField.isAllowMultiple());
		}
		{
			NSFFormField authorsField = fields.get(13);
			assertEquals("authorsField should be 'AuthorsField'", "AuthorsField", authorsField.getName());
			assertEquals("authorsField should be type text", ValueType.TEXT_LIST, authorsField.getType());
			assertEquals("authorsField type should match", DisplayType.AUTHORS, authorsField.getDisplayType());
			assertTrue("should be multi", authorsField.isAllowMultiple());
		}
		{
			NSFFormField readersField = fields.get(14);
			assertEquals("readersField should be 'ReadersField'", "ReadersField", readersField.getName());
			assertEquals("readersField should be type text", ValueType.TEXT, readersField.getType());
			assertEquals("readersField type should match", DisplayType.READERS, readersField.getDisplayType());
			assertFalse("should not be multi", readersField.isAllowMultiple());
		}
		{
			NSFFormField namesField = fields.get(15);
			assertEquals("namesField should be 'NamesField'", "NamesField", namesField.getName());
			assertEquals("namesField should be type text", ValueType.TEXT_LIST, namesField.getType());
			assertEquals("namesField type should match", DisplayType.NAMES, namesField.getDisplayType());
			assertTrue("should be multi", namesField.isAllowMultiple());
		}
	}
	
	@Test
	public void testForm2() throws Exception {
		NSFForm form2 = designTestbed.getDesign().getForm("Form 2");
		assertNotEquals("form2 should not be null", null, form2);
		
		List<NSFFormField> fields = form2.getFields(true);
		assertNotEquals("fields should not be null", null, fields);
		assertEquals("fields should have 3 elements", 3, fields.size());
		
		{
			NSFFormField textField = fields.get(0);
			assertEquals("textField should be 'Text1'", "Text1", textField.getName());
			assertEquals("textField should be type text", ValueType.TEXT, textField.getType());
		}
		{
			NSFFormField textField = fields.get(1);
			assertEquals("textField should be 'SubformText'", "SubformText", textField.getName());
			assertEquals("textField should be type text", ValueType.TEXT, textField.getType());
		}
		{
			NSFFormField textField = fields.get(2);
			assertEquals("textField should be 'Text2'", "Text2", textField.getName());
			assertEquals("textField should be type text", ValueType.TEXT, textField.getType());
		}
	}
	
	@Test
	public void testAdminLog() throws Exception {
		NSFForm adminLog = designTestbed.getDesign().getForm("AdminLog");
		assertNotEquals("adminLog should not be null", null, adminLog);
		
		NSFFormField proxyAuthor = adminLog.getField("ProxyAuthor", false);
		assertNotEquals("ProxyAuthor should not be null", null, proxyAuthor);
		assertEquals("ProxyAuthor name should match", "ProxyAuthor", proxyAuthor.getName());
		
		assertEquals("ProxyAuthor should be an AUTHORS field", DisplayType.AUTHORS, proxyAuthor.getDisplayType());
	}
	
	@Test
	public void testAdminRequest() throws Exception {
		NSFForm form = designTestbed.getDesign().getForm("AdminRequest");
		assertNotEquals("form should not be null", null, form);
		
		NSFFormField field = form.getField("ChangeManHierarchy", false);
		assertNotEquals("ChangeManHierarchy should not be null", null, field);
		assertEquals("ChangeManHierarchy name should match", "ChangeManHierarchy", field.getName());
		
		assertEquals("Field UI type should be TEXT", DisplayType.TEXT, field.getDisplayType());
		assertEquals("Field type should be TEXT", ValueType.TEXT, field.getType());
	}
	
	@Test
	public void testCapabilities() throws Exception {
		NSFForm form = designTestbed.getDesign().getForm("CertificateRequest");
		assertNotEquals("form should not be null", null, form);
		
		NSFFormField field = form.getField("Capabilities", false);
		assertNotEquals("Capabilities should not be null", null, field);
		assertEquals("Capabilities name should match", "Capabilities", field.getName());
		
		assertEquals("Field UI type should be DIALOG_LIST", DisplayType.DIALOG_LIST, field.getDisplayType());
		assertEquals("Field type should be TEXT", ValueType.TEXT_LIST, field.getType());
		assertTrue("Should be multi", field.isAllowMultiple());
	}
}
