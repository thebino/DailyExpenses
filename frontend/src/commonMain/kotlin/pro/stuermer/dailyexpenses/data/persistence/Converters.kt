package pro.stuermer.dailyexpenses.data.persistence

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pro.stuermer.dailyexpenses.model.Category as NetworkCategory

class CategoryConverter {

    @TypeConverter
    fun categoryToString(category: NetworkCategory): String = Json.encodeToString(value = category)

    @TypeConverter
    fun stringToCategory(categoryString: String): NetworkCategory =
        categoryString.let { Json.decodeFromString(string = it) }
}

class DateConverter {
    @TypeConverter
    fun stringToDate(dateString: String?): LocalDate? {
        return if (dateString == null) {
            null
        } else {
            LocalDate.parse(dateString)
        }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.toString()
    }
}

class DateTimeConverter {
    @TypeConverter
    fun stringToDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
            LocalDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
