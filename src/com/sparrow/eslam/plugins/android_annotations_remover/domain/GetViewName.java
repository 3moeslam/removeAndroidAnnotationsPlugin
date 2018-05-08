package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import io.reactivex.functions.Function;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class GetViewName implements Function<String,String> {
    @Override
    public String apply(String documentString) {
        return Arrays.stream(documentString.split("\n"))
                .filter(line -> line.contains("@EFragment")||line.contains("@EActivity"))
                .map(line -> line.substring(line.indexOf("(") + 1, line.indexOf(")")))
                .findFirst()
                .get();
    }
}
