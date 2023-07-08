package ru.lanik.wlaudio.transport.channel

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import ru.lanik.wlaudio.consts.ChannelStatus
import ru.lanik.wlaudio.transport.WiFiTCPTransportController

class WiFiTCPServerTransportChannel(
    private val serverSocket: ServerSocket,
    private val onDestroy: () -> WiFiTCPTransportController,
) : ServerTransportChannel {
    private lateinit var channel: Socket
    private lateinit var readChannel: ByteReadChannel
    private var state: ChannelStatus = ChannelStatus.CHANNEL_OPEN

    private val serverAcceptIncomingConnection = CoroutineScope(serverSocket.socketContext).async(start = CoroutineStart.LAZY) {
        channel = serverSocket.accept()
        readChannel = channel.openReadChannel()
    }

    init {
        serverAcceptIncomingConnection.start()
    }

    override suspend fun getData(): ByteArray? {
        if (state == ChannelStatus.CHANNEL_OPEN && ::readChannel.isInitialized) {
            if (readChannel.availableForRead != 0) {
                changeState(ChannelStatus.CHANNEL_BUSY)
                val dst = ByteArray(readChannel.availableForRead)
                readChannel.readAvailable(dst)
                changeState(ChannelStatus.CHANNEL_OPEN)
                return dst
            }
        }
        return null
    }

    override fun getState(): ChannelStatus {
        return this.state
    }

    override fun open() {
        if (::channel.isInitialized) {
            readChannel = channel.openReadChannel()
            changeState(ChannelStatus.CHANNEL_OPEN)
        } else {
            serverAcceptIncomingConnection.start()
        }
    }

    override fun close() {
        if (::channel.isInitialized) {
            changeState(ChannelStatus.CHANNEL_CLOSE)
            channel.close()
        } else {
            serverAcceptIncomingConnection.cancel()
        }
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
        withContext(channel.socketContext) {
            state = newState
        }
    }
}