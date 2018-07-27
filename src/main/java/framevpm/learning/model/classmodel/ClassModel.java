package framevpm.learning.model.classmodel;

import framevpm.organize.model.FileType;

import java.util.List;

public interface ClassModel {

List<String> getClassList();

String correspondingToTypeFile(FileType fileType);

String getName();
}
