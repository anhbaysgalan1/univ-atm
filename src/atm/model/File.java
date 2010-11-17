
package atm.model;

/**
 *
 * @author heldercorreia
 */
class File extends java.io.File {
    
    private static String dir = "data";

    File(String filename) {
        super(dir + File.separator + filename);
    }

    File(java.io.File file) {
        super(file.getAbsolutePath());
    }

    void copy(File src) throws java.io.IOException {
        java.io.FileReader in  = new java.io.FileReader(src);
        java.io.FileWriter out = new java.io.FileWriter(this);

        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
        
        in.close();
        out.close();
    }

    static File createTempFile() throws java.io.IOException {
        java.io.File temp = java.io.File.createTempFile("atm", null);
        return new File(temp);
    }

    boolean equals(File dest) throws java.io.IOException {
        java.io.FileReader src = new java.io.FileReader(this);
        java.io.FileReader dst = new java.io.FileReader(dest);

        int s, d;
        while (((s = src.read()) != -1) && ((d = dst.read()) != -1)) {
            if (s != d) {
                return false;
            }
        }

        return true;
    }
}
