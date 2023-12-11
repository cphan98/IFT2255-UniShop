package serializationUtil;

import BackEndUtility.DataBase;

import java.io.*;

public class SerializationUtil {
    // UTILITIES

    // database -------------------------------------------------------------------------------------------------------

    public static void saveDataBase(DataBase dataBase, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dataBase);
        }
    }

    public static DataBase loadDataBase(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (DataBase) ois.readObject();
        }
    }
}
