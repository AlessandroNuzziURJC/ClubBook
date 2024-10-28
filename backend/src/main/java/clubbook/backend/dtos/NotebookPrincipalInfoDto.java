package clubbook.backend.dtos;

import clubbook.backend.model.Notebook;

public class NotebookPrincipalInfoDto {

    private int notebookId;
    private String classGroupName;

    public NotebookPrincipalInfoDto(int notebookId, String classGroupName) {
        this.notebookId = notebookId;
        this.classGroupName = classGroupName;
    }

    public NotebookPrincipalInfoDto(Notebook notebook) {
        this.notebookId = notebook.getId();
        this.classGroupName = notebook.getClassgroup().getName();
    }

    public int getNotebookId() {
        return notebookId;
    }

    public String getClassGroupName() {
        return classGroupName;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }

    public void setClassGroupName(String classGroupName) {
        this.classGroupName = classGroupName;
    }
}
