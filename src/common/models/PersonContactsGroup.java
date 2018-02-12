package common.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс - модель группы контактов
 */
@XmlRootElement(name = "group")
public class PersonContactsGroup {

    @XmlElement(name = "contactId")
    private List<Long> personContactList = new ArrayList<>();
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

    public List<Long> getPersonContactsList() {
        return personContactList;
    }

    @Override
    public String toString() {
        return getGroupLabel();
    }
}
