package org.cri.swarm.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.HashSet;

/**
 *
 * @author arthur
 */
public class ClientContext {

    private static int lastId = 0;
    private final int clientId;
    private final SelectionKey key;
    private InetSocketAddress clientAddress;
    private final ByteBuffer buffR = ByteBuffer.allocate(3 * Double.BYTES);
    private final DatagramChannel dc;
    private double X;
    private double Y;
    private double Z;

    ClientContext(SelectionKey key) {
        this.key = key;
        key.attach(this);
        this.clientId = lastId;
        lastId++;
        dc = (DatagramChannel) key.channel();
    }

    void read() throws IOException {

        dc.read(buffR);
        buffR.flip();
        X = buffR.getDouble();
        Y = buffR.getDouble();
        Z = buffR.getDouble();
        buffR.clear();
    }

    void write(HashSet<ClientContext> listClient) throws IOException {
        ByteBuffer buffW = ByteBuffer.allocate(3 * listClient.size() * Double.BYTES);
        listClient.forEach(context -> {
            buffW.putDouble(context.X);
            buffW.putDouble(context.Y);
            buffW.putDouble(context.Z);
        });
        dc.write(buffW);
    }

}
