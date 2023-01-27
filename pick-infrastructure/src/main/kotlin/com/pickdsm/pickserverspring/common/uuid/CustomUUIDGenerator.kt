package com.pickdsm.pickserverspring.common.uuid

import com.github.f4b6a3.uuid.UuidCreator
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable

class CustomUUIDGenerator : IdentifierGenerator {

    override fun generate(session: SharedSessionContractImplementor, entity: Any): Serializable =
        UuidCreator.getTimeOrderedEpoch()
}