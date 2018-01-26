package contacts.models;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Класс - фабрика для создания объектов маршаллирования
 */
public class MarshallerCreater {

    /**
     * Создает маршаллинг для одного из двух классов моделей
     *
     * @return marshaller object
     * @throws Exception
     */
    public Marshaller createMarshall(Class c) {
        JAXBContext context = null;
        try {
            if (c.equals(PersonContactWrapper.class)) {
                context = JAXBContext.newInstance(PersonContactWrapper.class);
            }
            if (c.equals(ContactsGroupWrapper.class)) {
                context = JAXBContext.newInstance(ContactsGroupWrapper.class);
            }

            assert context != null;
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            return m;

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }

    }

    /**
     * Создает демаршаллинг для одного из двух классов моделей
     *
     * @return unmarshaller object
     * @throws Exception
     */
    public Unmarshaller createUnmarshall(Class c) {
        JAXBContext context = null;
        try {
            if (c.equals(PersonContactWrapper.class)) {
                context = JAXBContext.newInstance(PersonContactWrapper.class);
            }
            if (c.equals(ContactsGroupWrapper.class)) {
                context = JAXBContext.newInstance(ContactsGroupWrapper.class);
            }

            assert context != null;

            return context.createUnmarshaller();

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

}
