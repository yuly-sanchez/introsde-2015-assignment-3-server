package introsde.document.soap;
import introsde.document.model.Measure;
import introsde.document.model.Person;
import introsde.document.wrapper.HealthHistoryWrapper;
import introsde.document.wrapper.MeasureTypesWrapper;
import introsde.document.wrapper.PeopleWrapper;

import java.util.ArrayList;
import java.util.Calendar;
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

@WebService(endpointInterface = "introsde.document.soap.People",serviceName="PeopleService")
public class PeopleImpl implements People {

	/**
	 *  Method #1: readPersonList() => List 
	 *  Returns the list all the people in the database
	 */
	@Override
    public List<Person> getPeople() {
    	System.out.println("--> REQUEST: getPeople()");
    	
    	List<Person> people = Person.getAll();
    	for(Person p : people){
    		System.out.println(p.toString());
    	}
    	//PeopleWrapper pw = new PeopleWrapper();
    	//pw.setPeopleList(people);
        return people;
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
     * Method #3: updatePerson(Person p) => personId 
     * Update the personal information of the Person identified by {id} and return his/her personId
     */
    @Override
    public Long updatePerson(Person person){
    	System.out.println("--> REQUEST: updatePerson(p)");
    	
    	Person existing = Person.getPersonById(person.getIdPerson());
    	Long personId = 0L;
        
        if (existing == null) {
        	//the person is not found
        	System.out.println("---> id: "+ person.getIdPerson() + " not found!");
        	personId = new Long(-1);
        	
        } else {
        	person.setIdPerson(existing.getIdPerson());
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
            personId = person.getIdPerson(); 
        }
        return personId;
    }

    /**
     * Method #4: createPerson(Person p) => personId 
     * Creates a new Person and returns the his/her person id 
     * (if a health profile is included, create also those measurements for the new Person)
     */
    @Override
    public Long createPerson(Person person) {
    	
        //checks if person includes currentMeasure, in other words a 'measure'
    	if(person.currentHealth == null){
    		System.out.println("REQUESTED: createPerson(" + person.getFirstname() + ") without measure");
    		Person.savePerson(person);
    		return person.getIdPerson();
    		
    	}else{
    	
    		//removes the currentMeasure in the person and puts them in another variable
    		System.out.println("REQUESTED: createPerson(" +  person.getFirstname() + ") with new measure");
    		List<Measure> currentHealthList = new ArrayList<>();
    		currentHealthList.addAll(person.currentHealth);
    		
    		person.currentHealth.clear();
    		
    		//saves the person in the database and retrieves his/her idPerson
    		Person p = Person.savePerson(person);
    		//Long personId = p.getIdPerson();
    		
    		//create today date
			Calendar calendar = Calendar.getInstance();
			
    		//iterates on all 'measure' of the currentHealth list the client want insert
    		for(int i=0; i<currentHealthList.size(); i++){
    			
    			Measure newMeasure = currentHealthList.get(i); 
    			
    			//associates the 'measure' with the person
    			newMeasure.setPerson(p);
    			
    			//save the new current measure value of the Measure in the measure db 
    			newMeasure.setDateRegistered(calendar.getTime());
    			newMeasure.setMeasureType(currentHealthList.get(i).getMeasureType());
    			newMeasure.setMeasureValue(currentHealthList.get(i).getMeasureValue());
    			newMeasure.setValueType(currentHealthList.get(i).getValueType());
    			newMeasure.setIsCurrent(1);
    			System.out.println(newMeasure.toString());
    			
    			Measure.saveMeasure(newMeasure);
    		}
    		return person.getIdPerson();	
    	}
}

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
	public List<Measure> getPersonHistory(Long id, String measureType) {
		System.out.println("--> REQUEST: getPersonHistory("+ id + " , " + measureType + ")");
    	
		Person target = Person.getPersonById(id);
    	List<Measure> measureHistory = Measure.getHistoryMeasureByPerson(target, measureType);
    	for(Measure m : measureHistory){
    		System.out.println(m.toString());
    	}
    	//HealthHistoryWrapper hw = new HealthHistoryWrapper();
    	//hw.setHealthHistoryList(measureHistory);
		return measureHistory;
	}

	/**
	 * Method #7: readMeasureTypes() => List 
	 * Returns the list of measures
	 */
	@Override
	@WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes")
    public List<String> getMeasureTypes(){
		System.out.println("--> REQUEST: getMeasureTypes()");
		
		List<String> measureTypes = Measure.getByMeasureTypes();
		for(String m : measureTypes){
			System.out.println(m.toString());
		}
		//MeasureTypesWrapper mw = new MeasureTypesWrapper();
		//mw.setMeasureTypeList(measureTypes);
		return measureTypes;
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
	 * Method #9: savePersonMeasure(Long id, Measure m) => Long mid
	 * Saves a new measure object {m} (e.g. weight) of Person identified by {id} and 
	 * archive the old value in the history
	 */
	@Override
	public Long savePersonMeasure(Long idPerson, Measure measure) {
		System.out.println("--> REQUEST: savePersonMeasure("+ idPerson + " , " + measure.toString() + ")");
		
		Long measureId = 0L;
		Measure target = null;
		//set today date
		Calendar calendar = Calendar.getInstance();
		
		//search the person by idPerson 
		Person person = Person.getPersonById(idPerson);
		
		//check if found person with this idPerson
		if(person != null){
			System.out.println("--> Found person with id: " + idPerson);
			
			//check if measure already exits for person identified by id 
			target = Measure.foundCurrentMeasure(person, measure.getMeasureType());
			
			if(target != null){
				System.out.println("--> Found measure...");
				target.setIsCurrent(0);
				target.setPerson(person);
				Measure.updateMeasure(target);
				
				//create a new measure that the client want
				Measure newMeasure = new Measure();
				newMeasure.setIsCurrent(1);
				newMeasure.setDateRegistered(calendar.getTime());
				newMeasure.setMeasureType(measure.getMeasureType());
				newMeasure.setMeasureValue(measure.getMeasureValue());
				newMeasure.setValueType(measure.getValueType());
				newMeasure.setPerson(person);
				
				System.out.println("--> measure created...");
				
				//save the new measure
				Measure.saveMeasure(newMeasure);
				
				System.out.println("--> measure added");
				
				measureId = newMeasure.getIdMeasure();
			
			}else{
				target = new Measure();
				target.setIsCurrent(1);
				target.setDateRegistered(calendar.getTime());
				target.setMeasureType(measure.getMeasureType());
				target.setMeasureValue(measure.getMeasureValue());
				target.setValueType(measure.getValueType());
				target.setPerson(person);
				
				System.out.println("--> measure created...");
				
				//save the new measure
				Measure.saveMeasure(target);
				
				System.out.println("--> measure added");
				measureId = target.getIdMeasure();
			}
			return measureId;
		}else {
			return new Long(-1);
		}
	}

	/**
	 * Method #10: updatePersonMeasure(Long id, Measure m, Long mid) => mid
	 * Updates the measure identified with {m.mid}, related to the Person identified by {id}
	 * and return the mid associated to the measure updated 
	 */
	@Override
	public Long updatePersonM(Long idPerson, Measure measure, Long idMeasure) {
		System.out.println("--> REQUEST: updatePersonMeasure("+ idPerson + " , " + measure.toString() + " , " + idMeasure + ")");
		
		Measure existing = Measure.getMeasureById(idMeasure);
		Person person = Person.getPersonById(idPerson);
		//set today date
		Calendar calendar = Calendar.getInstance();
		
		if(existing == null){
			System.out.println("--> Update: MeasureType with " + idMeasure + " not found");
			return new Long(-1);
			
		}else{
			System.out.println("--> Update: MeasureType with " + idMeasure + " found");
			
			//isCurrent = 1
			if(existing.getIsCurrent() == 1){
				//change the value
				existing.setDateRegistered(calendar.getTime());
				existing.setMeasureValue(measure.getMeasureValue());
				Measure.updateMeasure(existing);
			
			//isCurrent = 0
			}else{
				//check if existing a measure current
				Measure target = Measure.foundCurrentMeasure(person, existing.getMeasureType());
				if(target != null){
					target.setIsCurrent(0);
					target.setPerson(person);
					Measure.updateMeasure(target);
					
					//change the value
					existing.setIsCurrent(1);
					existing.setDateRegistered(calendar.getTime());
					existing.setMeasureValue(measure.getMeasureValue());
					Measure.updateMeasure(existing);
				}
			}
			return existing.getIdMeasure();
		}
	}
}
