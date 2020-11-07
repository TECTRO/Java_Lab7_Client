package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Model {
    //region Accessors
    private BiConsumer<String, Object> NotifyProvider;

    public void setNotifyProvider(BiConsumer<String, Object> notifyProvider) {
        NotifyProvider = notifyProvider;
    }
    //endregion

    private Socket Client;

    private ArrayList<Integer> MainList;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    //region Constructor
    public Model(BiConsumer<String, Object> notifyProvider) {
        setNotifyProvider(notifyProvider);
    }
    //endregion

    public ArrayList<Integer> LoadTask1() {
        try {
            oos.writeInt(1);
            oos.flush();
            return (ArrayList<Integer>) ois.readObject();
        } catch (Exception ignored) {
        }
        return null;
    }

    public ArrayList<Integer> LoadTask2() {
        try {
            oos.writeInt(2);
            oos.flush();
            return (ArrayList<Integer>) ois.readObject();
        } catch (Exception ignored) {
        }
        return null;
    }


    private Stack<String> strings = new Stack<>();

    public void PushToStack(String str) {
        strings.push(str);
    }

    public void ClearStack() {
        strings = new Stack<>();
    }

    public Stack<String> LoadTask3() {
        try {
            oos.writeInt(3);
            oos.writeObject(strings);
            oos.flush();

            strings = (Stack<String>) ois.readObject();
            return strings;
        } catch (Exception ignored) {
        }
        return null;
    }

    //region Helpers
    private void Initialise() {
        if (Client != null) {
            try {
                oos = new ObjectOutputStream(Client.getOutputStream());
                ois = new ObjectInputStream(Client.getInputStream());
                MainList = (ArrayList<Integer>) ois.readObject();
                String result = MainList.stream().map(Object::toString).collect(Collectors.joining("; "));
                Notify("MainList", result);
            } catch
            (Exception e) {
                Notify("Err", e.getLocalizedMessage());
            }
        }
    }

    public boolean Connect(String adr, int port) {
        try {
            Client = new Socket(adr, port);
            Initialise();
            return true;
        } catch
        (Exception e) {
            Notify("Err", e.getLocalizedMessage());
        }
        return false;
    }

    private boolean Notify(String Key, Object value) {
        if (NotifyProvider != null) {
            NotifyProvider.accept(Key, value);
            return true;
        }
        return false;
    }
    //endregion

}
