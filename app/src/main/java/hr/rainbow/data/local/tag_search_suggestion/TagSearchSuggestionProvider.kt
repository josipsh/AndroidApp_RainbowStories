package hr.rainbow.data.local.tag_search_suggestion

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

private const val AUTHORITIES = "hr.rainbow.tag.search.suggestion"
private const val PATH = "items"
private const val ITEMS = 10
private const val ITEM_ID = 20
val TAG_SEARCH_SUGGESTION_PROVIDER_URI: Uri = Uri.parse("content://$AUTHORITIES/$PATH")

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITIES, PATH, ITEMS)
    addURI(AUTHORITIES, "${PATH}/#", ITEM_ID)
    this
}


class TagSearchSuggestionProvider : ContentProvider() {

    private lateinit var db: ITagSearchDb

    override fun onCreate(): Boolean {
        db = TagSearchSqLite(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return db.query(projection, selection, selectionArgs, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val id = db.insert(values)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> {
                return db.update(values, selection, selectionArgs)
            }
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return db.update(values, "_id=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> {
                return db.delete(selection, selectionArgs)
            }
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return db.delete("_id=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}