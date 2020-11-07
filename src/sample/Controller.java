package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Controller {
    public Label MainListHolder;
    public TextArea OtherMassHolder;
    public TextArea OtherSortedMassHolder;
    public Button ConnectBtn;

    private final Model model = new Model(this::Notify);
    public TabPane MainContainer;
    public TextArea StackItemHolder;
    public ListView StackHolder;

    public void GetOtherMass(ActionEvent actionEvent) {

        ArrayList<Integer> data = model.LoadTask1();
        if(data!=null)
        OtherMassHolder.setText(data.stream().map(Objects::toString).collect(Collectors.joining(", ")));
    }

    public void GetOtherSortedMass(ActionEvent actionEvent) {
        List<Integer> data = model.LoadTask2();
        if(data!=null)
        OtherSortedMassHolder.setText(data.stream().map(Objects::toString).collect(Collectors.joining(", ")));
    }

    private void Notify(String Key, Object value) {
        switch (Key) {
            case "Err":
            case "MainList":
                MainListHolder.setText((String) value);
                break;
        }
    }

    public void ConnectBtn(ActionEvent actionEvent) {
        if (model.Connect("localhost", 25565)) {
            ConnectBtn.disableProperty().setValue(true);
            MainContainer.disableProperty().setValue(false);
        }
    }

    public void PushStackBtn(ActionEvent actionEvent) {
        StackHolder.getItems().add(StackItemHolder.getText());
        model.PushToStack(StackItemHolder.getText());
        StackItemHolder.clear();
    }

    public void SendStack(ActionEvent actionEvent) {
        Stack<String> d  = model.LoadTask3();
        StackHolder.getItems().clear();
        StackHolder.getItems().addAll(d);
        StackItemHolder.clear();
    }

    public void ClearStack(ActionEvent actionEvent) {
        model.ClearStack();
        StackHolder.getItems().clear();
        StackItemHolder.clear();
    }
}
