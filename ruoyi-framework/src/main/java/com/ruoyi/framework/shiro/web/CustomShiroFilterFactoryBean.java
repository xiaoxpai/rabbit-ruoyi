package com.ruoyi.framework.shiro.web;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.InvalidRequestFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.BeanInitializationException;
import javax.servlet.Filter;
import java.util.Map;

/**
 * 自定义ShiroFilterFactoryBean解决资源中文路径问题
 *
 * <p>
 *     ShiroFilterFactoryBean作用：是生成一个ShiroFilter的工厂类，最终创建出一个DelegatingFilterProxy过滤器
 *     这个过滤器会委托给Spring IOC容器中id为shiroFilter的bean进行处理。
 * </p>
 * @author ruoyi
 */
public class CustomShiroFilterFactoryBean extends ShiroFilterFactoryBean
{
    /**
     * @return
     */
    @Override
    public Class<MySpringShiroFilter> getObjectType()
    {
        return MySpringShiroFilter.class;
    }

    @Override
    protected AbstractShiroFilter createInstance() throws Exception
    {

        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null)
        {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager))
        {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }

        FilterChainManager manager = createFilterChainManager();
        // Expose the constructed FilterChainManager by first wrapping it in a
        // FilterChainResolver implementation. The AbstractShiroFilter implementations
        // do not know about FilterChainManagers - only resolvers:
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        Map<String, Filter> filterMap = manager.getFilters();
        Filter invalidRequestFilter = filterMap.get(DefaultFilter.invalidRequest.name());
        if (invalidRequestFilter instanceof InvalidRequestFilter)
        {
            // 此处是关键,设置false跳过URL携带中文400，servletPath中文校验bug
            ((InvalidRequestFilter) invalidRequestFilter).setBlockNonAscii(false);
        }
        // Now create a concrete ShiroFilter instance and apply the acquired SecurityManager and built
        // FilterChainResolver. It doesn't matter that the instance is an anonymous inner class
        // here - we're just using it because it is a concrete AbstractShiroFilter instance that accepts
        // injection of the SecurityManager and FilterChainResolver:
        return new MySpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }


    private static final class MySpringShiroFilter extends AbstractShiroFilter
    {
        /**
         * protected 修饰方法，只能在本类和子类中访问，其他类不能访问，也不能重写，但是可以重载
         *
         * @param webSecurityManager 作用：
         * @param resolver
         */
        protected MySpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver)
        {
            if (webSecurityManager == null)
            {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            else
            {
                this.setSecurityManager(webSecurityManager);
                if (resolver != null)
                {
                    this.setFilterChainResolver(resolver);
                }
            }
        }
    }
}