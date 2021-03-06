package ch.makery.address;

import ch.makery.address.model.Person;
import ch.makery.address.viewControllers.PersonOverviewController;
import ch.makery.address.viewControllers.RootLayoutController;
import ch.makery.address.util.PersonDataFile;
import java.io.File;
import java.io.IOException;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import ch.makery.address.jdbc.AcessDB;
import java.util.ArrayList;

public class MainApp {

    Stage primaryStage;
    private BorderPane rootLayout;
    private PersonDataFile personDataFile;
    private static MainApp instance = null; 
    
    public static MainApp getInstance(){
        if(MainApp.instance == null){
            MainApp.instance = new MainApp();
        }
       return MainApp.instance;
    }
    
       /**
     * Os dados como uma observable list de Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    

    /**
     * Construtor
     */
    private MainApp() {
        personDataFile = new PersonDataFile();
    }
    
    /**
     * Retorna os dados como uma observable list de Persons. 
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }
    
    public ArrayList<Person> getPersonsAsArrayList(){
        ArrayList<Person> persons = new ArrayList<Person>();
        
        for( Person person : personData){
            persons.add(person);
        }
        return persons;
    }
    /*
     * Inicializa o root layout e tenta carregar o último arquivo
     * de pessoa aberto.
     */
    public void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Mostra a scene (cena) contendo o root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Dá ao controller o acesso ao main app.
            RootLayoutController controller = loader.getController();


            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tenta carregar o último arquivo de pessoa aberto.
        try{
        AcessDB acess = new AcessDB();
        acess.loadPersonDataFromDB();
        } catch(Exception e ){
            
            PersonDataFile personDataFile = getPersonDataFile();
            File file = personDataFile.getPersonFilePath().getPersonFilePath();
            if (file != null) {
                personDataFile.loadPersonDataFromFile(file);
            }
        }
        
    }
    /**
     * Mostra o person overview dentro do root layout.
     */
public void showPersonOverview() {
    try {
        // Carrega a person overview.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
        AnchorPane personOverview = (AnchorPane) loader.load();

        // Define a person overview no centro do root layout.
        rootLayout.setCenter(personOverview);

        // Dá ao controlador acesso à the main app.
        PersonOverviewController controller = loader.getController();
        controller.setItemsOnTable();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
   public PersonDataFile getPersonDataFile(){
       return personDataFile;
   }
    
    /**
     * Retorna o palco principal.
     * @return
     */

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
