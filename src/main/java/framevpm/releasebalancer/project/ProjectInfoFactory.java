package framevpm.releasebalancer.project;

import java.util.Map;
import java.util.TreeMap;

import static framevpm.releasebalancer.project.CProjectsInfo.*;

public class ProjectInfoFactory {

    public static Map<String, String> retrieveProjectReleaseCVE(String name) {
        switch (name) {
            case "linux_kernel":
                return LINUX_CVE_VERSIONS();
            case "openssl":
                return OPENSSL_CVE_VERSION();
            case "wireshark":
                return WIRESHARK_CVE_VERSION();
            case "systemd":
                return SYSTEMD_CVE_VERSION();
            default:
                return null;
        }
    }

    public static TreeMap<Long, String> retrieveProjectRelease(String name) {
        switch (name) {
            case "linux_kernel":
                return LINUX_VERSIONS();
            case "openssl":
                return OPENSSL_VERSION();
            case "wireshark":
                return WIRESHARK_VERSION();
            case "systemd":
                return SYSTEMD_VERSION();
            default:
                return null;
        }
    }

}
