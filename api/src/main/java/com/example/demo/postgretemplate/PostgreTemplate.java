
package com.example.demo.postgretemplate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;

@Data
public class PostgreTemplate<T> {

	private EntityManager entityManager;
	private Class<T> entityClass;
	private CriteriaBuilder crBuilder;
	private CriteriaQuery<T> crQuery;
	private Root<T> root;
	
	public PostgreTemplate(EntityManager manager , Class<T> entity) {
		entityClass = entity;
		entityManager = manager;
		crBuilder = entityManager.getCriteriaBuilder();
		crQuery = crBuilder.createQuery(entityClass);
		root = crQuery.from(entityClass);
		crQuery.select(root);
	}
	
	public List<T> findAll(){ 		 
		return entityManager.createQuery(crQuery).getResultList();
	}
	
	public List<T> findByEqaulsPropertyName(String propertyName , String value){
		crQuery.where(equalPredicate(propertyName, value));
		return entityManager.createQuery(crQuery).getResultList();
	}
	
	public List<T> findByContainsPropertyName(String propertyName , String value){
		crQuery.where(containsPredicate(propertyName, value));
		return entityManager.createQuery(crQuery).getResultList();
	}
	 
	public <V> List<T> findByPropertyNameIn(String propertyNameName , List<V> values){
		crQuery.where(inPredicate(propertyNameName, values));
		return entityManager.createQuery(crQuery).getResultList();
	}
	 
	
	// PREDICATES  
	private Predicate equalPredicate(String propertyName, String value) { 
		return crBuilder.equal(root.get(propertyName),value);
	}
	
	private javax.persistence.criteria.Predicate containsPredicate(String propertyName , String value) { 
		return crBuilder.like(root.get(propertyName), "%"+ value + "%");
	}
	
	private Predicate inPredicate(String propertyName , List<?> value) { 
		return root.get(propertyName).in(value);
	}
 
	
	
}
