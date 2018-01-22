package contacts.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashSet;
import java.util.Set;

public class PersonContact {
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty fullName;
    private StringProperty phoneNumber = new SimpleStringProperty();
    private StringProperty personEmail = new SimpleStringProperty();
    private Set<PersonContactsGroup> group = new HashSet<>();

    public PersonContact() {
    }

    public PersonContact(String fname, String lname, String phone, String email) {
        this.firstName = new SimpleStringProperty(fname);
        this.lastName = new SimpleStringProperty(lname);
        this.phoneNumber = new SimpleStringProperty(phone);
        this.personEmail = new SimpleStringProperty(email);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
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

    public StringProperty personEmailProperty() {
        return personEmail;
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName = new SimpleStringProperty(getFirstName().concat("\n").concat(getLastName()));
    }

    public Set<PersonContactsGroup> getGroup() {
        return group;
    }

    public void setGroup(Set<PersonContactsGroup> group) {
        this.group = group;
    }


}
