package com.ibeacon.hibernate;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.ibeacon.hibernate.PropertyFilter.MatchType;
import com.ibeacon.utils.ReflectionUtils;



/**
 * <pre>
 * @author chenwentao
 * @version 1.0
 *
 * 修改版本: 1.0
 * 修改日期: Sep 17, 2013
 * 修改人 :  chenwentao
 * 修改说明: 初步完成
 * 复审人 ：
 * </pre>
 */
@Scope("singleton")
@Repository("hibernateRepository")
@SuppressWarnings("unchecked")
public class HibernateRepository {

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * 默认的 HibernateRepository 构造方法
     */
    public HibernateRepository() {
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 取得sessionFactory.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 取得当前Session.
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * @param entity
     */
    public <T> Serializable save(T entity) {
        return getSession().save(entity);
    }

    /**
     * @param entity
     */
    public <T> void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    /**
     * @param entity
     */
    public <T> void update(T entity) {
        getSession().update(entity);
    }

    /**
     * @param object
     * @return
     */
    public <T> T merge(T entity) {
        return (T) getSession().merge(entity);
    }

    /**
     * @param persistentEntity
     */
    public <T> void delete(T entity) {
        Assert.notNull(entity, "entity不能为空");
        getSession().delete(entity);
    }

    /**
     * 按id删除对象.
     *
     * @param entityClass
     * @param id
     */
    public void delete(Class<?> entityClass, Serializable id) {
        delete(get(entityClass, id));
    }

    /**
     * @param <T>
     * @param entityClass
     * @param id
     * @return
     */
    public <T> T get(Class<?> entityClass, final Serializable id) {
        return (T) getSession().get(entityClass, id);
    }

    /**
     * @param <T>
     * @param entityClass
     * @param id
     * @param lockMode
     * @return
     */
    public <T> T load(Class<?> entityClass, Serializable id,
                      LockOptions lockMode) {
        return (T) getSession().load(entityClass, id, lockMode);
    }

    /**
     * @param <T>
     * @param hql
     * @param values
     * @return
     */
    public <T> List<T> find(final String hql, final Object... values) {
        return createQuery(null, false, null, null, hql, values).list();
    }

    /**
     * 按HQL查询唯一对象.
     *
     * @param values
     *            命名参数,按名称绑定.
     */
    public <T> T findUnique(final String hql, final Object... values) {
        return (T) createQuery(null, false, null, null, hql, values)
                .uniqueResult();
    }

    /**
     * 按属性查找唯一对象, 匹配方式为相等.
     */
    public <T> T findUniqueBy(Class<?> entityClass, final String propertyName,
                              final Object value) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(entityClass, criterion).uniqueResult();
    }

    /**
     * 执行HQL进行批量修改/删除操作.
     */
    public int executeHql(final String hql, final Object... values) {
        return createQuery(null, false, null, null, hql, values)
                .executeUpdate();
    }

    public <T> List<T> findBySQLSimple(final Class<?> entityClass,
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

    /**
     * @param <T>
     * @param entityClass
     * @return
     */
    public <T> List<T> findAll(Class<?> entityClass) {
        String queryString = new StringBuilder().append("from ")
                .append(entityClass.getName()).toString();

        return this.<T> find(queryString);
    }

    /**
     * 获取全部对象, 支持按属性行序.
     */
    public <T> List<T> findAll(Class<?> entityClass, String orderByProperty,
                               boolean isAsc) {
        Criteria c = createCriteria(entityClass);
        if (isAsc) {
            c.addOrder(Order.asc(orderByProperty));
        } else {
            c.addOrder(Order.desc(orderByProperty));
        }

        return c.list();
    }

    /**
     * 按HQL分页查询.
     *
     * @param page
     *            分页参数.不支持其中的orderBy参数.
     * @param hql
     *            hql语句.
     * @param values
     *            数量可变的查询参数,按顺序绑定.
     *
     * @return 分页查询结果, 附带结果列表及所有查询时的参数.
     */
    public <T> Page<T> findPage(final Page<T> page, final String hql,
                                final Object... values) {
        Query q = createQuery(null, false, null, null, hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }

        setPageParameterToQuery(q, page);
        List result = q.list();
        page.setResult(result);

        return page;
    }

    public <T> Page<T> findPageSql(final Page<T> page,final Class entityClass, final String hql,final Object... values) {
        SQLQuery queryObject = getSession().createSQLQuery(hql);

        if (entityClass!= null) {
            queryObject.addEntity(entityClass);
        }

        if (page.isAutoCount()) {
            long totalCount = countSqlResult(hql,values);
            page.setTotalCount(totalCount);
        }


        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }

        setPageParameterToQuery(queryObject, page);
        List result = queryObject.list();
        page.setResult(result);

        return page;
    }

    /**
     * 按Criteria分页查询.
     *
     * @param page
     *            分页参数.
     * @param criterions
     *            数量可变的Criterion.
     *
     * @return 分页查询结果.附带结果列表及所有查询时的参数.
     */

