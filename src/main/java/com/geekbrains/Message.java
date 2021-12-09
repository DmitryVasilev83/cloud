package com.geekbrains;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode()
@Data
@AllArgsConstructor

public class Message extends AbstractMessage{

    byte[] buf;
    private String typeOperation;
    private String fileName;
    private long fileSize;
    private LocalDateTime time;
    private String response;



}
