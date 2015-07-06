package org.cri.swarm.server;

import java.io.IOException;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */

interface ReadOp {
    void read() throws IOException;
    boolean isFinished();
}
