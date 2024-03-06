package edu.curtin.app.find;

import java.io.File;
import java.util.List;

public interface FindStrategy {
    List<String> find(String type, String key, File file);
}
