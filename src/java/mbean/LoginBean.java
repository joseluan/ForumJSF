/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mbean;

import javax.faces.bean.ManagedBean;
import model.Usuario;
import utils.AbstractController;
import utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@ManagedBean
public class LoginBean extends AbstractController {

    UsuarioBean ub = new UsuarioBean();
    Usuario usuario = new Usuario();

    public String login() {
        if (!validarLogin()) {
            addMsgError("Login ou senha não informados");
            return null;
        }
        
        try {
            Usuario u_banco = ub.buscarPorLoginESenha(usuario.getNome(), usuario.getSenha());
            if (!ValidatorUtil.isEmpty(u_banco)) {
                getCurrentSession().setAttribute("usuario", u_banco);
                addMsgInfo("Seja Bem vindo ao Fórum(a)");
                return "/autenticado/mural";
            }
        } catch (Exception e) {
            addMsgError("Login ou senha incorretos!");
            return "/publico/login";
        }
        addMsgError("Login ou senha incorretos!");
        return "/publico/login";
    }

    public String logoff() {
        getCurrentSession().removeAttribute("usuario");
        return "/publico/login";
    }
    
    public boolean validarLogin(){
        if (!ValidatorUtil.isEmpty(usuario.getNome()) && !ValidatorUtil.isEmpty(usuario.getSenha())) {
            return true;
        }
        return false;
    }

    public UsuarioBean getUb() {
        return ub;
    }

    public void setUb(UsuarioBean ub) {
        this.ub = ub;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
