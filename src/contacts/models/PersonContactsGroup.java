package contacts.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonContactsGroup {
    private StringProperty groupLabel = new SimpleStringProperty();
    private ObservableList<PersonContact> personContactList = FXCollections.observableArrayList();

    public PersonContactsGroup() {
    }

    public PersonContactsGroup(String name) {
        groupLabel.setValue(name);
    }

    public String getGroupLabel() {
        return groupLabel.get();
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel.setValue(groupLabel);
    }

    public ObservableList<PersonContact> getPersonContactsList() {
        return personContactList;
    }

    @Override
    public String toString() {
        return getGroupLabel();
    }
}
