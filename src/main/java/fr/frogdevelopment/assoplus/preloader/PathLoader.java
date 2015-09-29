/*
 * Copyright (c) Frog Development 2015.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.frogdevelopment.assoplus.preloader;

import java.io.IOException;
import java.io.InputStream;

public class PathLoader {

    private PathLoader() {
    }

    public static String getPath(String filename) throws IOException {
        StringBuilder buf = new StringBuilder();

        InputStream is = PathLoader.class.getResourceAsStream(filename);
        int read;
        while ((read = is.read()) != -1) {
            buf.append((char) read);
        }

        return buf.toString();
    }

    public static String getPath(int i) throws IOException {
        return getPath("/preloader/paths/path" + i);
    }

}
