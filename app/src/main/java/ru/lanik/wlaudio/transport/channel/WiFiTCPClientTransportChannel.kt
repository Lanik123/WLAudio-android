package ru.lanik.wlaudio.transport.channel

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.lanik.wlaudio.consts.ChannelStatus
import ru.lanik.wlaudio.transport.WiFiTCPTransportController

class WiFiTCPClientTransportChannel(
    private val clientSocket: Socket,
    private val onDestroy: () -> WiFiTCPTransportController,
) : ClientTransportChannel {
    private var state: ChannelStatus = ChannelStatus.CHANNEL_OPEN
    private val writeChannel = clientSocket.openWriteChannel()

    override fun sendData(data: ByteArray) = runBlocking {
        withContext(clientSocket.socketContext) {
            if (state == ChannelStatus.CHANNEL_OPEN) {
                state = ChannelStatus.CHANNEL_BUSY
                writeChannel.writeAvailable(data)
                state = ChannelStatus.CHANNEL_OPEN
            }
        }
    }

    override fun getState(): ChannelStatus {
        return this.state
    }

    override fun open() {
        clientSocket.openWriteChannel()
        changeState(ChannelStatus.CHANNEL_OPEN)
    }

    override fun close() {
        changeState(ChannelStatus.CHANNEL_CLOSE)
        clientSocket.close()
    }

    override fun destroy(): WiFiTCPTransportController {
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