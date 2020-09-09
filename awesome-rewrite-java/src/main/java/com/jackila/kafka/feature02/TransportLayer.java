package com.jackila.kafka.feature02;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;
import java.security.Principal;

/**
 * @Author: jackila
 * @Date: 13:59 2020-08-20
 */
public interface TransportLayer extends ScatteringByteChannel, GatheringByteChannel {

    /**
     * Returns true if the channel has handshake and authentication done.
     */
    boolean ready();

    /**
     * Finishes the process of connecting a socket channel.
     */
    boolean finishConnect() throws IOException;

    /**
     * disconnect socketChannel
     */
    void disconnect();

    /**
     * Tells whether or not this channel's network socket is connected.
     */
    boolean isConnected();

    /**
     * returns underlying socketChannel
     */
    SocketChannel socketChannel();


    /**
     * Performs SSL handshake hence is a no-op for the non-secure
     * implementation
     * @throws IOException
     */
    void handshake() throws IOException;

    /**
     * Returns true if there are any pending writes
     */
    boolean hasPendingWrites();

    /**
     * Returns `SSLSession.getPeerPrincipal()` if this is a SslTransportLayer and there is an authenticated peer,
     * `KafkaPrincipal.ANONYMOUS` is returned otherwise.
     */
    Principal peerPrincipal() throws IOException;

    void addInterestOps(int ops);

    void removeInterestOps(int ops);

    boolean isMute();

    /**
     * Transfers bytes from `fileChannel` to this `TransportLayer`.
     *
     * This method will delegate to {@link FileChannel#transferTo(long, long, java.nio.channels.WritableByteChannel)},
     * but it will unwrap the destination channel, if possible, in order to benefit from zero copy. This is required
     * because the fast path of `transferTo` is only executed if the destination buffer inherits from an internal JDK
     * class.
     *
     * @param fileChannel The source channel
     * @param position The position within the file at which the transfer is to begin; must be non-negative
     * @param count The maximum number of bytes to be transferred; must be non-negative
     * @return The number of bytes, possibly zero, that were actually transferred
     * @see FileChannel#transferTo(long, long, java.nio.channels.WritableByteChannel)
     */
    long transferFrom(FileChannel fileChannel, long position, long count) throws IOException;

}
