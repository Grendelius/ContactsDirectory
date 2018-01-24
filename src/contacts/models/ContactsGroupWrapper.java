package contacts.models;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Класс - обертка для списка групп
 */
@XmlRootElement(namespace = "groups")
public class ContactsGroupWrapper implements Serializable {
    private List<PersonContactsGroup> groupsList;

    @XmlElement(name = "group")
    public List<PersonContactsGroup> getGroups() {
        return groupsList;
    }

    public void setGroups(List<PersonContactsGroup> list) {
        this.groupsList = list;
    }
}
