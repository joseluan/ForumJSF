package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.primefaces.model.UploadedFile;
import utils.Criptografia;

@Entity
public class Usuario implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Como assim você não tem nome? conserte")
    private String nome;

    @Column(nullable = false)
    @NotNull(message = "E você vai ficar sem segurança, coloque pelo menos 123!")
    private String senha;

    @Column(nullable = false)
    @NotNull(message = "Precisamos ter contato com vc colega de uma força!")
    private String email;

    /**
     * Atributo não persisitido que armazena uma foto que o usuário deseja para
     * seu perfil.
     *
     */
    @Transient
    private UploadedFile arquivo;

    @Column(name = "id_foto")
    private Integer idFoto;

    @OneToMany(mappedBy = "usuario", targetEntity = Pergunta.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Pergunta> perguntas;

    /**
     * Obtém a URL através da qual a foto do usuário pode ser carregada.
     * @return url String
     */
    public String getUrlFotoUsuario() {
        return "/verArquivo?"
                + "idArquivo=" + getIdFoto() //id do arquivo
                + "&key=" + Criptografia.esconderMD5(String.valueOf(getIdFoto())) //chave criptografada para acesso à imagem 
                + "&salvar=false";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }

    public Integer getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Integer idFoto) {
        this.idFoto = idFoto;
    }

}
