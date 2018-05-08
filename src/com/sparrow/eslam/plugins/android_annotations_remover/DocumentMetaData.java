package com.sparrow.eslam.plugins.android_annotations_remover;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import kotlin.Triple;

import java.util.ArrayList;
import java.util.List;

public class DocumentMetaData {
    private PsiClass clazz;
    private Document document;
    private final List<Triple<String,String,String>> viewsList = new ArrayList<>();
    private String viewName;
    private String superClassName;
    private PsiMethod creationMethod;

    public PsiClass getClazz() {
        return clazz;
    }

    public void setClazz(PsiClass clazz) {
        this.clazz = clazz;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<Triple<String, String, String>> getViewsList() {
        return viewsList;
    }

    public void addToViews(Triple<String, String, String> viewItem) {
        this.viewsList.add(viewItem);
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }

    public PsiMethod getCreationMethod() {
        return creationMethod;
    }

    public void setCreationMethod(PsiMethod creationMethod) {
        this.creationMethod = creationMethod;
    }
}
