package contacts.models;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Класс - обертка для списка групп
 */
@XmlRootElement(name = "groups")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactsGroupWrapper {
    @XmlElement(name = "group")
    private List<PersonContactsGroup> groupsList;

    public List<PersonContactsGroup> getGroups() {
        return groupsList;
    }

    public void setGroups(List<PersonContactsGroup> list) {
        this.groupsList = list;
    }
}
