package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.psi.*;
import io.reactivex.Maybe;
import io.reactivex.functions.BiFunction;


public class FragmentCreationMethodGetter implements BiFunction<PsiClass, String, Maybe<PsiMethod>> {

    @Override
    public Maybe<PsiMethod> apply(PsiClass clazz, String viewName) throws Exception {
        if (clazz.findMethodsByName("onCreateView", false).length == 1) {
            return Maybe.just(clazz.findMethodsByName("onCreateView", false)[0]);
        } else if (clazz.findMethodsByName("onViewsLoaded", false).length == 1) {
            PsiMethod onViewLoadedMethod = clazz.findMethodsByName("onViewsLoaded", false)[0];
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(clazz.getProject());
            onViewLoadedMethod.setName("onCreateView");
            onViewLoadedMethod.getParameterList().add(factory.createStatementFromText("LayoutInflater inflater,", clazz));
            onViewLoadedMethod.getParameterList().add(factory.createStatementFromText("@Nullable ViewGroup container,", clazz));
            onViewLoadedMethod.getParameterList().add(factory.createStatementFromText("@Nullable Bundle savedInstanceState", clazz));
            PsiStatement inflateView = factory.createStatementFromText("View view = inflater.inflate(" + viewName + ", container, false);", clazz);
            PsiStatement returnView = factory.createStatementFromText("return view;", clazz);
            onViewLoadedMethod.getBody().addBefore(inflateView, onViewLoadedMethod.getBody().getFirstBodyElement());
            onViewLoadedMethod.getBody().addAfter(returnView, onViewLoadedMethod.getBody().getLastBodyElement());
            return Maybe.just(onViewLoadedMethod);
        } else {
            String methodText = "@Override\n" +
                    "    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {\n" +
                    "        View view = inflater.inflate(" + viewName + ", container, false);" +
                    "        return view;" +
                    "}";
            new MethodWriter(methodText, clazz, clazz.getContainingFile()).run();

            return Maybe.just(clazz.findMethodsByName("onCreateView", false)[0]);
        }
    }
}
