package hr.rainbow.data.local.recent_search_suggestion

import android.content.ContentValues
import android.database.Cursor

interface IRecentSearchDb {
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