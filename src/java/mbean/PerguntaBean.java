/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mbean;

import dao.Operacoes;
import dao.PerguntaDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Pergunta;
import utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@ViewScoped
@ManagedBean
public class PerguntaBean extends PerguntaDAO implements Operacoes{
    Pergunta pergunta = new Pergunta();
    List<Pergunta> perguntas = limitePerguntas(20);
    
    @Override 
    public String criar() {
        if(pergunta.getTexto() == null || pergunta.getTitulo() == null)
            return null;
        
        persist(pergunta);
        addMsgInfo("Pergunta Cadastrada!");
        pergunta = new Pergunta();
        perguntas = limitePerguntas(20);
        return "/autenticado/mural";
    }

    @Override
    public String atualizar() {
        merge(pergunta);
        addMsgInfo("Mensagem Atualizada !");
        return null;
    }
    
    public String atualizarPorSession() {
        if(ValidatorUtil.isEmpty(pergunta.getTitulo()) || ValidatorUtil.isEmpty(pergunta.getTexto()) ){
            addMsgWarning("Digite pelo menos 2 caracteres no texto de atualização da respsota");
            return null;
        }
        
        mergePorSession(pergunta);
        addMsgInfo("Mensagem Atualizada !");
        return null;
    }
    
    public String selecionarPergunta(Pergunta p){
        pergunta = p;
        return "/autenticado/pergunta";
    }

    @Override
    public String remover() {
        remove(pergunta);
        pergunta = new Pergunta();
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public String remover(Pergunta p) {
        remove(p);
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public String buscarPorId(){
        if (!ValidatorUtil.isEmpty(getById(pergunta.getId()))) {
            this.pergunta = getById(pergunta.getId());
        }
        return null;
    }
    
    public String buscarPorIdESession(){
        if (ValidatorUtil.isEmpty(pergunta.getId()))
            return null;
        
        Pergunta p = getByIdESession(pergunta.getId());
        
        if (!ValidatorUtil.isEmpty(p))
            pergunta = p;
            
        return null;
    }
    
    public List<Pergunta> getSessionPerguntas(){
        return minhasPerguntas();
    }
    
    public List<Pergunta> getTodasPerguntas(){
        return todasPerguntas();
    }
    
    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }
    
}
