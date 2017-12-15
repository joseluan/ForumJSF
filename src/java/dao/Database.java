package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import utils.AbstractController;

/**
 * Singleton que controla a comunicação com o banco de dados, inclusive o
 * gerenciamento do EntityManager.
 *
 * @author Renan
 */
public class Database extends AbstractController{
    
    private static final Database singleton = new Database();
    protected static EntityManager em;
    
    public Database() {
        criarEM();
    }
    
    static {
        criarEM();
    }

    private static void criarEM() {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("NISIA-PU");
        em = emf.createEntityManager();
    }

    public static Database getInstance() {
        return singleton;
    }

    public EntityManager getEntityManager() {
        if (!em.isOpen()) {
            criarEM();
        }

        return em;
    }

    public Session getSession() {
        return (Session) em.getDelegate();
    }
    
}
