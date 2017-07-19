package com.ibeacon.hibernate;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * <pre>
 * @author chenwentao
 *
 * @version 0.9
 *
 * 修改版本: 0.9
 * 修改日期: Jan 30, 2011
 * 修改人 :  chenwentao
 * 修改说明: 初步完成
 * 复审人 ：
 * </pre>
 */

public abstract class PageUtil {

    /**
     * 初始化分页对象,
     *
     * @param page
     * @param orderBy
     * @param orders
     * @param pageSize
     */
    public static void initPage(Page<?> page, String orderBy, String orders,
                                int pageSize) {
        // 设置默认pageSize
        if (page.getPageSize() <= 0) {
            page.setPageSize(pageSize);
        }

        // 判断orderBy,orders是否为空
        if (StringUtils.isEmpty(orderBy) || StringUtils.isEmpty(orders)) {
            return;
        }

        // 设置默认排序方式
        if (!page.isOrderBySetted()) {
            page.setOrderBy(orderBy);
            page.setOrder(orders);
            // 验证
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');
            Assert.isTrue(orderByArray.length == orderArray.length,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
        }
    }
}