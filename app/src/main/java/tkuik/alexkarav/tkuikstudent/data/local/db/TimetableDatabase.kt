package tkuik.alexkarav.tkuikstudent.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import tkuik.alexkarav.tkuikstudent.data.local.db.models.TimetableLocalModel

@Database(version = 2, entities = [TimetableLocalModel::class])
abstract class TimetableDatabase: RoomDatabase() {
   abstract fun getTimetableDao(): TimetableDao

   companion object {
      val MIGRATION_1_2 = object : Migration(1, 2) {
         override fun migrate(db: SupportSQLiteDatabase) {
            // Создайте новую таблицу с autoGenerate для id
            db.execSQL("""
            CREATE TABLE TimetableLocalModel_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                dayOfWeek INTEGER NOT NULL,
                pairNumber INTEGER NOT NULL,
                distantPair INTEGER NOT NULL,
                subjectName TEXT NOT NULL,
                teacherName TEXT NOT NULL,
                cabinetNumber TEXT NOT NULL,
                pairBeginTime TEXT NOT NULL,
                pairEndTime TEXT NOT NULL,
                denominatorOptions TEXT NOT NULL
            )
        """.trimIndent())

            // Скопируйте данные из старой таблицы в новую
            db.execSQL("""
            INSERT INTO TimetableLocalModel_new (dayOfWeek, pairNumber, distantPair, subjectName, teacherName, cabinetNumber, pairBeginTime, pairEndTime, denominatorOptions)
            SELECT dayOfWeek, pairNumber, distantPair, subjectName, teacherName, cabinetNumber, pairBeginTime, pairEndTime, denominatorOptions FROM TimetableLocalModel
        """.trimIndent())

            // Удалите старую таблицу
            db.execSQL("DROP TABLE TimetableLocalModel")

            // Переименуйте новую таблицу в старое имя
            db.execSQL("ALTER TABLE TimetableLocalModel_new RENAME TO TimetableLocalModel")
         }
      }
   }
}