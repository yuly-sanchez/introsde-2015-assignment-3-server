package introsde.document.model;

import introsde.document.adapter.DateAdapter;
import introsde.document.converter.DateConverter;
import introsde.document.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity  
@Table(name="Measure")  
@NamedQuery(name="Measure.findAll", query="SELECT m FROM Measure m")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement
public class Measure implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(generator="sqlite_measure")
    @TableGenerator(name="sqlite_measure", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq", pkColumnValue="Measure")
    @Column(name="idMeasure")
	@XmlElement(name="mid")
	private Long idMeasure;
	
	@Temporal(TemporalType.DATE)
    @Column(name="dateRegistered")
	@Convert(converter=DateConverter.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date dateRegistered;
	
	@Column(name="measureType")
	private String measureType;
	
	@Column(name="measureValue")
	private String measureValue;
	
	@Column(name="measureValueType")
	private String measureValueType;

	@ManyToOne
	@JoinColumn(name="idPerson", referencedColumnName="idPerson")
	@XmlTransient
	private Person person;

	// Getters
	public Long getIdMeasure() {
		return idMeasure;
	}
	
	public Date getDateRegistered() {
		return dateRegistered;
	}

	public String getMeasureType() {
		return measureType;
	}

	public String getMeasureValue() {
		return measureValue;
	}

	public String getMeasureValueType() {
		return measureValueType;
	}

	public Person getPerson() {
		return person;
	}

	// Setters
	public void setIdMeasure(Long idMeasure) {
		this.idMeasure = idMeasure;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	public void setMeasureValueType(String measureValueType) {
		this.measureValueType = measureValueType;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public String toString(){
		return "Measure ( " + idMeasure + " " + dateRegistered + " " + measureType + " " + measureValue + " " + measureValueType + " )";
	}
	
	
	// database operations
    public static Measure getMeasureById(Long measureId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Measure m = em.find(Measure.class, measureId);
        LifeCoachDao.instance.closeConnections(em);
        return m;
    }

    public static List<Measure> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Measure> list = em.createNamedQuery("Measure.findAll", Measure.class).getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Measure saveMeasure(Measure m) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(m);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return m;
    } 

    public static Measure updateMeasure(Measure m) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        m=em.merge(m);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return m;
    }

    public static void removeMeasure(Measure m) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        m=em.merge(m);
        em.remove(m);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}
