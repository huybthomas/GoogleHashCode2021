package com.stevecorp.codecontest.hashcode.facilitator.config;

public interface FinalConfigBuilder {

    FinalConfigBuilder withCustomInputFolder(String fullInputFolderPath, boolean createFolderIfAbsent);
    FinalConfigBuilder withCustomOutputFolder(String fullOutputFolderPath, boolean createFolderIfAbsent);
    void run();

}
