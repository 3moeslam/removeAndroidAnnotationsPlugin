package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public class MethodWriter extends WriteCommandAction.Simple  {
    private String methodText ;
    PsiClass clazz;
    protected MethodWriter(String methodText, PsiClass clazz, PsiFile... files) {
        super(clazz.getProject(), files);
        this.methodText = methodText;
        this.clazz = clazz;
    }

    @Override
    protected void run() {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(clazz.getProject());
        PsiMethod onCreate = factory.createMethodFromText(methodText,clazz);
        clazz.add(onCreate);
    }
}
