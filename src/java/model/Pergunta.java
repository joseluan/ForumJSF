package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Pergunta implements Serializable{

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private Usuario usuario;
    
    @Column(nullable=false)
    @NotNull(message="O titulo precisa ser preenchido!")
    private String titulo;
    
    @Column(length=2810,nullable=false)
    @NotNull(message="Como uma pergunta não tem texto? preencha agora!!!")
    private String texto;
    
    @Column(nullable=false)
    private Date data;

    @OneToMany(mappedBy = "pergunta", targetEntity = Resposta.class, fetch = FetchType.LAZY,cascade=CascadeType.REMOVE) 
    private List<Resposta> respostas;
    
    @Transient
    private Resposta novaResposta;

    public Pergunta() {
        novaResposta = new Resposta();
    }
    
    
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    public Resposta getNovaResposta() {
        return novaResposta;
    }

    public void setNovaResposta(Resposta resposta) {
        this.novaResposta = resposta;
    }
    
}
