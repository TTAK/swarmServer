
package org.cri.swarm.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */
interface Response {

    boolean isSent();

    int write(SocketChannel sc) throws IOException;

}
