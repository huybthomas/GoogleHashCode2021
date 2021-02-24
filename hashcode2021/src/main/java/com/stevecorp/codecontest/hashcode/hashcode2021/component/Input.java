package com.stevecorp.codecontest.hashcode.hashcode2021.component;

import com.stevecorp.codecontest.hashcode.facilitator.configurator.input.InputModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class Input implements InputModel {

    @Override
    public Input cloneInput() {
        return this.toBuilder()
                .build();
    }

}
