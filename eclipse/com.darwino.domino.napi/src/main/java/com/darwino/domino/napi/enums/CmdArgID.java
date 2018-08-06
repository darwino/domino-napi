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

/**
 * (htmlapi.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum CmdArgID implements INumberEnum<Integer> {
	Start(DominoAPI.CAI_Start),
	StartKey(DominoAPI.CAI_StartKey),
	Count(DominoAPI.CAI_Count),
	Expand(DominoAPI.CAI_Expand),
	FullyExpand(DominoAPI.CAI_FullyExpand),
	ExpandView(DominoAPI.CAI_ExpandView),
	Collapse(DominoAPI.CAI_Collapse),
	CollapseView(DominoAPI.CAI_CollapseView),
	/** Maps to constant CAI_3PaneUI */
	ThreePaneUI(DominoAPI.CAI_3PaneUI),
	TargetFrame(DominoAPI.CAI_TargetFrame),
	FieldElemType(DominoAPI.CAI_FieldElemType),
	FieldElemFormat(DominoAPI.CAI_FieldElemFormat),
	SearchQuery(DominoAPI.CAI_SearchQuery),
	OldSearchQuery(DominoAPI.CAI_OldSearchQuery),
	SearchMax(DominoAPI.CAI_SearchMax),
	SearchWV(DominoAPI.CAI_SearchWV),
	SearchOrder(DominoAPI.CAI_SearchOrder),
	SearchThesarus(DominoAPI.CAI_SearchThesarus),
	ResortAscending(DominoAPI.CAI_ResortAscending),
	ResortDescending(DominoAPI.CAI_ResortDescending),
	ParentUNID(DominoAPI.CAI_ParentUNID),
	Click(DominoAPI.CAI_Click),
	UserName(DominoAPI.CAI_UserName),
	Password(DominoAPI.CAI_Password),
	To(DominoAPI.CAI_To),
	ISMAPx(DominoAPI.CAI_ISMAPx),
	ISMAPy(DominoAPI.CAI_ISMAPy),
	Grid(DominoAPI.CAI_Grid),
	Date(DominoAPI.CAI_Date),
	TemplateType(DominoAPI.CAI_TemplateType),
	TargetUNID(DominoAPI.CAI_TargetUNID),
	ExpandSection(DominoAPI.CAI_ExpandSection),
	Login(DominoAPI.CAI_Login),
	PickupCert(DominoAPI.CAI_PickupCert),
	PickupCACert(DominoAPI.CAI_PickupCACert),
	SubmitCert(DominoAPI.CAI_SubmitCert),
	ServerRequest(DominoAPI.CAI_ServerRequest),
	ServerPickup(DominoAPI.CAI_ServerPickup),
	PickupID(DominoAPI.CAI_PickupID),
	TranslateForm(DominoAPI.CAI_TranslateForm),
	SpecialAction(DominoAPI.CAI_SpecialAction),
	AllowGetMethod(DominoAPI.CAI_AllowGetMethod),
	Seq(DominoAPI.CAI_Seq),
	BaseTarget(DominoAPI.CAI_BaseTarget),
	ExpandOutline(DominoAPI.CAI_ExpandOutline),
	StartOutline(DominoAPI.CAI_StartOutline),
	Days(DominoAPI.CAI_Days),
	TableTab(DominoAPI.CAI_TableTab),
	MIME(DominoAPI.CAI_MIME),
	RestrictToCategory(DominoAPI.CAI_RestrictToCategory),
	Highlight(DominoAPI.CAI_Highlight),
	Frame(DominoAPI.CAI_Frame),
	FrameSrc(DominoAPI.CAI_FrameSrc),
	Navigate(DominoAPI.CAI_Navigate),
	SkipNavigate(DominoAPI.CAI_SkipNavigate),
	SkipCount(DominoAPI.CAI_SkipCount),
	EndView(DominoAPI.CAI_EndView),
	TableRow(DominoAPI.CAI_TableRow),
	RedirectTo(DominoAPI.CAI_RedirectTo),
	SessionId(DominoAPI.CAI_SessionId),
	SourceFolder(DominoAPI.CAI_SourceFolder),
	SearchFuzzy(DominoAPI.CAI_SearchFuzzy),
	HardDelete(DominoAPI.CAI_HardDelete),
	SimpleView(DominoAPI.CAI_SimpleView),
	SearchEntry(DominoAPI.CAI_SearchEntry),
	Name(DominoAPI.CAI_Name),
	Id(DominoAPI.CAI_Id),
	RootAlias(DominoAPI.CAI_RootAlias),
	Scope(DominoAPI.CAI_Scope),
	DblClkTarget(DominoAPI.CAI_DblClkTarget),
	Charset(DominoAPI.CAI_Charset),
	EmptyTrash(DominoAPI.CAI_EmptyTrash),
	EndKey(DominoAPI.CAI_EndKey),
	PreFormat(DominoAPI.CAI_PreFormat),
	ImgIndex(DominoAPI.CAI_ImgIndex),
	AutoFramed(DominoAPI.CAI_AutoFramed),
	OutputFormat(DominoAPI.CAI_OutputFormat),
	InheritParent(DominoAPI.CAI_InheritParent),
	Last(DominoAPI.CAI_Last);
	
	private final int value;
	
	private CmdArgID(int value) {
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
