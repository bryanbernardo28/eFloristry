package com.example.eflorisity

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import com.example.eflorisity.ui.cart.Cart
import java.lang.Exception

class SQLiteDbHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CartDB"
        private val TABLE_NAME = "Cart"
        private val KEY_PRODUCT_ID = "id"
        private val KEY_PRODUCT_NAME = "product_name"
        private val KEY_PRODUCT_DESCRIPTION = "product_description"
        private val KEY_PRODUCT_PRICE = "product_price"
        private val KEY_PRODUCT_QUANTITY = "product_quantity"
        private val KEY_PRODUCT_DISCOUNT = "product_discount"
        private val KEY_PRODUCT_IS_DISCOUNTED = "product_is_discounted"
        private val KEY_PRODUCT_PHOTO_URL = "product_photo_url"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = (
                                    "CREATE TABLE " + TABLE_NAME + "("
                                    + "${KEY_PRODUCT_ID} INTEGER PRIMARY KEY,"
                                    + "${KEY_PRODUCT_NAME} TEXT,"
                                    + "${KEY_PRODUCT_DESCRIPTION} TEXT,"
                                    + "${KEY_PRODUCT_PRICE} TEXT,"
                                    + "${KEY_PRODUCT_QUANTITY} INTEGER,"
                                    + "${KEY_PRODUCT_DISCOUNT} TEXT,"
                                    + "${KEY_PRODUCT_IS_DISCOUNTED} INTEGER,"
                                    + "${KEY_PRODUCT_PHOTO_URL} TEXT"
                                    + ")"
                )
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addToCart(cart:Cart) : Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_PRODUCT_ID,cart.product_id)
        contentValues.put(KEY_PRODUCT_NAME,cart.product_name)
        contentValues.put(KEY_PRODUCT_PRICE,cart.product_price)
        contentValues.put(KEY_PRODUCT_QUANTITY,cart.product_quantity)
        contentValues.put(KEY_PRODUCT_DISCOUNT,cart.product_discount)
        contentValues.put(KEY_PRODUCT_IS_DISCOUNTED,cart.product_is_discounted)
        contentValues.put(KEY_PRODUCT_PHOTO_URL,cart.product_photo_url)

        val success = db.replace(TABLE_NAME,null,contentValues)
        db.close()
        return success
    }

    fun getCart():ArrayList<Cart>{
        val cartList:ArrayList<Cart> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor:Cursor?

        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (e: Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }

        if (cursor.moveToFirst()){
            do {
                val productId = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_ID))
                val productName = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME))
                val productPrice = cursor.getStringOrNull(cursor.getColumnIndex(KEY_PRODUCT_PRICE))
                val productQuantity = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_QUANTITY))
                val productDiscount = cursor.getStringOrNull(cursor.getColumnIndex(KEY_PRODUCT_DISCOUNT))
                val productIsDiscounted = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_IS_DISCOUNTED))
                val productPhotoUrl = cursor.getStringOrNull(cursor.getColumnIndex(KEY_PRODUCT_PHOTO_URL))

                val cart = Cart(productId,productName,productPrice,productQuantity,productDiscount,productIsDiscounted,productPhotoUrl)
                cartList.add(cart)
            }while (cursor.moveToNext())
        }

        db.close()

        return cartList
    }

    fun getProductQuantity(id: String): Int {
        val selectQuery = "SELECT $KEY_PRODUCT_QUANTITY FROM $TABLE_NAME WHERE $KEY_PRODUCT_ID = ?"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, arrayOf(id))
        var quantity = 1
        if (cursor.moveToFirst()){
            quantity = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_QUANTITY)) + 1
        }
        db.close()
        return quantity
    }


    fun truncateCart(){
        val db = this.writableDatabase
        val selectQuery = "DELETE FROM $TABLE_NAME"
        db.execSQL(selectQuery)
        db.close()
    }
}