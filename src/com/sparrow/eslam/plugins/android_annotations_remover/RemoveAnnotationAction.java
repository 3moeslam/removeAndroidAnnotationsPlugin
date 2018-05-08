package com.sparrow.eslam.plugins.android_annotations_remover;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import static org.codehaus.groovy.tools.groovydoc.Main.execute;

public class RemoveAnnotationAction extends BaseGenerateAction {


    public RemoveAnnotationAction() {
        super(null);
    }

    public RemoveAnnotationAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // Prepare file
        try {
            Project project = event.getData(PlatformDataKeys.PROJECT);
            Editor editor = event.getData(PlatformDataKeys.EDITOR);
            PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
            PsiClass clazz = getTargetClass(editor, file);
            Document document = editor.getDocument();
            new DeleteAction(project, file, document,clazz).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
