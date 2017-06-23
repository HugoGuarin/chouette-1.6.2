package fr.certu.chouette.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.certu.chouette.common.ChouetteException;
import fr.certu.chouette.model.neptune.AccessLink;
import fr.certu.chouette.model.neptune.AccessPoint;
import fr.certu.chouette.model.neptune.StopArea;
import fr.certu.chouette.model.neptune.type.LinkOrientationEnum;
import fr.certu.chouette.model.user.User;

/**
 * 
 * @author mamadou keira
 *
 */
public class AccessLinkManager extends AbstractNeptuneManager<AccessLink> 
{
	private static final Logger logger = Logger.getLogger(AccessLinkManager.class);

	public AccessLinkManager() {
		super(AccessLink.class,AccessLink.ACCESSLINK_KEY);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveAll(User user, List<AccessLink> links, boolean propagate,boolean fast)
	throws ChouetteException 
	{
		if (propagate)
		{
			INeptuneManager<StopArea> stopAreaManager = (INeptuneManager<StopArea>) getManager(StopArea.class);
			INeptuneManager<AccessPoint> accessPointManager = (INeptuneManager<AccessPoint>) getManager(AccessPoint.class);

			List<StopArea> stopAreas = new ArrayList<StopArea>();
			List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();

			for (AccessLink accessLink : links) 
			{
				addIfMissingInCollection(stopAreas, accessLink.getStopArea());
				addIfMissingInCollection(accessPoints, accessLink.getAccessPoint());
			}
			if (!stopAreas.isEmpty()) stopAreaManager.saveAll(user, stopAreas, propagate,fast);
			if (!accessPoints.isEmpty()) accessPointManager.saveAll(user, accessPoints, propagate,fast);
		}
		super.saveAll(user, links, propagate,fast);
	}

	@Override
	public void completeObject(User user, AccessLink bean) throws ChouetteException 
	{
		if (bean.getLinkOrientation() != null)
		{
			if (bean.getLinkOrientation().equals(LinkOrientationEnum.ACCESSPOINT_TO_STOPAREA))
			{
				if (bean.getAccessPoint() != null)
					bean.setStartOfLinkId(bean.getAccessPoint().getObjectId());
				if (bean.getStopArea() != null)
					bean.setEndOfLinkId(bean.getStopArea().getObjectId());
			}
			else if (bean.getLinkOrientation().equals(LinkOrientationEnum.STOPAREA_TO_ACCESSPOINT))
			{
				if (bean.getAccessPoint() != null)
					bean.setEndOfLinkId(bean.getAccessPoint().getObjectId());
				if (bean.getStopArea() != null)
					bean.setStartOfLinkId(bean.getStopArea().getObjectId());
			}
		}
	}



}
