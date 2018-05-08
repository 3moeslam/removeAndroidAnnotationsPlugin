package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.actions.DeleteAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import io.reactivex.Observable;

public class DeleteImports extends DeleteAction {

    public void run(Document document, Project project) {
        Observable.fromArray(document.getText().split("\n"))
                .filter(this::isRemovableLine)
                .subscribe((line) -> deleteLine(document, line), Throwable::printStackTrace, () -> commitDocument(document, project));
    }

    private boolean isRemovableLine(String line) {
        return line.contains("org.androidannotations") || line.contains("@ViewById") || line.contains("@EFragment");
    }

    private void deleteLine(Document document, String line) {
        int startOffset = document.getText().indexOf(line);
        int endOffset = document.getLineEndOffset(document.getLineNumber(startOffset));
        document.deleteString(startOffset, endOffset);
    }

    private void commitDocument(Document document, Project project) {
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        manager.commitDocument(document);
    }
}
