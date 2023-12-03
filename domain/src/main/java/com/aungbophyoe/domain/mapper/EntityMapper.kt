package com.aungbophyoe.domain.mapper

interface EntityMapper<Entity,DomainModel> {
    fun mapFromEntity(entity: Entity) : DomainModel
    fun mapToEntity(domainModel: DomainModel) : Entity
}