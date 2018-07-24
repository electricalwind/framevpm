package framevpm.statistics.vulnerabilities.project;

import static framevpm.statistics.vulnerabilities.project.ProjectKind.LINUX_KERNEL_KIND;
import static framevpm.statistics.vulnerabilities.project.ProjectKind.OPENSSL_KIND;
import static framevpm.statistics.vulnerabilities.project.ProjectKind.WIRESHARK_KIND;

public class ProjectKindFactory {

    public static String[] retrieveProjectKind(String name) {
        switch (name) {
            case "linux_kernel":
                return LINUX_KERNEL_KIND();
            case "openssl":
                return OPENSSL_KIND();
            case "wireshark":
                return WIRESHARK_KIND();
            default:
                return null;
        }
    }


}
