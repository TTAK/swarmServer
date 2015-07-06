
package org.cri.swarm.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */
interface Request {

    Response process(ClientContext context);

    void readData(SocketChannel sc) throws IOException;

    boolean ready();
    
}
