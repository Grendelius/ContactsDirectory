package contacts.models;

import java.io.File;

public class FileReturner {
    private File groupsFile;
    private File contactsFile;

    public FileReturner(File contactsFile, File groupsFile) {
        this.contactsFile = contactsFile;
        this.groupsFile = groupsFile;
    }

    public static FileReturner returnFiles(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(path2);

        return new FileReturner(file1, file2);
    }

    public File getContactsFile() {
        return contactsFile;
    }

    public File getGroupsFile() {
        return groupsFile;
    }


}
