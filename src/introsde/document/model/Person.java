package introsde.document.model;

import introsde.document.adapter.DateAdapter;
import introsde.document.converter.DateConverter;
import introsde.document.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity  
@Table(name="Person")
@NamedQueries({
	@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p"),
	@NamedQuery(name="Person.currentHealth", query="SELECT m FROM Measure m WHERE m.dateRegistered IN (SELECT MAX(h.dateRegistered) FROM Measure h WHERE h.person = ?1 GROUP BY h.measureType)")
	
	//@NamedQuery(name="Person.currentHealth" , query="SELECT m FROM Measure m WHERE m.person = ?1 GROUP BY m.measureType HAVING m.dateRegistered = MAX(m.dateRegistered)")
	//SELECT * FROM Measure m WHERE m.dateRegistered IN (SELECT MAX(h.dateRegistered) FROM Measure h GROUP BY h.measureType) 
})

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Person implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
	@GeneratedValue(generator = "sqlite_person")
	@TableGenerator(name = "sqlite_person", table = "sqlite_sequence", 
					pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
    @Column(name="idPerson")
    @XmlElement(name="personId")
    private Long idPerson;
    
    @Column(name="firstname")
    @XmlElement
    private String firstname;
    
    @Column(name="lastname")
    @XmlElement
    private String lastname;
    
    @Temporal(TemporalType.DATE)
    @Column(name="birthdate")
    @Convert(converter=DateConverter.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
    @XmlElement
    private Date birthdate; 
    
    // mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
    @OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public List<Measure> currentHealth;
	
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Measure> healthHistory;
	
	// Getters
	public Long getIdPerson() {
		return idPerson;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Date getBirthdate() {
		return birthdate;
		/*DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        // Get the date today using Calendar object.
        return df.format(birthdate);*/
	}

	@XmlElementWrapper(name="currentHealth")
	@XmlElement(name="measure")
	public List<Measure> getCurrentHealth() {
		//return currentHealth;
		return this.getCurrentMeasure();
	}

	public List<Measure> getHealthHistory() {
		return healthHistory;
	}

	// Setters
	public void setIdPerson(Long idPerson) {
		this.idPerson = idPerson;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setBirthdate(Date birthdate) throws ParseException{
		this.birthdate = birthdate;    
		/*DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	    Date date = format.parse(birthdate);
	    this.birthdate = date;*/    
	}
	
	public void setCurrentHealth(List<Measure> currentHealth) {
		this.currentHealth = currentHealth;
	}

	public void setHealthHistory(List<Measure> healthHistory) {
		this.healthHistory = healthHistory;
	}

	public String toString(){
		return "Person ( " + idPerson + " " + firstname + " " + lastname + " " + birthdate + " )";
	}
	
	// database operations
    public static Person getPersonById(Long personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Person savePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static Person updatePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
    public List<Measure> getCurrentMeasure() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        //em.getEntityManagerFactory().getCache().evictAll();
        List<Measure> list = em.createNamedQuery("Person.currentHealth", Measure.class)
        		.setParameter(1, this)
        		.getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
	}
}