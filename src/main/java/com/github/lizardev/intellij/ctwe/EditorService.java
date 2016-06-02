package com.github.lizardev.intellij.ctwe;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import static com.intellij.openapi.fileEditor.FileEditorManagerListener.FILE_EDITOR_MANAGER;

public class EditorService extends AbstractProjectComponent {

    private volatile VirtualFile editorFile;

    public static EditorService getInstance(Project project) {
        return project.getComponent(EditorService.class);
    }

    protected EditorService(Project project) {
        super(project);
        myProject.getMessageBus().connect().subscribe(FILE_EDITOR_MANAGER, new FileEditorManagerAdapter() {
            public void selectionChanged(FileEditorManagerEvent event) {
                editorFile = event.getNewFile();
            }
        });
    }

    public void projectOpened() {
        editorFile = FileEditorManagerEx.getInstanceEx(myProject).getCurrentFile();
    }

    public VirtualFile getCurrentFile() {
        return editorFile;
    }
}

