package com.stevecorp.codecontest.hashcode.facilitator.configurator.input;

public interface InputModel {

    <T extends InputModel> T cloneInput();

}
