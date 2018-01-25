package contacts.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "group")
public class PersonContactsGroup {
    @XmlElement(name = "contact")
    private ObservableList<PersonContact> personContactList = FXCollections.observableArrayList();

    private StringProperty groupLabel = new SimpleStringProperty();

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
