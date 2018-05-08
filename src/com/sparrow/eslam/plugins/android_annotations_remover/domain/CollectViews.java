package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.openapi.editor.Document;
import io.reactivex.functions.Function;
import kotlin.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class CollectViews implements Function<Document, List<Triple<String, String, String>>> {

    private final static String ANNOTATION_NAME = "ViewById";

    // (A => B) -----map-------> (T[A] => T[B])
    // (A => T[B]) ---flatMap--> (T[A] => T[B])

    @Override
    public List<Triple<String, String, String>> apply(Document document){
        return Arrays.stream(document.getText().split(";"))
                .filter(line -> line.contains("@"))
                .filter(line-> line.contains(ANNOTATION_NAME))
                .map(this::viewTriple)
                .collect(Collectors.toList());
    }

    @NotNull
    private Triple<String, String, String> viewTriple(String line) {
        String viewId = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
        String viewType = line.trim().split("\n")[1].trim().split(" ")[0];
        String viewName = line.trim().split("\n")[1].trim().split(" ")[1];
        return new Triple<>(viewId, viewType,viewName);
    }
}
