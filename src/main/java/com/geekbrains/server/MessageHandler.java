package com.geekbrains.server;

import com.geekbrains.AbstractMessage;
import com.geekbrains.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.scene.control.TreeItem;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private Path serverDir = Paths.get("server");


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                AbstractMessage abstractMessage) throws Exception {
        // log.debug("Received: {}", abstractMessage);
        //  ctx.writeAndFlush(abstractMessage);            // отправляет ответ на клиент


        Message msg = (Message) abstractMessage;

        if (msg.getTypeOperation().equals("#file")){

            try (FileOutputStream fos = new FileOutputStream("server/" + msg.getFileName())) {
                fos.write(msg.getBuf());
            }
            Message serverMessage = new Message(null, null, null, 0, LocalDateTime.now(),
                    "file uploaded");
            ctx.writeAndFlush(serverMessage);

        }else if (msg.getTypeOperation().equals("#serverFileView")){
//  по команде передаем на клиент TreeItem<String> с сервера
//            System.out.println("#serverFileView");
//            TreeItem<String> root = getNodesServerDirectory(new File("server"));
//                // как перевести root в байтбуфер

            byte[] buf = "файл".getBytes(StandardCharsets.UTF_8);
            Message serverMessage = new Message(buf, null, null, 0, LocalDateTime.now(),
                    null);
            ctx.writeAndFlush(serverMessage);
        }


    }




    public TreeItem<String> getNodesServerDirectory(File directory) {   //  Отображаем каталог файлов в окне - 2
        TreeItem<String> root = new TreeItem<String>(directory.getName());
        for(File f : Objects.requireNonNull(directory.listFiles())) {

            if(f.isDirectory())
                root.getChildren().add(getNodesServerDirectory(f));
            else
                root.getChildren().add(new TreeItem<String>(f.getName()));
        }
        return root;
    }
}
