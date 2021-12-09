package com.geekbrains.client;

import com.geekbrains.AbstractMessage;
import com.geekbrains.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.*;


public class ClientMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private final OnMessageReceived callback;

    public ClientMessageHandler(OnMessageReceived callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                AbstractMessage abstractMessage) throws Exception {

        Message msg = (Message) abstractMessage;
        callback.onReceive(msg.getResponse());
        JOptionPane.showMessageDialog(null, msg.getResponse());    //ответ сервера в отдельном окне(swing)


    }
}
