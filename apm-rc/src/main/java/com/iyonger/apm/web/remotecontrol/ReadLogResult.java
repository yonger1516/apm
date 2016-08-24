package com.iyonger.apm.web.remotecontrol;

public class ReadLogResult {

    public ReadLogResult(int startLine, int endLine, String content){
        super();
        this.startLine = startLine;
        this.endLine = endLine;
        this.content = content;
    }

    private int    startLine;
    private int    endLine;
    private String content;

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
