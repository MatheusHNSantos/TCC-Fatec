package model.entity;

public interface Entity {

    public int id  = -1;

    public void setId(int id);

    public int getId();

    /**
     * Este método é responsável por criar ou atualizar um entidade.
     *
     * @return boolean
     */
    public boolean save();
}
