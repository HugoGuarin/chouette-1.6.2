package fr.certu.chouette.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.certu.chouette.model.neptune.Line;
import fr.certu.chouette.model.neptune.RestrictionConstraint;

/**
 * 
 * @author mamadou keira
 * 
 */

public class RestrictionConstraintJdbcDao extends AbstractJdbcDao<RestrictionConstraint> 
{
	@Override
	protected void populateStatement(PreparedStatement ps, RestrictionConstraint restrictionConstraint)
	throws SQLException {
		ps.setString(1, restrictionConstraint.getName());
		ps.setString(2, restrictionConstraint.getObjectId());
		Line line = restrictionConstraint.getLine();
		Long lineId = null;
		if(line != null)
			lineId = line.getId();
		ps.setLong(3, lineId);
	}

}
