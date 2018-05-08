package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.psi.*;
import io.reactivex.Maybe;
import io.reactivex.functions.BiFunction;


public class ActivityCreationMethodGetter implements BiFunction<PsiClass,String,Maybe<PsiMethod>> {

    @Override
    public Maybe<PsiMethod> apply(PsiClass clazz,String viewName) throws Exception {
        if(clazz.findMethodsByName("onCreate",false).length==1){
            return Maybe.just(clazz.findMethodsByName("onCreate",false)[0]);
        }else if(clazz.findMethodsByName("onViewsLoaded",false).length ==1){
            PsiMethod onViewLoadedMethod = clazz.findMethodsByName("onViewsLoaded",false)[0];
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(clazz.getProject());
            onViewLoadedMethod.setName("onCreate");
            onViewLoadedMethod.getParameterList().add(factory.createStatementFromText("Bundle savedInstanceState", clazz));
            PsiStatement superOnCreate = factory.createStatementFromText("super.onCreate(savedInstanceState);", clazz);
            PsiStatement setContentView = factory.createStatementFromText("setContentView(" + viewName + ");\n", clazz);
            onViewLoadedMethod.getBody().addBefore(superOnCreate, onViewLoadedMethod.getBody().getFirstBodyElement());
            onViewLoadedMethod.getBody().addAfter(setContentView,  onViewLoadedMethod.getBody().getFirstBodyElement());
            return Maybe.just(onViewLoadedMethod);
        }else {
            //Create onCreateMethod
            String methodText = "@Override\n" +
                    "    public void onCreate(Bundle savedInstanceState) {\n" +
                    "        super.onCreate(savedInstanceState);\n" +
                    "        setContentView("+viewName+");\n" +
                    "        }";
            new MethodWriter(methodText, clazz, clazz.getContainingFile()).run();

            return Maybe.just(clazz.findMethodsByName("onCreate", false)[0]);
        }
    }
}
