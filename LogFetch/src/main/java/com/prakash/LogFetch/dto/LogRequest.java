package com.prakash.LogFetch.dto;


import lombok.Data;

@Data
public class LogRequest {
    private String ip;
    private int port;
    private String filePath;
    private String userName;
    private String password;
}
