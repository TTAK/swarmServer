
package org.cri.swarm.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */
interface Response {
    
    static final class Codes{
        static final byte GAME_PARAM = 11;
        static final byte INVALID_CODE = 100;
    }

    boolean isSent();

    int write(SocketChannel sc) throws IOException;

}
