package com.stevecorp.codecontest.hashcode.facilitator.configurator;

public interface FinalConfigBuilder {

    FinalConfigBuilder withCustomInputFolder(String fullInputFolderPath);
    FinalConfigBuilder withCustomOutputFolder(String fullOutputFolderPath);
    void run();

}
