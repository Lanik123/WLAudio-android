package ru.lanik.wlaudio.transport

import ru.lanik.wlaudio.transport.channel.ClientTransportChannel
import ru.lanik.wlaudio.transport.channel.ServerTransportChannel

sealed interface TransportController {
    fun createClientChannel(): ClientTransportChannel
    fun createServerChannel(): ServerTransportChannel
}