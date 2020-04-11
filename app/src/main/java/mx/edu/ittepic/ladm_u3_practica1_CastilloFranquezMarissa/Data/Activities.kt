package mx.edu.ittepic.ladm_u3_practica1_CastilloFranquezMarissa.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import mx.edu.ittepic.ladm_u3_practica1_CastilloFranquezMarissa.Utils.DB
import mx.edu.ittepic.ladm_u3_practica1_CastilloFranquezMarissa.Utils.Utils

class Activities()
{
    var idActivity = -1
    lateinit var pointer : Context
    lateinit var description : String
    lateinit var FechaCaptura : String
    lateinit var FechaEntrega : String

    constructor(description : String, FechaCaptura : String, deliveryDate : String) : this() {
        this.description = description
        this.FechaCaptura = FechaCaptura
        this.FechaEntrega = deliveryDate
    }

    fun asignPointer(context : Context) {
        pointer = context
    }

    fun insertActivity() : Boolean {
        try {
            val db = DB.getInstance(pointer)
            val insert = db?.writableDatabase
            val data = ContentValues()

            data.put("description", description)
            data.put("captureDate", FechaCaptura)
            data.put("deliveryDate", FechaEntrega)

            val answer = insert?.insert("ACTIVITIES", "idActivity", data)
            if(answer?.toInt() == -1){
                return false
            }
            insert?.close()
            db?.close()
        } catch(e : SQLiteException) {
            return false
        }
        return true
    }

    fun getActivities() : List<Activities> {
        val data = ArrayList<Activities>()

        try {
            val db = DB.getInstance(pointer)
            val select = db?.readableDatabase
            val columns = arrayOf("*")

            val cursor = select?.query("ACTIVITIES", columns, null, null, null, null, null)
            if(cursor!!.moveToFirst()) {
                var temporalActivity : Activities?= null
                do {
                    temporalActivity = Activities(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                        )
                    temporalActivity.idActivity = cursor.getInt(0)
                    data.add(temporalActivity)
                } while(cursor.moveToNext())
            }

            select.close()
            db.close()
        } catch(err:SQLiteException) {
            Utils.alertMessage(err.message.toString(), "Atencion", pointer)
        }
        return data
    }

    fun deleteActivity(id : Int) : Boolean {
        try {
            val deleteEvidence = Evidence()
            deleteEvidence.asignPointer(pointer)

            if(deleteEvidence.findEvidence(idActivity.toString()).size != 0) {
                if(!deleteEvidence.deleteEvidence(id, false))
                    return false
            }

            val db = DB.getInstance(pointer)
            val delete = db?.writableDatabase
            val idDelete = arrayOf(id.toString())

            val answer = delete?.delete("ACTIVITIES", "idActivity = ?", idDelete)

            if(answer == 0){
                return false
            }
            delete?.close()
            db?.close()
        } catch(e : SQLiteException) {
            return false
        }
        return true
    }
}