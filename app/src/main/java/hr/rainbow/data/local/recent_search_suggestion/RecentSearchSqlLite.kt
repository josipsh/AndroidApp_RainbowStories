package hr.rainbow.data.local.recent_search_suggestion

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.rainbow.domain.model.SuggestionItem

private const val DB_NAME = "RecentSearch"
private const val DB_VERSION = 1
private const val TABLE_NAME = "RecentSearch"
private val CREATE = "create table $TABLE_NAME (" +
        "_id integer primary key autoincrement," +
        "${SuggestionItem::query.name} text not null," +
        "${SuggestionItem::type.name} integer not null," +
        "UNIQUE(${SuggestionItem::query.name})" + ")"

class RecentSearchSqlLite(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    IRecentSearchDb {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {}

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun insert(values: ContentValues?): Long {
        return writableDatabase.insertWithOnConflict(
            TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    override fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = writableDatabase.update(
        TABLE_NAME,
        values,
        selection,
        selectionArgs
    )

    override fun delete(selection: String?, selectionArgs: Array<String>?): Int =
        writableDatabase.delete(
            TABLE_NAME, selection, selectionArgs
        )
}