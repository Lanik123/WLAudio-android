package ru.lanik.wlaudio.transport.channel

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.readAvailable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.lanik.wlaudio.consts.ChannelStatus
import ru.lanik.wlaudio.transport.WiFiUDPTransportController

class WiFiUDPServerTransportChannel(
    private val serverSocket: BoundDatagramSocket,
    private val onDestroy: () -> WiFiUDPTransportController,
) : ServerTransportChannel {
    private var state: ChannelStatus = ChannelStatus.CHANNEL_OPEN

    override suspend fun getData(): ByteArray? {
        if (state == ChannelStatus.CHANNEL_OPEN) {
            val datagram = serverSocket.receive()
            val data = ByteArray(datagram.packet.pool.capacity)
            datagram.packet.readAvailable(data)
            return data
        }
        return null
    }

    override fun getState(): ChannelStatus {
        return this.state
    }

    override fun open() {
        changeState(ChannelStatus.CHANNEL_OPEN)
    }

    override fun close() {
        changeState(ChannelStatus.CHANNEL_CLOSE)
        serverSocket.close()
    }

    override fun destroy(): WiFiUDPTransportController {
        return onDestroy()
    }

    private fun changeState(newState: ChannelStatus) {
        when (state == ChannelStatus.CHANNEL_BUSY) {
            false -> state = newState
            true -> changeBusyChannelState(newState)
        }
    }

    private fun changeBusyChannelState(newState: ChannelStatus) = runBlocking {
        withContext(serverSocket.socketContext) {
            state = newState
        }
    }
}