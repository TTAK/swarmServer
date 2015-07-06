package org.cri.swarm.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 *
 * @author Renaud Bastien <renaudbastien@outlook.com>, Arthur Besnard
 * <arthur.besnard@ovh.fr>
 */
public class Requests {

    private static final Charset UTF8_CHARSET = Charset.forName("UTF8");

    static Request getRequest(byte get) {
        switch (get) {
            case Request.Codes.REGISTER:
                return new Request() {
                    private final ByteBuffer sizeReadBuffer = ByteBuffer.allocate(Integer.BYTES);
                    private ByteBuffer nameReadBuffer;
                    private String name;
                    private boolean ready;

                    @Override
                    public Response process(ClientContext context) {
                        ByteBuffer sendBuffer = ByteBuffer.allocate(Integer.BYTES * (context.getPlayerList().size() + 1) + 1);
                        sendBuffer.put(Response.Codes.GAME_PARAM);
                        sendBuffer.putInt(context.getPlayerList().size());
                        context.getPlayerList().forEach(playerContext -> sendBuffer.putInt(playerContext.clientId));
                        sendBuffer.flip();

                        return new Response() {

                            @Override
                            public boolean isSent() {
                                return !sendBuffer.hasRemaining();
                            }

                            @Override
                            public int write(SocketChannel sc) throws IOException {
                                return sc.write(sendBuffer);
                            }
                            
                        };
                    }

                    @Override
                    public void readData(SocketChannel sc) throws IOException {
                        if (!ready()) {
                            if (!sizeReadBuffer.hasRemaining()) {
                                sizeReadBuffer.flip();
                                nameReadBuffer = ByteBuffer.allocate(sizeReadBuffer.getInt());
                            } else {
                                sc.read(sizeReadBuffer);
                            }
                            if (nameReadBuffer != null) {
                                if (nameReadBuffer.hasRemaining()) {
                                    sc.read(nameReadBuffer);
                                } else {
                                    nameReadBuffer.flip();
                                    name = UTF8_CHARSET.decode(nameReadBuffer).toString();
                                    ready = true;
                                }
                            }
                        }
                    }

                    @Override
                    public boolean ready() {
                        return ready;
                    }
                };
                
            default:
                return new Request() {
                    
                    @Override
                    public Response process(ClientContext context) {
                        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                        buffer.putInt(Response.Codes.INVALID_CODE);
                        buffer.flip();
                        
                        return new Response() {
                            
                            @Override
                            public boolean isSent() {
                                return !buffer.hasRemaining();
                            }

                            @Override
                            public int write(SocketChannel sc) throws IOException {
                                return sc.write(buffer);
                            }
                            
                        };
                    }

                    @Override
                    public void readData(SocketChannel sc) throws IOException {
                        //Nothing to do;
                    }

                    @Override
                    public boolean ready() {
                        return true;
                    }
                };
        }
    }
}
