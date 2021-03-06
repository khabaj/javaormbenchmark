package com.khabaj.ormbenchmark.launcher.benchmark.datasources;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.khabaj.ormbenchmark.launcher.benchmark.JDBCDriver;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DataSourceFormCtrl implements Initializable {

    @FXML
    DataSourcesPanelCtrl dataSourcesPanelCtrl;
    @FXML
    JFXTextField connectionName;
    @FXML
    JFXTextField connectionURL;
    @FXML
    JFXTextField username;
    @FXML
    JFXPasswordField password;
    @FXML
    JFXComboBox<JDBCDriver> jdbcDriverComboBox;
    @FXML
    Label connectionStatusLabel;
    @FXML
    Pane dataSourceForm;

    private DataSource dataSource;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataSource = new DataSource();

        jdbcDriverComboBox.getItems().addAll(JDBCDriver.values());
        jdbcDriverComboBox.getSelectionModel().selectFirst();
        prepareValidators();
    }

    private void prepareValidators() {
        prepareValidator(connectionName);
        prepareValidator(connectionURL);
    }

    private void prepareValidator(Control control) {

        RequiredFieldValidator validator = new RequiredFieldValidator();

        validator.setMessage("This field is required");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING)
                .size("1em").styleClass("error").build());

        if (control instanceof JFXTextField) {
            JFXTextField textField = ((JFXTextField) control);
            textField.getValidators().add(validator);
            textField.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal)
                    textField.validate();
            });
        }
    }

    public void setParentForm(DataSourcesPanelCtrl parent) {
        dataSourcesPanelCtrl = parent;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;

        connectionName.setText(dataSource.getConnectionName());
        connectionURL.setText(dataSource.getConnectionURL());
        username.setText(dataSource.getUsername());
        password.setText(dataSource.getPassword());

        JDBCDriver jdbcDriver = dataSource.getJdbcDriver();
        if (jdbcDriver != null) {
            jdbcDriverComboBox.getSelectionModel().select(jdbcDriver);
        }
    }

    @FXML
    private void save(ActionEvent event) {
        boolean hasErrors = !connectionName.validate() | !connectionURL.validate();

        if (!hasErrors) {
            dataSource.setConnectionName(connectionName.getText());
            dataSource.setConnectionURL(connectionURL.getText());
            dataSource.setUsername(username.getText());
            dataSource.setPassword(password.getText());
            dataSource.setJdbcDriver(jdbcDriverComboBox.getSelectionModel().getSelectedItem());
            dataSourcesPanelCtrl.addDataSource(dataSource);
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void testConnection() {

        DataSourceService dataSourceService = DataSourceService.getInstance();
        JDBCDriver jdbcDriver = jdbcDriverComboBox.getSelectionModel().getSelectedItem();
        boolean connectionSuccess = false;

        try {
            connectionSuccess = dataSourceService.testConnection(
                    new DataSource(connectionName.getText(), connectionURL.getText(),
                            username.getText(), password.getText(), jdbcDriver));
        } catch (Exception e) {
            JFXSnackbar bar = new JFXSnackbar(dataSourceForm);
            bar.setPrefWidth(250);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(e.getMessage()));
        } finally {
            setConnectionStatusLabel(connectionSuccess);
        }
    }

    private void setConnectionStatusLabel(boolean connectionSuccess) {
        if (connectionSuccess) {
            connectionStatusLabel.setText("Success");
            connectionStatusLabel.setStyle("-fx-text-fill: green");
        } else {
            connectionStatusLabel.setText("Failed");
            connectionStatusLabel.setStyle("-fx-text-fill: darkred");
        }
    }
}
