package contacts.models;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class MarshallerCreater {

    /**
     * Создает маршаллинг для одного из двух классов моделей
     *
     * @param c - класс контакта или класс группы контактов
     * @return marshaller object
     * @throws Exception
     */
    public static Marshaller createMarshall(Class c) throws Exception {
        JAXBContext context = null;
        if (c.equals(PersonContactWrapper.class)) {
            context = JAXBContext.newInstance(PersonContactWrapper.class);
        }
        if (c.equals(ContactsGroupWrapper.class)) {
            context = JAXBContext.newInstance(ContactsGroupWrapper.class);
        }
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return m;
    }

    /**
     * Создает демаршаллинг для одного из двух классов моделей
     *
     * @param c - класс контакта или класс группы контактов
     * @return unmarshaller object
     * @throws Exception
     */
    public static Unmarshaller createUnmarshall(Class c) throws Exception {
        JAXBContext context = null;
        if (c.equals(PersonContactWrapper.class)) {
            context = JAXBContext.newInstance(PersonContactWrapper.class);
        }
        if (c.equals(ContactsGroupWrapper.class)) {
            context = JAXBContext.newInstance(ContactsGroupWrapper.class);
        }
        Unmarshaller um = context.createUnmarshaller();

        return um;
    }
}
