package com.sparrow.eslam.plugins.android_annotations_remover.domain;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.actions.DeleteAction;
import io.reactivex.Observable;

public class DeleteInjectViews extends DeleteAction {
    public void run(Document document) {
        Observable.fromArray(document.getText().split("\n"))
                .filter((s) -> s.contains("@ViewById(R.")||s.contains("@EFragment("))
                .subscribe((s)-> {
                    System.out.println("Text is: "+s);
                    int startOffset = document.getText().indexOf(s);
                    int endOffset = document.getLineEndOffset(document.getLineNumber(startOffset));
                    document.replaceString(startOffset,endOffset,"");
                });
    }
}
