package org.cri.swarm.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard
 * <arthur.besnard@ovh.fr>
 */
public class ClientContext {

    private static final int MAX_NAME_BUFFER_SIZE = 256;
    private static final Charset UTF8_CS = Charset.forName("UTF8");
    private static int lastId = 0;
    private final int clientId;
    private final SelectionKey key;
    private InetSocketAddress clientAddress;
    private final ByteBuffer codeReadBuffer = ByteBuffer.allocate(Byte.BYTES);
    private final ByteBuffer readSizeBuffer = ByteBuffer.allocate(Integer.BYTES);
    private final ByteBuffer nameReadBuffer = ByteBuffer.allocate(MAX_NAME_BUFFER_SIZE);
    private final ByteBuffer dataWriteBuffer = ByteBuffer.allocate(Double.BYTES * 10 * 3);
    private int nameSize;
    private final SocketChannel sc;
    private String clientName;
    private double X;
    private double Y;
    private double Z;
    private long lastActionDone;
    private final Code code = new Code();



    ClientContext(SelectionKey key, SocketChannel sc, long currentTime) {
        this.key = key;
        this.clientId = lastId;
        lastId++;
        this.sc = sc;
        lastActionDone = currentTime;
    }

    void read() throws IOException {
        if(!code.isLocked()) {
            sc.read(codeReadBuffer);
            if (!codeReadBuffer.hasRemaining()) {
                codeReadBuffer.flip();
                code.lockCurrentCode(codeReadBuffer.get());
                codeReadBuffer.clear();
            }
        }
        switch (code.getLockedCode()) {
            case (byte) 10:
                sc.read(readSizeBuffer);
                if(!readSizeBuffer.hasRemaining()) {
                    readSizeBuffer.flip();
                    nameSize = readSizeBuffer.getInt();
                    readSizeBuffer.clear();
                    
                    sc.read(nameReadBuffer);
                    while (nameReadBuffer.hasRemaining() || nameReadBuffer.position()<nameSize-1) {
                        sc.read(nameReadBuffer);
                    }
                    nameReadBuffer.flip();
                    clientName = UTF8_CS.decode(nameReadBuffer).toString();
                    code.unlockCurrentCode();
                }
                    
                
        }

    }

    void write(HashSet<ClientContext> listClient) throws IOException {
        ByteBuffer buffW = ByteBuffer.allocate(3 * listClient.size() * Double.BYTES);
        listClient.forEach(context -> {
            buffW.putDouble(context.X);
            buffW.putDouble(context.Y);
            buffW.putDouble(context.Z);
        });
        sc.write(buffW);
    }

    void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
