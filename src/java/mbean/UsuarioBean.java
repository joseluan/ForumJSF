/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mbean;

import dao.Operacoes;
import dao.UsuarioDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.persistence.Query;
import model.Usuario;
import utils.Criptografia;
import utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@ManagedBean
public class UsuarioBean extends UsuarioDAO implements Operacoes{
    Usuario usuario = new Usuario();
    
    @Override
    public String criar() {
        usuario.setSenha(Criptografia.esconderMD5(usuario.getSenha()));
        persist(usuario);
        addMsgInfo("Usuario Cadastrado com sucesso!");
        return null;
    }

    @Override
    public String atualizar() {
        usuario.setSenha(Criptografia.esconderMD5(usuario.getSenha()));
        merge(usuario);
        addMsgInfo("Alterações salvas!");
        return "/autenticado/inicio";
    }
    
    public String atualizarSesion() {
        if (!ValidatorUtil.isEmpty(usuario.getSenha()))
            usuario_session.setSenha(Criptografia.esconderMD5(usuario.getSenha()));
       
        merge(usuario_session);
        addMsgInfo("Alterações salvas!");
        return null;
    }

    @Override
    public String remover() {
        remove(usuario);
        addMsgInfo("Usuario removido com sucesso!");
        return null;
    }
    
    public String sessionRemover() {
        removeSession();
        addMsgInfo("Usuario removido com sucesso!");
        return null;
    }
    
    public Usuario buscarPorLoginESenha(String login, String senha){
        Query query = getEntityManager().createQuery("SELECT x FROM Usuario x WHERE x.nome = :login and x.senha = :senha ");
        query.setParameter("login", login);
        query.setParameter("senha", Criptografia.esconderMD5(senha));
        return (Usuario) query.getSingleResult();
    }

    public List<Usuario> getLimiteUsuarios(){
        return limiteResposta(5);
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
