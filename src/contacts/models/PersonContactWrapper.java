package contacts.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Класс - обертка для содержимого листа контактов
 */
@XmlRootElement(name = "contacts")
public class PersonContactWrapper {

    private List<PersonContact> personContactsList;

    @XmlElement(name = "contact")
    public List<PersonContact> getContacts() {
        return personContactsList;
    }

    public void setContacts(List<PersonContact> list) {
        this.personContactsList = list;
    }
}
