
package org.cri.tcputils;

import java.io.IOException;
import java.nio.channels.Channel;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard <arthur.besnard@ovh.fr>
 */
public class TCPUtils {
    
    public static void silentlyClose(Channel channel){
        if (channel !=  null) {
            try {
                channel.close();
            } catch (IOException ex) {
                //Nothing to do
            }
        }
    }
    
}
