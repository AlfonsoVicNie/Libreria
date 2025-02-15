package es.librafy.gestionBiblioteca.controlador;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import es.librafy.gestionBiblioteca.modelo.Libros;
import es.librafy.gestionBiblioteca.util.DatabaseConnection;
import es.librafy.gestionBiblioteca.util.SesionUsuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuentaControlador {

    private String usuarioActual;  // Variable para almacenar el usuario actual

    @FXML
    private Label lblNombre; // Para mostrar el nombre del usuario

    @FXML
    private Label lblEmail;  // Para mostrar el correo electrónico del usuario

    @FXML
    private Label lblNumTelf;
    
    @FXML
    private Label lblDni;
    
    @FXML
    private Label lblFnacimiento;
    
    @FXML
    private Label lblDireccion;
    
    @FXML
    private Label lblNomUsuario;
    
    @FXML
    private Label lblContrasena;
    
    @FXML
    private Label lblMensaje;
    
    @FXML
    private Button btn_modificar;
    
    @FXML
    private Button btn_volver;
    
    // Método para establecer el nombre del usuario y cargar los datos del mismo
    public void setUsuarioActual(String usuario) {
        this.usuarioActual = usuario;
        SesionUsuario.setUsuarioActual(usuario); // Actualizar la sesión
        cargarDatosUsuario();  // Llamamos al método para cargar los datos del usuario
    }

    // Método para cargar los datos del usuario desde la base de datos
    private void cargarDatosUsuario() {
        // Verificar que el usuario no sea nulo o vacío
        if (usuarioActual != null && !usuarioActual.isEmpty()) {
            String query = "SELECT * FROM usuarios WHERE nomUsuario = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, usuarioActual);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Cargar los datos del usuario desde la base de datos
                    String nombre = resultSet.getString("nombre");
                    String email = resultSet.getString("email");
                    String num_telefono = resultSet.getString("num_telefono");
                    String dni = resultSet.getString("dni");
                    String fecha_nacimiento = resultSet.getString("fecha_nacimiento");
                    String direccion = resultSet.getString("direccion");
                    String nomUsuario = resultSet.getString("nomUsuario");
                    String contrasena = resultSet.getString("contrasena");

                    // Mostrar los datos en los Labels
                    lblNombre.setText(nombre);
                    lblEmail.setText(email);
                    lblNumTelf.setText(num_telefono);
                    lblDni.setText(dni);
                    lblFnacimiento.setText(fecha_nacimiento);
                    lblDireccion.setText(direccion);
                    lblNomUsuario.setText(nomUsuario);
                    lblContrasena.setText(contrasena);
                    lblMensaje.setText(nombre);
                    
                    
                    // Y así para los demás labels
                } else {
                    lblNombre.setText("Usuario no encontrado");
                    lblEmail.setText("Usuario no encontrado");
                    lblNumTelf.setText("Usuario no encontrado");
                    lblDni.setText("Usuario no encontrado");
                    lblFnacimiento.setText("Usuario no encontrado");
                    lblDireccion.setText("Usuario no encontrado");
                    lblNomUsuario.setText("Usuario no encontrado");
                    lblContrasena.setText("Usuario no encontrado");
                    lblMensaje.setText("Usuario no encontrado");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lblNombre.setText("Error al cargar los datos.");
                lblEmail.setText("Error al cargar los datos.");
                lblNumTelf.setText("Error al cargar los datos.");
                lblDni.setText("Error al cargar los datos.");
                lblFnacimiento.setText("Error al cargar los datos.");
                lblDireccion.setText("Error al cargar los datos.");
                lblNomUsuario.setText("Error al cargar los datos.");
                lblContrasena.setText("Error al cargar los datos.");
                lblMensaje.setText("Error al cargar los datos.");
            }
        }
    }

    // Método para abrir la ventana de modificar cuenta
    @FXML
    private void abrirVentanaModificarCuenta() {
        try {
            // Cargar la ventana de modificación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/librafy/gestionBiblioteca/vista/ModificarCuenta.fxml"));
            Parent root = loader.load();
            
            // Obtener el controlador de la ventana de modificar cuenta
            ModificarCuentaControlador modificarControlador = loader.getController();
            
            // Pasar los datos actuales a la ventana de modificación
            modificarControlador.cargarDatosParaModificar(
                lblNomUsuario.getText(),
                lblEmail.getText(),
                lblNumTelf.getText(),
                lblDireccion.getText(),
                lblContrasena.getText()
            );
            
            // Crear la nueva escena
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Modificar Cuenta");
            stage.setScene(scene);
            
            // Mostrar la ventana de modificación
            stage.showAndWait();  // Usamos `showAndWait` para bloquear la interacción hasta que se cierre la ventana

            // Después de que la ventana de modificación se cierra, recargamos los datos
            cargarDatosUsuario(); // Recargamos los datos para asegurarnos que se actualicen

        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores si la ventana no se carga correctamente
        }
    }
    
    /*
    @FXML
    private void abrirHistorial(ActionEvent event) {
        try {
            // Cargar la ventana del historial
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/librafy/gestionBiblioteca/vista/Historial.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de historial
            HistorialControlador historialControlador = loader.getController();

            // Pasar el usuario actual al controlador del historial
            historialControlador.setUsuarioActual(SesionUsuario.getUsuarioActual());

            // Crear un nuevo Stage (ventana) para el historial
            Stage historialStage = new Stage();
            historialStage.initStyle(StageStyle.UNDECORATED); // Otras opciones de estilo: StageStyle.DECORATED
            historialStage.setTitle("Historial del Usuario");
            historialStage.setScene(new Scene(root));
            historialStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores si la ventana no se carga correctamente
        }
    }
*/
    
    
 // En CuentaControlador
    @FXML
    private void abrirCesta(ActionEvent event) throws IOException {
        // Cargar la ventana de la cesta (FXML)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/librafy/gestionBiblioteca/vista/Cesta.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la ventana de la cesta
        CestaControlador cestaControlador = loader.getController();

        // Obtener los libros de la cesta desde la sesión del usuario
        List<Libros> librosEnCesta = SesionUsuario.getLibrosEnCesta();

        // Pasar los libros al controlador de la ventana de la cesta
        cestaControlador.setLibrosEnCesta(librosEnCesta);

        // Crear un nuevo Stage (nueva ventana) para la cesta
        Stage cestaStage = new Stage();
        cestaStage.initStyle(StageStyle.UNDECORATED);
        cestaStage.setTitle("Cesta de Compras"); // Título de la nueva ventana
        cestaStage.setScene(new Scene(root));  // Establecer la escena para la ventana de la cesta
        cestaStage.show(); // Mostrar la nueva ventana
    }


    // Método hipotético para obtener los libros en la cesta
    private List<Libros> obtenerLibrosEnCesta() {
        // Este método debe obtener los libros en la cesta desde la base de datos o la sesión
        return new ArrayList<>(); // Retorna una lista de libros en la cesta
    }


    // Método para volver a la Interfaz Principal
    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/librafy/gestionBiblioteca/vista/InterfazPrincipal.fxml"));
            AnchorPane root = loader.load();

            // Crear una nueva ventana
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) btn_volver.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
