/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package fr.certu.chouette.model.neptune;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Neptune Company 
 * <p/>
 * Note for fields comment : <br/>
 * when readable is added to comment, a implicit getter is available <br/>
 * when writable is added to comment, a implicit setter is available
 */
@NoArgsConstructor
public class Company extends NeptuneIdentifiedObject
{
	private static final long serialVersionUID = -8086291270595894778L;
	/**
	 * Short Name
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String shortName; // BD
	/**
	 * Organization Unit
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String organisationalUnit; // BD
	/**
	 * Operating Department Name
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String operatingDepartmentName; // BD
	/**
	 * Postal (Zip) Code
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String code; // BD
	/**
	 * Phone Number 
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String phone; // BD
	/**
	 * Fax Number
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String fax; // BD
	/**
	 * Email
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String email; // BD
	/**
	 * Registration Number
	 * <br/><i>readable/writable</i>
	 */
	@Getter @Setter private String registrationNumber; // BD

//	/* (non-Javadoc)
//	 * @see fr.certu.chouette.model.neptune.NeptuneBean#expand(fr.certu.chouette.manager.NeptuneBeanManager.DETAIL_LEVEL)
//	 */
//	@Override
//	public void expand(DetailLevelEnum level)
//	{
//		// to avoid circular call check if level is already set according to this level
//		if (getLevel().ordinal() >= level.ordinal()) return;
//		super.expand(level);
//		switch (level)
//		{
//		case ATTRIBUTE : 
//			break;
//		case NARROW_DEPENDENCIES :
//			break;
//		case STRUCTURAL_DEPENDENCIES : 
//		case ALL_DEPENDENCIES :
//		}
//	}
	
	/* (non-Javadoc)
	 * @see fr.certu.chouette.model.neptune.NeptuneIdentifiedObject#toString(java.lang.String, int)
	 */
	@Override
	public String toString(String indent,int level)
	{
		StringBuilder sb = new StringBuilder(super.toString(indent,level));
		sb.append("\n").append(indent).append("shortName = ").append(shortName);
		sb.append("\n").append(indent).append("organisationalUnit = ").append(organisationalUnit);
		sb.append("\n").append(indent).append("operatingDepartmentName = ").append(operatingDepartmentName);
		sb.append("\n").append(indent).append("code = ").append(code);
		sb.append("\n").append(indent).append("phone = ").append(phone);
		sb.append("\n").append(indent).append("fax = ").append(fax);
		sb.append("\n").append(indent).append("email = ").append(email);
		sb.append("\n").append(indent).append("fax = ").append(fax);
		sb.append("\n").append(indent).append("registrationNumber = ").append(registrationNumber);

		return sb.toString();
	}

}
