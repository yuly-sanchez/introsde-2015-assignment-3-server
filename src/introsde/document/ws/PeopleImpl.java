package introsde.document.ws;
import introsde.document.dao.LifeCoachDao;
import introsde.document.model.Measure;
import introsde.document.model.Person;
import introsde.document.wrapper.HealthHistoryWrapper;
import introsde.document.wrapper.MeasureTypesWrapper;
import introsde.document.wrapper.PeopleWrapper;

import java.text.ParseException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.persistence.EntityTransaction;

/**
 * Service Implementation
 * @author yuly
 *
 */

@WebService(endpointInterface = "introsde.document.ws.People",serviceName="PeopleService")
public class PeopleImpl implements People {

	/**
	 *  readPersonList() => List 
	 *  Returns the list all the people in the database
	 */
	@Override
    public PeopleWrapper getPeople() {
    	System.out.println("--> REQUEST: getPeople()");
    	
    	List<Person> people = Person.getAll();
    	for(Person p : people){
    		System.out.println(p.toString());
    	}
    	PeopleWrapper pw = new PeopleWrapper();
    	pw.setPeopleList(people);
        return pw;
    }
	
	/**
	 * readPerson(Long id) => Person 
	 * Returns all the Personal information plus current measures of one person identified by {id}
	 */
    @Override
    public Person getPerson(Long id) {
        System.out.println("---> REQUEST: getPerson(" + id + ")");
        Person p = Person.getPersonById(id);
        if (p!=null) {
            System.out.println("---> Found Person by id = " + id + " => " + p.getFirstname());
        } else {
            System.out.println("---> Didn't find any Person with  id = " + id);
        }
        return p;
    }

    /**
     * updatePerson(Person p) => Person 
     * Returns the personal information updated of the Person identified by {id}
     */
    @Override
    public Person updatePerson(Person person) throws ParseException{
    	System.out.println("--> REQUEST: updatePerson(p)");
        System.out.println("--> " + person.toString());
        Person existing = Person.getPersonById(person.getIdPerson());

        if (existing == null) {
        	//the person is not found
        	System.out.println("---> Didn't update any Person with  id = " + person.getIdPerson());
        } else {
            //person.setIdPerson(this.id);
            //checks if the client sent a name in order to update the person
            //if there is no name, remain the previous name, the same happens with Lastname and Birthdate
            if (person.getFirstname() == null){
            	person.setFirstname(existing.getFirstname());
            }
            if (person.getLastname() == null){
            	person.setLastname(existing.getLastname());
            }
            if (person.getBirthdate() == null){
            	person.setBirthdate(existing.getBirthdate());
            }
            person.setCurrentHealth(existing.getCurrentHealth());
            Person.updatePerson(person);
        }
        return person;
    }

    /**
     * createPerson(Person p) => Person 
     * Creates a new Person and returns the newly created person with its assigned id 
     * (if a health profile is included, create also those measurements for the new Person)
     */
    @Override
    public Person createPerson(Person person) {
        //Person.savePerson(person);
        //return person.getIdPerson();
    	return Person.savePerson(person);
    }

    /**
     * deletePerson(Long id) => personId
     * Deletes the Person identified by {id} from the system
     */
    @Override
    public String deletePerson(Long id) {
        System.out.println("REQUESTED: deletePerson(" + id.longValue() + ")");
		String result = "";
		Person target = Person.getPersonById(id);
		if(target != null){
			/*EntityTransaction tx = LifeCoachDao.instance.createEntityManager().getTransaction();
			tx.begin();
			target = LifeCoachDao.instance.createEntityManager().merge(target);
			LifeCoachDao.instance.createEntityManager().remove(target);
			tx.commit();*/
			Person.removePerson(target);
			result = "Person with id: " + id.longValue() + " deleted";
			return result;
		}
		else{
			result = "Person with id: " + id.longValue() + " not found!";
			return result;
		}
    }

	@Override
	public HealthHistoryWrapper getPersonHistory(Long id, String measureType) {
		System.out.println("--> REQUEST: getPersonHistory("+ id + " , " + measureType + ")");
    	
		Person target = Person.getPersonById(id);
    	List<Measure> measureHistory = Measure.getByPersonMeasure(target, measureType);
    	for(Measure m : measureHistory){
    		System.out.println(m.toString());
    	}
    	HealthHistoryWrapper hw = new HealthHistoryWrapper();
    	hw.setHealthHistoryList(measureHistory);
		return hw;
	}

	@Override
	@WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes")
    public MeasureTypesWrapper getMeasureTypes(){
		System.out.println("--> REQUEST: getMeasureTypes()");
		
		List<String> measureTypes = Measure.getByMeasureTypes();
		for(String m : measureTypes){
			System.out.println(m.toString());
		}
		MeasureTypesWrapper mw = new MeasureTypesWrapper();
		mw.setMeasureTypeList(measureTypes);
		return mw;
	}
	
	@Override
	public String getPersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measureType") String measureType, @WebParam(name="mid") Long idMeasure){
		System.out.println("--> REQUEST: getPersonMeasure("+ idPerson + " , " + measureType + " , " + idMeasure + ")");
		
		Person target = Person.getPersonById(idPerson);
		Measure measure = Measure.getByPersonMid(target, measureType, idMeasure);
		return measure.getMeasureValue();
	}
   /* @Override
    public int updatePersonHP(int id, LifeStatus hp) {
        LifeStatus ls = LifeStatus.getLifeStatusById(hp.getIdMeasure());
        if (ls.getPerson().getIdPerson() == id) {
            LifeStatus.updateLifeStatus(hp);
            return hp.getIdMeasure();
        } else {
            return -1;
        }
    }*/

}
