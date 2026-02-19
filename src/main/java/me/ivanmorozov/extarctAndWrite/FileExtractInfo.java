package me.ivanmorozov.extarctAndWrite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileExtractInfo {
    public void extract(File file, Path toPath) throws IOException;
}
