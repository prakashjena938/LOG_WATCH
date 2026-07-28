package com.prakash.LogFetch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AnalysisResult {

    public AnalysisResult(String ip,
        String logPath,
        String AnalyseAt,
        int totalLines,
        int errorCount,
        int warningCount,
        int infoCount,

        List<String> errors,
        List<String> warnings,
        List<String> infos,
        List<String> allLines){

    this.ip=ip;
    this.logPath=logPath;
    this.AnalyseAt=AnalyseAt;
    this.totalLines=totalLines;
    this.errorCount=errorCount;
    this.warningCount=warningCount;
    this.infoCount=infoCount;
    this.errors=errors;
    this.warnings=warnings;
    this.infos=infos;
    this.allLines=allLines;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String ip;
    private String logPath;
    private String AnalyseAt;

    // Username of the authenticated user who ran this analysis.
    private String requestedBy;

    private int totalLines;
    private int errorCount;
    private int warningCount;
    private int infoCount;

    @ElementCollection
    @CollectionTable(name = "log_errors", joinColumns = @JoinColumn (name = "result_id"))
    @Column(name = "error_line",length =3000 )
    private List<String> errors;

    @ElementCollection
    @CollectionTable(name = "log_warning", joinColumns = @JoinColumn (name = "result_id"))
    @Column(name = "error_line",length =3000 )
    private List<String> warnings;

    @ElementCollection
    @CollectionTable(name = "log_infos", joinColumns = @JoinColumn (name = "result_id"))
    @Column(name = "error_line",length =3000 )
    private List<String> infos;

    @ElementCollection
    @CollectionTable(name = "log_allLines", joinColumns = @JoinColumn (name = "result_id"))
    @Column(name = "error_line",length =3000 )
    private List<String> allLines;

}
