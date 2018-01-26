package contacts.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Класс - модель контакта
 */
@XmlRootElement(name = "contact")
public class PersonContact {

    @XmlElement(name = "id")
    private long contactId;
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty phoneNumber = new SimpleStringProperty();
    private StringProperty personEmail = new SimpleStringProperty();


    public PersonContact() {
        contactId = IdGenerator.getInstance().getNextId();
    }

    public PersonContact(String fname, String lname, String phone, String email) {
        this.firstName = new SimpleStringProperty(fname);
        this.lastName = new SimpleStringProperty(lname);
        this.phoneNumber = new SimpleStringProperty(phone);
        this.personEmail = new SimpleStringProperty(email);
        contactId = IdGenerator.getInstance().getNextId();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getPersonEmail() {
        return personEmail.get();
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail.set(personEmail);
    }

    public StringProperty fullNameProperty() {
        return new SimpleStringProperty(getLastName().concat("\n").concat(getFirstName()));
    }

    public Long getId() {
        return contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonContact that = (PersonContact) o;
        return Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getPersonEmail(), that.getPersonEmail()) &&
                Objects.equals(contactId, that.contactId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFirstName(), getLastName(), getPhoneNumber(), getPersonEmail(), contactId);
    }
}
