package fr.certu.chouette.struts.json.data;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.beanutils.BeanUtils;

import fr.certu.chouette.model.neptune.AreaCentroid;
import fr.certu.chouette.model.neptune.NeptuneIdentifiedObject;
import fr.certu.chouette.model.neptune.StopArea;
import fr.certu.chouette.model.neptune.type.ChouetteAreaEnum;

public class JSONStopArea extends NeptuneIdentifiedObject 
{
	@Getter @Setter private AreaCentroid areaCentroid;
	@Getter @Setter private String comment;
	@Getter @Setter private ChouetteAreaEnum areaType;
	@Getter @Setter private Integer fareCode;
	@Getter @Setter private Boolean liftAvailable;
	@Getter @Setter private Boolean mobilityRestrictedSuitable;
	@Getter @Setter private Boolean stairsAvailable;
	@Getter @Setter private String nearestTopicName;
	@Getter @Setter private String registrationNumber;


	public JSONStopArea(StopArea area) throws Exception
	{
		BeanUtils.copyProperties(this, area);
	}
}
