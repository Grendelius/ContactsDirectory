package contacts.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonContactsGroup {

    @XmlElement(name = "id")
    private ObservableList<Long> personContactList = FXCollections.observableArrayList();

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

    public ObservableList<Long> getPersonContactsList() {
        return personContactList;
    }

    @Override
    public String toString() {
        return getGroupLabel();
    }
}
