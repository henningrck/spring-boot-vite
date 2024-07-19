package com.henningstorck.springbootvite.common.frontend;

public record FrontendFile(FrontendFileType type, String path) {
    public static FrontendFile script(String path) {
        return new FrontendFile(FrontendFileType.SCRIPT, path);
    }

    public static FrontendFile stylesheet(String path) {
        return new FrontendFile(FrontendFileType.STYLESHEET, path);
    }
}

