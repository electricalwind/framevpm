package framevpm;

import data7.Importer;
import data7.project.CProjects;
import data7.project.Project;
import framevpm.analyze.Application;
import framevpm.bugcollector.BugCollector;
import framevpm.organize.Organize;

import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        ResourcesPathExtended pathExtended = new ResourcesPathExtended("/home/matthieu/vpm/");
        Project[] projects = new Project[]{//CProjects.SYSTEMD, CProjects.OPEN_SSL,
                CProjects.WIRESHARK, CProjects.LINUX_KERNEL};
        for (Project project : projects) {
            try {
                new Importer(pathExtended).updateOrCreateDatasetFor(project);
                System.gc();
                new BugCollector(pathExtended).updateOrCreateBugDataset(project.getName());
                System.gc();
                new Organize(pathExtended, project.getName()).balance(true);
                System.gc();
                new Application(pathExtended, project.getName()).runAll();
                System.gc();

            } catch (ParseException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
