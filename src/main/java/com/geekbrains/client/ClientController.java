package com.geekbrains.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.geekbrains.Message;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class ClientController implements Initializable {

    public TextField input;
    public ListView<String> listView;
    public TreeView<String> treeViewClient;
    public TreeView<String> treeViewServer;
    private Path clientDir;
    private NettyNet net;
    private byte[] buf;

    public void sendMsg() throws IOException {

    }


    private void sendFile(String fileName) throws IOException {         //отправляем файл на сервер
        Path filePath = clientDir.resolve(fileName);
        byte[] buf = Files.readAllBytes(filePath);
        long fileSize = Files.size(filePath);
        net.sendMessage(new Message(buf, "#file", fileName, fileSize, LocalDateTime.now(),
                null));
    }


    private void initClickListener() {         // по 1 нажатию мыши название файла из окна директории listView
                                                // появляется в поле inputText
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                String item = listView.getSelectionModel().getSelectedItem();
                input.setText(item);
            }
        });
    }
    private void initClickListener2() {         // по 1 нажатию мыши название файла из окна директории treeView
        // появляется в поле inputText
        treeViewClient.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                String item = treeViewClient.getSelectionModel().getSelectedItem().getValue();
                input.setText(item);
            }
        });
    }



    public void clickButtonUpload(ActionEvent actionEvent) {     // методы кнопок
        try {
            sendFile(input.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clickButtonDownload(ActionEvent actionEvent) {


    }

    public void clickButtonDelete(ActionEvent actionEvent) {


    }

    public void clickButtonRename(ActionEvent actionEvent) {


    }

    public void clickButtonUpdateFiles(ActionEvent actionEvent) {

        fillServerFileView();
    }

    private void fillFileView() throws IOException {          // показывает в окне все файлы client директории
                                                               // Уже не нужно но пусть пока будет
        List<String> files = Files.list(clientDir)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
        listView.getItems().addAll(files);
    }

    private void fillClientFileView(){                   //  Отображаем каталог файлов клиента в окне
//        2. как указать в пути к файлу диск. Почему в TreeView по пути treeView.setRoot(getNodesForDirectory(new File("D:\Programming")));
//        дерево строится и отображается нормально, а по пути  treeView.setRoot(getNodesForDirectory(new File("D:")));
//        выдает только папки внутри проекта
        treeViewClient.setRoot(getNodesForDirectory(new File("client")));
    }

    private void fillServerFileView(){                   //  Отображаем каталог файлов сервера в окне

//        treeViewServer.setRoot(getNodesForDirectory(new File("server")));

        net.sendMessage(new Message(null, "#serverFileView", null, 0, LocalDateTime.now(),
                null));

//      пока непонятно как получить ответ от сервера через callback

    }

    public TreeItem<String> getNodesForDirectory(File directory) {   //  Отображаем каталог файлов в окне - 2
        TreeItem<String> root = new TreeItem<String>(directory.getName());
        for(File f : Objects.requireNonNull(directory.listFiles())) {

            if(f.isDirectory())
                root.getChildren().add(getNodesForDirectory(f));
            else
                root.getChildren().add(new TreeItem<String>(f.getName()));
        }
        return root;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buf = new byte[8192];
        clientDir = Paths.get("client");

        try {
            fillFileView();
            fillClientFileView();

        } catch (IOException e) {
            e.printStackTrace();
        }

        initClickListener();
        initClickListener2();
        net = new NettyNet(System.out::println);

    }
}
