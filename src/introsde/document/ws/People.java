
package introsde.document.ws;

import introsde.document.model.Measure;
import introsde.document.model.Person;
import introsde.document.wrapper.HealthHistoryWrapper;
import introsde.document.wrapper.MeasureTypesWrapper;
import introsde.document.wrapper.PeopleWrapper;

import java.text.ParseException;
import java.util.List;

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
    @WebResult(name="people") 
    public PeopleWrapper getPeople();
	
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person getPerson(@WebParam(name="personId") Long id);

    @WebMethod(operationName="updatePerson")
    @WebResult(name="person") 
    public Person updatePerson(@WebParam(name="person") Person person) throws ParseException;
    
    @WebMethod(operationName="createPerson")
    @WebResult(name="person") 
    public Person createPerson(@WebParam(name="person") Person person);

    @WebMethod(operationName="deletePerson")
    @WebResult(name="message") 
    public String deletePerson(@WebParam(name="personId") Long id);

    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="healthProfile-history")
    public HealthHistoryWrapper getPersonHistory(@WebParam(name="personId") Long id, @WebParam(name="measureType") String measureType);
    
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes")
    public MeasureTypesWrapper getMeasureTypes();
    
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measure")
    public String getPersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measureType") String measureType, @WebParam(name="mid") Long idMeasure);
    
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="measure")
    public Measure savePersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measure") Measure measure);
    
    @WebMethod(operationName="updatePersonMeasure")
    @WebResult(name="measure")
    public Measure updatePersonMeasure(@WebParam(name="personId") Long idPerson, @WebParam(name="measure") Measure measure);
    
    //@WebMethod(operationName="updatePersonHealthProfile")
    //@WebResult(name="hpId") 
    //public int updatePersonHP(@WebParam(name="personId") int id, @WebParam(name="healthProfile") LifeStatus hp);
}