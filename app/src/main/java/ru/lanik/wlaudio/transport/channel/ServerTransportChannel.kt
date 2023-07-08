package ru.lanik.wlaudio.transport.channel

import ru.lanik.wlaudio.consts.ChannelStatus
import ru.lanik.wlaudio.transport.TransportController

interface ServerTransportChannel {
    suspend fun getData(): ByteArray?

    fun getState(): ChannelStatus

    fun open()

    fun close()

    fun destroy(): TransportController
}