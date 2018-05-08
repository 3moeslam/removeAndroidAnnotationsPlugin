package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import kotlin.Triple;

import java.util.List;
import java.util.function.BiConsumer;

public class AddViewsToCreationMethod {

    public void accept(List<Triple<String, String, String>> triples, PsiMethod creationMethod, String inflateFrom) {
        triples.forEach(item -> {
            String statment = item.getThird() + " = " + inflateFrom + "findViewById(" + item.getFirst() + ");";
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(creationMethod.getProject());
            PsiStatement statement = factory.createStatementFromText(statment, creationMethod);
            if (inflateFrom.equals(""))
                creationMethod.addAfter(statement, creationMethod.getBody().getStatements()[1]);
            else
                creationMethod.addAfter(statement, creationMethod.getBody().getStatements()[0]);

        });
    }
}
