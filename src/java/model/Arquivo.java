package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entidade responsavel por armazenar um arquivo no banco de dados.
 *
 * @author Renan
 */
@Entity
public class Arquivo implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_arquivo", nullable = false)
    private int id;

    @Column(nullable = false)
    private String nome;

    /**
     * Bytes do arquivo.
     */
    @Column(nullable = false)
    private byte[] bytes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

}
