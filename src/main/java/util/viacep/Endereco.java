package util.viacep;

/**
 * Entidade baseada nos dados do WS do viacep.com
 */
public class Endereco {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String rua;
    private String uf;
    private String unidade;
    private String ibge;
    private String gia;

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getRua() {
        return rua;
    }

    public String getUf() {
        return uf;
    }

    public String getUnidade() {
        return unidade;
    }

    public String getIbge() {
        return ibge;
    }

    public String getGia() {
        return gia;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setRua(String localidade) {
        this.rua = localidade;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Endereco setIbge(String ibge) {
        this.ibge = ibge;
        return this;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

}
