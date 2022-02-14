package hr.rainbow.data.local.tag_search_suggestion

import android.content.ContentValues
import android.database.Cursor

interface ITagSearchDb {
    fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor

    fun insert(values: ContentValues?): Long

    fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int

    fun delete(selection: String?, selectionArgs: Array<String>?): Int

}