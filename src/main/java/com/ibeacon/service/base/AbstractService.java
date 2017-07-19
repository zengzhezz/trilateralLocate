package com.ibeacon.service.base;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ibeacon.hibernate.HibernateRepository;
import com.ibeacon.hibernate.Page;
import com.ibeacon.hibernate.PropertyFilter;



/**
 * <pre>
 * Service的抽象父类 专门用来继承给子类使用,减少程序员的重复的代码量
 * @author chenwentao
 *
 * @version 0.9
 *
 * 修改版本: 0.9
 * 修改日期: Nov 16, 2010
 * 修改人 :  chenwentao
 * 修改说明: 初步完成
 * 复审人 ：
 * </pre>
 */
@Service("abstractService")
@Transactional
public abstract class AbstractService {

    @Autowired
    protected HibernateRepository hibernateRepository;

    /**
     * Spring可以不需要这个方法,写他的目的主要是给Mock对象注入
     *
     * @param hibernateRepository
     */
    public void setHibernateRepository(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public SessionFactory getSessionFactory() {
        return this.hibernateRepository.getSessionFactory();
    }

    public Session getSession() {
        return this.hibernateRepository.getSession();
    }

    // --------------------------CRUD----------------------------------------------
    public void merge(Object entity) {
        hibernateRepository.merge(entity);
    }

    public void saveOrUpdate(Object entity) {
        hibernateRepository.saveOrUpdate(entity);
    }

    public Serializable save(Object entity) {
        return hibernateRepository.save(entity);
    }

    public void update(Object entity) {
        hibernateRepository.update(entity);
    }

    @SuppressWarnings("unchecked")
    public void delete(Class entityClass, Serializable id) {
        hibernateRepository.delete(entityClass, id);
    }

    public void delete(Object entity) {
        hibernateRepository.delete(entity);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<?> entityClass, Serializable id) {
        return (T) hibernateRepository.get(entityClass, id);
    }

    public <T> List<T> find(String hql, Object... values) {
        return hibernateRepository.find(hql, values);
    }

    public <T> T findUnique(String hql, Object... values) {
        List<T> results = hibernateRepository.find(hql, values);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }

        return results.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> T findUniqueBy(Class<T> clazz, String propertyName, Object value) {
        return (T) hibernateRepository.findUniqueBy(clazz, propertyName, value);
    }

    /**
     * <pre>
     * 根据page对象，hql查询语句，values传入的参数返回page对象
     * </pre>
     *
     * @param <T>
     * @param page
     * @param hql
     * @param values
     * @return
     */
    public <T> Page<T> findPage(final Page<T> page, final String hql,
                                final Object... values) {
        return hibernateRepository.findPage(page, hql, values);
    }

    public <T> Page<T> getPageSql(final Page<T> page,final Class classEntity,final String hql,
                                  final List<PropertyFilter> filters) {
        StringBuilder sb=new StringBuilder(hql);
        sb.append(" where 1=1 ");
        Object[] values=new Object[filters.size()];

        for (int i = 0; i < filters.size(); i++) {
            PropertyFilter pf=filters.get(i);
            sb.append(" and ").append(pf.getPropertyName()).append(" ").append(pf.getMatchType().getSql());
            values[i]=pf.getMatchValue();
        }

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');
            sb.append(" order by ");
            for(int i=0;i<orderByArray.length;i++){
                sb.append(" ").append(orderByArray[i]).append(" ").append(" ").append(orderArray[i]);
                if((orderByArray.length-1)!=i){
                    sb.append(",");
                }
            }
        }

        return hibernateRepository.findPageSql(page,classEntity ,sb.toString(),values);
    }


    public <T> Page<T> findPageSql(final Page<T> page,final String hql,
                                   final List<PropertyFilter> filters) {
        StringBuilder sb=new StringBuilder(hql);
        sb.append(" where 1=1 ");
        Object[] values=new Object[filters.size()];

        for (int i = 0; i < filters.size(); i++) {
            PropertyFilter pf=filters.get(i);
            sb.append(" and ").append(pf.getPropertyName()).append(" ").append(pf.getMatchType().getSql());
            values[i]=pf.getMatchValue();
        }

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');
            sb.append(" order by ");
            for(int i=0;i<orderByArray.length;i++){
                sb.append(" ").append(orderByArray[i]).append(" ").append(" ").append(orderArray[i]);
                if((orderByArray.length-1)!=i){
                    sb.append(",");
                }
            }
        }

        return hibernateRepository.findPage(page, sb.toString(), values);
    }


    public <T> Page<T> findPageHql(final Page<T> page,final String hql,
                                   final List<PropertyFilter> filters) {
        StringBuilder sb=new StringBuilder(hql);
        sb.append(" where 1=1 ");
        Object[] values=new Object[filters.size()];

        for (int i = 0; i < filters.size(); i++) {
            PropertyFilter pf=filters.get(i);
            sb.append(" and ").append(pf.getPropertyName()).append(" ").append(pf.getMatchType().getSql());
            values[i]=pf.getMatchValue();
        }

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');
            sb.append(" order by ");
            for(int i=0;i<orderByArray.length;i++){
                sb.append(" ").append(orderByArray[i]).append(" ").append(" ").append(orderArray[i]);
                if((orderByArray.length-1)!=i){
                    sb.append(",");
                }
            }
        }

        return hibernateRepository.findPage(page, sb.toString(), values);
    }



    public <T> Page<T> findPage(final Class entityClass, final Page<T> page,
                                final List<PropertyFilter> filters) {

        return hibernateRepository.findPage(entityClass, page, filters);
    }



    /**
     * 统计数量
     */
    public Long count(final String hql, Object... values) {
        return (Long) createQuery(hql, values).uniqueResult();
    }

    public int executeHql(String hql, Object... values) {
        return hibernateRepository.executeHql(hql, values);
    }

    /**
     * 自定义查询，此处主要是做统计用
     *
     * @return
     */
    public Query createQuery(String queryString, Object... values) {
        return hibernateRepository.createQuery(queryString, values);
    }

    public <T> List<T> findBySQLSimple(final Class entityClass,
                                       final String sql, final Object... values) {
        SQLQuery queryObject = getSession().createSQLQuery(sql);

        if (entityClass != null) {
            queryObject.addEntity(entityClass);
        }

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }

        return queryObject.list();
    }

    public void init(Object object) {
        hibernateRepository.init(object);
    }

    /**
     * 刷新缓存数据
     */
    public void flush() {
        hibernateRepository.flush();
    }
}
