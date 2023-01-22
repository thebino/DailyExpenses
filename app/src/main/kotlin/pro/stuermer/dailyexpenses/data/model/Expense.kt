@file:UseSerializers(LocalDateSerializer::class, LocalDateTimeSerializer::class, CategorySerializer::class)
package pro.stuermer.dailyexpenses.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pro.stuermer.dailyexpenses.domain.model.Category
import pro.stuermer.dailyexpenses.data.persistence.model.Expense as PersistenceExpense

@Serializable
data class Expense(
    @SerialName("id") val id: String,
    val category: Category,
    val expenseDate: LocalDate,
    val creationDate: LocalDateTime,
    val updatedDate: LocalDateTime? = null,
    val deletedDate: LocalDateTime? = null,
    val description: String,
    val amount: Float,
) {
    fun toPersistenceExpense(): PersistenceExpense = PersistenceExpense(
        identifier = id,
        category = category,
        expenseDate = expenseDate,
        creationDate = creationDate,
        updatedDate = updatedDate,
        deletedDate = deletedDate,
        description = description,
        amount = amount
    )
}

@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("LocalDate")
    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }
    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("LocalDateTime")
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}

@Serializer(forClass = Category::class)
object CategorySerializer : KSerializer<Category> {
    override fun serialize(encoder: Encoder, value: Category) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Category {
        return Category.valueOf(decoder.decodeString())
    }
}
