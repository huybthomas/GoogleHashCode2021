package com.stevecorp.codecontest.hashcode.facilitator.configurator.input.model;

public interface InputModel {

    <T extends InputModel> T cloneInput();

}
