package fr.certu.chouette.dao.hibernate;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;

import fr.certu.chouette.filter.Filter;

public class FilterToHibernateClauseTranslator {

	private static final Logger logger = Logger.getLogger(FilterToHibernateClauseTranslator.class);
	private HashSet<String> aliasses;

	/**
	 * @param clause
	 * @return
	 */
	public  Criterion translate(Filter clause)
	{
		if (clause == null) throw new NullPointerException("JE VIENS DE RENCONTRER UNE CLAUSE NON INITIALISEE .. J'ARRETE TOUT TRAITEMENT");

		switch (clause.getFamilly())
		{
		case EMPTY : 
			return null;  // pas de filtre, uniquement de l'ordre et limite
		case TERMINAL : 
			return translateTerminal(clause);
		case COMBINED : 
			return translateCombined(clause);
		}
		return null; // 
	}


	/**
	 * @param clause
	 * @return
	 */
	private Criterion translateCombined(Filter clause)
	{
		Filter[] filters = clause.getCombinedFilters();
		switch (clause.getType())
		{
		case NOT : return Restrictions.not(translate(filters[0]));
		case AND : 

		{
			if (filters.length == 1) return translate(filters[0]);
			Criterion criterion = Restrictions.and(translate(filters[0]),translate(filters[1]));
			for (int i = 2; i < filters.length; i++) {
				criterion = Restrictions.and(criterion,translate(filters[i]));
			}
			return criterion;
		}
		case OR : 
		{
			if (filters.length == 1) return translate(filters[0]);
			Criterion criterion = Restrictions.or(translate(filters[0]),translate(filters[1]));
			for (int i = 2; i < filters.length; i++) {
				criterion = Restrictions.or(criterion,translate(filters[i]));
			}
			return criterion;
		}
		}
		return null;
	}

	/**
	 * @param clause
	 * @return
	 */
	private Criterion translateTerminal(Filter clause)
	{
		String propertyName = clause.getAttribute();

		switch (clause.getType())
		{
		case IS_NULL : 
			return Restrictions.isNull(propertyName);
		case EQUALS : 
			return Restrictions.eq(propertyName, clause.getFirstValue());
		case NOT_EQUALS : 
			return Restrictions.ne(propertyName, clause.getFirstValue());
		case LESS : 
			return Restrictions.lt(propertyName, clause.getFirstValue());
		case LESS_OR_EQUALS : 
			return Restrictions.le(propertyName, clause.getFirstValue());
		case GREATER : 
			return Restrictions.gt(propertyName, clause.getFirstValue());
		case GREATER_OR_EQUALS : 
			return Restrictions.ge(propertyName, clause.getFirstValue());
		case LIKE : 
			return Restrictions.like(propertyName, getILikeOrLikeRestrictionValue(clause.getFirstValue()));
		case ILIKE : 
			return Restrictions.ilike(propertyName, getILikeOrLikeRestrictionValue(clause.getFirstValue()));
		case IN : 
			return translateIn(clause);
		case BETWEEN : 
			return Restrictions.between(propertyName, clause.getFirstValue(), clause.getSecondValue());
		case SQL_WHERE : 
			return Restrictions.sqlRestriction(propertyName);
		}
		return null; // TODO : throw exception
	}

