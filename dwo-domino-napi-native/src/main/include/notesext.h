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


#ifndef _NOTESEXT_H_
#define _NOTESEXT_H_

// ===========================================================================================
// Undocumented NSFNoteSetInfo flags
// ===========================================================================================

#define UPDATE_REPLICA 0x0008
#define UPDATE_PTR 0x0800
#define UPDATE_KEEP_MODTIME 0x00020000

// ===========================================================================================
// Undocumented nsfsearc.h
// ===========================================================================================

extern "C" STATUS far PASCAL NSFSearchExtended3 (DBHANDLE hDB,
                                                                FORMULAHANDLE hFormula,
                                                                DHANDLE hFilter,
                                                                DWORD FilterFlags,
                                                                char *ViewTitle,
                                                                DWORD SearchFlags,
                                                                DWORD SearchFlags1,
                                                                DWORD SearchFlags2,
                                                                DWORD SearchFlags3,
                                                                DWORD SearchFlags4,
                                                                WORD NoteClassMask,
                                                                TIMEDATE *Since,
                                                                STATUS (far PASCAL *EnumRoutine)
                                                                                        (void *EnumRoutineParameter,
                                                                                        SEARCH_MATCH *SearchMatch,
                                                                                        ITEM_TABLE *SummaryBuffer),
                                                                void *EnumRoutineParameter,
                                                                TIMEDATE *retUntil,
                                                                DHANDLE namelist);

#define        SEARCH_FILTER_NONE                                0x00000000        /*        No filter specified (hFilter ignored). */
#define        SEARCH_FILTER_NOTEID_TABLE                0x00000001        /*        hFilter is a Note ID table. */
#define SEARCH_FILTER_FOLDER                        0x00000002        /* hFilter is a View note handle */
#define SEARCH_FILTER_DBDIR_PROPERTY        0x00000004  /* Filter on particular Properties. */
#define SEARCH_FILTER_DBOPTIONS                        0x00000010  /* Filter on Database Options (bits set). */
#define SEARCH_FILTER_DBOPTIONS_CLEAR        0x00000020  /* Filter on Database Options (bits clear). */
#define SEARCH_FILTER_FORMSKIMMED                0x00000040        /* Filter based on a set of form names */
#define SEARCH_FILTER_NOFORMSKIMMED                0x00000080        /* Don't try to filter on form names, we know it won't work */
#define SEARCH_FILTER_QUERY_VIEW                0x00000100        /* Filter on Query View SQL */
#define SEARCH_FILTER_ITEM_TIME                        0x00000200        /* Filter on item revision times */
#define SEARCH_FILTER_RANGE                                0x00000400        /* Filter on time range input */
#define SEARCH_FILTER_NO_NDX                        0x00000800        /* Filter out .ndx files */
#define SEARCH_FILTER_INLINE_INDEX                0x00001000        /* Search for databases with inline indexing */

#define SEARCH1_SELECT_NAMED_GHOSTS        (0x00000001 | SEARCH1_SIGNATURE)

        /* Include profile documents (a specific type of named ghost note) in the search
         * Note: set SEARCH1_SELECT_NAMED_GHOSTS, too, if you want the selection formula
         * to be applied to the profile docs (so as not to get them all back as matches).
         */
#define SEARCH1_PROFILE_DOCS                (0X00000002 | SEARCH1_SIGNATURE)

        /* Skim off notes whose summary buffer can't be generated because its size is too big.
         */
#define SEARCH1_SKIM_SUMMARY_BUFFER_TOO_BIG \
                                                                        (0x00000004 | SEARCH1_SIGNATURE)
#define SEARCH1_RETURN_THREAD_UNID_ARRAY \
                                                                        (0x00000008 | SEARCH1_SIGNATURE)
#define SEARCH1_RETURN_TUA                         SEARCH1_RETURN_THREAD_UNID_ARRAY

#define SEARCH1_REPORT_NOACCESS        (0x000000010 | SEARCH1_SIGNATURE) /* flag for reporting noaccess in case of reader's field at the doc level*/
#define SEARCH1_ONLY_ABSTRACTS  (0x000000020 | SEARCH1_SIGNATURE)     /* Search "Truncated" documents */
#define SEARCH1_FIXUP_PURGED        (0x000000040 | SEARCH1_SIGNATURE)     /* Search documents fixup purged.
                                                                                                                                                This distinct and mutually exlusive from SEARCH_INCLUDE_PURGED
                                                                                                                                                which is used for view processing by NIF etc to remove purged notes from views.
                                                                                                                                                This is used for replication restoring corrupt documents. */
#define SEARCH1_SIGNATURE                0x80000000                /* for setting/verifying that bits 28-31 of search 1 flags are 1000 */

// ===========================================================================================
// Undocumented nsfdb.h
// ===========================================================================================

extern "C" STATUS far PASCAL NSFTransactionBegin(DBHANDLE hDB, DWORD flags);
extern "C" STATUS far PASCAL NSFTransactionCommit(DBHANDLE hDB, DWORD flags);
extern "C" STATUS far PASCAL NSFTransactionRollback(DBHANDLE hDB);

#define NSF_TRANSACTION_BEGIN_SUB_COMMIT 0x00000001 /* Transactions is Sub-Commited if a Sub Transaction */
#define NSF_TRANSACTION_BEGIN_LOCK_DB 0x00000002 /* When starting a txn (not a sub tran) get an intent shared lock on the db */

#define TRANCOMMIT_SKIP_AUTO_ABORT 1 /* Don't automatically abort if Commit Processing Fails */

#endif
