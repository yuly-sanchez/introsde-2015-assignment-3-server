package introsde.document.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class wrapper used when are listened the measureTypes
 * @author yuly
 *
 */

@XmlRootElement(name="measureTypes")
public class MeasureTypesWrapper{

	@XmlElement(name="measureType")
	private List<String> measureTypeList;

	/*public List<String> getMeasureTypeList() {
		return measureTypeList;
	}*/

	public void setMeasureTypeList(List<String> measureTypeList) {
		this.measureTypeList = new ArrayList<String>();
		this.measureTypeList.addAll(measureTypeList);
	}
	
}
