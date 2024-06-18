package pro.stuermer.dailyexpenses.data

import pro.stuermer.dailyexpenses.data.persistence.SharingEntity
import pro.stuermer.dailyexpenses.domain.Sharing as DomainSharing

object SharingMapper {
    fun toDomain(sharing: SharingEntity) = DomainSharing(
        identifier = sharing.identifier,
        code = sharing.code,
    )
}
