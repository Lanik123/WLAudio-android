package ru.lanik.wlaudio.transport

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.lanik.wlaudio.transport.channel.ClientTransportChannel
import ru.lanik.wlaudio.transport.channel.ServerTransportChannel
import ru.lanik.wlaudio.transport.channel.WiFiTCPClientTransportChannel
import ru.lanik.wlaudio.transport.channel.WiFiTCPServerTransportChannel

class WiFiTCPTransportController(
    private val destination: SocketAddress,
) : TransportController {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private var socket = aSocket(selectorManager).tcp()

    override fun createClientChannel(): ClientTransportChannel = runBlocking {
        val client = socket.connect(destination)
        return@runBlocking WiFiTCPClientTransportChannel(
            clientSocket = client,
            onDestroy = {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        client.close()
                    }
                }
                return@WiFiTCPClientTransportChannel WiFiTCPTransportController(client.remoteAddress)
            },
        )
    }

    override fun createServerChannel(): ServerTransportChannel = runBlocking {
        val server = socket.bind(localAddress = destination)
        return@runBlocking WiFiTCPServerTransportChannel(
            serverSocket = server,
            onDestroy = {
                val localAddress = server.localAddress
                runBlocking {
                    withContext(Dispatchers.IO) {
                        server.close()
                    }
                }
                return@WiFiTCPServerTransportChannel WiFiTCPTransportController(localAddress)
            },
        )
    }
}