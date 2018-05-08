package com.sparrow.eslam.plugins.android_annotations_remover;

import com.chaining.Chain;
import com.chaining.ChainConfiguration;
import com.functional.curry.Tuples;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.sparrow.eslam.plugins.android_annotations_remover.domain.*;
import kotlin.Triple;

import java.util.List;


public class DeleteAction extends WriteCommandAction.Simple {

    {
        ChainConfiguration.setDebugging(true);
    }

    private final DocumentMetaData documentMetaData = new DocumentMetaData();
    PsiElementFactory factory;

    protected DeleteAction(Project project, PsiFile file, Document document, PsiClass clazz) {
        super(project, file);

        //factory = JavaPsiFacade.getElementFactory(project);

        documentMetaData.setClazz(clazz);
        documentMetaData.setDocument(document);
        factory = JavaPsiFacade.getElementFactory(project);
    }

    @Override
    protected void run(){
        Chain.let(documentMetaData)
                .map(DocumentMetaData::getDocument)
                .map(Document::getText)
                .map(new GetViewName())
                .debug(viewName -> System.out.println("viewName : "+viewName))
                .pair(new CollectViews().apply(documentMetaData.getDocument()))
                .debug(pair -> System.out.println("pair : "+pair))
                .invoke(() -> new DeleteImports().run(documentMetaData.getDocument(),getProject()))
                .when(pair -> isActivity())
                .then(Tuples.toConsumer(this::injectFindViewByIdForActivity))
                .when(pair -> isFragment())
                .then(Tuples.toConsumer(this::injectFindViewByIdForFragment));
    }


    private boolean isActivity(){
        return Chain.optional(documentMetaData)
                .map(DocumentMetaData::getClazz)
                .map(PsiClass::getSuperClass)
                .map(PsiClass::getName)
                .map(name -> name.contains("RuntimeException"))
                .defaultIfEmpty(false)
                .call();
    }

    private void injectFindViewByIdForActivity(String viewName, List<Triple<String, String, String>> viewsList) throws Exception {
        new ActivityCreationMethodGetter()
                .apply(documentMetaData.getClazz(),viewName)
                .doOnSuccess(method -> System.out.println("activity onCreateMethod -> "+method))
                .subscribe(method -> new AddViewsToCreationMethod().accept(viewsList,method,"view."),Throwable::printStackTrace);
    }

    private boolean isFragment(){
        return Chain.optional(documentMetaData)
                .map(DocumentMetaData::getClazz)
                .map(PsiClass::getSuperClass)
                .map(PsiClass::getName)
                .map(name -> name.contains("Exception"))
                .defaultIfEmpty(false)
                .call();
    }

    private void injectFindViewByIdForFragment(String viewName, List<Triple<String, String, String>> viewsList) throws Exception {
        new FragmentCreationMethodGetter()
                .apply(documentMetaData.getClazz(),viewName)
                .doOnSuccess(method -> System.out.println("fragment onCreateMethod -> "+method))
                .subscribe(method -> new AddViewsToCreationMethod().accept(viewsList,method,""),Throwable::printStackTrace);

    }



}
