package com.prakash.LogFetch.service;

import com.prakash.LogFetch.dto.LogRequest;
import com.prakash.LogFetch.model.AnalysisResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class LogAnalysis {

    //history list array
    List<AnalysisResult> history = new ArrayList<>();
    static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") ;


    // result means output
    AnalysisResult lastResult = null;


public AnalysisResult analysis(String rawLogs, LogRequest request){

    List<String> errors = new ArrayList<>();
    List<String> warnings  = new ArrayList<>();
    List<String> infos = new ArrayList<>();
    List<String> allLines = new ArrayList<>();

    String [] lines = rawLogs.split("\n");
    for( String line : lines){

        if (line.isBlank()){
            continue;
        }

        allLines.add(line);
        String upperlatter = line.toUpperCase();

        if (upperlatter.contains("ERROR") || upperlatter.contains("EXCEPTION") || upperlatter.contains("FATAL")){
            errors.add(line);
        }

        else if (upperlatter.contains("WARN") || upperlatter.contains("WARNING") ){
            warnings.add(line);
        }

        else if (upperlatter.contains("INFO")){
            infos.add(line);
        }
    }


    AnalysisResult result=new AnalysisResult(
            request.getIp(),
            request.getFilePath(),
            LocalDateTime.now().format(Formatter),
            allLines.size(),
            errors.size(),
            warnings.size(),
            infos.size(),
            errors,
            warnings,
            infos,
            allLines
    );

    this.lastResult = result;
    this.history.add(result);  // history list updated by one

    return lastResult;

}



    public AnalysisResult getLastResult(){
    return lastResult;
    }


    //when history calls
    public List<AnalysisResult> getHistory(){
    return  history;
    }


    //when clear history calls

    public void clearHistory(){
    history.clear();
    lastResult =null;
    }


}
