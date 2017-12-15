package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Criptografia;

/**
 * Servlet para download de arquivos da base de arquivos. Esta servlet responde
 * por uma URL chamada: /verArquivo
 *
 * @author Renan de Oliveira
 */
@SuppressWarnings("serial")
public class VerArquivoServlet extends HttpServlet {

    private String URL = "jdbc:mysql://localhost:3306/forum";
    private String usuario = "root";
    private String pass = "admin";

    /**
     * Faz o download do arquivo. Basta informar seu ID e uma chave de
     * confirma��o criptografada.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        //Pegando o parametro da URL que diz qual é o ID do arquivo que se quer acessar
        String arquivo = req.getParameter("idArquivo");

        //Indica se o usu�rio quer fazer download do arquivo (salvar) ou s� visualiz�-lo
        String auxSalvar = req.getParameter("salvar");
        Boolean salvar = Boolean.valueOf(auxSalvar != null ? req.getParameter("salvar") : "true");

        if (arquivo != null && !arquivo.equals("")) {

            int idArquivo = new Integer(arquivo);

            //Pega o par�metro da chave passado atrav�s da URL 
            String key = req.getParameter("key");

            //Compara com a chave gerada atrav�s de criptografia
            String generatedKey = Criptografia.esconderMD5(String.valueOf(idArquivo));

            /*
			 * Compara a chave informada na URL com a chave gerada aqui.
			 * Se forem iguais, permite o download do arquivo.
             */
            if (key != null && key.equals(generatedKey)) {

                Connection conn = null;

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(URL, usuario, pass);

                    PreparedStatement ps = conn.prepareStatement("select bytes, nome from arquivo where id_arquivo = ? ");
                    ps.setInt(1, idArquivo);

                    ResultSet result = ps.executeQuery();
                    result.next();

                    byte[] bytes = result.getBytes("bytes");
                    String nome = result.getString("nome");

                    if (salvar) {
                        res.setHeader("Content-disposition", "attachment; filename=\"" + nome + "\"");
                    } else {
                        res.setHeader("Content-disposition", "inline; filename=\"" + nome + "\"");
                    }

                    res.getOutputStream().write(bytes);

                } catch (IOException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (conn != null && !(conn.isClosed())) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                res.getWriter().print("Acesso Negado!");
                res.getWriter().flush();
            }
        }
    }

}
