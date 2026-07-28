package com.prakash.LogFetch.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.prakash.LogFetch.dto.LogRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class FetchLogs {


    public String fetch(LogRequest request) throws Exception{


        Session  session = null;
        ChannelSftp sftp = null;   // set as null if when  it is not stated yet
        JSch jsch = new JSch();

        try{
        session = jsch.getSession( request.getUserName(),request.getIp(),request.getPort());
        session.setPassword(request.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(15000);



        //connect the channel through the sftp protocol

        sftp = (ChannelSftp) session.openChannel("sftp");  //open sftp channel
        sftp.connect();


        // time to read the file and return it

        InputStream inputStream = sftp.get(request.getFilePath());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        StringBuilder rawLogs = new StringBuilder();
        String line = bufferedReader.readLine();

        while (line !=null){
            rawLogs.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        return rawLogs.toString();

        }
        finally {
            if (sftp!=null){
                sftp.disconnect();
            }

            if(session!=null){
                session.disconnect();
            }

        }

    }

    //fetch last n number of line

    public String fetchLastNLine(LogRequest request, int n) throws Exception{

        String all = fetch(request);
        String[]lines = all.split("\n");
        int start = lines.length-n;

        StringBuilder sb = new StringBuilder();
        for(int i = start;i<lines.length;i++){  //i = start because array is a zero "0" index based number
            sb.append(lines[i]).append("\n");
        }

        return sb.toString();
    }

}
