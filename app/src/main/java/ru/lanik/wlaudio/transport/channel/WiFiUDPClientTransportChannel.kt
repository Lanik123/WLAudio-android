package ru.lanik.wlaudio.transport.channel

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.lanik.wlaudio.consts.ChannelStatus
import ru.lanik.wlaudio.transport.WiFiUDPTransportController

class WiFiUDPClientTransportChannel(
    private val clientSocket: ConnectedDatagramSocket,
    private val onDestroy: () -> WiFiUDPTransportController,
) : ClientTransportChannel {
    private var state: ChannelStatus = ChannelStatus.CHANNEL_OPEN

    override fun sendData(data: ByteArray) = runBlocking {
        withContext(clientSocket.socketContext) {
            if (state == ChannelStatus.CHANNEL_OPEN) {
                state = ChannelStatus.CHANNEL_BUSY
                val datagram = Datagram(
                    buildPacket {
                        writeFully(data)
                    },
                    clientSocket.remoteAddress,
                )
                clientSocket.outgoing.send(datagram)
                state = ChannelStatus.CHANNEL_OPEN
            }
        }
    }

    override fun getState(): ChannelStatus {
        return this.state
    }

    override fun open() {
        changeState(ChannelStatus.CHANNEL_OPEN)
    }

    override fun close() {
        changeState(ChannelStatus.CHANNEL_CLOSE)
    }

    override fun destroy(): WiFiUDPTransportController {
        close()
        return onDestroy()
    }

    private fun changeState(newState: ChannelStatus) {
        when (state == ChannelStatus.CHANNEL_BUSY) {
            false -> state = newState
            true -> changeBusyChannelState(newState)
        }
    }

    private fun changeBusyChannelState(newState: ChannelStatus) = runBlocking {
        withContext(clientSocket.socketContext) {
            state = newState
        }
    }
}