/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import model.Usuario;
import model.Arquivo;
import utils.AbstractController;

/**
 *
 * @author Luan
 */
public class UsuarioDAO extends Database {

    public AbstractController abc = new AbstractController();
    public HttpSession session = abc.getCurrentSession();
    public Usuario usuario_session = (Usuario) session.getAttribute("usuario");

    public Usuario getById(int id) {
        return em.find(Usuario.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> findAll() {
        return em.createQuery("FROM " + Usuario.class.getName()).getResultList();
    }

    public void persist(Usuario usuario) {
        try {
            em.getTransaction().begin();

            //Cria nova entidade arquivo
            Arquivo arq = new Arquivo();
            arq.setNome(usuario.getArquivo().getFileName());
            arq.setBytes(usuario.getArquivo().getContents());
            
            em.persist(arq);

            usuario.setIdFoto(arq.getId());
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    public void merge(Usuario usuario) {
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void remove(Usuario usuario) {
        try {
            em.getTransaction().begin();
            usuario = em.find(Usuario.class, usuario.getId());
            em.remove(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    //esse método remove o usuario logado no sistema do DB
    public void removeSession() {
        try {
            remove(usuario_session);
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void removeById(final int id) {
        try {
            Usuario usuario = getById(id);
            remove(usuario);
        } catch (Exception ex) {
        }
    }

    public List<Usuario> limiteResposta(int limite) {
        Query query = em.createQuery("SELECT x FROM Usuario x ORDER BY x.id DESC");
        query.setMaxResults(limite);
        return query.getResultList();
    }

    public Usuario getUsuario_session() {
        return usuario_session;
    }

    public void setUsuario_session(Usuario usuario_session) {
        this.usuario_session = usuario_session;
    }

}
