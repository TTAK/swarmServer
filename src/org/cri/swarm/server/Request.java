
package org.cri.swarm.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Define a client request to the server
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */
interface Request {
    
    static final class Codes{
        static final byte REGISTER = 10;
    }
    /**
     * Process a request, the request must be ready before calling this method.
     * @param context the client Context used during the processing
     * @return the Response of the server after the processing
     */
    Response process(ClientContext context);
    
    /**
     * Read the request data on the given socket channel.
     * @param sc the socket channel used to read
     * @throws IOException 
     */
    void readData(SocketChannel sc) throws IOException;

    /**
     * Indicate if a request is ready to be processed
     * @return true if the request is ready to be processed, false otherwise.
     */
    boolean ready();
    
}
