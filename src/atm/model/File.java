
package atm.model;

/**
 * Extensão da classe File, nativa do java, com métodos que ajudam a
 * criação de testes, e abstraindo a localização dos ficheiros de dados.
 *
 * Apenas o construtor público abstrai a localização dos ficheiros,
 * enquanto que os métodos "package private" são usados nos testes.
 *
 * @author heldercorreia
 */
public class File extends java.io.File {

    /** Subdirectório onde estão localizados os ficheiros de dados */
    private static String dir = "data";

    /**
     * Construtor usado por defeito.
     *
     * Serve apenas como abstracção para a localização
     * dos ficheiros de dados.
     *
     * Uso: new File("clientes.txt");
     *
     * @param filename  nome do ficheiro
     */
    public File(String filename) {
        super(dir + File.separator + filename);
    }

    /**
     * Constructor que transforma um objecto do tipo java.io.File para
     * um do tipo atm.model.File
     *
     * Usado nos testes, quando é preciso criar ficheiros temporários.
     *
     * @see createTempFile()
     *
     * @param file  objecto File nativo ao java
     */
    File(java.io.File file) {
        super(file.getAbsolutePath());
    }

    /**
     * Copia os conteúdos de um ficheiro para este, assumindo que
     * não há erros de leitura.
     *
     * Útil na criação dos testes, para retornar um ficheiro de dados
     * ao seu estado inicial (com base num template), após cada teste.
     *
     * @see FileTest
     *
     * @param src  ficheiro para copiar
     */
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

    /**
     * Cria um ficheiro temporário, se não houver erros de leitura.
     *
     * O ficheiro fica guardado na pasta de temporários do sistema.
     * Usado para testar esta classe (e.g. cópia entre ficheiros)
     *
     * @see FileTest
     *
     * @return  objecto File temporário
     */
    static File createTempFile() throws java.io.IOException {
        java.io.File temp = java.io.File.createTempFile("atm", null);
        return new File(temp);
    }

    /**
     * Compara dois ficheiros, para ver se têm o mesmo conteúdo.
     *
     * Útil na criação de testes, para efeitos de comparação.
     * Usado para certificar que uma cópia foi efectuada com sucesso.
     *
     * @see FileText
     *
     * @param dest  ficheiro de destino, para comparar
     * @return      true se os ficheiros têm o mesmo conteúdo;
     *              false caso contrário
     */
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
