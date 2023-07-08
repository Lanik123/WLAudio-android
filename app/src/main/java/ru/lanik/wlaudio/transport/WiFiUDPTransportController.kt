package ru.lanik.wlaudio.transport

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import ru.lanik.wlaudio.transport.channel.ClientTransportChannel
import ru.lanik.wlaudio.transport.channel.ServerTransportChannel
import ru.lanik.wlaudio.transport.channel.WiFiUDPClientTransportChannel
import ru.lanik.wlaudio.transport.channel.WiFiUDPServerTransportChannel

class WiFiUDPTransportController(
    private val destination: SocketAddress,
) : TransportController {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private var socket = aSocket(selectorManager).udp()

    override fun createClientChannel(): ClientTransportChannel {
        val client = socket.connect(destination)
        return WiFiUDPClientTransportChannel(
            clientSocket = client,
            onDestroy = {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        client.close()
                    }
                }
                return@WiFiUDPClientTransportChannel WiFiUDPTransportController(client.remoteAddress)
            },
        )
    }

    override fun createServerChannel(): ServerTransportChannel {
        val server = socket.bind()
        return WiFiUDPServerTransportChannel(
            serverSocket = server,
            onDestroy = {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        server.close()
                    }
                }
                return@WiFiUDPServerTransportChannel WiFiUDPTransportController(server.localAddress)
            },
        )
    }
}