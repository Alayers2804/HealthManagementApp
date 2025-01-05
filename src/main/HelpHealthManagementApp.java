package main;

import frame.FrameGUI;
import javax.swing.SwingUtilities;
import manager.MedicalFacilitiesManager;
import manager.PatientsManager;
import manager.ProcedureManager;
import menu.StartMenu;

public class HelpHealthManagementApp {

    private FrameGUI gui;
    private PatientsManager patientManager;
    private MedicalFacilitiesManager facilitiesManager;
    private ProcedureManager procedureManager;

    public HelpHealthManagementApp() {
        gui = new FrameGUI();
        facilitiesManager = new MedicalFacilitiesManager();
        patientManager = new PatientsManager();
        procedureManager = new ProcedureManager();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HelpHealthManagementApp app = new HelpHealthManagementApp();
            app.showStartMenu();
        });
    }

    private void showStartMenu() {
        new StartMenu(this).display();
    }

    public FrameGUI getGui() {
        return gui;
    }

    public PatientsManager getPatientManager() {
        return patientManager;
    }

    public MedicalFacilitiesManager getFacilitiesManager() {
        return facilitiesManager;
    }
    
    public ProcedureManager getProcedureManager(){
        return procedureManager;
    }
}
