package contacts.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class PersonContactsGroup {
    private StringProperty groupLabel = new SimpleStringProperty();
    private List<PersonContact> personContactList = new ArrayList<>();

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

    public StringProperty groupLabelProperty() {
        return groupLabel;
    }

    public List<PersonContact> getPersonContactsList() {
        return personContactList;
    }

    public void setPersonContactList(List<PersonContact> personContactList) {
        this.personContactList = personContactList;
    }

    @Override
    public String toString() {
        return getGroupLabel();
    }
}
