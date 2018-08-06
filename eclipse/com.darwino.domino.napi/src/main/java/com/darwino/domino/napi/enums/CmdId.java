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

package com.darwino.domino.napi.enums;

import com.darwino.domino.napi.DominoAPI;

public enum CmdId implements INumberEnum<Integer> {
	Unknown(DominoAPI.kUnknownCmdId),
	OpenServer(DominoAPI.kOpenServerCmdId),
	OpenDatabase(DominoAPI.kOpenDatabaseCmdId),
	OpenView(DominoAPI.kOpenViewCmdId),
	OpenDocument(DominoAPI.kOpenDocumentCmdId),
	OpenElement(DominoAPI.kOpenElementCmdId),
	OpenForm(DominoAPI.kOpenFormCmdId),
	OpenAgent(DominoAPI.kOpenAgentCmdId),
	OpenNavigator(DominoAPI.kOpenNavigatorCmdId),
	OpenIcon(DominoAPI.kOpenIconCmdId),
	OpenAbout(DominoAPI.kOpenAboutCmdId),
	OpenHelp(DominoAPI.kOpenHelpCmdId),
	CreateDocument(DominoAPI.kCreateDocumentCmdId),
	SaveDocument(DominoAPI.kSaveDocumentCmdId),
	EditDocument(DominoAPI.kEditDocumentCmdId),
	DeleteDocument(DominoAPI.kDeleteDocumentCmdId),
	SearchView(DominoAPI.kSearchViewCmdId),
	SearchSite(DominoAPI.kSearchSiteCmdId),
	Navigate(DominoAPI.kNavigateCmdId),
	ReadForm(DominoAPI.kReadFormCmdId),
	RequestCert(DominoAPI.kRequestCertCmdId),
	ReadDesign(DominoAPI.kReadDesignCmdId),
	ReadViewEntries(DominoAPI.kReadViewEntriesCmdId),
	ReadEntries(DominoAPI.kReadEntriesCmdId),
	OpenPage(DominoAPI.kOpenPageCmdId),
	OpenFrameSet(DominoAPI.kOpenFrameSetCmdId),
	OpenField(DominoAPI.kOpenFieldCmdId),
	SearchDomain(DominoAPI.kSearchDomainCmdId),
	DeleteDocuments(DominoAPI.kDeleteDocumentsCmdId),
	LoginUser(DominoAPI.kLoginUserCmdId),
	LogoutUser(DominoAPI.kLogoutUserCmdId),
	OpenImageResource(DominoAPI.kOpenImageResourceCmdId),
	OpenImage(DominoAPI.kOpenImageCmdId),
	CopyToFolder(DominoAPI.kCopyToFolderCmdId),
	MoveToFolder(DominoAPI.kMoveToFolderCmdId),
	RemoveFromFolder(DominoAPI.kRemoveFromFolderCmdId),
	UndeleteDocuments(DominoAPI.kUndeleteDocumentsCmdId),
	Redirect(DominoAPI.kRedirectCmdId),
	GetOrbCookie(DominoAPI.kGetOrbCookieCmdId),
	OpenCssResource(DominoAPI.kOpenCssResourceCmdId),
	OpenFileResource(DominoAPI.kOpenFileResourceCmdId),
	OpenJavascriptLib(DominoAPI.kOpenJavascriptLibCmdId),
	UnImplemented_01(DominoAPI.kUnImplemented_01),
	ChangePassword(DominoAPI.kChangePasswordCmdId),
	OpenPreferences(DominoAPI.kOpenPreferencesCmdId),
	OpenWebService(DominoAPI.kOpenWebServiceCmdId),
	Wsdl(DominoAPI.kWsdlCmdId),
	GetImage(DominoAPI.kGetImageCmdId);
	
	private final int value;
	
	private CmdId(int value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
