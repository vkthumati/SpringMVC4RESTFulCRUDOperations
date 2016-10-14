package com.ws.crud.operations.exception.resolvers;

import static com.google.common.collect.ImmutableBiMap.copyOf;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Integer.signum;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair; 
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;
import org.springframework.ws.server.endpoint.AbstractEndpointExceptionResolver;

public class SoapExceptionResolverMapper extends AbstractEndpointExceptionResolver {

    public SoapExceptionResolverMapper() {
        this.setOrder(Integer.MIN_VALUE);
    }

    private Map<Class<? extends Exception>, EndpointExceptionResolver> resolvers = newHashMap();

    public void registerResolver(Class<? extends Exception> exceptionType, EndpointExceptionResolver resolver) {
        resolvers.put(exceptionType, resolver);
    }

    public Map<Class, EndpointExceptionResolver> resolvers() {
        return copyOf(resolvers);
    }

    @Override
    protected boolean resolveExceptionInternal(MessageContext messageContext, Object endpoint, Exception ex) {
        EndpointExceptionResolver resolver = findResolver(ex);
        return resolver != null && resolver.resolveException(messageContext, endpoint, ex);
    }

    /**
     * Searches for a the exception class or the closest superclass of the exception.
     */
    @SuppressWarnings("unchecked")
    protected EndpointExceptionResolver findResolver(Exception ex) {
        Optional min = resolvers.keySet().stream().
                map(i -> new ImmutablePair<>(i, getDepth(i, ex))).
                filter(p -> p.getRight() >= 0).
                min((o1, o2) -> signum(o1.getRight() - o2.getRight()));

        if (min.isPresent()) {
            Pair<Class<? extends Exception>, Integer> pair = (Pair<Class<? extends Exception>, Integer>) min.get();
            return resolvers.get(pair.getLeft());
        }

        return null;
    }

    /**
     * Return the depth to the superclass matching. {@code 0} means ex matches exactly. Returns {@code -1} if
     * there's no match. Otherwise, returns depth. Lowest depth wins.
     * <p>
     * <p>Follows the same algorithm as RollbackRuleAttribute, and SimpleMappingExceptionResolver
     */
    protected int getDepth(Class<? extends Exception> mappedClass, Exception ex) {
        return getDepth(mappedClass, ex.getClass(), 0);
    }

    @SuppressWarnings("unchecked")
    private int getDepth(Class<? extends Exception> mappedClass, Class<? extends Exception> exceptionClass, int depth) {
        if (exceptionClass.equals(Throwable.class)) {
            return -1;
        }

        if (mappedClass.equals(exceptionClass)) {
            return depth;
        }

        return getDepth(mappedClass, (Class<? extends Exception>) exceptionClass.getSuperclass(), depth + 1);
    }
}

