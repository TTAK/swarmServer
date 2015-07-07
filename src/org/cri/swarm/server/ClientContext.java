package org.cri.swarm.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;
import org.cri.tcputils.TCPUtils;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */
public class ClientContext {
    private static int lastId = 0;
    final int clientId;
    
    private final SocketChannel sc;
    private final SelectionKey key;
    private long lastActionDone;
    private long currentTime;
    private final Server server;
    private String name;
    
    double X;
    double Y;
    double Z;
    
    private final ByteBuffer codeReadBuffer = ByteBuffer.allocate(Byte.BYTES);
    private Request request;
    private Response response;

    ClientContext(SelectionKey key, SocketChannel sc, long currentTime, Server server) {
        this.server = server;
        this.key = key;
        this.key.interestOps(SelectionKey.OP_READ);
        this.clientId = lastId;
        lastId++;
        this.sc = sc;
        this.lastActionDone = currentTime;
        this.currentTime = currentTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    void read() throws IOException {
        if(request == null){
            request = readCode();
        }
        else if(!request.ready()){
            request.readData(sc);
        }else{
            response = request.process(this);
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }
    
    private Request readCode() throws IOException{
        if(sc.read(codeReadBuffer) == -1){
            this.close();
            return null;
        }else{
            if(!codeReadBuffer.hasRemaining()){
                codeReadBuffer.flip();
                Request r = Requests.getRequest(codeReadBuffer.get());
                codeReadBuffer.clear();
                
                return r;
            }else{
                return null;
            }
        }
    }

    void write() throws IOException {
        if(response == null){
            this.close();
        }else if (!response.isSent()){
            if(response.write(sc) == -1){
                this.close();
            }
            if(response.isSent()){
                this.response = null;
                key.interestOps(SelectionKey.OP_READ);
            }
        }
        lastActionDone = currentTime;
    }
    

    void close() {
        key.attach(null);
        key.cancel();
        TCPUtils.silentlyClose(sc);
    }
    
    boolean isTimeOut(long currentTime, long timeOut){
        this.currentTime = currentTime;
        return lastActionDone + timeOut < currentTime;
    }

    Set<ClientContext> getPlayerList() {
        return server.getListClient();
    }
}
