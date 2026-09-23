package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.vyorg.clinica.kinal0.model.Rol;
import main.java.com.vyorg.clinica.kinal0.model.Usuario;
import main.java.com.vyorg.clinica.kinal0.service.UsuarioService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;
import main.java.com.vyorg.clinica.kinal0.util.Sesion;

public class UsuarioController implements Initializable {

    private final UsuarioService usuarioService;
    private final SceneManager sceneManager;

    private Usuario usuarioSeleccionado;

    @FXML private TextField campoBuscar;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Boolean> colActivo;

    @FXML private TextField campoNombreCompleto;
    @FXML private TextField campoCorreo;
    @FXML private TextField campoNombreUsuario;
    @FXML private ComboBox<Rol> comboRol;
    @FXML private PasswordField campoPassword;
    @FXML private CheckBox checkActivo;

    public UsuarioController(UsuarioService usuarioService, SceneManager sceneManager) {
        this.usuarioService = usuarioService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("nombreRol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        comboRol.setItems(FXCollections.observableArrayList(usuarioService.listarRoles()));
        checkActivo.setSelected(true);

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarEnFormulario(seleccionado));

        cargarTabla(usuarioService.listar());
    }

    @FXML
    private void buscarUsuarios() {
        cargarTabla(usuarioService.buscar(campoBuscar.getText()));
    }

    @FXML
    private void nuevoUsuario() {
        usuarioSeleccionado = null;
        limpiarFormulario();
    }

    @FXML
    private void guardarUsuario() {
        try {
            Usuario usuario = usuarioSeleccionado != null ? usuarioSeleccionado : new Usuario();

            usuario.setNombreCompleto(campoNombreCompleto.getText());
            usuario.setCorreo(campoCorreo.getText());
            usuario.setNombreUsuario(campoNombreUsuario.getText());
            usuario.setActivo(checkActivo.isSelected());

            Rol rolSeleccionado = comboRol.getValue();
            usuario.setIdRol(rolSeleccionado != null ? rolSeleccionado.getIdRol() : 0);

            usuarioService.guardar(usuario, campoPassword.getText());
            sceneManager.showAlertInfo("Listo", "Usuario guardado", "Los datos se guardaron correctamente.",
                    AlertType.INFORMATION);

            nuevoUsuario();
            cargarTabla(usuarioService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo guardar", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            sceneManager.showAlertInfo("Sin selección", "Elige un usuario",
                    "Selecciona un usuario de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        if (Sesion.getUsuarioActual() != null
                && usuarioSeleccionado.getIdUsuario() == Sesion.getUsuarioActual().getIdUsuario()) {
            sceneManager.showAlertInfo("No permitido", "No puedes eliminar tu propio usuario",
                    "Pide a otro administrador que lo haga.", AlertType.WARNING);
            return;
        }

        try {
            usuarioService.eliminar(usuarioSeleccionado.getIdUsuario());
            nuevoUsuario();
            cargarTabla(usuarioService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo eliminar", "Ocurrió un error", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void volver() {
        try {
            sceneManager.showDashboardView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al panel principal", e.getMessage(),
                    AlertType.ERROR);
        }
    }

    private void cargarTabla(List<Usuario> usuarios) {
        ObservableList<Usuario> datos = FXCollections.observableArrayList(usuarios);
        tablaUsuarios.setItems(datos);
    }

    private void cargarEnFormulario(Usuario usuario) {
        usuarioSeleccionado = usuario;
        if (usuario == null) {
            limpiarFormulario();
            return;
        }
        campoNombreCompleto.setText(usuario.getNombreCompleto());
        campoCorreo.setText(usuario.getCorreo());
        campoNombreUsuario.setText(usuario.getNombreUsuario());
        checkActivo.setSelected(usuario.isActivo());
        campoPassword.clear();

        comboRol.getItems().stream()
                .filter(r -> r.getIdRol() == usuario.getIdRol())
                .findFirst()
                .ifPresent(comboRol::setValue);
    }

    private void limpiarFormulario() {
        campoNombreCompleto.clear();
        campoCorreo.clear();
        campoNombreUsuario.clear();
        campoPassword.clear();
        comboRol.setValue(null);
        checkActivo.setSelected(true);
        tablaUsuarios.getSelectionModel().clearSelection();
    }
}