package clubbook.backend.dtos;

import clubbook.backend.model.Notebook;

/**
 * Data Transfer Object that represents the principal information of a notebook.
 * It contains the notebook ID and the name of the associated class group.
 */
public class NotebookPrincipalInfoDto {

    /**
     * The unique identifier of the notebook.
     */
    private int notebookId;

    /**
     * The name of the class group associated with the notebook.
     */
    private String classGroupName;

    /**
     * Constructs a NotebookPrincipalInfoDto with the specified notebook ID and class group name.
     *
     * @param notebookId the unique identifier of the notebook
     * @param classGroupName the name of the class group associated with the notebook
     */
    public NotebookPrincipalInfoDto(int notebookId, String classGroupName) {
        this.notebookId = notebookId;
        this.classGroupName = classGroupName;
    }

    /**
     * Constructs a NotebookPrincipalInfoDto from a Notebook object.
     *
     * @param notebook the Notebook object to extract information from
     */
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
