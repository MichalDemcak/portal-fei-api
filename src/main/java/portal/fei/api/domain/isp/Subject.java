package portal.fei.api.domain.isp;

import javax.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue()
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rozsah")
    private String rozsah;

    @Column(name = "sposobUkoncenia")
    private String sposobUkoncenia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRozsah() {
        return rozsah;
    }

    public void setRozsah(String rozsah) {
        this.rozsah = rozsah;
    }

    public String getSposobUkoncenia() {
        return sposobUkoncenia;
    }

    public void setSposobUkoncenia(String sposobUkoncenia) {
        this.sposobUkoncenia = sposobUkoncenia;
    }
}
