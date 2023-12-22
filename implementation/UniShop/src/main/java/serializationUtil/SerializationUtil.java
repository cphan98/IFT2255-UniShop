package serializationUtil;

import BackEndUtility.DataBase;

import java.io.*;
/**
 * The SerializationUtil class provides utility methods for serializing and deserializing objects,
 * specifically instances of the DataBase class.
 *
 */
public class SerializationUtil {
    // UTILITIES

    // database -------------------------------------------------------------------------------------------------------
    /**
     * Saves a DataBase object to a file using serialization.
     *
     * @param dataBase The DataBase object to be saved.
     * @param filename The name of the file where the object will be saved.
     * @throws IOException If an I/O error occurs while saving the object.
     */
    public static void saveDataBase(DataBase dataBase, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dataBase);
        }
    }
    /**
     * Loads a DataBase object from a file using deserialization.
     *
     * @param filename The name of the file from which to load the object.
     * @return The loaded DataBase object.
     * @throws IOException            If an I/O error occurs while loading the object.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static DataBase loadDataBase(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (DataBase) ois.readObject();
        }
    }
}
