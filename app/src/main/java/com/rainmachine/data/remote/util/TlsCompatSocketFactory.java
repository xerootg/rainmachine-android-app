package com.rainmachine.data.remote.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Wraps an {@link SSLSocketFactory} and, on every socket it produces, enables ALL TLS protocol
 * versions and cipher suites that the device's TLS engine supports - not just the modern defaults.
 *
 * Modern Android (API 29+) only enables TLS 1.2/1.3 with a small set of strong cipher suites by
 * default, but RainMachine controllers (especially on older firmware) speak legacy TLS (1.0/1.1)
 * with old cipher suites. Re-enabling everything the device still supports lets the app reach those
 * controllers. Certificate/hostname trust is handled separately (see RemoteModule).
 */
public class TlsCompatSocketFactory extends SSLSocketFactory {

    private final SSLSocketFactory delegate;

    public TlsCompatSocketFactory(SSLSocketFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose)
            throws IOException {
        return enableLegacy(delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return enableLegacy(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
            throws IOException, UnknownHostException {
        return enableLegacy(delegate.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableLegacy(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
                               int localPort) throws IOException {
        return enableLegacy(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableLegacy(Socket socket) {
        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            try {
                sslSocket.setEnabledProtocols(sslSocket.getSupportedProtocols());
            } catch (Exception ignored) {
                // Keep whatever the platform enabled by default.
            }
            try {
                sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
            } catch (Exception ignored) {
                // Keep whatever the platform enabled by default.
            }
        }
        return socket;
    }
}
