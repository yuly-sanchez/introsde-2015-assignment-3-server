
package introsde.document.soap;

import java.util.List;

import introsde.document.model.Measure;
import introsde.document.model.Person;
import introsde.document.wrapper.HealthHistoryWrapper;
import introsde.document.wrapper.MeasureTypesWrapper;
import introsde.document.wrapper.PeopleWrapper;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
/**
 * Web Service Endpoint Interface
 * @author yuly
 *
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface People {
	
	@WebMethod(operationName="readPersonList")
    @WebResult(name="person") 
    public List<Person> getPeople();
	
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person getPerson(@WebParam(name="personId") Long id);

    @WebMethod(operationName="updatePerson")
    @WebResult(name="person") 
    public Long updatePerson(@WebParam(name="person") Person person);
    
    @WebMethod(operationName="createPerson")
    @WebResult(name="person") 
    public Long createPerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="deletePerson")
    @WebResult(name="message") 
    public String deletePerson(@WebParam(name="personId") Long id);

    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="mesure")
    public List<Measure> getPersonHistory(@WebParam(name="personId") Long id, @WebParam(name="measureType") String measureType);
    
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureType")
    public List<String> getMeasureTypes();
    
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measure")
    public String getPersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measureType") String measureType, @WebParam(name="mid") Long idMeasure);
    
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="measure")
    public Long savePersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measure") Measure measure);
    
    @WebMethod(operationName="updatePersonMeasure")
    @WebResult(name="measure")
    public Long updatePersonM(@WebParam(name="personId") Long idPerson, @WebParam(name="measure") Measure measure, @WebParam(name="mid") Long idMeasure);
    
}