	/**
	 * @param clause
	 * @return
	 */
	private Criterion translateIn(Filter clause)
	{
		if (clause.getFirstValue() != null)
		{
			// sous requête non implémentée dans hibernate
		}
		else if (clause.getValueArray() != null)
		{
			return Restrictions.in(clause.getAttribute(), clause.getValueArray());
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	private String getILikeOrLikeRestrictionValue(Object value) 
	{
		return "%" + value + "%";
	}


	public Criterion translate(Filter clause, Criteria criteria, ClassMetadata metadata) 
	{
		aliasses = new HashSet<String>();
		if (clause == null) throw new NullPointerException("JE VIENS DE RENCONTRER UNE CLAUSE NON INITIALISEE .. J'ARRETE TOUT TRAITEMENT");

		switch (clause.getFamilly())
		{
		case EMPTY : 
			break ;  // pas de filtre, uniquement de l'ordre et limite
		case TERMINAL : 
			return  translateTerminal(clause,criteria,metadata);
		case COMBINED : 
			return  translateCombined(clause,criteria,metadata);
		}
		return null; // 


	}


	private Criterion translateCombined(Filter clause,
			Criteria criteria, ClassMetadata metadata) 
	{
		Filter[] filters = clause.getCombinedFilters();
		switch (clause.getType())
		{
		case NOT : return Restrictions.not(translate(filters[0],criteria,metadata));
		case AND :
		{
			if (filters.length == 1) return translate(filters[0]);
			Criterion criterion = Restrictions.and(translate(filters[0]),translate(filters[1],criteria,metadata));
			for (int i = 2; i < filters.length; i++) {
				criterion = Restrictions.and(criterion,translate(filters[i],criteria,metadata));
			}
			return criterion;
		}
		case OR : 
		{
			if (filters.length == 1) return translate(filters[0],criteria,metadata);
			Criterion criterion = Restrictions.or(translate(filters[0],criteria,metadata),translate(filters[1],criteria,metadata));
			for (int i = 2; i < filters.length; i++) {
				criterion = Restrictions.or(criterion,translate(filters[i],criteria,metadata));
			}
			return criterion;
		}
		}

		return null;
	}


	private Criterion translateTerminal(Filter clause, Criteria criteria, ClassMetadata metadata) 
	{
		String propertyName = clause.getAttribute();
		if (propertyName.contains("."))
		{
			// manage aliases
			String[] items = propertyName.split("\\.");
			if (items.length == 2)
			{
				String alias = items[0];
				String path = items[0];
				// check association 
				if (metadata.getPropertyType(path).isAssociationType())
				{
					if (!aliasses.contains(alias))
					{
						aliasses.add(alias);
						criteria.createCriteria(path,alias);
						criteria.setFetchMode(path, FetchMode.JOIN);
					}
				}
			}
			/*
			for (int i = 1; i < items.length -1; i++)
			{
				alias += "_"+items[i];
				path += "."+items[i];
				if (!aliasses.contains(alias))
				{
					aliasses.add(alias);
					System.out.println("alias "+alias+ "créé sur "+path);
					//criteria.createAlias(path, alias,Criteria.LEFT_JOIN);
					subCriteria.setFetchMode(items[i], FetchMode.JOIN);
					subCriteria = criteria.createCriteria(path,alias);
					// criteria.setFetchMode(path, FetchMode.JOIN);
				}

			}
			propertyName = alias+"."+items[items.length -1];
			System.out.println("property remplacé par "+propertyName);
			 */
		}
		return translateTerminal(clause);
	}

	
	public String translateToHQLDelete(Filter clause, ClassMetadata metadata) 
	{
		aliasses = new HashSet<String>();
		if (clause == null) throw new NullPointerException("JE VIENS DE RENCONTRER UNE CLAUSE NON INITIALISEE .. J'ARRETE TOUT TRAITEMENT");

		switch (clause.getFamilly())
		{
		case EMPTY : throw new NullPointerException("Empty query to HQL not yet implemented");
			
		case TERMINAL : 
			return  translateTerminalToHQLDelete(clause,metadata);
		case COMBINED : 
			throw new NullPointerException("Combined query to HQL not yet implemented");
		}
		return null; // 


	}


	private String translateTerminalToHQLDelete(Filter clause, ClassMetadata metadata) 
	{
		String propertyName = clause.getAttribute();

		String entityName = metadata.getEntityName();
		
		switch (clause.getType())
		{
//		case IS_NULL : 
//			return Restrictions.isNull(propertyName);
		case EQUALS : 
		{
			return "delete "+entityName+" bean where bean."+propertyName+" = "+toHQL(clause.getFirstValue());
		}
//		case NOT_EQUALS : 
//			return Restrictions.ne(propertyName, clause.getFirstValue());
//		case LESS : 
//			return Restrictions.lt(propertyName, clause.getFirstValue());
//		case LESS_OR_EQUALS : 
//			return Restrictions.le(propertyName, clause.getFirstValue());
//		case GREATER : 
//			return Restrictions.gt(propertyName, clause.getFirstValue());
//		case GREATER_OR_EQUALS : 
//			return Restrictions.ge(propertyName, clause.getFirstValue());
//		case LIKE : 
//			return Restrictions.like(propertyName, getILikeOrLikeRestrictionValue(clause.getFirstValue()));
//		case ILIKE : 
//			return Restrictions.ilike(propertyName, getILikeOrLikeRestrictionValue(clause.getFirstValue()));
//		case IN : 
//			return translateIn(clause);
//		case BETWEEN : 
//			return Restrictions.between(propertyName, clause.getFirstValue(), clause.getSecondValue());
//		case SQL_WHERE : 
//			return Restrictions.sqlRestriction(propertyName);
		}
		throw new NullPointerException(clause.getType()+" to HQL not yet implemented");
	}


	private String toHQL(Object value) 
	{
		if (value instanceof Number)
		{
			return value.toString();
		}
		if (value instanceof String)
		{
		   return StringHelper.quote((String) value);
		}
		throw new NullPointerException(value.getClass().getName()+" to HQL not yet implemented");
			
	}


}
