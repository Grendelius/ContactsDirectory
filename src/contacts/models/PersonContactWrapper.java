package contacts.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Класс - обертка для списка контактов
 */
@XmlRootElement(name = "contacts")
@XmlAccessorType(XmlAccessType.FIELD)
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
