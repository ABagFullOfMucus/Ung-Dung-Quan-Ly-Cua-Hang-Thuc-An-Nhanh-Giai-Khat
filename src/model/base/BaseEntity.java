package model.base;

public abstract class BaseEntity {
    protected String id;
    protected boolean active;

    public BaseEntity() {
        this.active = true;
    }

    public BaseEntity(String id) {
        this.id = id;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public boolean deactivate() {
        this.active = false;
        return true;
    }

    public boolean activate() {
        this.active = true;
        return true;
    }
}
