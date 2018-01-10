package control.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonContact {
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty phoneNumber = new SimpleStringProperty();
    private StringProperty personEmail = new SimpleStringProperty();

    public PersonContact() {
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

    public StringProperty fullNameProperty() {
        return new SimpleStringProperty(getLastName().concat("\n").concat(getFirstName()));
    }

}
