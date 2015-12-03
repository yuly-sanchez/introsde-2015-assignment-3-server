package introsde.document.ws;
import introsde.document.model.Measure;
import introsde.document.model.Person;
import introsde.document.wrapper.HealthHistoryWrapper;
import introsde.document.wrapper.MeasureTypesWrapper;
import introsde.document.wrapper.PeopleWrapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Web Service Endpoint Implementation
 * @author yuly
 *
 */

@WebService(endpointInterface = "introsde.document.ws.People",serviceName="PeopleService")
public class PeopleImpl implements People {

	/**
	 *  Method #1: readPersonList() => List 
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
	 * Method #2: readPerson(Long id) => Person 
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
     * Method #3: updatePerson(Person p) => Person 
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
     * Method #4: createPerson(Person p) => Person 
     * Creates a new Person and returns the newly created person with its assigned id 
     * (if a health profile is included, create also those measurements for the new Person)
     */
    @Override
    public Person createPerson(Person person) {
        System.out.println("REQUESTED: createPerson(" + person.toString() + ")");
    	Person p = Person.savePerson(person);
    	return p;
    	
    	/*//checks if person includes currentMeasure, in other words a 'measure'
    	if(person.getCurrentHealth() == null){
    		System.out.println("REQUESTED: createPerson(Person person) without measure");
    		return Person.savePerson(person);
    		
    	}else{
    	
    		//removes the currentMeasure in the person and puts them in another variable
    		System.out.println("REQUESTED: createPerson(Person person) with new measure");
    		List<Measure> currentHealthList = new ArrayList<>();
    		currentHealthList.addAll(person.getCurrentHealthMeasure());
    		
    		person.setCurrentHealth(null);
    		
    		//saves the person in the database and retrieves his/her idPerson
    		Person p = Person.savePerson(person);
    		Long personId = p.getIdPerson();
    		
    		//List<Measure> checkCurrentMeasureList = new ArrayList<>();
    		
    		//checks the list of the currentHealth saved
    		for(int i=0; i<currentHealthList.size(); i++){
    			
    			//obtains a measure of the CurrentHealth list
    			Measure target = currentHealthList.get(i);
    			System.out.println(target.toString());
    			
    			//obtains a value of the new measure saved into currentHealth
    			String measureType = target.getMeasureType();
    			String measureValue = target.getMeasureValue();
    			String measureValueType = target.getMeasureValueType();
    			
    			//create today date
    			Calendar calendar = Calendar.getInstance();
    			
    			//archive the new value current measure also into measure db 
    			Measure m = new Measure();
    			m.setDateRegistered(calendar.getTime());
    			m.setMeasureType(measureType);
    			m.setMeasureValue(measureValue);
    			m.setMeasureValueType(measureValueType);
    			m.setPerson(p);
    			
    			Measure.saveMeasure(m);
    			//checkCurrentMeasureList.add(m);
    		}
    		//person.setCurrentHealth(checkCurrentMeasureList);
    		return Person.getPersonById(personId);	
    	}
*/    }

    /**
     * Method #5: deletePerson(Long id) => personId
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
    
    /**
     * Method #6: readPersonHistory(Long id, String measureType) => List 
	 * Returns the list of values (the history) of {measureType} (e.g. weight) 
	 * for Person identified by {id}
     */
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

	/**
	 * Method #7: readMeasureTypes() => List 
	 * Returns the list of measures
	 */
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
	
	/**
	 * Method #8: readPersonMeasure(Long id, String measureType, Long mid) => Measure 
	 * Returns the value of {measureType} (e.g. weight) identified by {mid} 
	 * for Person identified by {id}
	 */
	@Override
	public String getPersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measureType") String measureType, @WebParam(name="mid") Long idMeasure){
		System.out.println("--> REQUEST: getPersonMeasure("+ idPerson + " , " + measureType + " , " + idMeasure + ")");
		
		Person target = Person.getPersonById(idPerson);
		Measure measure = Measure.getByPersonMid(target, measureType, idMeasure);
		return measure.getMeasureValue();
	}

	/**
	 * Method #9: savePersonMeasure(Long id, Measure m) => Measure
	 * Saves a new measure object {m} (e.g. weight) of Person identified by {id} and 
	 * archive the old value in the history
	 */
	@Override
	public Measure savePersonMeasure(Long idPerson, Measure measure) {
		//search the person by idPerson 
		Person person = Person.getPersonById(idPerson);
		//search the measureValueType by measureType
		String measureValueType = Measure.getMeasureByMeasureType(measure.getMeasureType(), person);
		
		//set today date
		Calendar calendar = Calendar.getInstance();
		
		//set dateRegistered if there isn't a date defined
		Date date;
		if(measure.getDateRegistered() == null){
			date = calendar.getTime();
		}else{
			date = measure.getDateRegistered();
		}
		
		//save new 'measure' passed as input and insert the new measure value in the measure db
		Measure m = new Measure(date, measure.getMeasureType(), measure.getMeasureValue(), measureValueType, person);
		m = Measure.saveMeasure(m);
		
		return  Measure.getMeasureById(m.getIdMeasure().longValue());
	}

	/**
	 * Method #10: updatePersonMeasure(Long id, Measure m) => Measure
	 * Updates the measure identified with {m.mid}, related to the Person identified by {id}
	 */
	@Override
	public Measure updatePersonMeasure(Long idPerson, Measure measure) {
		// TODO Auto-generated method stub
		return null;
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
