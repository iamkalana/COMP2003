package edu.curtin.app.entity;

import java.util.List;

public interface Directory {
    void findMatchingLines(List<String> criteria);
    int getMatchingCount();
    void printCount();
    void printLines();
}
