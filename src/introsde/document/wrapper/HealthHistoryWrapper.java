package introsde.document.wrapper;

import introsde.document.model.Measure;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class wrapper used when are listened the MeasureHistory
 * @author yuly
 *
 */

@XmlRootElement(name="healthProfile-history")
public class HealthHistoryWrapper{
	
	@XmlElement(name="measure")
	public List<Measure> healthHistoryList = null;

	public List<Measure> getHealthHistoryList() {
		return healthHistoryList;
	}

	public void setHealthHistoryList(List<Measure> healthHistoryList) {
		this.healthHistoryList = new ArrayList<Measure>();
		this.healthHistoryList.addAll(healthHistoryList);
	}
		
}
