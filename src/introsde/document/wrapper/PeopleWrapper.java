package introsde.document.wrapper;

import introsde.document.model.Person;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class wrapper used when are listened the person
 * @author yuly
 *
 */

@XmlRootElement(name="people")
public class PeopleWrapper {
	
	@XmlElement(name="person")
	private List<Person> peopleList = null;

	public void setPeopleList(List<Person> peopleList) {
		this.peopleList = new ArrayList<Person>();
		this.peopleList.addAll(peopleList);
	}	
}
