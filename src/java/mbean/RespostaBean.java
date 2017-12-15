/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mbean;

import dao.Operacoes;
import dao.RespostaDAO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Pergunta;
import model.Resposta;
import utils.ValidatorUtil;

/**
 *
 * @author Luan
 */

@ManagedBean
@ViewScoped
public class RespostaBean extends RespostaDAO implements Operacoes{
    List<Resposta> lista = null;
    Resposta resposta = new Resposta();
    Pergunta pergunta = new Pergunta();
    
    @Override
    public String criar() {
        resposta.setPergunta(pergunta);
        persist(resposta);
        resposta = new Resposta();
        return null;
    }
    
    public String criar(Pergunta p) {
        
        
        resposta.setPergunta(p);
        persist(resposta);
        resposta = new Resposta();
        pergunta = new Pergunta();
        return null;
    }

    @Override
    public String atualizar() {
        if (ValidatorUtil.isEmpty(resposta.getTexto())){
            addMsgWarning("Digite pelo menos 2 caracteres no texto de atualização da respsota");
            return null;
        }
        
        mergePorSession(resposta);
        addMsgInfo("Resposta Atualizada !");
        return null;
    }

    @Override
    public String remover() {
        remove(resposta);
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public List<Resposta> getTodasResposta(){
        return todasRespostas();
    }
    
    public List<Resposta> getLimiteRespostaByPergunta(Pergunta p){
        return limiteResposta(p,5);
    }
    
    public List<Resposta> getRespostaByPergunta(Pergunta p){
        return todasRespostas(p);
    }
    
    public List<Resposta> getSessionRespostas(){
        return minhasRespostas();
    }

    public String buscarPorId(){
        if (ValidatorUtil.isEmpty(resposta.getId()))
            return null;
        
        Resposta r = getByIdPorSession(resposta.getId());
       
        if (!ValidatorUtil.isEmpty(r)) 
            resposta = r;
        
        return null;
    }
    
    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public List<Resposta> getLista() {
        return lista;
    }

    public void setLista(List<Resposta> lista) {
        this.lista = lista;
    }
    
}
