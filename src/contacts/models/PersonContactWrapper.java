package contacts.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Класс - обертка для списка контактов
 */
@XmlRootElement(name = "contacts")
public class PersonContactWrapper {

    @XmlElement(name = "contact")
    private List<PersonContact> personContactsList;

    public List<PersonContact> getContacts() {
        return personContactsList;
    }

    public void setContacts(List<PersonContact> list) {
        this.personContactsList = list;
    }
}
