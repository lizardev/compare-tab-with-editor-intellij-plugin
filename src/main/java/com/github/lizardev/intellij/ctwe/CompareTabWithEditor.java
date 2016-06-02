package com.github.lizardev.intellij.ctwe;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diff.DiffRequest;
import com.intellij.openapi.diff.SimpleDiffRequest;
import com.intellij.openapi.diff.actions.CompareFiles;
import com.intellij.openapi.diff.ex.DiffContentFactory;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT;
import static com.intellij.openapi.diff.DiffBundle.message;

public class CompareTabWithEditor extends CompareFiles {

    protected DiffRequest getDiffData(DataContext dataContext) {
        final Project project = PROJECT.getData(dataContext);
        final DiffRequest diffRequest = DIFF_REQUEST.getData(dataContext);
        if (diffRequest != null) {
            return diffRequest;
        }
        return getDiffRequest(project, getTabFile(project), getEditorFile(project));
    }

    private VirtualFile getTabFile(Project project) {
        return FileEditorManagerEx.getInstanceEx(project).getCurrentFile();
    }

    private VirtualFile getEditorFile(Project project) {
        return EditorService.getInstance(project).getCurrentFile();
    }

    @Nullable
    private static DiffRequest getDiffRequest(Project project, VirtualFile left, VirtualFile right) {
        String title = message("diff.element.qualified.name.vs.element.qualified.name.dialog.title",
                getVirtualFileContentTitle(left),
                getVirtualFileContentTitle(right));
        SimpleDiffRequest diffRequest = DiffContentFactory.compareVirtualFiles(project, left, right, title);
        if (diffRequest == null) {
            return null;
        }
        diffRequest.setContentTitles(getVirtualFileContentTitle(left), getVirtualFileContentTitle(right));
        return diffRequest;
    }

    public void update(AnActionEvent e) {
        Project project = e.getData(PROJECT);
        VirtualFile tabFile = getTabFile(project);
        VirtualFile editorFile = getEditorFile(project);
        Presentation presentation = e.getPresentation();
        if (editorFile != null && tabFile != null && !tabFile.equals(editorFile)) {
            presentation.setVisible(true);
        } else {
            presentation.setVisible(false);
        }
    }
}