/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import model.Pergunta;
import model.Usuario;
import utils.AbstractController;

/**
 *
 * @author Luan
 */
public class PerguntaDAO extends Database {

    //pegando o usuario logado
    public AbstractController abc = new AbstractController();
    public HttpSession session = abc.getCurrentSession();
    public Usuario usuario_session = (Usuario) session.getAttribute("usuario");

    public Pergunta getById(int id) {
        Pergunta p = new Pergunta();
        try {
            p = em.find(Pergunta.class, id);
        } catch (NullPointerException e) {
            addMsgError("Pergunta não existe!");
        }
        return p;
    }

    public Pergunta getByIdESession(int id) {
        Pergunta buscaPergunta = new Pergunta();
        try {
            buscaPergunta = em.find(Pergunta.class, id);
            if (Objects.equals(usuario_session.getId(), buscaPergunta.getUsuario().getId())) {
                return buscaPergunta;
            }
        } catch (NullPointerException e) {
            addMsgError("Pergunta não existe!");
        }
        return buscaPergunta;
    }

    @SuppressWarnings("unchecked")
    public List<Pergunta> findAll() {
        //buscando todos os registro de pergunta
        return em.createQuery("SELECT x FROM :tabela x ")
                .setParameter("tabela", Pergunta.class.getName())
                .getResultList();
    }

    public void persist(Pergunta pergunta) {
        try {
            //adicionando a pergunta o usuario da sessão
            pergunta.setUsuario(usuario_session);
            pergunta.setData(new Date());

            //cadastrando a pergunta
            em.getTransaction().begin();
            em.persist(pergunta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void mergePorSession(Pergunta pergunta) {
        try {
            em.getTransaction().begin();
            //pegando a pergunta do banco
            Pergunta atualizarPergunta = getById(pergunta.getId());

            if (Objects.equals(usuario_session.getId(), atualizarPergunta.getUsuario().getId())) {

                atualizarPergunta.setTexto(pergunta.getTexto());
                atualizarPergunta.setTitulo(pergunta.getTitulo());

                em.merge(atualizarPergunta);
                em.getTransaction().commit();
            } else {
                addMsgError("Você não pode atualizar, essa pergunta não é sua!");
            }

        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void merge(Pergunta pergunta) {
        try {
            em.getTransaction().begin();
            Pergunta atualizarPergunta = getById(pergunta.getId());
            atualizarPergunta.setTexto(pergunta.getTexto());
            atualizarPergunta.setTitulo(pergunta.getTitulo());

            em.merge(atualizarPergunta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void remove(Pergunta pergunta) {
        try {
            em.getTransaction().begin();
            pergunta = em.find(Pergunta.class, pergunta.getId());
            em.remove(pergunta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public void removeById(final int id) {
        try {
            Pergunta pergunta = getById(id);
            remove(pergunta);
        } catch (Exception ex) {
        }
    }

    public List<Pergunta> todasPerguntas() {
        Query query = em.createQuery("SELECT x FROM Pergunta x ORDER BY x.data DESC");
        return query.getResultList();
    }

    public List<Pergunta> minhasPerguntas() {
        Query query = em.createQuery("SELECT x FROM Pergunta x WHERE x.usuario.id = :id  ORDER BY x.data DESC");
        query.setParameter("id", usuario_session.getId());
        return query.getResultList();
    }

    public List<Pergunta> limitePerguntas(int limite) {
        Query query = em.createQuery("SELECT x FROM Pergunta x ORDER BY x.data DESC");
        query.setMaxResults(limite);
        return query.getResultList();
    }
}
