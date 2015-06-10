package org.cri.swarm.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arthur besnard
 */
public class Server {

    private final DatagramChannel dc;
    //private
    private final Selector selector;
    private final HashSet<ClientContext> listClient = new HashSet<>();

    public Server(int port) throws IOException {
        dc = DatagramChannel.open();
        dc.configureBlocking(false);
        dc.bind(new InetSocketAddress(port));
        selector=Selector.open();
        dc.register(selector,SelectionKey.OP_ACCEPT);       
    }

    public void launch() throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        while (!Thread.interrupted()) {
            selector.select();
            for (SelectionKey key : selectedKeys) {
                if (key.isValid() && key.isWritable()) {
                    doWrite(key);
                }
                if (key.isValid() && key.isReadable()) {
                    doRead(key);
                }
            }
            selectedKeys.clear();
            
        }
    }

    public void main(String args[]) {
        int port;
        if (args.length < 1) {
            port = 5042;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try {
            Server server = new Server(port);
            server.launch();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doWrite(SelectionKey key) throws IOException {
        ClientContext context = (ClientContext) key.attachment();
        if(context == null) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "illegal state for client");
        } else {
            context.write(listClient);
        }
        
    }

    private void doRead(SelectionKey key) throws IOException {
        ClientContext context = (ClientContext) key.attachment();
        if(context == null) {
            context = new ClientContext(key);
            listClient.add(context);
        }
        context.read();
        
        
    }
}