    public <T> Page<T> findPage(final Class entityClass, final Page<T> page,
                                final Criterion... criterions) {
        Criteria c = createCriteria(entityClass, criterions);

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }
        setPageParameterToCriteria(c, page);
        List result = c.list();
        page.setResult(result);

        return page;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    protected <T> Criteria setPageParameterToCriteria(final Criteria c,
                                                      final Page<T> page) {
        // hibernate的firstResult的序号从0开始
        c.setFirstResult(page.getFirst() - 1);
        c.setMaxResults(page.getPageSize());

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");

            for (int i = 0; i < orderByArray.length; i++) {
                if (Page.ASC.equals(orderArray[i])) {
                    c.addOrder(Order.asc(orderByArray[i]));
                } else {
                    c.addOrder(Order.desc(orderByArray[i]));
                }
            }
        }

        return c;
    }

    /**
     * 按属性过滤条件列表分页查找对象.
     */
    public <T> Page<T> findPage(final Class entityClass, final Page<T> page,
                                final List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);

        return findPage(entityClass, page, criterions);
    }

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    protected long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;

        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtils.getFieldValue(impl,
                    "orderEntries");
            ReflectionUtils
                    .setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 执行Count查询
        Long totalCountObject = (Long) c.setProjection(Projections.rowCount())
                .uniqueResult();
        long totalCount = (totalCountObject != null) ? totalCountObject : 0;

        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return totalCount;
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数.
     */
    protected Criterion buildCriterion(final String propertyName,
                                       final Object propertyValue, final MatchType matchType) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = null;
        // 根据MatchType构造criterion
        switch (matchType) {
            case EQ:
                criterion = Restrictions.eq(propertyName, propertyValue);
                break;
            case LIKE:
                criterion = Restrictions.like(propertyName, (String) propertyValue,
                        MatchMode.ANYWHERE);
                break;

            case LE:
                criterion = Restrictions.le(propertyName, propertyValue);
                break;
            case LT:
                criterion = Restrictions.lt(propertyName, propertyValue);
                break;
            case GE:
                criterion = Restrictions.ge(propertyName, propertyValue);
                break;
            case GT:
                criterion = Restrictions.gt(propertyName, propertyValue);
        }
        return criterion;
    }

    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     */
    protected Criterion[] buildCriterionByPropertyFilter(
            final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        for (PropertyFilter filter : filters) {
            if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                Criterion criterion = buildCriterion(filter.getPropertyName(),
                        filter.getMatchValue(), filter.getMatchType());
                criterionList.add(criterion);
            } else {// 包含多个属性需要比较的情况,进行or处理.
                Disjunction disjunction = Restrictions.disjunction();
                for (String param : filter.getPropertyNames()) {
                    Criterion criterion = buildCriterion(param,
                            filter.getMatchValue(), filter.getMatchType());
                    disjunction.add(criterion);
                }
                criterionList.add(disjunction);
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    protected <T> Query setPageParameterToQuery(final Query q,
                                                final Page<T> page) {
        // hibernate的firstResult的序号从0开始
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());

        return q;
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     *
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(final String hql, final Object... values) {
        String countHql = prepareCountHql(hql);
        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
    }

    protected long countSqlResult(final String hql,final Object... values) {
        String fromHql = hql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;
        SQLQuery query= getSession().createSQLQuery(countHql);

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }

        Object count=query.uniqueResult();
        return ((BigInteger)count).longValue();
    }



    private String prepareCountHql(String orgHql) {
        String fromHql = orgHql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;

        return countHql;
    }

    /**
     * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     *
     * @param values
     *            数量可变的参数,按顺序绑定.
     */
    public Query createQuery(final String queryString, final Object... values) {
        return createQuery(null, false, null, null, queryString, values);
    }

    /**
     * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     *
     * @param values
     *            命名参数,按名称绑定.
     */
    public Query createQuery(final String queryString,
                             final Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     *
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param values
     *            数量可变的参数,按顺序绑定.
     */
    public Query createQuery(final LockOptions lockMode,
                             final boolean needQueryCache, final String cacheRegion,
                             final CacheMode chacheMode, final String queryString,
                             final Object... values) {
        Query query = getSession().createQuery(queryString);
        // 是否加锁
        if (lockMode != null) {
            query.setLockOptions(lockMode);
        }

        // 缓存配置
        if (needQueryCache) {
            query.setCacheable(needQueryCache);

            if (cacheRegion != null) {
                query.setCacheRegion(cacheRegion);
            }
            if (chacheMode != null) {
                query.setCacheMode(chacheMode);
            }
        }
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    /**
     * 根据Criterion条件创建Criteria.
     *
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param criterions
     *            数量可变的Criterion.
     */
    public Criteria createCriteria(final Class<?> entityClass,
                                   final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }

        return criteria;
    }

    /**
     * @param entity
     * @param locikMode
     */
    public void refresh(Object entity, LockOptions lockMode) {
        getSession().refresh(entity, lockMode);
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Flush当前Session.
     */
    public void flush() {
        getSession().flush();
    }

    public void init(Object obj) {
        Hibernate.initialize(obj);
    }

}
