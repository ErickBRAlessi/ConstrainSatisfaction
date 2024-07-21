module br.alessi.constrainsatisfaction {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.management;

    opens br.alessi.constrainsatisfaction to javafx.fxml;
    exports br.alessi.constrainsatisfaction;
}