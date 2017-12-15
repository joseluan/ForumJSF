/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.Database.em;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import model.Pergunta;
import model.Resposta;
import model.Usuario;
import utils.AbstractController;

/**
 *
 * @author Luan
 */
public class RespostaDAO extends Database {

    //pegando o usuario logado
    public AbstractController abc = new AbstractController();
    public HttpSession session = abc.getCurrentSession();
    public Usuario usuario_session = (Usuario) session.getAttribute("usuario");

    public Resposta getById(int id) {
        Resposta r = new Resposta();
        try {
            r = em.find(Resposta.class, id);
        } catch (NullPointerException e) {
            addMsgError("Resposta não existe!");
        }
        return r;
    }

    public Resposta getByIdPorSession(int id) {
        Resposta r = new Resposta();
        try {
            r = em.find(Resposta.class, id);
            if (Objects.equals(usuario_session.getId(), r.getUsuario().getId())) {
                return r;
            }
        } catch (NullPointerException e) {
            addMsgError("Resposta não existe!");
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    public List<Resposta> findAll() {
        return em.createQuery("FROM " + Resposta.class.getName()).getResultList();
    }

    public void persist(Resposta resposta) {
        try {
            em.getTransaction().begin();
            resposta.setData(new Date());
            resposta.setUsuario(usuario_session);

            em.persist(resposta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    public void mergePorSession(Resposta resposta) {
        try {
            em.getTransaction().begin();
            //pegando a pergunta do banco
            Resposta r = em.find(Resposta.class, resposta.getId());
            
            if (Objects.equals(usuario_session.getId(), r.getUsuario().getId())) {
                
                r.setTexto(resposta.getTexto());
                
                em.merge(r);
                em.getTransaction().commit();
            } else {
                addMsgError("Você não pode atualizar, essa pergunta não é sua!");
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void merge(Resposta resposta) {
        try {
            em.getTransaction().begin();
            em.merge(resposta);
            em.getTransaction().commit();
        } catch (NullPointerException ex) {
            em.getTransaction().rollback();
            addMsgError("Resposta não existe!");
        }
    }

    public void remove(Resposta resposta) {
        try {
            em.getTransaction().begin();
            resposta = em.find(Resposta.class, resposta.getId());
            em.remove(resposta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void removeById(final int id) {
        try {
            Resposta resposta = getById(id);
            remove(resposta);
        } catch (Exception ex) {
        }
    }

    public List<Resposta> todasRespostas() {
        Query query = em.createQuery("SELECT x FROM Resposta x ORDER BY x.data DESC");
        return query.getResultList();
    }

    public List<Resposta> limiteResposta(Pergunta p, int limite) {
        Query query = em.createQuery("SELECT x FROM Resposta x WHERE x.pergunta.id = :idPergunta ORDER BY x.data DESC");
        query.setParameter("idPergunta", p.getId());
        query.setMaxResults(limite);
        return query.getResultList();
    }
    
    public List<Resposta> todasRespostas(Pergunta p) {
        Query query = em.createQuery("SELECT x FROM Resposta x WHERE x.pergunta.id = :idPergunta ORDER BY x.data DESC");
        query.setParameter("idPergunta", p.getId());
        return query.getResultList();
    }

    public List<Resposta> minhasRespostas() {
        Query query = em.createQuery("SELECT x FROM Resposta x WHERE x.usuario.id = :id ORDER BY x.data DESC");
        query.setParameter("id", usuario_session.getId());
        return query.getResultList();
    }

}
