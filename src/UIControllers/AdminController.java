package UIControllers;

import Actors.Admin;
import Actors.Faculty;
import Actors.Student;
import Supplementary.*;
import Utils.SendEmail;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import sun.awt.image.ImageWatched;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminController {

    @FXML AnchorPane tableanchor;
    public static Stage stage2;
    @FXML
    public void handleMailLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("getpassword.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.show();

    }

    @FXML
    public void handleMailLogout(ActionEvent event) {
        CurrentLoggenInUser.setNull();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Logout Successful");
        alert.show();
    }

    @FXML private TextField password;

    @FXML
    public void handleStorePassword(ActionEvent event) {
        String password2 = password.getText();
        CurrentLoggenInUser.setadminMailPassword(password2);
        AdminController.stage2.close();
    }

    @FXML
    public void handleCourseReq(ActionEvent event) throws IOException, ClassNotFoundException {
        CourseRequests c = new CourseRequests();
        c.setCourseRequests(c.deserialize());
        tableanchor.getChildren().clear();
        TableView tb = new TableView<>(generateDataInMap4(c));
        tb.prefWidthProperty().bind(tableanchor.widthProperty());
        tb.prefHeightProperty().bind(tableanchor.heightProperty());
        TableColumn<Map, String> col1 = new TableColumn<>("Proposed By");
        TableColumn<Map, String> col2 = new TableColumn<>("Course Name");
        TableColumn<Map, String> col3 = new TableColumn<>("Credits");
        TableColumn<Map, String> col4 = new TableColumn<>("Pre Requisites");
        TableColumn<Map, String> col5 = new TableColumn<>("Post Conditions");
        col1.setCellValueFactory(new MapValueFactory("Proposed By"));
        col2.setCellValueFactory(new MapValueFactory("Course Name"));
        col3.setCellValueFactory(new MapValueFactory("Credits"));
        col4.setCellValueFactory(new MapValueFactory("Pre Requisites"));
        col5.setCellValueFactory(new MapValueFactory("Post Conditions"));
        tb.getSelectionModel().setCellSelectionEnabled(true);
        tb.getColumns().setAll(col1, col2, col3, col4, col5);
        Callback<TableColumn<Map, String>,TableCell<Map, String>>
                cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell<Map, String> call(TableColumn<Map, String> p) {
                return new TextFieldTableCell<>(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        };
        col1.setCellFactory(cellFactoryForMap);
        col2.setCellFactory(cellFactoryForMap);
        col3.setCellFactory(cellFactoryForMap);
        col4.setCellFactory(cellFactoryForMap);
        col5.setCellFactory(cellFactoryForMap);
        tableanchor.getChildren().add(tb);
        ContextMenu cm = new ContextMenu();
        MenuItem item1 = new MenuItem("Accept");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, String> hm = new HashMap<>((HashMap)tb.getSelectionModel().getSelectedItem());
                tb.getItems().remove(hm);
                File file = new File("./src/DataFiles/coursereqs.txt");
                CourseRequests c = new CourseRequests();
                if (file.exists()) {
                    try {
                        c.setCourseRequests(c.deserialize());
                        System.out.println(hm);
                        c.removeCourse(hm);
                    } catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                } else {
                    c.newCourse();
                }
                try {
                    c.serialize(c.getcourserequests());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        MenuItem item2 = new MenuItem("Reject");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, String> hm = new HashMap<>((HashMap)tb.getSelectionModel().getSelectedItem());
                tb.getItems().remove(hm);
                File file = new File("./src/DataFiles/coursereqs.txt");
                CourseRequests c = new CourseRequests();
                if (file.exists()) {
                    try {
                        c.setCourseRequests(c.deserialize());
                        System.out.println(hm);
                        c.removeCourse(hm);
                    } catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                } else {
                    c.newCourse();
                }
                try {
                    c.serialize(c.getcourserequests());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        cm.getItems().add(item1);
        cm.getItems().add(item2);
        tb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY && tb.getSelectionModel().getSelectedItem() != null) {
                    cm.show(tb, event.getScreenX(), event.getScreenY());
                } else {
                    cm.hide();
                }
            }
        });
    }

    public ObservableList<Map> generateDataInMap4(CourseRequests c) {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 0; i < c.getcourserequests().size() ; ++i) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Proposed By", (String)c.getcourserequests().get(i).get("Proposed By"));
            dataRow.put("Course Name", (String)c.getcourserequests().get(i).get("Course Name"));
            dataRow.put("Credits", (String)c.getcourserequests().get(i).get("Credits"));
            dataRow.put("Pre Requisites", (String)c.getcourserequests().get(i).get("Pre Requisites"));
            dataRow.put("Post Conditions", (String)c.getcourserequests().get(i).get("Post Conditions"));
            allData.add(dataRow);
        }
        return allData;
    }

    @FXML
    public void handleAccReq(ActionEvent event) throws IOException, ClassNotFoundException {
        AccountRequests a = new AccountRequests();
        a.setAccountRequests(a.deserialize());
        tableanchor.getChildren().clear();
        TableView tb = new TableView<>(generateDataInMap3(a));
        tb.prefWidthProperty().bind(tableanchor.widthProperty());
        tb.prefHeightProperty().bind(tableanchor.heightProperty());
        TableColumn<Map, String> col1 = new TableColumn<>("User Type");
        TableColumn<Map, String> col2 = new TableColumn<>("First Name");
        TableColumn<Map, String> col3 = new TableColumn<>("Last Name");
        TableColumn<Map, String> col4 = new TableColumn<>("Phone Number");
        TableColumn<Map, String> col5 = new TableColumn<>("Email ID");
        TableColumn<Map, String> col6 = new TableColumn<>("Password");
        TableColumn<Map, String> col7 = new TableColumn<>("Date of Birth");
        TableColumn<Map, String> col8 = new TableColumn<>("Roll Number");
        TableColumn<Map, String> col9 = new TableColumn<>("Branch");

        col1.setCellValueFactory(new MapValueFactory("User Type"));
        col2.setCellValueFactory(new MapValueFactory("First Name"));
        col3.setCellValueFactory(new MapValueFactory("Last Name"));
        col4.setCellValueFactory(new MapValueFactory("Phone Number"));
        col5.setCellValueFactory(new MapValueFactory("Email ID"));
        col6.setCellValueFactory(new MapValueFactory<>("Password"));
        col7.setCellValueFactory(new MapValueFactory<>("Date of Birth"));
        col8.setCellValueFactory(new MapValueFactory<>("Roll Number"));
        col9.setCellValueFactory(new MapValueFactory<>("Branch"));

        //tb.setEditable(true);
        tb.getSelectionModel().setCellSelectionEnabled(true);
        tb.getColumns().setAll(col1, col2, col3, col4, col5, col6, col7, col8, col9);
        Callback<TableColumn<Map, String>,TableCell<Map, String>>
                cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell<Map, String> call(TableColumn<Map, String> p) {
                return new TextFieldTableCell<>(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        };
        col1.setCellFactory(cellFactoryForMap);
        col2.setCellFactory(cellFactoryForMap);
        col3.setCellFactory(cellFactoryForMap);
        col4.setCellFactory(cellFactoryForMap);
        col5.setCellFactory(cellFactoryForMap);
        col6.setCellFactory(cellFactoryForMap);
        col7.setCellFactory(cellFactoryForMap);
        col8.setCellFactory(cellFactoryForMap);
        col9.setCellFactory(cellFactoryForMap);
        tableanchor.getChildren().add(tb);

        ContextMenu cm = new ContextMenu();
        MenuItem item1 = new MenuItem("Accept");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, String> hm = new HashMap<>((HashMap)tb.getSelectionModel().getSelectedItem());
                //Add to db.txt here
                tb.getItems().remove(hm);
                File file = new File("./src/DataFiles/accountreqs.txt");
                AccountRequests a = new AccountRequests();
                if (file.exists()) {
                    try {
                        a.setAccountRequests(a.deserialize());
                        System.out.println(hm);
                        if (CurrentLoggenInUser.getadminMailPassword() != null) {
                            try {
                                SendEmail.send(CurrentLoggenInUser.adminMailID, CurrentLoggenInUser.getadminMailPassword(), hm.get("Email ID"), "Account Request Approved", "Hello " + hm.get("First Name") + " " + hm.get("Last Name") + "\n\n" + "Your Account Request has been approved. You may Now Log In");
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Wrong Email Password entered. Please verify. No approval email will be sent this time.");
                                alert.show();
                            }
                        }
                        a.removeAccount(hm);
                        if (hm.get("User Type").equals("Student")) {
                            Student student = new Student(hm.get("First Name"), hm.get("Last Name"), hm.get("Phone Number"), hm.get("Email ID"), hm.get("Password"), hm.get("User Type"), hm.get("Date of Birth"), hm.get("Roll Number"), hm.get("Branch"));
                            RegisterController.serializeData(student);
                        } else if (hm.get("User Type").equals("Faculty")) {
                            Faculty faculty = new Faculty(hm.get("First Name"), hm.get("Last Name"), hm.get("Phone Number"), hm.get("Email ID"), hm.get("Password"), hm.get("User Type"), hm.get("Date of Birth"));
                            RegisterController.serializeData(faculty);
                        } else {
                            Admin admin = new Admin(hm.get("First Name"), hm.get("Last Name"), hm.get("Phone Number"), hm.get("Email ID"), hm.get("Password"), hm.get("User Type"), hm.get("Date of Birth"));
                            RegisterController.serializeData(admin);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    a.newAccount();
                }
                try {
                    a.serialize(a.getAccountRequests());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        MenuItem item2 = new MenuItem("Reject");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, String> hm = new HashMap<String, String>((HashMap)tb.getSelectionModel().getSelectedItem());
                tb.getItems().remove(hm);
                File file = new File("./src/DataFiles/accountreqs.txt");
                AccountRequests a = new AccountRequests();
                if (file.exists()) {
                    try {
                        a.setAccountRequests(a.deserialize());
                        a.removeAccount(hm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    a.newAccount();
                }
                try {
                    a.serialize(a.getAccountRequests());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        cm.getItems().add(item1);
        cm.getItems().add(item2);
        tb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY && tb.getSelectionModel().getSelectedItem() != null) {
                    cm.show(tb, event.getScreenX(), event.getScreenY());
                } else {
                    cm.hide();
                }
            }
        });

    }

    public ObservableList<Map> generateDataInMap3(AccountRequests a) {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 0; i < a.getAccountRequests().size() ; ++i) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("User Type", (String)a.getAccountRequests().get(i).get("User Type"));
            dataRow.put("First Name", (String)a.getAccountRequests().get(i).get("First Name"));
            dataRow.put("Last Name", (String)a.getAccountRequests().get(i).get("Last Name"));
            dataRow.put("Phone Number", (String)a.getAccountRequests().get(i).get("Phone Number"));
            dataRow.put("Email ID", (String)a.getAccountRequests().get(i).get("Email ID"));
            dataRow.put("Password", (String)a.getAccountRequests().get(i).get("Password"));
            dataRow.put("Date of Birth", (String)a.getAccountRequests().get(i).get("Date of Birth"));
            dataRow.put("Roll Number", (String)a.getAccountRequests().get(i).get("Roll Number"));
            dataRow.put("Branch", (String)a.getAccountRequests().get(i).get("Branch"));
            allData.add(dataRow);
        }
        return allData;
    }

    @FXML
    public void handleBookReq(ActionEvent event) throws ClassNotFoundException, IOException{
        BookingRequests b = new BookingRequests();
        b.setBookinRequests(b.deserialize());
        System.out.println(b.getBookingrequests());
        tableanchor.getChildren().clear();
        TableView tb = new TableView<>(generateDataInMap2(b));
        tb.prefWidthProperty().bind(tableanchor.widthProperty());
        tb.prefHeightProperty().bind(tableanchor.heightProperty());
        TableColumn<Map, String> col1 = new TableColumn<>("Day");
        TableColumn<Map, String> col2 = new TableColumn<>("Room Number");
        TableColumn<Map, String> col3 = new TableColumn<>("Purpose");
        TableColumn<Map, String> col4 = new TableColumn<>("Start Time");
        TableColumn<Map, String> col5 = new TableColumn<>("End Time");
        TableColumn<Map, String> col6 = new TableColumn<>("Requested by");
        col1.setCellValueFactory(new MapValueFactory("Day"));
        col2.setCellValueFactory(new MapValueFactory("Room Number"));
        col3.setCellValueFactory(new MapValueFactory("Purpose"));
        col4.setCellValueFactory(new MapValueFactory("Start Time"));
        col5.setCellValueFactory(new MapValueFactory("End Time"));
        col6.setCellValueFactory(new MapValueFactory<>("Requested by"));
        //tb.setEditable(true);
        tb.getSelectionModel().setCellSelectionEnabled(true);
        tb.getColumns().setAll(col1, col2, col3, col4, col5, col6);
        Callback<TableColumn<Map, String>,TableCell<Map, String>>
                cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell<Map, String> call(TableColumn<Map, String> p) {
                return new TextFieldTableCell<>(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        };
        col1.setCellFactory(cellFactoryForMap);
        col2.setCellFactory(cellFactoryForMap);
        col3.setCellFactory(cellFactoryForMap);
        col4.setCellFactory(cellFactoryForMap);
        col5.setCellFactory(cellFactoryForMap);
        col6.setCellFactory(cellFactoryForMap);
        tableanchor.getChildren().add(tb);
        ContextMenu cm = new ContextMenu();
        MenuItem item1 = new MenuItem("Accept");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, String> hm = new HashMap<String, String>((HashMap)tb.getSelectionModel().getSelectedItem());
                Booking.bookings.add(hm);
                try {
                    Booking.serialize();
                } catch (IOException e){
                    e.printStackTrace();
                }
                if (CurrentLoggenInUser.getadminMailPassword() != null) {
                    try {
                        SendEmail.send(CurrentLoggenInUser.adminMailID, CurrentLoggenInUser.getadminMailPassword(), hm.get("Email ID"), "Booking Request Approved", "Hello " + hm.get("First Name") + " " + hm.get("Last Name") + "\n\n" + "Your Booking Request for " + hm.get("Purpose") + " has been approved.");
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Email Password entered. Please verify. No approval email will be sent this time.");
                        alert.show();
                    }
                }
                tb.getItems().remove(hm);
                File file = new File("./src/DataFiles/bookingreqs.txt");
                BookingRequests b = new BookingRequests();
                if (file.exists()) {
                    try {
                        b.setBookinRequests(b.deserialize());
                        System.out.println(hm);
                        b.removeBooking(hm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    b.newBooking();
                }
                try {
                    b.serialize(b.getBookingrequests());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        MenuItem item2 = new MenuItem("Reject");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, String> hm = new HashMap<String, String>((HashMap)tb.getSelectionModel().getSelectedItem());
                tb.getItems().remove(hm);
                File file = new File("./src/DataFiles/bookingreqs.txt");
                BookingRequests b = new BookingRequests();
                if (file.exists()) {
                    try {
                        b.setBookinRequests(b.deserialize());
                        b.removeBooking(hm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    b.newBooking();
                }
                try {
                    b.serialize(b.getBookingrequests());
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        cm.getItems().add(item1);
        cm.getItems().add(item2);
        tb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY && tb.getSelectionModel().getSelectedItem() != null) {
                    cm.show(tb, event.getScreenX(), event.getScreenY());
                } else {
                    cm.hide();
                }
            }
        });

    }

    public ObservableList<Map> generateDataInMap2(BookingRequests b) {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 0; i < b.getBookingrequests().size() ; ++i) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Day", (String)b.getBookingrequests().get(i).get("Day"));
            dataRow.put("Room Number", (String)b.getBookingrequests().get(i).get("Room Number"));
            dataRow.put("Purpose", (String)b.getBookingrequests().get(i).get("Purpose"));
            dataRow.put("Start Time", (String)b.getBookingrequests().get(i).get("Start Time"));
            dataRow.put("End Time", (String)b.getBookingrequests().get(i).get("End Time"));
            dataRow.put("Requested by", (String)b.getBookingrequests().get(i).get("Requested by"));
            allData.add(dataRow);
        }
        return allData;
    }

    @FXML
    public void handleBookRoom(ActionEvent event) throws IOException {
        Parent newscene = FXMLLoader.load(getClass().getResource("requestbook.fxml"));
        requestbookController.setCallingClass("admin.fxml");
        Main.primaryStage.setScene(new Scene(newscene, 1200, 800));
        Main.primaryStage.show();
    }

    @FXML
    public void handleCurrentBookings(ActionEvent event) {
        tableanchor.getChildren().clear();
        TableView tb = new TableView<>(generateDataInMap());
        tb.prefWidthProperty().bind(tableanchor.widthProperty());
        tb.prefHeightProperty().bind(tableanchor.heightProperty());
        TableColumn<Map, String> col1 = new TableColumn<>("Day");
        TableColumn<Map, String> col2 = new TableColumn<>("Room Number");
        TableColumn<Map, String> col3 = new TableColumn<>("Purpose");
        TableColumn<Map, String> col4 = new TableColumn<>("Start Time");
        TableColumn<Map, String> col5 = new TableColumn<>("End Time");
        col1.setCellValueFactory(new MapValueFactory("Day"));
        col2.setCellValueFactory(new MapValueFactory("Room Number"));
        col3.setCellValueFactory(new MapValueFactory("Purpose"));
        col4.setCellValueFactory(new MapValueFactory("Start Time"));
        col5.setCellValueFactory(new MapValueFactory("End Time"));
        tb.setEditable(true);
        tb.getSelectionModel().setCellSelectionEnabled(true);
        tb.getColumns().setAll(col1, col2, col3, col4, col5);
        Callback<TableColumn<Map, String>,TableCell<Map, String>>
                cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
            @Override
            public TableCell<Map, String> call(TableColumn<Map, String> p) {
                return new TextFieldTableCell<>(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });
            }
        };
        col1.setCellFactory(cellFactoryForMap);
        col2.setCellFactory(cellFactoryForMap);
        col3.setCellFactory(cellFactoryForMap);
        col4.setCellFactory(cellFactoryForMap);
        col5.setCellFactory(cellFactoryForMap);
        tableanchor.getChildren().add(tb);
    }

    private ObservableList<Map> generateDataInMap() {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        for (int i = 0; i < Booking.bookings.size() ; ++i) {
            Map<String, String> dataRow = new HashMap<>();

            dataRow.put("Day", (String)Booking.bookings.get(i).get("Day"));
            dataRow.put("Room Number", (String)Booking.bookings.get(i).get("Room Number"));
            dataRow.put("Purpose", (String)Booking.bookings.get(i).get("Purpose"));
            dataRow.put("Start Time", (String)Booking.bookings.get(i).get("Start Time"));
            dataRow.put("End Time", (String)Booking.bookings.get(i).get("End Time"));
            allData.add(dataRow);
        }
        return allData;
    }

    @FXML
    private void handleLogout() throws IOException{
        Parent newscene = FXMLLoader.load(getClass().getResource("entryPage.fxml"));
        Main.primaryStage.setScene(new Scene(newscene, 600, 400));
        Main.primaryStage.show();
    }

	public void showAboutPage(ActionEvent actionEvent) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.show();
	}
}
