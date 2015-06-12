package org.cri.swarm.server;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
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
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
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
        dc.register(selector,SelectionKey.OP_READ);       
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
